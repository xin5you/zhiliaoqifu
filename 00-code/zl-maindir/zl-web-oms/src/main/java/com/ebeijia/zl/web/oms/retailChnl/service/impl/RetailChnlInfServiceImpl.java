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

        int orderResult = batchOrderService.addBatchOrderAndOrderList(req, batchOrderList, TransCode.MB80.getCode(), UserType.TYPE400.getCode());
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
    public int addRetailChnlTransfer(HttpServletRequest req, MultipartFile evidenceUrlFile) {
        HttpSession session = req.getSession();
        User user = (User)session.getAttribute(Constants.SESSION_USER);

        String channelId = StringUtil.nullToString(req.getParameter("channelId"));
       /* String remitAmt = StringUtil.nullToString(req.getParameter("remitAmt"));
        String evidenceUrl = StringUtil.nullToString(req.getParameter("evidenceUrl"));*/
        String inaccountAmt = StringUtil.nullToString(req.getParameter("inaccountAmt"));
        String A00 = StringUtil.nullToString(req.getParameter("A00"));
        String B01 = StringUtil.nullToString(req.getParameter("B01"));
        String B02 = StringUtil.nullToString(req.getParameter("B02"));
        String B03 = StringUtil.nullToString(req.getParameter("B03"));
        String B04 = StringUtil.nullToString(req.getParameter("B04"));
        String B05 = StringUtil.nullToString(req.getParameter("B05"));
        String B06 = StringUtil.nullToString(req.getParameter("B06"));
        String B07 = StringUtil.nullToString(req.getParameter("B07"));
        String B08 = StringUtil.nullToString(req.getParameter("B08"));
        String remarks = StringUtil.nullToString(req.getParameter("remarks"));

        InaccountOrder order = new InaccountOrder();
        order.setOrderId(IdUtil.getNextId());
        order.setOrderType(UserType.TYPE400.getCode());
        order.setCheckStat(CheckStatEnum.CHECK_FALSE.getCode());
       /* order.setRemitAmt(new BigDecimal(NumberUtils.RMBYuanToCent(remitAmt)));*/
        order.setInaccountAmt(new BigDecimal(NumberUtils.RMBYuanToCent(inaccountAmt)));
        order.setProviderId(channelId);
        order.setRemitCheck(RemitCheckEnum.REMIT_TRUE.getCode());
        order.setInaccountCheck(InaccountCheckEnum.INACCOUNT_FALSE.getCode());
        order.setTransferCheck(TransferCheckEnum.INACCOUNT_FALSE.getCode());
        order.setRemarks(remarks);
        order.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
        order.setCreateUser(user.getId());
        order.setUpdateUser(user.getId());
        order.setCreateTime(System.currentTimeMillis());
        order.setUpdateTime(System.currentTimeMillis());
        order.setLockVersion(0);

        /*if (evidenceUrlFile == null || evidenceUrlFile.isEmpty()) {
            logger.error("## 上传图片为空");
            return 0;
        }
        FTPImageVo imgVo = new FTPImageVo();
        imgVo.setImgId(order.getOrderId());
        imgVo.setService(IMG_SERVER);
        imgVo.setNewPath(FILE_NEW_PATH);
        imgVo.setSeparator(FILE_UPLAOD_SEPARATOR);
        imgVo.setUploadPath(FILE_UPLAOD_PATH);
        imgVo.setImgType(ImageTypeEnum.ImageTypeEnum_03.getValue());
        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap = commonService.uploadImangeName(imgVo, evidenceUrlFile);
            if (String.valueOf(resultMap.get("status").toString()).equals("true")) {
                order.setEvidenceUrl(resultMap.get("msg").toString());
            } else {
                logger.error("## 图片上传失败，msg--->{}", resultMap.get("msg"));
                return 0;
            }
        } catch (Exception e) {
            logger.error("## 图片上传异常，msg--->{}", resultMap.get("msg"));
            return 0;
        }*/

        /*Map<String, Object> resultMap = commonService.saveFile(evidenceUrlFile, req, order.getOrderId());
        if (resultMap.get("status").equals(false)) {
            logger.error("## 图片上传失败，msg--->{}", resultMap.get("msg"));
            return 0;
        }
        order.setEvidenceUrl(resultMap.get("msg").toString());*/

        List<InaccountOrderDetail> orderDetailList = new ArrayList<InaccountOrderDetail>();
        if (!StringUtil.isNullOrEmpty(A00)) {
            InaccountOrderDetail orderDetail = new InaccountOrderDetail();
            orderDetail.setOrderListId(IdUtil.getNextId());
            orderDetail.setOrderId(order.getOrderId());
            orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
            orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(A00)));
            orderDetail.setBId("A00");
            orderDetail.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
            orderDetail.setCreateUser(user.getId());
            orderDetail.setUpdateUser(user.getId());
            orderDetail.setCreateTime(System.currentTimeMillis());
            orderDetail.setUpdateTime(System.currentTimeMillis());
            orderDetail.setLockVersion(0);
            orderDetailList.add(orderDetail);
        }
        if (!StringUtil.isNullOrEmpty(B01)) {
            InaccountOrderDetail orderDetail = new InaccountOrderDetail();
            orderDetail.setOrderListId(IdUtil.getNextId());
            orderDetail.setOrderId(order.getOrderId());
            orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
            orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B01)));
            orderDetail.setBId("B01");
            orderDetail.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
            orderDetail.setCreateUser(user.getId());
            orderDetail.setUpdateUser(user.getId());
            orderDetail.setCreateTime(System.currentTimeMillis());
            orderDetail.setUpdateTime(System.currentTimeMillis());
            orderDetail.setLockVersion(0);
            orderDetailList.add(orderDetail);
        }
        if (!StringUtil.isNullOrEmpty(B02)) {
            InaccountOrderDetail orderDetail = new InaccountOrderDetail();
            orderDetail.setOrderListId(IdUtil.getNextId());
            orderDetail.setOrderId(order.getOrderId());
            orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
            orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B02)));
            orderDetail.setBId("B02");
            orderDetail.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
            orderDetail.setCreateUser(user.getId());
            orderDetail.setUpdateUser(user.getId());
            orderDetail.setCreateTime(System.currentTimeMillis());
            orderDetail.setUpdateTime(System.currentTimeMillis());
            orderDetail.setLockVersion(0);
            orderDetailList.add(orderDetail);
        }
        if (!StringUtil.isNullOrEmpty(B03)) {
            InaccountOrderDetail orderDetail = new InaccountOrderDetail();
            orderDetail.setOrderListId(IdUtil.getNextId());
            orderDetail.setOrderId(order.getOrderId());
            orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
            orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B03)));
            orderDetail.setBId("B03");
            orderDetail.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
            orderDetail.setCreateUser(user.getId());
            orderDetail.setUpdateUser(user.getId());
            orderDetail.setCreateTime(System.currentTimeMillis());
            orderDetail.setUpdateTime(System.currentTimeMillis());
            orderDetail.setLockVersion(0);
            orderDetailList.add(orderDetail);
        }
        if (!StringUtil.isNullOrEmpty(B04)) {
            InaccountOrderDetail orderDetail = new InaccountOrderDetail();
            orderDetail.setOrderListId(IdUtil.getNextId());
            orderDetail.setOrderId(order.getOrderId());
            orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
            orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B04)));
            orderDetail.setBId("B04");
            orderDetail.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
            orderDetail.setCreateUser(user.getId());
            orderDetail.setUpdateUser(user.getId());
            orderDetail.setCreateTime(System.currentTimeMillis());
            orderDetail.setUpdateTime(System.currentTimeMillis());
            orderDetail.setLockVersion(0);
            orderDetailList.add(orderDetail);
        }
        if (!StringUtil.isNullOrEmpty(B05)) {
            InaccountOrderDetail orderDetail = new InaccountOrderDetail();
            orderDetail.setOrderListId(IdUtil.getNextId());
            orderDetail.setOrderId(order.getOrderId());
            orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
            orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B05)));
            orderDetail.setBId("B05");
            orderDetail.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
            orderDetail.setCreateUser(user.getId());
            orderDetail.setUpdateUser(user.getId());
            orderDetail.setCreateTime(System.currentTimeMillis());
            orderDetail.setUpdateTime(System.currentTimeMillis());
            orderDetail.setLockVersion(0);
            orderDetailList.add(orderDetail);
        }
        if (!StringUtil.isNullOrEmpty(B06)) {
            InaccountOrderDetail orderDetail = new InaccountOrderDetail();
            orderDetail.setOrderListId(IdUtil.getNextId());
            orderDetail.setOrderId(order.getOrderId());
            orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
            orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B06)));
            orderDetail.setBId("B06");
            orderDetail.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
            orderDetail.setCreateUser(user.getId());
            orderDetail.setUpdateUser(user.getId());
            orderDetail.setCreateTime(System.currentTimeMillis());
            orderDetail.setUpdateTime(System.currentTimeMillis());
            orderDetail.setLockVersion(0);
            orderDetailList.add(orderDetail);
        }
        if (!StringUtil.isNullOrEmpty(B07)) {
            InaccountOrderDetail orderDetail = new InaccountOrderDetail();
            orderDetail.setOrderListId(IdUtil.getNextId());
            orderDetail.setOrderId(order.getOrderId());
            orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
            orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B07)));
            orderDetail.setBId("B07");
            orderDetail.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
            orderDetail.setCreateUser(user.getId());
            orderDetail.setUpdateUser(user.getId());
            orderDetail.setCreateTime(System.currentTimeMillis());
            orderDetail.setUpdateTime(System.currentTimeMillis());
            orderDetail.setLockVersion(0);
            orderDetailList.add(orderDetail);
        }
        if (!StringUtil.isNullOrEmpty(B08)) {
            InaccountOrderDetail orderDetail = new InaccountOrderDetail();
            orderDetail.setOrderListId(IdUtil.getNextId());
            orderDetail.setOrderId(order.getOrderId());
            orderDetail.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
            orderDetail.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B08)));
            orderDetail.setBId("B08");
            orderDetail.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
            orderDetail.setCreateUser(user.getId());
            orderDetail.setUpdateUser(user.getId());
            orderDetail.setCreateTime(System.currentTimeMillis());
            orderDetail.setUpdateTime(System.currentTimeMillis());
            orderDetail.setLockVersion(0);
            orderDetailList.add(orderDetail);
        }

        if (!inaccountOrderService.save(order)) {
            logger.error("## 新增入账订单信息失败");
            return 0;
        }

        if (!inaccountOrderDetailService.saveBatch(orderDetailList)) {
            logger.error("## 新增入账订单明细失败");
            return 0;
        }
        return 1;
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
            txnVo.setTxnAmt(d.getTransAmt());
            txnVo.setUpLoadAmt(d.getTransAmt());
            transList.add(txnVo);
            bIds.add(d.getBId());
        }

        AccountRechargeReqVo reqVo = new AccountRechargeReqVo();
        reqVo.setFromCompanyId(channelId);
        reqVo.setTransAmt(order.getInaccountAmt());
        reqVo.setUploadAmt(order.getInaccountAmt());
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
    public int editRetailChnlTransfer(HttpServletRequest req, MultipartFile evidenceUrlFile) {
        HttpSession session = req.getSession();
        User user = (User)session.getAttribute(Constants.SESSION_USER);

        String orderId = StringUtil.nullToString(req.getParameter("orderId"));
        String channelId = StringUtil.nullToString(req.getParameter("channelId"));
       /* String remitAmt = StringUtil.nullToString(req.getParameter("remitAmt"));
        String evidenceUrl = StringUtil.nullToString(req.getParameter("evidenceUrl"));*/
        String inaccountAmt = StringUtil.nullToString(req.getParameter("inaccountAmt"));
        String A00 = StringUtil.nullToString(req.getParameter("A00"));
        String B01 = StringUtil.nullToString(req.getParameter("B01"));
        String B02 = StringUtil.nullToString(req.getParameter("B02"));
        String B03 = StringUtil.nullToString(req.getParameter("B03"));
        String B04 = StringUtil.nullToString(req.getParameter("B04"));
        String B05 = StringUtil.nullToString(req.getParameter("B05"));
        String B06 = StringUtil.nullToString(req.getParameter("B06"));
        String B07 = StringUtil.nullToString(req.getParameter("B07"));
        String B08 = StringUtil.nullToString(req.getParameter("B08"));
        String remarks = StringUtil.nullToString(req.getParameter("remarks"));

        InaccountOrder order = inaccountOrderService.getInaccountOrderByOrderId(orderId);
        if (order == null) {
            logger.error("## 根据上账订单号{}查询订单信息为空", orderId);
            return 0;
        }
       /* order.setRemitAmt(new BigDecimal(NumberUtils.RMBYuanToCent(remitAmt)));*/
        order.setInaccountAmt(new BigDecimal(NumberUtils.RMBYuanToCent(inaccountAmt)));
        order.setRemarks(remarks);
        order.setUpdateUser(user.getId());
        order.setUpdateTime(System.currentTimeMillis());
        order.setLockVersion(order.getLockVersion() + 1);

        /*if (evidenceUrlFile == null || evidenceUrlFile.isEmpty()) {
            logger.error("## 上传图片为空");
            return 0;
        }
        FTPImageVo imgVo = new FTPImageVo();
        imgVo.setImgId(order.getOrderId());
        imgVo.setService(IMG_SERVER);
        imgVo.setNewPath(FILE_NEW_PATH);
        imgVo.setSeparator(FILE_UPLAOD_SEPARATOR);
        imgVo.setUploadPath(FILE_UPLAOD_PATH);
        imgVo.setImgType(ImageTypeEnum.ImageTypeEnum_03.getValue());
        Map<String, Object> resultMap = new HashMap<>();
        try {
            resultMap = commonService.uploadImangeName(imgVo, evidenceUrlFile);
            if (String.valueOf(resultMap.get("status").toString()).equals("true")) {
                order.setEvidenceUrl(resultMap.get("msg").toString());
            } else {
                logger.error("## 图片上传失败，msg--->{}", resultMap.get("msg"));
                return 0;
            }
        } catch (Exception e) {
            logger.error("## 图片上传异常，msg--->{}", resultMap.get("msg"));
            return 0;
        }*/

        /*Map<String, Object> resultMap = commonService.saveFile(evidenceUrlFile, req, order.getOrderId());
        if (resultMap.get("status").equals(false)) {
            logger.error("## 图片上传失败，msg--->{}", resultMap.get("msg"));
            return 0;
        }
        order.setEvidenceUrl(resultMap.get("msg").toString());*/

        List<InaccountOrderDetail> editOrderDetailList = new ArrayList<>();
        List<InaccountOrderDetail> addOrderDetailList = new ArrayList<>();
        List<InaccountOrderDetail> delOrderDetailList = new ArrayList<>();
        InaccountOrderDetail detailA00 = new InaccountOrderDetail();
        detailA00.setBId("A00");
        detailA00.setOrderId(order.getOrderId());
        InaccountOrderDetail orderDetailA00 = inaccountOrderDetailService.getInaccountOrderDetailByOrderIdAndBid(detailA00);
        if (orderDetailA00 == null) {
            if (!StringUtil.isNullOrEmpty(A00)) {
                orderDetailA00 = new InaccountOrderDetail();
                orderDetailA00.setOrderListId(IdUtil.getNextId());
                orderDetailA00.setOrderId(order.getOrderId());
                orderDetailA00.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
                orderDetailA00.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(A00)));
                orderDetailA00.setBId("A00");
                orderDetailA00.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
                orderDetailA00.setCreateUser(user.getId());
                orderDetailA00.setUpdateUser(user.getId());
                orderDetailA00.setCreateTime(System.currentTimeMillis());
                orderDetailA00.setUpdateTime(System.currentTimeMillis());
                orderDetailA00.setLockVersion(0);
                addOrderDetailList.add(orderDetailA00);
            }
        } else {
            if (!StringUtil.isNullOrEmpty(A00)) {
                orderDetailA00.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(A00)));
                orderDetailA00.setUpdateUser(user.getId());
                orderDetailA00.setUpdateTime(System.currentTimeMillis());
                orderDetailA00.setLockVersion(orderDetailA00.getLockVersion() + 1);
                editOrderDetailList.add(orderDetailA00);
            } else {
                delOrderDetailList.add(orderDetailA00);
            }
        }
        InaccountOrderDetail detailB01 = new InaccountOrderDetail();
        detailB01.setBId("B01");
        detailB01.setOrderId(order.getOrderId());
        InaccountOrderDetail orderDetailB01 = inaccountOrderDetailService.getInaccountOrderDetailByOrderIdAndBid(detailB01);
        if (orderDetailB01 == null) {
            if (!StringUtil.isNullOrEmpty(B01)) {
                orderDetailB01 = new InaccountOrderDetail();
                orderDetailB01.setOrderListId(IdUtil.getNextId());
                orderDetailB01.setOrderId(order.getOrderId());
                orderDetailB01.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
                orderDetailB01.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B01)));
                orderDetailB01.setBId("B01");
                orderDetailB01.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
                orderDetailB01.setCreateUser(user.getId());
                orderDetailB01.setUpdateUser(user.getId());
                orderDetailB01.setCreateTime(System.currentTimeMillis());
                orderDetailB01.setUpdateTime(System.currentTimeMillis());
                orderDetailB01.setLockVersion(0);
                addOrderDetailList.add(orderDetailB01);
            }
        } else {
            if (!StringUtil.isNullOrEmpty(B01)) {
                orderDetailB01.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B01)));
                orderDetailB01.setUpdateUser(user.getId());
                orderDetailB01.setUpdateTime(System.currentTimeMillis());
                orderDetailB01.setLockVersion(orderDetailB01.getLockVersion() + 1);
                editOrderDetailList.add(orderDetailB01);
            } else {
                delOrderDetailList.add(orderDetailB01);
            }
        }
        InaccountOrderDetail detailB02 = new InaccountOrderDetail();
        detailB02.setBId("B02");
        detailB02.setOrderId(order.getOrderId());
        InaccountOrderDetail orderDetailB02 = inaccountOrderDetailService.getInaccountOrderDetailByOrderIdAndBid(detailB02);
        if (orderDetailB02 == null) {
            if (!StringUtil.isNullOrEmpty(B02)) {
                orderDetailB02 = new InaccountOrderDetail();
                orderDetailB02.setOrderListId(IdUtil.getNextId());
                orderDetailB02.setOrderId(order.getOrderId());
                orderDetailB02.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
                orderDetailB02.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B02)));
                orderDetailB02.setBId("B02");
                orderDetailB02.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
                orderDetailB02.setCreateUser(user.getId());
                orderDetailB02.setUpdateUser(user.getId());
                orderDetailB02.setCreateTime(System.currentTimeMillis());
                orderDetailB02.setUpdateTime(System.currentTimeMillis());
                orderDetailB02.setLockVersion(0);
                addOrderDetailList.add(orderDetailB02);
            }
        } else {
            if (!StringUtil.isNullOrEmpty(B02)) {
                orderDetailB02.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B02)));
                orderDetailB02.setUpdateUser(user.getId());
                orderDetailB02.setUpdateTime(System.currentTimeMillis());
                orderDetailB02.setLockVersion(orderDetailB02.getLockVersion() + 1);
                editOrderDetailList.add(orderDetailB02);
            } else {
                delOrderDetailList.add(orderDetailB02);
            }
        }
        InaccountOrderDetail detailB03 = new InaccountOrderDetail();
        detailB03.setBId("B03");
        detailB03.setOrderId(order.getOrderId());
        InaccountOrderDetail orderDetailB03 = inaccountOrderDetailService.getInaccountOrderDetailByOrderIdAndBid(detailB03);
        if (orderDetailB03 == null) {
            if (!StringUtil.isNullOrEmpty(B03)) {
                orderDetailB03 = new InaccountOrderDetail();
                orderDetailB03.setOrderListId(IdUtil.getNextId());
                orderDetailB03.setOrderId(order.getOrderId());
                orderDetailB03.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
                orderDetailB03.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B03)));
                orderDetailB03.setBId("B03");
                orderDetailB03.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
                orderDetailB03.setCreateUser(user.getId());
                orderDetailB03.setUpdateUser(user.getId());
                orderDetailB03.setCreateTime(System.currentTimeMillis());
                orderDetailB03.setUpdateTime(System.currentTimeMillis());
                orderDetailB03.setLockVersion(0);
                addOrderDetailList.add(orderDetailB03);
            }
        } else {
            if (!StringUtil.isNullOrEmpty(B03)) {
                orderDetailB03.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B03)));
                orderDetailB03.setUpdateUser(user.getId());
                orderDetailB03.setUpdateTime(System.currentTimeMillis());
                orderDetailB03.setLockVersion(orderDetailB03.getLockVersion() + 1);
                editOrderDetailList.add(orderDetailB03);
            } else {
                delOrderDetailList.add(orderDetailB03);
            }
        }
        InaccountOrderDetail detailB04 = new InaccountOrderDetail();
        detailB04.setBId("B04");
        detailB04.setOrderId(order.getOrderId());
        InaccountOrderDetail orderDetailB04 = inaccountOrderDetailService.getInaccountOrderDetailByOrderIdAndBid(detailB04);
        if (orderDetailB04 == null) {
            if (!StringUtil.isNullOrEmpty(B04)) {
                orderDetailB04 = new InaccountOrderDetail();
                orderDetailB04.setOrderListId(IdUtil.getNextId());
                orderDetailB04.setOrderId(order.getOrderId());
                orderDetailB04.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
                orderDetailB04.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B04)));
                orderDetailB04.setBId("B04");
                orderDetailB04.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
                orderDetailB04.setCreateUser(user.getId());
                orderDetailB04.setUpdateUser(user.getId());
                orderDetailB04.setCreateTime(System.currentTimeMillis());
                orderDetailB04.setUpdateTime(System.currentTimeMillis());
                orderDetailB04.setLockVersion(0);
                addOrderDetailList.add(orderDetailB04);
            }
        } else {
            if (!StringUtil.isNullOrEmpty(B04)) {
                orderDetailB04.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B04)));
                orderDetailB04.setUpdateUser(user.getId());
                orderDetailB04.setUpdateTime(System.currentTimeMillis());
                orderDetailB04.setLockVersion(orderDetailB04.getLockVersion() + 1);
                editOrderDetailList.add(orderDetailB04);
            } else {
                delOrderDetailList.add(orderDetailB04);
            }
        }
        InaccountOrderDetail detailB05 = new InaccountOrderDetail();
        detailB05.setBId("B05");
        detailB05.setOrderId(order.getOrderId());
        InaccountOrderDetail orderDetailB05 = inaccountOrderDetailService.getInaccountOrderDetailByOrderIdAndBid(detailB05);
        if (orderDetailB05 == null) {
            if (!StringUtil.isNullOrEmpty(B05)) {
                orderDetailB05 = new InaccountOrderDetail();
                orderDetailB05.setOrderListId(IdUtil.getNextId());
                orderDetailB05.setOrderId(order.getOrderId());
                orderDetailB05.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
                orderDetailB05.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B05)));
                orderDetailB05.setBId("B05");
                orderDetailB05.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
                orderDetailB05.setCreateUser(user.getId());
                orderDetailB05.setUpdateUser(user.getId());
                orderDetailB05.setCreateTime(System.currentTimeMillis());
                orderDetailB05.setUpdateTime(System.currentTimeMillis());
                orderDetailB05.setLockVersion(0);
                addOrderDetailList.add(orderDetailB05);
            }
        } else {
            if (!StringUtil.isNullOrEmpty(B05)) {
                orderDetailB05.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B05)));
                orderDetailB05.setUpdateUser(user.getId());
                orderDetailB05.setUpdateTime(System.currentTimeMillis());
                orderDetailB05.setLockVersion(orderDetailB05.getLockVersion() + 1);
                editOrderDetailList.add(orderDetailB05);
            } else {
                delOrderDetailList.add(orderDetailB05);
            }
        }
        InaccountOrderDetail detailB06 = new InaccountOrderDetail();
        detailB06.setBId("B06");
        detailB06.setOrderId(order.getOrderId());
        InaccountOrderDetail orderDetailB06 = inaccountOrderDetailService.getInaccountOrderDetailByOrderIdAndBid(detailB06);
        if (orderDetailB06 == null) {
            if (!StringUtil.isNullOrEmpty(B06)) {
                orderDetailB06 = new InaccountOrderDetail();
                orderDetailB06.setOrderListId(IdUtil.getNextId());
                orderDetailB06.setOrderId(order.getOrderId());
                orderDetailB06.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
                orderDetailB06.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B06)));
                orderDetailB06.setBId("B06");
                orderDetailB06.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
                orderDetailB06.setCreateUser(user.getId());
                orderDetailB06.setUpdateUser(user.getId());
                orderDetailB06.setCreateTime(System.currentTimeMillis());
                orderDetailB06.setUpdateTime(System.currentTimeMillis());
                orderDetailB06.setLockVersion(0);
                addOrderDetailList.add(orderDetailB06);
            }
        } else {
            if (!StringUtil.isNullOrEmpty(B06)) {
                orderDetailB06.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B06)));
                orderDetailB06.setUpdateUser(user.getId());
                orderDetailB06.setUpdateTime(System.currentTimeMillis());
                orderDetailB06.setLockVersion(orderDetailB06.getLockVersion() + 1);
                editOrderDetailList.add(orderDetailB06);
            } else {
                delOrderDetailList.add(orderDetailB06);
            }
        }
        InaccountOrderDetail detailB07 = new InaccountOrderDetail();
        detailB07.setBId("B07");
        detailB07.setOrderId(order.getOrderId());
        InaccountOrderDetail orderDetailB07 = inaccountOrderDetailService.getInaccountOrderDetailByOrderIdAndBid(detailB07);
        if (orderDetailB07 == null) {
            if (!StringUtil.isNullOrEmpty(B07)) {
                orderDetailB07 = new InaccountOrderDetail();
                orderDetailB07.setOrderListId(IdUtil.getNextId());
                orderDetailB07.setOrderId(order.getOrderId());
                orderDetailB07.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
                orderDetailB07.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B07)));
                orderDetailB07.setBId("B07");
                orderDetailB07.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
                orderDetailB07.setCreateUser(user.getId());
                orderDetailB07.setUpdateUser(user.getId());
                orderDetailB07.setCreateTime(System.currentTimeMillis());
                orderDetailB07.setUpdateTime(System.currentTimeMillis());
                orderDetailB07.setLockVersion(0);
                addOrderDetailList.add(orderDetailB07);
            }
        } else {
            if (!StringUtil.isNullOrEmpty(B07)) {
                orderDetailB07.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B07)));
                orderDetailB07.setUpdateUser(user.getId());
                orderDetailB07.setUpdateTime(System.currentTimeMillis());
                orderDetailB07.setLockVersion(orderDetailB07.getLockVersion() + 1);
                editOrderDetailList.add(orderDetailB07);
            } else {
                delOrderDetailList.add(orderDetailB07);
            }
        }
        InaccountOrderDetail detailB08 = new InaccountOrderDetail();
        detailB08.setBId("B08");
        detailB08.setOrderId(order.getOrderId());
        InaccountOrderDetail orderDetailB08 = inaccountOrderDetailService.getInaccountOrderDetailByOrderIdAndBid(detailB08);
        if (orderDetailB08 == null) {
            if (!StringUtil.isNullOrEmpty(B08)) {
                orderDetailB08 = new InaccountOrderDetail();
                orderDetailB08.setOrderListId(IdUtil.getNextId());
                orderDetailB08.setOrderId(order.getOrderId());
                orderDetailB08.setIsInvoice(IsInvoiceEnum.INVOICE_FALSE.getCode());
                orderDetailB08.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B08)));
                orderDetailB08.setBId("B08");
                orderDetailB08.setDataStat(DataStatEnum.TRUE_STATUS.getCode());
                orderDetailB08.setCreateUser(user.getId());
                orderDetailB08.setUpdateUser(user.getId());
                orderDetailB08.setCreateTime(System.currentTimeMillis());
                orderDetailB08.setUpdateTime(System.currentTimeMillis());
                orderDetailB08.setLockVersion(0);
                addOrderDetailList.add(orderDetailB08);
            }
        } else {
            if (!StringUtil.isNullOrEmpty(B08)) {
                orderDetailB08.setTransAmt(new BigDecimal(NumberUtils.RMBYuanToCent(B08)));
                orderDetailB08.setUpdateUser(user.getId());
                orderDetailB08.setUpdateTime(System.currentTimeMillis());
                orderDetailB08.setLockVersion(orderDetailB08.getLockVersion() + 1);
                editOrderDetailList.add(orderDetailB08);
            } else {
                delOrderDetailList.add(orderDetailB08);
            }
        }

        if (!inaccountOrderService.updateById(order)) {
            logger.error("## 更新入账订单信息失败");
            return 0;
        }

        if (addOrderDetailList != null && addOrderDetailList.size() >= 1) {
            if (!inaccountOrderDetailService.saveBatch(addOrderDetailList)) {
                logger.error("## 新增入账订单明细失败");
                return 0;
            }
        }

        if (editOrderDetailList != null && editOrderDetailList.size() >= 1) {
            if (!inaccountOrderDetailService.updateBatchById(editOrderDetailList)) {
                logger.error("## 编辑入账订单明细失败");
                return 0;
            }
        }

        if (delOrderDetailList != null && delOrderDetailList.size() >= 1) {
            if (delOrderDetailList != null && delOrderDetailList.size() >= 1) {
                for (InaccountOrderDetail d : delOrderDetailList) {
                    if (!inaccountOrderDetailService.removeById(d)) {
                        logger.error("## 删除入账订单明细失败");
                        return 0;
                    }
                }
            }
        }
        return 1;
    }

    @Override
    public Map<String, Object> deleteRetailChnlTransfer(HttpServletRequest req) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("status", Boolean.TRUE);

        HttpSession session = req.getSession();
        User user = (User)session.getAttribute(Constants.SESSION_USER);

        String orderId = StringUtil.nullToString(req.getParameter("orderId"));

        InaccountOrder order = inaccountOrderService.getInaccountOrderByOrderId(orderId);
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
