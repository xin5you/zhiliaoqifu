package com.ebeijia.zl.shop.service.pay.impl;

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
import com.ebeijia.zl.shop.utils.AdviceMessenger;
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
import java.util.ArrayList;
import java.util.Iterator;
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
        TbEcomPlatfOrder platfOrder = new TbEcomPlatfOrder();
        platfOrder.setLockVersion(order.getLockVersion()+1);
        platfOrder.setPayTime(System.currentTimeMillis());
        platfOrder.setPayStatus("1");
        platfOrder.setUpdateUser("System");
        platfOrder.setUpdateTime(System.currentTimeMillis());
        //处理订单
        QueryWrapper<TbEcomPlatfOrder> wrapper = new QueryWrapper<>(order);

        boolean update = platfOrderDao.update(platfOrder, wrapper);
        if (update==false){
            throw new BizException(500,"支付失败，请检查您的订单状态");
        }
        //请求支付
        BaseResult result = executeConsume(payInfo, memberInfo.getOpenId(), order.getDmsRelatedKey(), "商城消费");
        //判断result

        //修改订单状态

        throw new AdviceMessenger(200,"支付成功");
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
        return list;
    }

    /**
     * 商城消费
     * @param payInfo
     * @param openId
     * @param dmsRelatedKey
     * @param desc
     * @return
     * @throws Exception
     */
    private BaseResult executeConsume(PayInfo payInfo, String openId, String dmsRelatedKey, String desc) {
        AccountConsumeReqVo req=new AccountConsumeReqVo();
        //交易与渠道
        req.setTransId(TransCode.CW10.getCode());
        req.setTransChnl(TransChnl.CHANNEL6.toString());
        //消费端用户识别
        req.setUserChnl(UserChnlCode.USERCHNL2001.getCode());
        req.setUserChnlId(openId);
        req.setUserType(UserType.TYPE100.getCode());
        req.setTransList(buildTxnVo(payInfo));
        req.setDmsRelatedKey(dmsRelatedKey);
        if (desc == null){
            desc = "商城消费";
        }
        req.setTransDesc(desc);
        BaseResult result= null;
        try {
            result = accountTransactionFacade.executeConsume(req);
        } catch (Exception e) {
            logger.error(e.getStackTrace().toString());
            throw new BizException(500,"账户系统调用异常");
        }
        return result;
    }

    /**
     * 简化的支付信息拆解
     * @param payInfo
     * @return
     */
    private List<AccountTxnVo> buildTxnVo(PayInfo payInfo) {
        ArrayList<AccountTxnVo> result = new ArrayList<>();
        SpecAccountTypeEnum typeA = SpecAccountTypeEnum.findByBId(payInfo.getTypeA());
        if (typeA!=null) {
            AccountTxnVo accountTxnVo = new AccountTxnVo();
            accountTxnVo.setTxnBId(typeA.getbId());
            accountTxnVo.setUpLoadAmt(BigDecimal.valueOf(payInfo.getCostA()));
            accountTxnVo.setTxnAmt(BigDecimal.valueOf(payInfo.getCostA()));
            result.add(accountTxnVo);
        }
        SpecAccountTypeEnum typeB = SpecAccountTypeEnum.findByBId(payInfo.getTypeB());
        if (typeB!=null) {
            AccountTxnVo accountTxnVo = new AccountTxnVo();
            accountTxnVo.setTxnBId(typeB.getbId());
            accountTxnVo.setUpLoadAmt(BigDecimal.valueOf(payInfo.getCostB()));
            accountTxnVo.setTxnAmt(BigDecimal.valueOf(payInfo.getCostB()));
            result.add(accountTxnVo);
        }
        return result;
    }
}
