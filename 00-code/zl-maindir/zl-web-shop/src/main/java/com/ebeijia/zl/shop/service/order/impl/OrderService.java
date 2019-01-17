package com.ebeijia.zl.shop.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.SpecAccountTypeEnum;
import com.ebeijia.zl.common.utils.exceptions.BizException;
import com.ebeijia.zl.common.utils.tools.StringUtils;
import com.ebeijia.zl.shop.constants.ResultState;
import com.ebeijia.zl.shop.dao.goods.domain.*;
import com.ebeijia.zl.shop.dao.goods.service.*;
import com.ebeijia.zl.shop.dao.info.domain.TbEcomItxLogDetail;
import com.ebeijia.zl.shop.dao.info.service.ITbEcomItxLogDetailService;
import com.ebeijia.zl.shop.dao.order.domain.*;
import com.ebeijia.zl.shop.dao.order.service.*;
import com.ebeijia.zl.shop.service.goods.IProductService;
import com.ebeijia.zl.shop.service.order.IOrderService;
import com.ebeijia.zl.shop.service.pay.IPayService;
import com.ebeijia.zl.shop.utils.AdviceMessenger;
import com.ebeijia.zl.shop.utils.ShopTransactional;
import com.ebeijia.zl.shop.vo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.ebeijia.zl.shop.constants.ResultState.*;

@Service
public class OrderService implements IOrderService {

    @Autowired
    private ITbEcomPlatfOrderService platfOrderDao;

    @Autowired
    private ITbEcomPlatfShopOrderService shopOrderDao;

    @Autowired
    private ITbEcomOrderProductItemService orderProductItemDao;

    @Autowired
    private ITbEcomOrderShipService orderShipDao;

    @Autowired
    private ITbEcomGoodsProductService productDao;

    @Autowired
    private ITbEcomGoodsService goodsDao;

    @Autowired
    private ITbEcomGoodsBillingService goodsBillingDao;

    @Autowired
    private ITbEcomGoodsDetailService detailDao;

    @Autowired
    private ITbEcomDmsRelatedDetailService dmsRelatedDetailDao;

    @Autowired
    private ITbEcomSpecificationService specificationDao;

    @Autowired
    private ITbEcomSpecValuesService specValuesDao;

    @Autowired
    private IProductService productService;

    @Autowired
    private IPayService payService;

    @Autowired
    private HttpSession session;

    @Autowired
    private ITbEcomItxLogDetailService logDetailDao;

    private static Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Override
    @ShopTransactional
    public TbEcomPlatfOrder createSimpleOrder(OrderItemInfo orderItemInfo, AddressInfo address) {
        //获得身份信息
        MemberInfo memberInfo = (MemberInfo) session.getAttribute("user");
        if (memberInfo == null) {
            throw new BizException(NOT_ACCEPTABLE, "参数异常");
        }
        //构造主订单
        TbEcomPlatfOrder platfOrder = initOrderObject(memberInfo.getMemberId());
        String orderId = platfOrder.getOrderId();

        //构造物流信息
        TbEcomOrderShip ship = processOrderShip(platfOrder, address);

        //判断商品渠道,按照List中的内容构建多个子Map，这里的list只有一个元素
        HashMap<String, HashMap<TbEcomGoodsProduct, Integer>> itemMap = processOrderProduct(Collections.singletonList(orderItemInfo));
        //初始化容器，用于存储订单
        List<TbEcomOrderProductItem> subOrderItemList = new LinkedList<>();
        List<TbEcomPlatfShopOrder> subOrderList = new LinkedList<>();
        //循环处理各渠道信息
        for (String ecom : itemMap.keySet()) {
            //根据Map构造子订单，这里用了简化逻辑
            TbEcomPlatfShopOrder shopOrder = initShopOrderObject(platfOrder);

            //获取商品列表和购买数量
            HashMap<TbEcomGoodsProduct, Integer> productInfo = itemMap.get(ecom);

            //构造订单商品列表,这里只有一个商品
            List<TbEcomOrderProductItem> product = processOrder(productInfo, ecom, shopOrder.getSOrderId());
            subOrderItemList.addAll(product);

            //构造订单商品物流信息
            processShopOrderInfo(shopOrder, ecom);

            //完善渠道订单
            processShopOrderPrice(shopOrder, product);

            //完善关联（目前在支付过程中关联）
//            processRelatedDetail(shopOrder)

            //存储子订单，待持久化
            subOrderList.add(shopOrder);
        }

        //完善主订单
        processOrderPrice(platfOrder, subOrderList);

        //完善主订单关联（目前没有其他关联，在支付过程中处理）

        //持久化 主订单、子订单、订单商品、收货地址
        platfOrderDao.save(platfOrder);
        orderShipDao.save(ship);

        for (TbEcomPlatfShopOrder subOrder : subOrderList) {
            shopOrderDao.save(subOrder);
        }
        for (TbEcomOrderProductItem item : subOrderItemList) {
            //处理库存
            productService.productStoreConsumer(item.getProductId(), item.getProductNum());
            orderProductItemDao.save(item);
        }
        //校验信息
        return platfOrder;
    }


    @Override
    public TbEcomPlatfOrder cancelOrder(String orderId) {
        MemberInfo memberInfo = (MemberInfo) session.getAttribute("user");
        if (memberInfo == null) {
            throw new BizException(NOT_ACCEPTABLE, "参数异常");
        }
        //获取订单对象
        TbEcomPlatfOrder order = platfOrderDao.getById(orderId);
        if (order == null) {
            throw new BizException(NOT_FOUND, "找不到订单");
        }
        //校验身份
        String memberId = memberInfo.getMemberId();
        if (StringUtils.isEmpty(memberId) || !memberId.equals(order.getMemberId())) {
            throw new BizException(NOT_ACCEPTABLE, "参数异常");
        }
        //获得路径
        if (!order.getPayStatus().equals("0")) {
            throw new BizException(NOT_ACCEPTABLE, "您的订单无需取消");
        }
        order.setPayStatus("8");
        platfOrderDao.updateById(order);
        TbEcomPlatfShopOrder query = new TbEcomPlatfShopOrder();
        query.setOrderId(order.getOrderId());
        TbEcomPlatfShopOrder updataInf = new TbEcomPlatfShopOrder();
        updataInf.setSubOrderStatus("27");
        shopOrderDao.update(updataInf, new QueryWrapper<>(query));
        updataInf = shopOrderDao.getOne(new QueryWrapper<>(query));
        TbEcomOrderProductItem queryItem = new TbEcomOrderProductItem();
        queryItem.setSOrderId(updataInf.getSOrderId());
        TbEcomOrderProductItem productItem = orderProductItemDao.getOne(new QueryWrapper<>(queryItem));
        productService.productStoreRecover(productItem.getProductId(), productItem.getProductNum());
        //执行操作


        return order;
    }

    /**
     * 简单的支付订单功能
     *
     * @param payInfo
     * @return
     */
    @Override
    public TbEcomPlatfOrder applyOrder(PayInfo payInfo) {
        logger.info(String.format("支付订单开始，参数%s", payInfo));
        MemberInfo memberInfo = (MemberInfo) session.getAttribute("user");
        if (memberInfo == null) {
            throw new BizException(NOT_ACCEPTABLE, "参数异常");
        }
        //生成DMS
        String dmsRelatedKey = IdUtil.getNextId();
        //验证输入信息有效性，订单查询
        Long payAmount = getPayAmount(payInfo);
        TbEcomPlatfOrder order = platfOrderDao.getById(payInfo.getOrderId());
        order.setDmsRelatedKey(dmsRelatedKey);
        if (order == null) {
            throw new BizException(NOT_FOUND, "订单不存在");
        }
        if (!memberInfo.getMemberId().equals(order.getMemberId())) {
            throw new BizException(NOT_ACCEPTABLE, "验证失败");
        }
        if (!order.getPayStatus().equals("0") || ("1").equals(order.getDataStat())) {
            throw new BizException(NOT_ACCEPTABLE, "支付状态有误");
        }
        //乐观锁
        order.setPayTime(System.currentTimeMillis());
        order.setPayStatus("1");
        order.setUpdateUser("System");
        order.setUpdateTime(System.currentTimeMillis());
        order = orderUpdateLocker(order);
        //类型、总金额验证
        TbEcomPlatfShopOrder shopOrder = new TbEcomPlatfShopOrder();
        shopOrder.setOrderId(order.getOrderId());
        QueryWrapper<TbEcomPlatfShopOrder> query = new QueryWrapper<>(shopOrder);
        List<TbEcomPlatfShopOrder> shopOrders = shopOrderDao.list(query);
        if (payAmount.compareTo(order.getOrderPrice()) != 0) {
            throw new BizException(NOT_ACCEPTABLE, "订单金额不正确");
        }
        //存储所有订单item
        List<TbEcomOrderProductItem> itemList = new LinkedList();
        //创建订单简介，用于账务流水
        StringBuilder descBuilder = new StringBuilder("购买");

        String title = "";
        String descinfo = "";
        Long price = 0L;
        String image = "";
        Integer amount = 0;
        String outId = "";
        String itxKey = "";

        //免费订单逻辑
        if (payAmount == 0) {
            //TODO
        } else {
            //TODO 组合订单类型判断（未完成，目前仅能处理一种商品，并且只支持商品和账户类型一对一的情况）
            Iterator<TbEcomPlatfShopOrder> iterator = shopOrders.iterator();
            while (iterator.hasNext()) {
                TbEcomPlatfShopOrder platfShopOrder = iterator.next();
                platfShopOrder.setDmsRelatedKey(dmsRelatedKey);
                String sOrderId = platfShopOrder.getSOrderId();
                TbEcomOrderProductItem item = orderProductItemDao.getOrderProductItemBySOrderId(sOrderId);
                Goods goods = new Goods();
                goods.setProductId(item.getProductId());
                goods = goodsDao.getGoods(goods);
                String ecomName = "";
                if (goods != null) {
                    ecomName = "[" + goods.getEcomName() + "]";
                    if (goods.getIsDisabled().equals("1") ||
                            goods.getMarketEnable().equals("0")) {
                       throw new BizException(ResultState.STAT_ERROR,"该订单商品已下架");
                    }
                }
                itemList.add(item);
                TbEcomGoodsProduct product = productDao.getById(item.getProductId());
                TbEcomGoodsProduct goodsProductBySkuCode = productDao.getGoodsProductBySkuCode(product.getSkuCode());

                //TODO INF
                title = item.getProductName();

                TbEcomSpecValues specValues = null;
                if (goodsProductBySkuCode != null) {
                    specValues = specValuesDao.getById(goodsProductBySkuCode.getSpecValueId());
                }
                if (specValues != null) {
                    descinfo = ecomName + product.getPageTitle() + "  " + specValues.getSpecName() + ":" + specValues.getSpecValueName();
                } else {
                    descinfo = ecomName + product.getPageTitle();
                }
                image = product.getPicUrl();
                price = item.getProductPrice();
                amount = item.getProductNum();

                descBuilder.append(item.getProductName());
                if (iterator.hasNext()) {
                    descBuilder.append(",");
                }
                TbEcomGoodsBilling goodsBilling = new TbEcomGoodsBilling();
                goodsBilling.setGoodsId(product.getGoodsId());
                List<TbEcomGoodsBilling> list = goodsBillingDao.list(new QueryWrapper<>(goodsBilling));
                //如果没有指定账户类型，那不允许用B类账户支付
                if (list == null && payInfo.getCostB() != null) {
                    throw new BizException(NOT_ACCEPTABLE, "支付类型不正确");
                }
                //当存在账户类型时，必须和支付类型匹配（这里只判定了商品和账户类型一对一的情况）
                if (list != null && payInfo.getTypeB() != null) {
                    String bId = list.get(0).getBId();
                    if (!bId.equals(payInfo.getTypeB())) {
                        throw new BizException(NOT_ACCEPTABLE, "支付类型不正确");
                    }
                }
            }
            if (!shopOrderDao.updateBatchById(shopOrders)) {
                throw new BizException(ERROR, "子订单提交异常");
            }

            //处理订单支付过程，调用了payService
            BaseResult baseResult = payService.payOrder(payInfo, memberInfo.getOpenId(), dmsRelatedKey, descBuilder.toString());
            if (!baseResult.getCode().equals("00")) {
                logger.info(String.format("支付失败，订单%s，参数%s", order.getOrderId(), payInfo));
                throw new BizException(ResultState.NOT_ACCEPTABLE, "参数异常");
            }
            //INF
            itxKey = (String) baseResult.getObject();
        }
        //TODO INF
        TbEcomItxLogDetail log = new TbEcomItxLogDetail();
        log.setMemberId(memberInfo.getMemberId());
        log.setTitle(title);
        log.setPrice(price);
        log.setDescinfo(descinfo);
        log.setOutId(payInfo.getOrderId());
        log.setItxKey(itxKey);
        log.setAmount(amount);
        log.setImg(image);

        String bId = payInfo.getTypeB() == null ? payInfo.getTypeA() : payInfo.getTypeB();
        bId = SpecAccountTypeEnum.findByBId(bId).getbId();

        log.setSourceBid(bId);
        logDetailDao.save(log);

        //修改订单状态
        order.setPayStatus("2");
        //持久化并且锁版本加1
        order = orderUpdateLocker(order);
        //实际库存调整
        for (TbEcomOrderProductItem i : itemList) {
            productService.productStoreChange(i.getProductId(), 0 - i.getProductNum());
        }
        return order;
    }

    @Override
    public OrderDetailInfo orderDetail(String orderId) {
        OrderDetailInfo result = new OrderDetailInfo();
        SubOrder subOrder = new SubOrder();

        OrderInfo orderQuery = new OrderInfo();
        orderQuery.setOrderId(orderId);
        result.setOrder(platfOrderDao.getOrderInfo(orderQuery));

        TbEcomOrderShip shipQuery = new TbEcomOrderShip();
        shipQuery.setOrderId(orderId);
        result.setShip(orderShipDao.getOne(new QueryWrapper<>(shipQuery)));

        TbEcomOrderProductItem orderProductItemBySOrderId = orderProductItemDao.getOrderProductItemBySOrderId(result.getOrder().getSOrderId());
        HashMap<String, Integer> map = new HashMap<>();
        map.put(orderProductItemBySOrderId.getProductId(), orderProductItemBySOrderId.getProductNum());
        subOrder.setAmount(map);

        Goods goodsQuery = new Goods();
        goodsQuery.setProductId(orderProductItemBySOrderId.getProductId());
        Goods goods = goodsDao.getGoods(goodsQuery);
        goods.setGoodsName(orderProductItemBySOrderId.getProductName());
        goods.setGoodsPrice(String.valueOf(orderProductItemBySOrderId.getProductPrice()));
        LinkedList<Goods> products = new LinkedList<>();
        products.add(goods);
        subOrder.setProducts(products);

        TbEcomPlatfShopOrder shopQuery = new TbEcomPlatfShopOrder();
        shopQuery.setOrderId(orderId);
        TbEcomPlatfShopOrder shopOrder = shopOrderDao.getOne(new QueryWrapper<>(shopQuery));

        subOrder.setShopOrder(shopOrder);
        LinkedList<SubOrder> subOrderList = new LinkedList<>();
        subOrderList.add(subOrder);
        result.setSubOrders(subOrderList);
        return result;
    }

    @Override
    public PageInfo<OrderDetailInfo> listOrderDetail(String orderStat, Integer start, Integer limit) {
        MemberInfo memberInfo = (MemberInfo) session.getAttribute("user");
        if (memberInfo == null) {
            throw new BizException(NOT_ACCEPTABLE, "参数异常");
        }
        logger.info(String.format("前端请求入参%s,%s,%s", orderStat, start, limit));
        if (limit == null || limit > 100) {
            limit = 20;
        }
        if (start == null) {
            start = 1;
        }

        TbEcomPlatfOrder query = new TbEcomPlatfOrder();
        query.setMemberId(memberInfo.getMemberId());
        query.setDataStat("0");
        if (!StringUtils.isEmpty(orderStat)) {
            query.setPayStatus(orderStat);
        }
        PageHelper.startPage(start, limit);
        QueryWrapper<TbEcomPlatfOrder> queryWrapper = new QueryWrapper<>(query);
        queryWrapper.orderByDesc("create_time");
        List<TbEcomPlatfOrder> orderList = platfOrderDao.list(queryWrapper);
        LinkedList<OrderDetailInfo> result = new LinkedList<>();
        if (orderList != null) {
            for (TbEcomPlatfOrder order : orderList) {
                result.add(orderDetail(order.getOrderId()));
            }
        }
        return new PageInfo<>(result);
    }

    @Override
    public Integer disableOrder(String orderId) {
        MemberInfo memberInfo = (MemberInfo) session.getAttribute("user");
        if (memberInfo == null) {
            throw new BizException(NOT_ACCEPTABLE, "参数异常");
        }
        //获取订单对象
        TbEcomPlatfOrder order = platfOrderDao.getById(orderId);
        if (order == null || ("1").equals(order.getDataStat())) {
            throw new BizException(NOT_FOUND, "找不到订单");
        }
        //校验身份
        String memberId = memberInfo.getMemberId();
        if (StringUtils.isEmpty(memberId) || !memberId.equals(order.getMemberId())) {
            throw new BizException(NOT_ACCEPTABLE, "参数异常");
        }
        order.setDataStat("1");
        orderUpdateLocker(order);
        return 200;
    }

    private TbEcomPlatfOrder orderUpdateLocker(TbEcomPlatfOrder order) {
        TbEcomPlatfOrder query = new TbEcomPlatfOrder();
        query.setOrderId(order.getOrderId());
        query.setLockVersion(order.getLockVersion());
        order.setLockVersion(order.getLockVersion() + 1);
        boolean update = platfOrderDao.update(order, new QueryWrapper<>(query));
        if (!update) {
            throw new BizException(NOT_ACCEPTABLE, "状态异常,请重试");
        }
        return order;
    }

    /**
     * 计算支付总和，以及判断支付金额有效性
     *
     * @param payInfo
     * @return
     */
    private Long getPayAmount(PayInfo payInfo) {
        String typeA = payInfo.getTypeA();
        String typeB = payInfo.getTypeB();
        Long costA = payInfo.getCostA();
        Long costB = payInfo.getCostB();
        Long sum = 0L;
        if (costA != null && costA < 0L) {
            throw new BizException(NOT_ACCEPTABLE, "参数异常");
        }
        if (costB != null && costB < 0L) {
            throw new BizException(NOT_ACCEPTABLE, "参数异常");
        }
        if (StringUtils.isAllEmpty(typeA, typeB)) {
            throw new BizException(NOT_ACCEPTABLE, "参数异常");
        }
        sum += costA == null ? 0L : costA;
        sum += costB == null ? 0L : costB;
        if (sum.compareTo(0L) < 0) {
            throw new BizException(NOT_ACCEPTABLE, "参数异常");
        }
        return sum;
    }

    /**
     * 记录订单总价和总邮费
     */
    private void processOrderPrice(TbEcomPlatfOrder platfOrder, List<TbEcomPlatfShopOrder> subOrderList) {
        AtomicReference<Long> price = new AtomicReference<>(0L);
        AtomicReference<Long> shipPrice = new AtomicReference<>(0L);
        subOrderList.stream().forEach(subOrder -> {
            price.updateAndGet(v -> v + subOrder.getChnlOrderPrice());
            shipPrice.updateAndGet(v -> v + subOrder.getChnlOrderPostage());
        });
        platfOrder.setOrderPrice(price.get());
        platfOrder.setOrderFreightAmt(shipPrice.get());
    }

    private void processShopOrderInfo(TbEcomPlatfShopOrder shopOrder, String ecom) {
        shopOrder.setPayStatus("0");
        shopOrder.setDataStat("0");
        shopOrder.setEcomCode(ecom);
        shopOrder.setSubOrderStatus("00");
    }

    //TODO 邮费计算规则需要调整
    private void processShopOrderPrice(TbEcomPlatfShopOrder shopOrder, List<TbEcomOrderProductItem> product) {
        AtomicReference<Long> orderPrice = new AtomicReference<>(0L);
        product.stream().forEach(item -> orderPrice.updateAndGet(v -> v + item.getProductPrice() * item.getProductNum()));
        if (orderPrice.get().compareTo(0L) < 0) {
            logger.error("订单状态异常：" + product.toString());
            throw new BizException(500, "订单价格异常");
        }
        shopOrder.setChnlOrderPrice(orderPrice.get());
    }

    /**
     * 处理按照专项类型分组的商品，转化为订单商品对象
     *
     * @param sOrderId
     * @return
     */

    private List<TbEcomOrderProductItem> processOrder(HashMap<TbEcomGoodsProduct, Integer> productInfo, String ecom, String sOrderId) {
        List<TbEcomOrderProductItem> orderProductItemList = new LinkedList<>();
        Iterator<TbEcomGoodsProduct> productIterator = productInfo.keySet().iterator();
        while (productIterator.hasNext()) {
            TbEcomGoodsProduct product = productIterator.next();
            String goodsId = product.getGoodsId();
            TbEcomGoods goods = goodsDao.getById(goodsId);
            TbEcomOrderProductItem orderProductItem = initOrderProductObject(sOrderId, product, goods, productInfo.get(product));
            orderProductItemList.add(orderProductItem);
        }
        return orderProductItemList;
    }


    private TbEcomOrderShip processOrderShip(TbEcomPlatfOrder order, AddressInfo address) {
        //构建ship信息
        TbEcomOrderShip orderShip = new TbEcomOrderShip();
        orderShip.setOrderId(order.getOrderId());
        orderShip.setShipName(address.getRecipient());
        orderShip.setShipAddr(makeSimpleAddress(address));
        orderShip.setShipTelephone(address.getPhoneNo());
        orderShip.setDataStat("0");

        //记录操作
        orderShip.setCreateTime(System.currentTimeMillis());
        orderShip.setCreateUser("ShopSystem");
        return orderShip;
    }

    private String makeSimpleAddress(AddressInfo address) {
        StringBuilder builder = new StringBuilder();
        String space = "  ";
        if (address.getProvince() != null) {
            builder.append(address.getProvince());
        }
        builder.append(space);
        if (address.getCity() != null) {
            builder.append(address.getCity());
        }
        builder.append(space);
        if (address.getProvince() != null) {
            builder.append(address.getCounty());
        }
        builder.append(space);
        builder.append(address.getAddress());
        return builder.toString();
    }

    /**
     * 将传入的购买商品列表，按照渠道转化为多个Map
     */
    private HashMap<String, HashMap<TbEcomGoodsProduct, Integer>> processOrderProduct(List<OrderItemInfo> orderItemInfo) {
        HashMap<String, HashMap<TbEcomGoodsProduct, Integer>> itemMap = new HashMap<>();
        for (OrderItemInfo o : orderItemInfo) {
            TbEcomGoodsProduct sku = productDao.getById(o.getProductId());
            checkItemAvaliable(sku, o.getAmount());
            String ecomCode = sku.getEcomCode();
            if (!itemMap.keySet().contains(ecomCode)) {
                itemMap.put(ecomCode, new HashMap<>());
            }
            itemMap.get(ecomCode).put(sku, o.getAmount());
        }
        return itemMap;
    }

    /**
     * 根据不同渠道生成多个子订单（未完成、本期未使用）
     *
     * @param platfOrder
     * @param items
     * @param address
     * @return
     */
    private List<TbEcomPlatfShopOrder> makeShopOrder(TbEcomPlatfOrder platfOrder, List<TbEcomOrderProductItem> items, AddressInfo address) {
        if (platfOrder == null || items == null || address == null) {
            throw new AdviceMessenger(NOT_ACCEPTABLE, "参数异常");
        }
        for (TbEcomOrderProductItem item : items) {

        }
        TbEcomPlatfShopOrder shopOrder = new TbEcomPlatfShopOrder();
        //TODO
        return null;
    }

    /**
     * 检查商品库存、状态
     *
     * @param sku
     * @param amount
     */
    private void checkItemAvaliable(TbEcomGoodsProduct sku, Integer amount) {
        if (null == sku) {
            throw new BizException(NOT_ACCEPTABLE, "错误的商品");
        }
        if ("1".equals(sku.getDataStat())) {
            throw new BizException(STAT_ERROR, "该商品下架了");
        }
        if ("0".equals(sku.getProductEnable())) {
            throw new BizException(STAT_ERROR, "该商品下架了");
        }
        //TODO 使用Redis记录库存
        Integer enableStore = sku.getEnableStore();
        if (amount == null || enableStore == null || amount.compareTo(enableStore) > 0) {
            throw new BizException(NOT_ACCEPTABLE, "商品库存不足");
        }

    }

    /**
     * 初始化对象
     *
     * @param platfOrder
     * @return
     */
    private TbEcomPlatfShopOrder initShopOrderObject(TbEcomPlatfOrder platfOrder) {
        TbEcomPlatfShopOrder shopOrder = new TbEcomPlatfShopOrder();
        shopOrder.setSOrderId(IdUtil.getNextId());
        shopOrder.setMemberId(platfOrder.getMemberId());
        shopOrder.setOrderId(platfOrder.getOrderId());
        shopOrder.setChnlOrderPostage(0L);
        shopOrder.setLockVersion(0);
        //记录操作
        shopOrder.setCreateTime(System.currentTimeMillis());
        shopOrder.setCreateUser("ShopSystem");
        return shopOrder;
    }

    private TbEcomPlatfOrder initOrderObject(String memberId) {
        TbEcomPlatfOrder platfOrder = new TbEcomPlatfOrder();
        platfOrder.setOrderId(IdUtil.getNextId());
        platfOrder.setMemberId(memberId);
        //记录操作
        platfOrder.setPayStatus("0");
        platfOrder.setLockVersion(0);
        platfOrder.setCreateTime(System.currentTimeMillis());
        platfOrder.setCreateUser("ShopSystem");
        return platfOrder;
    }

    //初始化订单产品状态
    private TbEcomOrderProductItem initOrderProductObject(String sOrderId, TbEcomGoodsProduct sku, TbEcomGoods goods, Integer amounts) {
        TbEcomOrderProductItem productItem = new TbEcomOrderProductItem();
        productItem.setOItemId(IdUtil.getNextId());
        productItem.setSOrderId(sOrderId);
        productItem.setProductId(sku.getProductId());
        productItem.setProductName(goods.getGoodsName());
        productItem.setProductNum(amounts);
        productItem.setProductPrice(Long.valueOf(sku.getGoodsPrice()));
        productItem.setDataStat("0");
        productItem.setLockVersion(0);
        //记录操作
        productItem.setCreateTime(System.currentTimeMillis());
        productItem.setCreateUser("ShopSystem");
        return productItem;
    }


}
