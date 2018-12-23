package com.ebeijia.zl.shop.service.order.impl;

import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomOrderProductItem;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomPlatfOrder;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomPlatfShopOrder;
import com.ebeijia.zl.shop.dao.order.service.ITbEcomOrderProductItemService;
import com.ebeijia.zl.shop.dao.order.service.ITbEcomPlatfOrderService;
import com.ebeijia.zl.shop.dao.order.service.ITbEcomPlatfShopOrderService;
import com.ebeijia.zl.shop.service.order.IOrderService;
import com.ebeijia.zl.shop.utils.AdviceMessenger;
import com.ebeijia.zl.shop.vo.AddressInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class OrderService implements IOrderService {

    @Autowired
    private ITbEcomPlatfOrderService platfOrderDao;

    @Autowired
    private ITbEcomPlatfShopOrderService shopOrderDao;

    @Autowired
    ITbEcomOrderProductItemService orderProductItemDao;

    @Autowired
    private Session session;

    @Override
    public TbEcomPlatfShopOrder createSimpleOrder(String productId, Integer amounts, AddressInfo address) {
        String memberId = session.getAttribute("memberId");
        TbEcomPlatfOrder platfOrder = new TbEcomPlatfOrder();
        platfOrder.setMemberId(memberId);
        platfOrder.setOrderId(IdUtil.getNextId());
        platfOrderDao.save(platfOrder);
        List<TbEcomOrderProductItem> products = new LinkedList<>();
        TbEcomOrderProductItem product = new TbEcomOrderProductItem();
        product.setProductId(productId);
        product.setProductNum(amounts);
        products.add(product);
        List<TbEcomPlatfShopOrder> shopOrderList = makeShopOrder(platfOrder,products,address);
        //校验信息
        return null;
    }

    private List<TbEcomPlatfShopOrder> makeShopOrder(TbEcomPlatfOrder platfOrder, List<TbEcomOrderProductItem> items, AddressInfo address) {
        if (platfOrder==null || items==null || address==null){
            throw new AdviceMessenger(406,"参数异常");
        }
        for (TbEcomOrderProductItem item: items){

        }
        TbEcomPlatfShopOrder shopOrder = new TbEcomPlatfShopOrder();
        //TODO
        return null;
    }
}
