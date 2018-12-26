package com.ebeijia.zl.shop.service.pay.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ebeijia.zl.common.utils.enums.UserChnlCode;
import com.ebeijia.zl.common.utils.enums.UserType;
import com.ebeijia.zl.common.utils.exceptions.BizException;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.common.utils.tools.StringUtils;
import com.ebeijia.zl.facade.account.req.AccountQueryReqVo;
import com.ebeijia.zl.facade.account.service.AccountQueryFacade;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    public PageInfo<AccountLogVO> listDeals(String session, String userId, String type, String start, String limit) {
        AccountQueryReqVo req = new AccountQueryReqVo();
        req.setUserType(UserType.TYPE100.getCode());
        req.setUserChnlId(userId);
        req.setUserChnl(UserChnlCode.USERCHNL2001.getCode());
        return accountQueryFacade.getAccountLogPage(0, 20, req);
    }


    @Override
    public List<AccountVO> listAccountDetail(String userId, String session) {
        //TODO 判断session
        return listAccountDetail(userId);
    }


    private List<AccountVO> listAccountDetail(String userId) {
        AccountQueryReqVo req = new AccountQueryReqVo();
        req.setUserType(UserType.TYPE100.getCode());
        req.setUserChnlId(userId);
        req.setUserChnl(UserChnlCode.USERCHNL2001.getCode());
        List<AccountVO> list = accountQueryFacade.getAccountInfList(req);
        //TODO 判断session
        return list;
    }

    private Map<String, Object> getAccountInfPage(HttpServletRequest req) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("status", Boolean.TRUE);
        String providerId = StringUtil.nullToString(req.getParameter("providerId"));
        String companyId = StringUtil.nullToString(req.getParameter("companyId"));
        String channelId = StringUtil.nullToString(req.getParameter("channelId"));
        int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
        int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);
        AccountQueryReqVo reqVo = new AccountQueryReqVo();
        if (!StringUtil.isNullOrEmpty(providerId)) {
            reqVo.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
            reqVo.setUserChnlId(providerId);
            reqVo.setUserType(UserType.TYPE300.getCode());
        } else if (!StringUtil.isNullOrEmpty(companyId)) {
            reqVo.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
            reqVo.setUserChnlId(companyId);
            reqVo.setUserType(UserType.TYPE300.getCode());
        } else if (!StringUtil.isNullOrEmpty(channelId)) {
            reqVo.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
            reqVo.setUserChnlId(channelId);
            reqVo.setUserType(UserType.TYPE400.getCode());
        } else {
            resultMap.put("status", Boolean.FALSE);
            resultMap.put("msg", "查询账户不正确");
            return resultMap;
        }
        try {
            PageInfo<AccountLogVO> pageList = accountQueryFacade.getAccountLogPage(startNum, pageSize, reqVo);
            resultMap.put("pageInfo", pageList);
        } catch (Exception e) {
            logger.error("## 查询账户余额列表异常");
            resultMap.put("status", Boolean.FALSE);
            resultMap.put("msg", "查询账户余额列表异常");
            return resultMap;
        }
        return resultMap;
    }


}
