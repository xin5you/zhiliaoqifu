package com.ebeijia.zl.web.oms.withdraw.controller;

import com.ebeijia.zl.common.utils.tools.NumberUtils;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawDetail;
import com.ebeijia.zl.facade.account.dto.AccountWithdrawOrder;
import com.ebeijia.zl.facade.account.service.AccountWithDrawOrderFacade;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlInf;
import com.ebeijia.zl.web.oms.withdraw.service.WithdrawOrderService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "withdraw")
public class WithdrawController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WithdrawOrderService withdrawOrderService;

    @Autowired
    private AccountWithDrawOrderFacade accountWithDrawOrderFacade;

    /**
     * 查看提现订单信息
     * @param req
     * @param response
     * @return
     */
    @RequestMapping(value = "/listWithdrawOrder")
    public ModelAndView listWithdrawOrder(HttpServletRequest req, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("withdraw/listWithdrawOrder");

        int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
        int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);

        PageInfo<AccountWithdrawOrder> pageList = null;
        try {
            pageList = accountWithDrawOrderFacade.getWithdrawOrderPage(startNum, pageSize, new AccountWithdrawOrder());
        } catch (Exception e) {
            logger.error(" ## 查询提现订单信息列表出错",e);
        }
        mv.addObject("pageInfo", pageList);
        return mv;
    }

    /**
     * 订单明细列表（含分页）
     * @param req
     * @param response
     * @return
     */
    @RequestMapping(value = "/listWithdrawDetail")
    public ModelAndView listWithdrawDetail(HttpServletRequest req, HttpServletResponse response) {
        ModelAndView mv = new ModelAndView("withdraw/listWithdrawDetail");

        int startNum = NumberUtils.parseInt(req.getParameter("pageNum"), 1);
        int pageSize = NumberUtils.parseInt(req.getParameter("pageSize"), 10);

        String batchNo = req.getParameter("batchNo");
        AccountWithdrawDetail withdrawDetail = new AccountWithdrawDetail();
        withdrawDetail.setBatchNo(batchNo);

        PageInfo<AccountWithdrawDetail> pageList = null;
        try {
            pageList = accountWithDrawOrderFacade.getWithdrawDetailPage(startNum, pageSize, withdrawDetail);
        } catch (Exception e) {
            logger.error(" ## 查询提现订单明细信息列表出错",e);
        }
        mv.addObject("pageInfo", pageList);
        return mv;
    }

}
