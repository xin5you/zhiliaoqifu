package com.ebeijia.zl.web.oms.retailChnl.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ebeijia.zl.basics.system.domain.User;
import com.ebeijia.zl.common.utils.IdUtil;
import com.ebeijia.zl.common.utils.constants.Constants;
import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.enums.*;
import com.ebeijia.zl.common.utils.http.HttpClientUtil;
import com.ebeijia.zl.common.utils.tools.*;
import com.ebeijia.zl.core.redis.utils.JedisClusterUtils;
import com.ebeijia.zl.facade.account.req.AccountRechargeReqVo;
import com.ebeijia.zl.facade.account.req.AccountTxnVo;
import com.ebeijia.zl.facade.account.service.AccountQueryFacade;
import com.ebeijia.zl.facade.account.service.AccountTransactionFacade;
import com.ebeijia.zl.facade.telrecharge.domain.ProviderOrderInf;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlInf;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlItemList;
import com.ebeijia.zl.facade.telrecharge.domain.RetailChnlOrderInf;
import com.ebeijia.zl.facade.telrecharge.resp.TeleRespVO;
import com.ebeijia.zl.facade.telrecharge.service.ProviderOrderInfFacade;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlInfFacade;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlItemListFacade;
import com.ebeijia.zl.facade.telrecharge.service.RetailChnlOrderInfFacade;
import com.ebeijia.zl.facade.telrecharge.utils.TeleConstants;
import com.ebeijia.zl.facade.telrecharge.utils.TeleConstants.ReqMethodCode;
import com.ebeijia.zl.web.oms.batchOrder.model.BatchOrderList;
import com.ebeijia.zl.web.oms.batchOrder.service.BatchOrderService;
import com.ebeijia.zl.web.oms.common.model.FTPImageVo;
import com.ebeijia.zl.web.oms.common.service.CommonService;
import com.ebeijia.zl.web.oms.common.util.OmsEnum.BatchOrderStat;
import com.ebeijia.zl.web.oms.common.util.OrderConstants;
import com.ebeijia.zl.web.oms.inaccount.model.InaccountOrder;
import com.ebeijia.zl.web.oms.inaccount.model.InaccountOrderDetail;
import com.ebeijia.zl.web.oms.inaccount.service.InaccountOrderDetailService;
import com.ebeijia.zl.web.oms.inaccount.service.InaccountOrderService;
import com.ebeijia.zl.web.oms.retailChnl.service.RetailChnlInfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("retailChnlInfService")
public class RetailChnlInfServiceImpl implements RetailChnlInfService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${FILE_UPLAOD_PATH}")
    private String FILE_UPLAOD_PATH;

    @Value("${FILE_UPLAOD_SEPARATOR}")
    private String FILE_UPLAOD_SEPARATOR;

    @Value("${IMG_SERVER}")
    private String IMG_SERVER;

    @Value("${FILE_NEW_PATH}")
    private String FILE_NEW_PATH;

    @Autowired
    private RetailChnlOrderInfFacade retailChnlOrderInfFacade;

    @Autowired
    private RetailChnlInfFacade retailChnlInfFacade;

    @Autowired
    private ProviderOrderInfFacade providerOrderInfFacade;

    @Autowired
    private RetailChnlItemListFacade retailChnlItemListFacade;

    @Autowired
    private AccountQueryFacade accountQueryFacade;

    @Autowired
    private BatchOrderService batchOrderService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private InaccountOrderService inaccountOrderService;

    @Autowired
    private InaccountOrderDetailService inaccountOrderDetailService;

    @Autowired
    private AccountTransactionFacade accountTransactionFacade;

    @Autowired
    @Qualifier("jedisClusterUtils")
    private JedisClusterUtils jedisClusterUtils;

    @Override
    public ModelMap doCallBackNotifyChannel(String channelOrderId) {
        ModelMap resultMap = new ModelMap();
        resultMap.addAttribute("status", Boolean.TRUE);
        // 回调通知分销商
        try {
            if (StringUtil.isNullOrEmpty(channelOrderId)) {
                resultMap.addAttribute("status", Boolean.FALSE);
                resultMap.addAttribute("msg", "该订单回调异常，请联系管理员");
                logger.error("## 该分销商订单回调异常，分销商订单号channelOrderId:[{}]是空", channelOrderId);
                return resultMap;
            }
            RetailChnlOrderInf retailChnlOrderInf = retailChnlOrderInfFacade.getRetailChnlOrderInfById(channelOrderId);
            if (StringUtil.isNullOrEmpty(retailChnlOrderInf.getNotifyUrl())) {
                resultMap.addAttribute("status", Boolean.FALSE);
                resultMap.addAttribute("msg", "该订单不能回调，回调地址是空");
                logger.error("## 该分销商订单不能回调，回调地址是空NotifyUrl:[{}]", retailChnlOrderInf.getNotifyUrl());
                return resultMap;
            }
            RetailChnlInf retailChnlInf = retailChnlInfFacade.getRetailChnlInfById(retailChnlOrderInf.getChannelId());

            ProviderOrderInf providerOrderInf = providerOrderInfFacade
                    .getOrderInfByChannelOrderId(channelOrderId);
            // 异步通知供应商
            TeleRespVO respVo = new TeleRespVO();
            respVo.setSaleAmount(retailChnlOrderInf.getPayAmt().toString());
            respVo.setChannelOrderId(retailChnlOrderInf.getChannelOrderId());
            respVo.setPayState(retailChnlOrderInf.getOrderStat());
            respVo.setRechargeState(providerOrderInf.getRechargeState()); // 充值状态
            if (providerOrderInf.getOperateTime() != null) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String operateTimeStr = format.format(providerOrderInf.getOperateTime());
                Date operateTimeDate = format.parse(operateTimeStr);
                respVo.setOperateTime(DateUtil.COMMON_FULL.getDateText(operateTimeDate));
            }
            respVo.setOrderTime(DateUtil.COMMON_FULL.getDateText(new Date(retailChnlOrderInf.getCreateTime()))); // 操作时间
            respVo.setFacePrice(retailChnlOrderInf.getRechargeValue().toString());
            respVo.setItemNum(retailChnlOrderInf.getItemNum());
            respVo.setOuterTid(retailChnlOrderInf.getOuterTid());
            respVo.setChannelId(retailChnlOrderInf.getChannelId());
            respVo.setChannelToken(retailChnlInf.getChannelCode());
            respVo.setV(retailChnlOrderInf.getAppVersion());
            respVo.setTimestamp(DateUtil.COMMON_FULL.getDateText(new Date()));
            respVo.setSubErrorCode(providerOrderInf.getResv1());

            if ("1".equals(retailChnlOrderInf.getRechargeType())) {
                respVo.setMethod(ReqMethodCode.R1.getValue());
            } else if ("2".equals(retailChnlOrderInf.getRechargeType())) {
                respVo.setMethod(ReqMethodCode.R2.getValue());
            }
            String psotToken = MD5SignUtils.genSign(respVo, "key", retailChnlInf.getChannelKey(),
                    new String[] { "sign", "serialVersionUID" }, null);
            respVo.setSign(psotToken);

            // 修改通知后 分销商的处理状态
            logger.info("##发起分销商回调[{}],返回参数:[{}]", retailChnlOrderInf.getNotifyUrl(),
                    JSONObject.toJSONString(ResultsUtil.success(respVo)));
            String result = HttpClientUtil.sendPost(retailChnlOrderInf.getNotifyUrl(),
                    JSONObject.toJSONString(ResultsUtil.success(respVo)));
            logger.info("## 发起分销商回调,返回结果:[{}]", result);
            if (result != null && "SUCCESS ".equals(result.toUpperCase())) {
                retailChnlOrderInf.setNotifyStat(TeleConstants.ChannelOrderNotifyStat.ORDER_NOTIFY_3.getCode());
            } else {
                retailChnlOrderInf.setNotifyStat(TeleConstants.ChannelOrderNotifyStat.ORDER_NOTIFY_2.getCode());
            }
            retailChnlOrderInfFacade.updateRetailChnlOrderInf(retailChnlOrderInf);
        } catch (Exception ex) {
            logger.error("## 手机充值 回调通知分销商异常-->{}", ex);
        }
        return resultMap;
    }

    @Override
    public boolean addRetailChnlRate(HttpServletRequest req, String channelId,String channelRate, String ids) {
        try {
            String[] productIds = ids.split(",");
            for (int i = 0; i < productIds.length; i++) {
                RetailChnlItemList retailChnlItemList = new RetailChnlItemList();
                HttpSession session = req.getSession();
                User user = (User) session.getAttribute(Constants.SESSION_USER);
                if (user != null) {
                    retailChnlItemList.setCreateUser(user.getId().toString());
                    retailChnlItemList.setUpdateUser(user.getId().toString());
                }
                retailChnlItemList.setProductId(productIds[i]);
                retailChnlItemList.setDataStat("0");
                retailChnlItemList.setChannelRate(new BigDecimal(channelRate));
                retailChnlItemList.setAreaId(channelId);
                retailChnlItemListFacade.saveRetailChnlItemList(retailChnlItemList);
            }
        } catch (Exception e) {
            logger.error("## 添加分销商折扣率失败", e);
            return false;
        }
        return true;
    }

    @Override
    public int retailChnlOpenAccount(HttpServletRequest req) {
        String channelId = StringUtil.nullToString(req.getParameter("channelId"));
        RetailChnlInf retailChnl = null;
        try {
            retailChnl = retailChnlInfFacade.getRetailChnlInfById(channelId);
            if (StringUtil.isNullOrEmpty(retailChnl)) {
                logger.error("## 查询分销商信息失败，channelId--->{}", channelId);
                return 0;
            }
        } catch (Exception e) {
            logger.error("## 查询分销商{}信息失败", channelId);
            return 0;
        }

        HttpSession session = req.getSession();
        User user = (User)session.getAttribute(Constants.SESSION_USER);

        BatchOrderList orderList = new BatchOrderList();
        orderList.setUserName(retailChnl.getChannelName());
        orderList.setPhoneNo(retailChnl.getPhoneNo());
        orderList.setAccountType(UserType.TYPE400.getCode());

        LinkedList<BatchOrderList> batchOrderList = new LinkedList<>();
        batchOrderList.add(orderList);

        int orderResult = batchOrderService.addBatchOrderAndOrderList(req, batchOrderList, TransCode.MB80.getCode(), UserType.TYPE400.getCode(), null);
        if (orderResult < 0) {
            logger.error("## 新增分销商开户订单信息失败");
            return 0;
        }

        String orderId = jedisClusterUtils.get(OrderConstants.retailChnlOrderIdSession);
        int i = batchOrderService.batchOpenAccountITF(orderId, user, BatchOrderStat.BatchOrderStat_30.getCode());
        if (i < 1) {
            logger.error("## 调用开户接口失败");
            return 0;
        }
        jedisClusterUtils.del(OrderConstants.retailChnlOrderIdSession);

        try {
            retailChnl.setIsOpen(IsOpenAccountEnum.ISOPEN_TRUE.getCode());
            if (!retailChnlInfFacade.updateRetailChnlInf(retailChnl)) {
                logger.error("## 更新分销商{}开户成功状态失败", channelId);
                return 0;
            }
        } catch (Exception e) {
            logger.error("## 更新分销商{}开户状态失败", channelId);
        }
        return 1;
    }

    @Override
    public Map<String, Object> addRetailChnlTransfer(HttpServletRequest req, MultipartFile evidenceUrlFile) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        HttpSession session = req.getSession();
        User user = (User)session.getAttribute(Constants.SESSION_USER);

        String channelId = StringUtil.nullToString(req.getParameter("channelId"));
       /* String remitAmt = StringUtil.nullToString(req.getParameter("remitAmt"));
        String evidenceUrl = StringUtil.nullToString(req.getParameter("evidenceUrl"));*/
        String inaccountAmt = StringUtil.nullToString(req.getParameter("inaccountAmt"));
        String A01 = StringUtil.nullToString(req.getParameter("A01"));
        String remarks = StringUtil.nullToString(req.getParameter("remarks"));

        Map<String, String> bMap = new HashMap<>();
        bMap.put("A01", A01);
        /*bMap.put("A00", A00);
        bMap.put("B01", B01);
        bMap.put("B02", B02);
        bMap.put("B03", B03);
        bMap.put("B04", B04);
        bMap.put("B05", B05);
        bMap.put("B06", B06);
        bMap.put("B07", B07);
        bMap.put("B08", B08);*/

        BigDecimal inaccountAmtSum = new BigDecimal(0);
        for(Map.Entry<String, String> entry : bMap.entrySet()) {
            if (!StringUtil.isNullOrEmpty(entry.getValue())) {
                inaccountAmtSum = inaccountAmtSum.add(new BigDecimal(entry.getValue()));
            }
        }

        if (inaccountAmtSum.compareTo(new BigDecimal(inaccountAmt)) != 0) {
            logger.error("## 分销商{}上账金额{}与专项金额{}不一致", channelId, inaccountAmt, inaccountAmtSum);
            resultMap.put("status", Boolean.FALSE);
            resultMap.put("msg", "添加上账信息失败，供应商上账金额不正确");
            return resultMap;
        }

        InaccountOrder order = new InaccountOrder();
        order.setOrderId(IdUtil.getNextId());
        order.setOrderType(UserType.TYPE400.getCode());
        order.setCheckStat(CheckStatEnum.CHECK_FALSE.getCode());
        order.setRemitAmt(new BigDecimal(NumberUtils.RMBYuanToCent(inaccountAmt)));
        order.setInaccountSumAmt(new BigDecimal(NumberUtils.RMBYuanToCent(inaccountAmt)));
        order.setCompanyInSumAmt(order.getInaccountSumAmt());
        order.setProviderId(channelId);
        order.setRemitCheck(RemitCheckEnum.REMIT_TRUE.getCode());
        order.setInaccountCheck(InaccountCheckEnum.INACCOUNT_FALSE.getCode());
       /* order.setTransferCheck(TransferCheckEnum.INACCOUNT_FALSE.getCode());*/
        order.setCompanyReceiverCheck(ReceiverEnum.RECEIVER_FALSE.getCode());
        order.setRemarks(remarks);
        order.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
        order.setCreateUser(user.getId());
        order.setUpdateUser(user.getId());
        order.setCreateTime(System.currentTimeMillis());
        order.setUpdateTime(System.currentTimeMillis());
        order.setLockVersion(0);

        if (evidenceUrlFile == null || evidenceUrlFile.isEmpty()) {
            logger.error("## 上传图片为空");
            resultMap.put("status", Boolean.FALSE);
            resultMap.put("msg", "上传凭证图片为空");
            return resultMap;
        }

        resultMap = commonService.uploadImage(evidenceUrlFile, req, ImageTypeEnum.ImageTypeEnum_11.getValue(), order.getOrderId());
        if (!String.valueOf(resultMap.get("status").toString()).equals("true")) {
            logger.error("## 图片上传失败，msg--->{}", resultMap.get("msg"));
            order.setEvidenceUrl("");
            resultMap.put("status", Boolean.FALSE);
            resultMap.put("msg", "凭证图片上传失败");
            return resultMap;
        }
        order.setEvidenceUrl(resultMap.get("msg").toString());

        List<InaccountOrderDetail> orderDetailList = new ArrayList<InaccountOrderDetail>();
        for(Map.Entry<String, String> entry : bMap.entrySet()) {
            if (!StringUtil.isNullOrEmpty(entry.getValue())) {
                InaccountOrderDetail orderDetail = new InaccountOrderDetail();
                orderDetail.setOrderListId(IdUtil.getNextId());
                orderDetail.setOrderId(order.getOrderId());
                orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
                orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(entry.getValue())));
                orderDetail.setInaccountAmt(orderDetail.getTransAmt());
                orderDetail.setCompanyInAmt(orderDetail.getTransAmt());
                orderDetail.setBId(entry.getKey());
                orderDetail.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
                orderDetail.setCreateUser(user.getId());
                orderDetail.setUpdateUser(user.getId());
                orderDetail.setCreateTime(System.currentTimeMillis());
                orderDetail.setUpdateTime(System.currentTimeMillis());
                orderDetail.setLockVersion(0);
                orderDetailList.add(orderDetail);
            }
        }

        BigDecimal remitInAmtSum = orderDetailList.stream().map(InaccountOrderDetail::getTransAmt).reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setRemitAmt(remitInAmtSum);
        order.setInaccountSumAmt(new BigDecimal(NumberUtils.RMBYuanToCent(inaccountAmtSum.toString())));

        if (!inaccountOrderService.save(order)) {
            logger.error("## 新增上账订单信息失败");
            resultMap.put("status", Boolean.FALSE);
            resultMap.put("msg", "添加上账订单信息失败");
            return resultMap;
        }

        if (!inaccountOrderDetailService.saveBatch(orderDetailList)) {
            logger.error("## 新增上账订单明细失败");
            resultMap.put("status", Boolean.FALSE);
            resultMap.put("msg", "添加上账订单明细信息失败");
            return resultMap;
        }

        resultMap.put("status", Boolean.TRUE);
        return resultMap;
    }

    @Override
    public int addRetailChnlTransferCommit(HttpServletRequest req) {
        String channelId = StringUtil.nullToString(req.getParameter("channelId"));
        String orderId = StringUtil.nullToString(req.getParameter("orderId"));

        InaccountOrder order = inaccountOrderService.getById(orderId);
        List<InaccountOrderDetail> orderDetail = inaccountOrderDetailService.getInaccountOrderDetailByOrderId(orderId);

        if (order == null || orderDetail == null || orderDetail.size() < 1) {
            logger.error("## 查询分销商{}上账订单{}信息为空", channelId, orderId);
            return 0;
        }

        List<AccountTxnVo> transList = new ArrayList<>();
        Set<String> bIds = new TreeSet<>();
        for (InaccountOrderDetail d : orderDetail) {
            AccountTxnVo txnVo = new AccountTxnVo();
            txnVo.setTxnBId(d.getBId());
            txnVo.setTxnAmt(d.getInaccountAmt());
            txnVo.setUpLoadAmt(d.getInaccountAmt());
            transList.add(txnVo);
            bIds.add(d.getBId());
        }

        AccountRechargeReqVo reqVo = new AccountRechargeReqVo();
        reqVo.setFromCompanyId(channelId);
        reqVo.setTransAmt(order.getInaccountSumAmt());
        reqVo.setUploadAmt(order.getInaccountSumAmt());
        reqVo.setTransList(transList);
        reqVo.setTransId(TransCode.MB20.getCode());
        reqVo.setTransChnl(TransChnl.CHANNEL0.toString());
        reqVo.setUserId(channelId);
        reqVo.setbIds(bIds);
        reqVo.setUserType(UserType.TYPE400.getCode());
        reqVo.setDmsRelatedKey(orderId);
        reqVo.setUserChnlId(channelId);
        reqVo.setUserChnl(UserChnlCode.USERCHNL1001.getCode());
        reqVo.setTransDesc(order.getRemarks());
        reqVo.setTransNumber(1);
        logger.info("分销商充值，远程调用充值接口请求参数--->{}", JSONArray.toJSONString(reqVo));
        BaseResult result = new BaseResult();
        try {
            result = accountTransactionFacade.executeRecharge(reqVo);
        } catch (Exception e) {
            logger.error("## 分销商充值，远程调用充值接口异常", e);
        }
        logger.info("分销商充值，远程调用充值接口返回参数--->{}", JSONArray.toJSONString(result));
        try {
            if (StringUtil.isNullOrEmpty(result.getCode())) {
                logger.info("分销商充值，远程调用查询接口请求参数--->dmsRelatedKey{},transChnl{}", reqVo.getDmsRelatedKey(), reqVo.getTransChnl());
                result = accountTransactionFacade.executeQuery(reqVo.getDmsRelatedKey(), reqVo.getTransChnl());
                logger.info("分销商充值，远程调用查询接口返回参数--->{}", JSONArray.toJSONString(result));
            }
        } catch (Exception e) {
            logger.error("## 分销商充值，远程调用查询接口出错,入参--->dmsRelatedKey{},transChnl{}", reqVo.getDmsRelatedKey(), reqVo.getTransChnl(), e);
            return 0;
        }

        if (result == null) {
            logger.error("## 分销商充值，远程调用充值接口失败，返回参数为空，orderId--->{},channelId--->{}", orderId, channelId);
            return 0;
        }

        if (result.getCode().equals(Constants.SUCCESS_CODE)) {
            order.setInaccountCheck(InaccountCheckEnum.INACCOUNT_TRUE.getCode());
            order.setCompanyReceiverCheck(ReceiverEnum.RECEIVER_TRUE.getCode());
        } else {
            order.setInaccountCheck(InaccountCheckEnum.INACCOUNT_FALSE.getCode());
        }

        if (!inaccountOrderService.updateById(order)) {
            logger.error("## 远程调用充值接口，更新订单失败orderId--->{},channelId--->{}", orderId, channelId);
            return 0;
        }
        return 1;
    }

    @Override
    public Map<String, Object> editRetailChnlTransfer(HttpServletRequest req, MultipartFile evidenceUrlFile) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        HttpSession session = req.getSession();
        User user = (User)session.getAttribute(Constants.SESSION_USER);

        String orderId = StringUtil.nullToString(req.getParameter("orderId"));
        String channelId = StringUtil.nullToString(req.getParameter("channelId"));
       /* String remitAmt = StringUtil.nullToString(req.getParameter("remitAmt"));*/
        String evidenceUrl = StringUtil.nullToString(req.getParameter("evidenceUrl"));
        String inaccountAmt = StringUtil.nullToString(req.getParameter("inaccountAmt"));
        String A01 = StringUtil.nullToString(req.getParameter("A01"));
        /*String A00 = StringUtil.nullToString(req.getParameter("A00"));
        String B01 = StringUtil.nullToString(req.getParameter("B01"));
        String B02 = StringUtil.nullToString(req.getParameter("B02"));
        String B03 = StringUtil.nullToString(req.getParameter("B03"));
        String B04 = StringUtil.nullToString(req.getParameter("B04"));
        String B05 = StringUtil.nullToString(req.getParameter("B05"));
        String B06 = StringUtil.nullToString(req.getParameter("B06"));
        String B07 = StringUtil.nullToString(req.getParameter("B07"));
        String B08 = StringUtil.nullToString(req.getParameter("B08"));*/
        String remarks = StringUtil.nullToString(req.getParameter("remarks"));

        Map<String, String> bMap = new HashMap<>();
        bMap.put("A01", A01);
        /*bMap.put("A00", A00);
        bMap.put("B01", B01);
        bMap.put("B02", B02);
        bMap.put("B03", B03);
        bMap.put("B04", B04);
        bMap.put("B05", B05);
        bMap.put("B06", B06);
        bMap.put("B07", B07);
        bMap.put("B08", B08);*/

        BigDecimal inaccountAmtSum = new BigDecimal(0);
        for(Map.Entry<String, String> entry : bMap.entrySet()) {
            if (!StringUtil.isNullOrEmpty(entry.getValue())) {
                inaccountAmtSum = inaccountAmtSum.add(new BigDecimal(entry.getValue()));
            }
        }

        if (inaccountAmtSum.compareTo(new BigDecimal(inaccountAmt)) != 0) {
            logger.error("## 分销商{}上账金额{}与专项金额{}不不一致", channelId, inaccountAmt, inaccountAmtSum);
            resultMap.put("status", Boolean.FALSE);
            resultMap.put("msg", "编辑上账信息失败，上账金额不正确");
            return resultMap;
        }

        InaccountOrder orderInf = new InaccountOrder();
        orderInf.setOrderId(orderId);
        orderInf.setOrderType(UserType.TYPE400.getCode());
        InaccountOrder order = inaccountOrderService.getInaccountOrderByOrderId(orderInf);
        if (order == null) {
            logger.error("## 根据上账订单号{}查询订单信息为空", orderId);
            resultMap.put("status", Boolean.FALSE);
            resultMap.put("msg", "编辑上账信息失败，查询上帐订单信息为空");
            return resultMap;
        }
        /* order.setRemitAmt(new BigDecimal(NumberUtils.RMBYuanToCent(remitAmt)));*/
        /*order.setInaccountAmt(new BigDecimal(NumberUtils.RMBYuanToCent(inaccountAmt)));*/
        order.setRemarks(remarks);
        order.setUpdateUser(user.getId());
        order.setUpdateTime(System.currentTimeMillis());
        order.setLockVersion(order.getLockVersion() + 1);

        if (evidenceUrlFile != null && !evidenceUrlFile.isEmpty()) {
            resultMap = commonService.uploadImage(evidenceUrlFile, req, ImageTypeEnum.ImageTypeEnum_11.getValue(), order.getOrderId());
            if (!String.valueOf(resultMap.get("status").toString()).equals("true")) {
                logger.error("## 图片上传失败，msg--->{}", resultMap.get("msg"));
                order.setEvidenceUrl("");
                resultMap.put("status", Boolean.FALSE);
                resultMap.put("msg", "上传凭证图片为空");
                return resultMap;
            } else {
                order.setEvidenceUrl(resultMap.get("msg").toString());
            }
        }

        List<InaccountOrderDetail> editOrderDetailList = new ArrayList<>();
        List<InaccountOrderDetail> addOrderDetailList = new ArrayList<>();
        List<InaccountOrderDetail> delOrderDetailList = new ArrayList<>();

        for(Map.Entry<String, String> entry : bMap.entrySet()) {
            if (!StringUtil.isNullOrEmpty(entry.getValue())) {
                InaccountOrderDetail detail = new InaccountOrderDetail();
                detail.setBId(entry.getKey());
                detail.setOrderId(order.getOrderId());
                InaccountOrderDetail orderDetail = inaccountOrderDetailService.getInaccountOrderDetailByOrderIdAndBid(detail);

                if (orderDetail == null) {
                    if (!StringUtil.isNullOrEmpty(entry.getValue())) {
                        orderDetail = new InaccountOrderDetail();
                        orderDetail.setOrderListId(IdUtil.getNextId());
                        orderDetail.setOrderId(order.getOrderId());
                        orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
                        orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(entry.getValue())));
                        orderDetail.setInaccountAmt(orderDetail.getTransAmt());
                        orderDetail.setCompanyInAmt(orderDetail.getTransAmt());
                        orderDetail.setBId(entry.getKey());
                        orderDetail.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
                        orderDetail.setCreateUser(user.getId());
                        orderDetail.setUpdateUser(user.getId());
                        orderDetail.setCreateTime(System.currentTimeMillis());
                        orderDetail.setUpdateTime(System.currentTimeMillis());
                        orderDetail.setLockVersion(0);
                        addOrderDetailList.add(orderDetail);
                    }
                } else {
                    if (!StringUtil.isNullOrEmpty(entry.getValue())) {
                        orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(entry.getValue())));
                        orderDetail.setInaccountAmt(orderDetail.getTransAmt());
                        orderDetail.setUpdateUser(user.getId());
                        orderDetail.setUpdateTime(System.currentTimeMillis());
                        orderDetail.setLockVersion(orderDetail.getLockVersion() + 1);
                        editOrderDetailList.add(orderDetail);
                    } else {
                        delOrderDetailList.add(orderDetail);
                    }
                }
            }
        }

        BigDecimal remitInAmtSum = new BigDecimal(0);
        BigDecimal companyInAmtSum = new BigDecimal(0);
        if (addOrderDetailList != null && addOrderDetailList.size() >= 1) {
            BigDecimal addOrderDetailCompanyInAmtSum = addOrderDetailList.stream().map(InaccountOrderDetail::getCompanyInAmt).reduce(BigDecimal.ZERO, BigDecimal::add);
            companyInAmtSum = addOrderDetailCompanyInAmtSum;
            BigDecimal addOrderDetailRemitInAmtSum = addOrderDetailList.stream().map(InaccountOrderDetail::getTransAmt).reduce(BigDecimal.ZERO, BigDecimal::add);
            remitInAmtSum = addOrderDetailRemitInAmtSum;
            if (!inaccountOrderDetailService.saveBatch(addOrderDetailList)) {
                logger.error("## 编辑上账订单明细失败");
                resultMap.put("status", Boolean.FALSE);
                resultMap.put("msg", "编辑上账订单明细信息失败");
                return resultMap;
            }
        }

        if (editOrderDetailList != null && editOrderDetailList.size() >= 1) {
            BigDecimal editOrderDetailCompanyInAmtSum = editOrderDetailList.stream().map(InaccountOrderDetail::getCompanyInAmt).reduce(BigDecimal.ZERO, BigDecimal::add);
            companyInAmtSum = companyInAmtSum.add(editOrderDetailCompanyInAmtSum);
            BigDecimal editOrderDetailRemitInAmtSum = editOrderDetailList.stream().map(InaccountOrderDetail::getTransAmt).reduce(BigDecimal.ZERO, BigDecimal::add);
            remitInAmtSum = remitInAmtSum.add(editOrderDetailRemitInAmtSum);
            if (!inaccountOrderDetailService.saveOrUpdateBatch(editOrderDetailList)) {
                logger.error("## 编辑上账订单明细失败");
                resultMap.put("status", Boolean.FALSE);
                resultMap.put("msg", "编辑上账订单明细信息失败");
                return resultMap;
            }
        }

        if (delOrderDetailList != null && delOrderDetailList.size() >= 1) {
            for (InaccountOrderDetail d : delOrderDetailList) {
                if (!inaccountOrderDetailService.removeById(d)) {
                    logger.error("## 删除上账订单明细失败");
                    resultMap.put("status", Boolean.FALSE);
                    resultMap.put("msg", "编辑上账订单明细信息失败");
                    return resultMap;
                }
            }
        }

        order.setRemitAmt(remitInAmtSum);
        order.setCompanyInSumAmt(companyInAmtSum);
        order.setInaccountSumAmt(new BigDecimal(NumberUtils.RMBYuanToCent(inaccountAmtSum.toString())));
        if (!inaccountOrderService.updateById(order)) {
            logger.error("## 更新上账订单信息失败");
            resultMap.put("status", Boolean.FALSE);
            resultMap.put("msg", "编辑上账订单信息失败");
            return resultMap;
        }

        resultMap.put("status", Boolean.TRUE);
        return resultMap;
    }

    @Override
    public Map<String, Object> deleteRetailChnlTransfer(HttpServletRequest req) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("status", Boolean.TRUE);

        HttpSession session = req.getSession();
        User user = (User)session.getAttribute(Constants.SESSION_USER);

        String orderId = StringUtil.nullToString(req.getParameter("orderId"));

        InaccountOrder orderInf = new InaccountOrder();
        orderInf.setOrderId(orderId);
        orderInf.setOrderType(UserType.TYPE400.getCode());
        InaccountOrder order = inaccountOrderService.getInaccountOrderByOrderId(orderInf);
        List<InaccountOrderDetail> orderDetailList = inaccountOrderDetailService.getInaccountOrderDetailByOrderId(orderId);

        if (order == null || orderDetailList == null || orderDetailList.size() < 1) {
            resultMap.put("status", Boolean.FALSE);
            resultMap.put("msg", "数据不存在，请重新查看订单信息");
            return resultMap;
        }

        order.setDataStat(DataStatEnum.FALSE_STATUS.getCode());
        order.setUpdateTime(System.currentTimeMillis());
        order.setUpdateUser(user.getId());
        order.setLockVersion(order.getLockVersion() + 1);
        for (InaccountOrderDetail orderDetail : orderDetailList) {
            orderDetail.setDataStat(DataStatEnum.FALSE_STATUS.getCode());
            orderDetail.setUpdateTime(System.currentTimeMillis());
            orderDetail.setUpdateUser(user.getId());
            orderDetail.setLockVersion(orderDetail.getLockVersion() + 1);
        }
        if (!inaccountOrderService.saveOrUpdate(order)) {
            resultMap.put("status", Boolean.FALSE);
            resultMap.put("msg", "删除失败，请稍后再试");
            return resultMap;
        }
        if (!inaccountOrderDetailService.saveOrUpdateBatch(orderDetailList)) {
            resultMap.put("status", Boolean.FALSE);
            resultMap.put("msg", "删除失败，请稍后再试");
            return resultMap;
        }

        return resultMap;
    }

}
