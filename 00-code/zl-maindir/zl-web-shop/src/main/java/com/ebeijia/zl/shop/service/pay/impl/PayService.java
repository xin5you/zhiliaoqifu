package com.ebeijia.zl.shop.service.pay.impl;

import com.alibaba.fastjson.JSONArray;
import com.ebeijia.zl.common.utils.enums.UserChnlCode;
import com.ebeijia.zl.common.utils.enums.UserType;
import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.account.req.AccountQueryReqVo;
import com.ebeijia.zl.facade.account.service.AccountQueryFacade;
import com.ebeijia.zl.facade.account.vo.AccountLogVO;
import com.ebeijia.zl.facade.account.vo.AccountVO;
import com.ebeijia.zl.shop.service.pay.IPayService;
import com.ebeijia.zl.shop.vo.DealInfo;
import com.ebeijia.zl.shop.vo.PayInfo;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PayService implements IPayService {

    @Autowired
    private AccountQueryFacade accountQueryFacade;


    Logger logger = LoggerFactory.getLogger(PayService.class);

    @Override
    public int transferToCard(DealInfo dealInfo, Double session) {
        return (int) (Math.random()*10000);
    }

    @Override
    public void payOrder(PayInfo payInfo, String session) {
        //验证输入信息有效性
        //幂等性验证
        //写入redis乐观锁
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
        return accountQueryFacade.getAccountLogPage(0,20,req);
    }

    @Override
    public List<AccountVO> listAccountDetail(String userId, String session) {
        AccountQueryReqVo req = new AccountQueryReqVo();
        req.setUserType(UserType.TYPE100.getCode());
        req.setUserChnlId(userId);
        req.setUserChnl(UserChnlCode.USERCHNL2001.getCode());
        List<AccountVO> list=accountQueryFacade.getAccountInfList(req);
        System.out.println(JSONArray.toJSONString(list));
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
