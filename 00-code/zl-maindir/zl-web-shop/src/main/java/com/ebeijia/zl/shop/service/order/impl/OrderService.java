package com.ebeijia.zl.shop.service.order.impl;

import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.exceptions.BizException;
import com.ebeijia.zl.common.utils.tools.StringUtils;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoods;
import com.ebeijia.zl.shop.dao.goods.domain.TbEcomGoodsProduct;
import com.ebeijia.zl.shop.dao.goods.service.ITbEcomGoodsProductService;
import com.ebeijia.zl.shop.dao.goods.service.ITbEcomGoodsService;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomOrderInf;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomOrderProductItem;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomPlatfOrder;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomPlatfShopOrder;
import com.ebeijia.zl.shop.dao.order.service.ITbEcomOrderProductItemService;
import com.ebeijia.zl.shop.dao.order.service.ITbEcomPlatfOrderService;
import com.ebeijia.zl.shop.dao.order.service.ITbEcomPlatfShopOrderService;
import com.ebeijia.zl.shop.service.order.IOrderService;
import com.ebeijia.zl.shop.utils.AdviceMessenger;
import com.ebeijia.zl.shop.utils.ShopTransactional;
import com.ebeijia.zl.shop.vo.AddressInfo;
import com.ebeijia.zl.shop.vo.OrderItemInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

import static com.ebeijia.zl.shop.constants.ResultState.*;

import java.util.*;

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
    private HttpSession session;

    @Override
    @ShopTransactional
    public TbEcomPlatfShopOrder createSimpleOrder(OrderItemInfo orderItemInfo, AddressInfo address) {
        //获得身份信息
        String memberId = (String) session.getAttribute("memberId");
        if (StringUtils.isEmpty(memberId)) {
            throw new BizException(NOT_ACCEPTABLE, "参数异常");
        }

        //构造主订单
        TbEcomPlatfOrder platfOrder = initOrderObject(memberId);
        String orderId = platfOrder.getOrderId();


        //判断商品渠道,按照List中的内容构建多个子Map，这里的list只有一个元素
        HashMap<String, HashMap<TbEcomGoodsProduct, Integer>> itemMap = processOrderProduct(Collections.singletonList(orderItemInfo));


        //循环处理各渠道信息
        Iterator<String> iterator = itemMap.keySet().iterator();
        while (iterator.hasNext()) {
            //根据Map构造子订单，这里用了简化逻辑
            TbEcomPlatfShopOrder shopOrder = initShopOrderObject(platfOrder);

            //获取渠道代码
            String ecom = iterator.next();

            //获取商品列表和购买数量
            HashMap<TbEcomGoodsProduct, Integer> productInfo = itemMap.get(ecom);

            //构造订单商品列表,这里只有一个商品
            List<TbEcomOrderProductItem> order = processOrder(productInfo, ecom, shopOrder.getSOrderId());

            //构造订单商品物流信息
            processShipItem(order);

            //完善渠道订单

            //完善关联
        }


        //完善主订单

        //完善主订单关联

        //持久化
        //校验信息
        return null;
    }


    /**
     * 处理经过分组的商品，转化为订单商品对象
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


    private void processShipItem(List<TbEcomOrderProductItem> orderProductItemList) {

    }


    /**
     * 将传入的购买商品列表，按照渠道转化为多个Hap
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


    @Override
    public TbEcomOrderInf changeOrderState(String orderId, String state) {
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
        shopOrder.setCreateUser("System");
        return shopOrder;
    }

    private TbEcomPlatfOrder initOrderObject(String memberId) {
        TbEcomPlatfOrder platfOrder = new TbEcomPlatfOrder();
        platfOrder.setOrderId(IdUtil.getNextId());
        platfOrder.setMemberId(memberId);
        //记录操作
        platfOrder.setCreateTime(System.currentTimeMillis());
        platfOrder.setCreateUser("System");
        return platfOrder;
    }

    private TbEcomOrderProductItem initOrderProductObject(String sOrderId, TbEcomGoodsProduct sku, TbEcomGoods goods, Integer amounts) {
        TbEcomOrderProductItem productItem = new TbEcomOrderProductItem();
        productItem.setOItemId(IdUtil.getNextId());
        productItem.setSOrderId(sOrderId);
        productItem.setProductId(sku.getProductId());
        productItem.setProductName(goods.getGoodsName());
        productItem.setProductNum(amounts);
        productItem.setProductPrice(Integer.valueOf(sku.getGoodsPrice()));
        //记录操作
        productItem.setCreateTime(System.currentTimeMillis());
        productItem.setCreateUser("System");
        return productItem;
    }


}
