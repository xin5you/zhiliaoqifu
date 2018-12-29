package com.ebeijia.zl.shop.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.exceptions.BizException;
import com.ebeijia.zl.common.utils.tools.StringUtils;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoods;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsProduct;
import com.ebeijia.zl.shop.dao.goods.service.ITbEcomGoodsProductService;
import com.ebeijia.zl.shop.dao.goods.service.ITbEcomGoodsService;
import com.ebeijia.zl.shop.dao.order.domain.*;
import com.ebeijia.zl.shop.dao.order.service.ITbEcomDmsRelatedDetailService;
import com.ebeijia.zl.shop.dao.order.service.ITbEcomOrderProductItemService;
import com.ebeijia.zl.shop.dao.order.service.ITbEcomPlatfOrderService;
import com.ebeijia.zl.shop.dao.order.service.ITbEcomPlatfShopOrderService;
import com.ebeijia.zl.shop.service.order.IOrderService;
import com.ebeijia.zl.shop.utils.AdviceMessenger;
import com.ebeijia.zl.shop.utils.ShopTransactional;
import com.ebeijia.zl.shop.utils.ShopUtils;
import com.ebeijia.zl.shop.vo.AddressInfo;
import com.ebeijia.zl.shop.vo.MemberInfo;
import com.ebeijia.zl.shop.vo.OrderItemInfo;
import com.ebeijia.zl.shop.vo.PayInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.ebeijia.zl.shop.constants.ResultState.NOT_ACCEPTABLE;
import static com.ebeijia.zl.shop.constants.ResultState.NOT_FOUND;

@Service
public class OrderService implements IOrderService {

    @Autowired
    private ITbEcomPlatfOrderService platfOrderDao;

    @Autowired
    private ITbEcomPlatfShopOrderService shopOrderDao;

    @Autowired
    ITbEcomOrderProductItemService orderProductItemDao;

    @Autowired
    ITbEcomGoodsProductService productDao;

    @Autowired
    ITbEcomGoodsService goodsDao;

    @Autowired
    ITbEcomDmsRelatedDetailService dmsRelatedDetailDao;

    @Autowired
    ShopUtils shopUtils;


    @Autowired
    private HttpSession session;

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
        for (String s : itemMap.keySet()) {
            //根据Map构造子订单，这里用了简化逻辑
            TbEcomPlatfShopOrder shopOrder = initShopOrderObject(platfOrder);

            //获取渠道代码
            String ecom = s;

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
        String dmsKey = IdUtil.getNextId();

        //完善主订单
        processOrderPrice(platfOrder, subOrderList);

        //完善主订单关联（目前没有其他关联，在支付过程中处理）

        //持久化 主订单、子订单、订单商品、收货地址
        platfOrderDao.save(platfOrder);
        for (TbEcomPlatfShopOrder subOrder : subOrderList) {
            shopOrderDao.save(subOrder);
        }
        for (TbEcomOrderProductItem item : subOrderItemList) {
            orderProductItemDao.save(item);
        }

        //校验信息
        return platfOrder;
    }


    @Override
    public TbEcomOrderInf cancelOrder(String orderId) {
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
        String memberId = (String) session.getAttribute("memberId");
        if (StringUtils.isEmpty(memberId) || !memberId.equals(order.getMemberId())) {
            throw new BizException(NOT_ACCEPTABLE, "参数异常");
        }
        //获得路径

        //执行操作
        return null;
    }

    @Override
    public TbEcomPlatfOrder applyOrder(AddressInfo address, PayInfo payInfo) {
        MemberInfo memberInfo = (MemberInfo) session.getAttribute("user");
        if (memberInfo == null) {
            throw new BizException(NOT_ACCEPTABLE, "参数异常");
        }
        //生成DMS
        String dmsRelatedKey = IdUtil.getNextId();
        //验证输入信息有效性，订单查询
        Long payAmount = getPayAmount(payInfo);
        TbEcomPlatfOrder order = platfOrderDao.getById(payInfo.getOrderId());
        if (order == null) {
            throw new BizException(NOT_FOUND, "订单不存在");
        }
        if (!memberInfo.getMemberId().equals(order.getMemberId())) {
            throw new BizException(NOT_ACCEPTABLE, "验证失败");
        }
        if (!order.getPayStatus().equals("0")) {
            throw new BizException(NOT_ACCEPTABLE, "支付状态有误");
        }

        //类型、总金额验证
        TbEcomPlatfShopOrder shopOrder = new TbEcomPlatfShopOrder();
        shopOrder.setOrderId(order.getOrderId());
        QueryWrapper<TbEcomPlatfShopOrder> query = new QueryWrapper<>(shopOrder);
        List<TbEcomPlatfShopOrder> shopOrders = shopOrderDao.list(query);
        if (payAmount.compareTo(order.getOrderPrice().longValue()) != 0) {
            throw new BizException(NOT_ACCEPTABLE, "订单金额不正确");
        }
        //TODO 组合订单类型判断（未完成）
        Iterator<TbEcomPlatfShopOrder> iterator = shopOrders.iterator();
        while (iterator.hasNext()) {
            String bId = iterator.next().toString();
            if (!payInfo.getTypeB().equals(bId)) {
                throw new BizException(NOT_ACCEPTABLE, "支付类型不正确");
            }
        }

        //TODO 幂等性验证

        //获取订单状态
        TbEcomPlatfOrder platfOrder = new TbEcomPlatfOrder();
        platfOrder.setLockVersion(order.getLockVersion() + 1);
        platfOrder.setPayTime(System.currentTimeMillis());
        platfOrder.setPayStatus("1");
        platfOrder.setUpdateUser("System");
        platfOrder.setUpdateTime(System.currentTimeMillis());
        //处理订单
        QueryWrapper<TbEcomPlatfOrder> wrapper = new QueryWrapper<>(order);

        boolean update = platfOrderDao.update(platfOrder, wrapper);
        if (update == false) {
            throw new BizException(500, "支付失败，请检查您的订单状态");
        }
        //修改订单状态

        return null;
    }


    private Long getPayAmount(PayInfo payInfo) {
        String typeA = payInfo.getTypeA();
        String typeB = payInfo.getTypeB();
        Long costA = payInfo.getCostA();
        Long costB = payInfo.getCostB();
        Long sum = costA + costB;
        if (StringUtils.isAnyEmpty(typeA, typeB)) {
            throw new BizException(NOT_ACCEPTABLE, "参数异常");
        }
        if (costA < 0 || costB < 0 || payInfo.getShipPrice()<0 || sum < costA || sum < costB) {
            throw new BizException(NOT_ACCEPTABLE, "参数异常");
        }
        return sum;
    }


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
        product.stream().forEach(item -> orderPrice.updateAndGet(v -> v + item.getProductPrice()));
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
            throw new BizException(NOT_ACCEPTABLE, "有一些商品下架了");
        }
        if ("0".equals(sku.getProductEnable())) {
            throw new BizException(NOT_ACCEPTABLE, "有一些商品下架了");
        }
        //TODO 使用Redis记录库存
        Integer enableStore = sku.getEnableStore();
        if (amount == null || enableStore == null || amount.compareTo(enableStore) < 0) {
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
        productItem.setProductPrice(Integer.valueOf(sku.getGoodsPrice()));
        productItem.setDataStat("0");
        //记录操作
        productItem.setCreateTime(System.currentTimeMillis());
        productItem.setCreateUser("ShopSystem");
        return productItem;
    }


}
