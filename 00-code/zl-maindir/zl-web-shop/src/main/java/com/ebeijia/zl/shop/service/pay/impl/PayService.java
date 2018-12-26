package com.ebeijia.zl.shop.service.pay.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.*;
import com.ebeijia.zl.common.utils.exceptions.BizException;
import com.ebeijia.zl.common.utils.tools.StringUtils;
import com.ebeijia.zl.facade.account.req.AccountConsumeReqVo;
import com.ebeijia.zl.facade.account.req.AccountQueryReqVo;
import com.ebeijia.zl.facade.account.req.AccountTxnVo;
import com.ebeijia.zl.facade.account.service.AccountQueryFacade;
import com.ebeijia.zl.facade.account.service.AccountTransactionFacade;
import com.ebeijia.zl.facade.account.vo.AccountLogVO;
import com.ebeijia.zl.facade.account.vo.AccountVO;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomPlatfOrder;
import com.ebeijia.zl.shop.dao.order.domain.TbEcomPlatfShopOrder;
import com.ebeijia.zl.shop.dao.order.service.ITbEcomPlatfOrderService;
import com.ebeijia.zl.shop.dao.order.service.ITbEcomPlatfShopOrderService;
import com.ebeijia.zl.shop.service.pay.IPayService;
import com.ebeijia.zl.shop.utils.ShopTransactional;
import com.ebeijia.zl.shop.vo.DealInfo;
import com.ebeijia.zl.shop.vo.MemberInfo;
import com.ebeijia.zl.shop.vo.PayInfo;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.ebeijia.zl.shop.constants.ResultState.NOT_ACCEPTABLE;
import static com.ebeijia.zl.shop.constants.ResultState.NOT_FOUND;

@Service
public class PayService implements IPayService {

    @Autowired
    private AccountQueryFacade accountQueryFacade;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    ITbEcomPlatfOrderService platfOrderDao;

    @Autowired
    ITbEcomPlatfShopOrderService shopOrderDao;

    @Autowired
    AccountTransactionFacade accountTransactionFacade;

    Logger logger = LoggerFactory.getLogger(PayService.class);

    @Override
    public int transferToCard(DealInfo dealInfo, Double session) {
        return (int) (Math.random() * 10000);
    }

    @Override
    @ShopTransactional
    public void payOrder(PayInfo payInfo, String session) {
        MemberInfo memberInfo = (MemberInfo) httpSession.getAttribute("user");
        if (memberInfo == null) {
            throw new BizException(NOT_ACCEPTABLE, "参数异常");
        }
        //验证输入信息有效性，订单查询
        String typeA = payInfo.getTypeA();
        String typeB = payInfo.getTypeB();
        Long costA = payInfo.getCostA();
        Long costB = payInfo.getCostB();
        Long sum = costA + costB;
        if (StringUtils.isAnyEmpty(typeA, typeB)) {
            throw new BizException(NOT_ACCEPTABLE, "参数异常");
        }
        if (costA < 0 || costB < 0 || sum < costA || sum < costB) {
            throw new BizException(NOT_ACCEPTABLE, "参数异常");
        }

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
        if (sum.compareTo(order.getOrderPrice().longValue()) != 0) {
            throw new BizException(NOT_ACCEPTABLE, "订单金额不正确");
        }
        //TODO 组合订单类型判断（未完成）
        Iterator<TbEcomPlatfShopOrder> iterator = shopOrders.iterator();
        while (iterator.hasNext()) {
            String bId = iterator.next().toString();
            if (!typeB.equals(bId)) {
                throw new BizException(NOT_ACCEPTABLE, "支付类型不正确");
            }
        }

        //幂等性验证
        //获取订单状态
        //写入新版本号
        //处理订单
        //持久化
    }

    @Override
    public PageInfo<AccountLogVO> listDeals(String session, String openId, String type, String start, String limit) {
        AccountQueryReqVo req = new AccountQueryReqVo();
        req.setUserType(UserType.TYPE100.getCode());
        req.setUserChnlId(openId);
        req.setUserChnl(UserChnlCode.USERCHNL2001.getCode());
        return accountQueryFacade.getAccountLogPage(0, 20, req);
    }


    @Override
    public List<AccountVO> listAccountDetail(String openId, String session) {
        //TODO 判断session
        return listAccountDetail(openId);
    }


    private List<AccountVO> listAccountDetail(String openId) {
        AccountQueryReqVo req = new AccountQueryReqVo();
        req.setUserType(UserType.TYPE100.getCode());
        req.setUserChnlId(openId);
        req.setUserChnl(UserChnlCode.USERCHNL2001.getCode());
        List<AccountVO> list = accountQueryFacade.getAccountInfList(req);
        //TODO 判断session
        return list;
    }

    public void executeConsumeToRetail() throws Exception{

        AccountConsumeReqVo req=new AccountConsumeReqVo();
        req.setTransId(TransCode.MB10.getCode());
        req.setTransChnl(TransChnl.CHANNEL0.toString());
        req.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
        req.setUserChnlId("0e04cf948e2af629a334c7c71fa3f8888");
        req.setUserType(UserType.TYPE400.getCode());
        List<AccountTxnVo> objects = new LinkedList<>();
        AccountTxnVo accountTxnVo = new AccountTxnVo();
        accountTxnVo.setTxnAmt(new BigDecimal(1000));
        accountTxnVo.setUpLoadAmt(new BigDecimal(1000));
        accountTxnVo.setTxnBId("A01");

        req.setTransList(objects);


        req.setDmsRelatedKey(IdUtil.getNextId());
        req.setTransDesc("消费描述");
        BaseResult result=accountTransactionFacade.executeConsume(req);
        System.out.println(JSONArray.toJSONString(result));
    }
}
