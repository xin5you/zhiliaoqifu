package com.ebeijia.zl.service.valid;

import com.ebeijia.zl.common.utils.domain.BaseResult;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.facade.account.req.*;

public class AccountTransValidUtils {

    /**
     * 账户充值验证
     * @param req
     * BaseResult req
     * @return resp
     */
    public static boolean executeRechargeValid(AccountRechargeReqVo req,BaseResult resp){

        if (StringUtil.isNullOrEmpty(req.getTransId())) {
            resp.setMsg("交易类型为空");
            return true;
        }
        if (StringUtil.isNullOrEmpty(req.getTransChnl())) {
            resp.setMsg("交易渠道为空");
            return true;
        }
        if (StringUtil.isNullOrEmpty(req.getUserChnlId()) || StringUtil.isNullOrEmpty(req.getUserType())) {
            resp.setMsg("用户信息不全");
            return true;
        }
        if(StringUtil.isNullOrEmpty(req.getUserType())){
            resp.setMsg("用户类型为空");
            return true;
        }
        if((StringUtil.isNullOrEmpty(req.getTransAmt())  || StringUtil.isNullOrEmpty(req.getUploadAmt()) ) && (req.getTransList()==null || req.getTransList().size()<1)){
            resp.setMsg("交易金额为空");
            return true;
        }
        if(StringUtil.isNullOrEmpty(req.getDmsRelatedKey()) ){
            resp.setMsg("外部交易订单号为空");
            return true;
        }
        return false;
    }


    /**
     * 消费交易验证
     * @param req
     * BaseResult req
     * @return resp
     */
    public static boolean executeConsumeValid(AccountConsumeReqVo req, BaseResult resp){

        if (StringUtil.isNullOrEmpty(req.getTransId())) {
            resp.setMsg("交易类型为空");
            return true;
        }
        if (StringUtil.isNullOrEmpty(req.getTransChnl())) {
            resp.setMsg("交易渠道为空");
            return true;
        }
        if (StringUtil.isNullOrEmpty(req.getUserChnlId()) || StringUtil.isNullOrEmpty(req.getUserType())) {
            resp.setMsg("用户信息不全");
            return true;
        }
        if(StringUtil.isNullOrEmpty(req.getUserType())){
            resp.setMsg("用户类型为空");
            return true;
        }
        if(req.getTransList()==null || req.getTransList().size()<1){
            resp.setMsg("交易金额为空");
            return true;
        }
        if(StringUtil.isNullOrEmpty(req.getMchntCode()) ){
            resp.setMsg("商户号为空");
            return true;
        }
        if(StringUtil.isNullOrEmpty(req.getDmsRelatedKey()) ){
            resp.setMsg("外部交易订单号为空");
            return true;
        }
        return false;
    }


    /**
     * 解冻提交
     * @param req
     * BaseResult req
     * @return resp
     */
    public static boolean executeCommitFrozenValid(AccountFrozenReqVo req, BaseResult resp){

        if (StringUtil.isNullOrEmpty(req.getTransId())) {
            resp.setMsg("交易类型为空");
            return true;
        }
        if (StringUtil.isNullOrEmpty(req.getTransChnl())) {
            resp.setMsg("交易渠道为空");
            return true;
        }
        if ((StringUtil.isNullOrEmpty(req.getUserChnlId()) && StringUtil.isNullOrEmpty(req.getUserType() ) || StringUtil.isNullOrEmpty(req.getUserId()))) {
            resp.setMsg("用户信息不全");
            return true;
        }
        if(StringUtil.isNullOrEmpty(req.getUserType())){
            resp.setMsg("用户类型为空");
            return true;
        }
        if(StringUtil.isNullOrEmpty(req.getTransAmt())  || StringUtil.isNullOrEmpty(req.getUploadAmt()) ){
            resp.setMsg("交易金额为空");
            return true;
        }

        if(StringUtil.isNullOrEmpty(req.getDmsRelatedKey()) ){
            resp.setMsg("外部交易订单号为空");
            return true;
        }
        return false;
    }

    /**
     * 转账交易验证
     * @param req
     * BaseResult req
     * @return resp
     */
    public static boolean executeTransferValid(AccountTransferReqVo req, BaseResult resp){

        if (StringUtil.isNullOrEmpty(req.getTransId())) {
            resp.setMsg("交易类型为空");
            return true;
        }
        if (StringUtil.isNullOrEmpty(req.getTransChnl())) {
            resp.setMsg("交易渠道为空");
            return true;
        }
        if (StringUtil.isNullOrEmpty(req.getUserChnlId()) || StringUtil.isNullOrEmpty(req.getUserType())) {
            resp.setMsg("用户信息不全");
            return true;
        }
        if(StringUtil.isNullOrEmpty(req.getUserType())){
            resp.setMsg("用户类型为空");
            return true;
        }
        if((StringUtil.isNullOrEmpty(req.getTransAmt())  || StringUtil.isNullOrEmpty(req.getUploadAmt()) ) && (req.getTransList()==null || req.getTransList().size()<1)){
            resp.setMsg("交易金额为空");
            return true;
        }
        if(StringUtil.isNullOrEmpty(req.getTfrOutUserId()) ){
            resp.setMsg("转出账号为空");
            return true;
        }
        if(StringUtil.isNullOrEmpty(req.getTfrInUserId()) ){
            resp.setMsg("转入账号为空");
            return true;
        }
        if(StringUtil.isNullOrEmpty(req.getDmsRelatedKey()) ){
            resp.setMsg("外部交易订单号为空");
            return true;
        }
        return false;
    }


    /**
     * 提现交易验证
     * @param req
     * BaseResult req
     * @return resp
     */
    public static boolean executeWithDrawValid(AccountWithDrawReqVo req, BaseResult resp){

        if (StringUtil.isNullOrEmpty(req.getTransId())) {
            resp.setMsg("交易类型为空");
            return true;
        }
        if (StringUtil.isNullOrEmpty(req.getTransChnl())) {
            resp.setMsg("交易渠道为空");
            return true;
        }
        if (StringUtil.isNullOrEmpty(req.getUserChnlId()) || StringUtil.isNullOrEmpty(req.getUserType())) {
            resp.setMsg("用户信息不全");
            return true;
        }
        if(StringUtil.isNullOrEmpty(req.getUserType())){
            resp.setMsg("用户类型为空");
            return true;
        }
        if(StringUtil.isNullOrEmpty(req.getTransAmt())  || StringUtil.isNullOrEmpty(req.getUploadAmt())){
            resp.setMsg("交易金额为空");
            return true;
        }
        if(StringUtil.isNullOrEmpty(req.getDmsRelatedKey()) ){
            resp.setMsg("外部交易订单号为空");
            return true;
        }
        if(StringUtil.isNullOrEmpty(req.getReceiverName()) ){
            resp.setMsg("receiverName不能为空");
            return true;
        }
        if(StringUtil.isNullOrEmpty(req.getReceiverCardNo()) ){
            resp.setMsg("receiverCardNo不能为空");
            return true;
        }
        if(StringUtil.isNullOrEmpty(req.getReceiverType()) ){
            resp.setMsg("receiverType不能为空");
            return true;
        }
        if(StringUtil.isNullOrEmpty(req.getBankName()) ){
            resp.setMsg("bankName不能为空");
            return true;
        }
        if(StringUtil.isNullOrEmpty(req.getBankCode()) ){
            resp.setMsg("bankCode不能为空");
            return true;
        }
        return false;
    }

    /**
     * 退款交易验证
     * @param req
     * BaseResult req
     * @return resp
     */
    public static boolean executeRefundVaild(AccountRefundReqVo req, BaseResult resp){

        if (StringUtil.isNullOrEmpty(req.getTransId())) {
            resp.setMsg("交易类型为空");
            return true;
        }
        if (StringUtil.isNullOrEmpty(req.getTransChnl())) {
            resp.setMsg("交易渠道为空");
            return true;
        }
        if (StringUtil.isNullOrEmpty(req.getUserChnlId()) || StringUtil.isNullOrEmpty(req.getUserType())) {
            resp.setMsg("用户信息不全");
            return true;
        }
        if(StringUtil.isNullOrEmpty(req.getUserType())){
            resp.setMsg("用户类型为空");
            return true;
        }
        if(req.getRefundList()==null || req.getRefundList().size()<1){
            resp.setMsg("交易金额为空");
            return true;
        }
        if(StringUtil.isNullOrEmpty(req.getOrgDmsRelatedKey()) && StringUtil.isNullOrEmpty(req.getOrgItfPrimaryKey())  ){
            resp.setMsg("平台原交易信息为空");
            return true;
        }
        if(StringUtil.isNullOrEmpty(req.getDmsRelatedKey()) ){
            resp.setMsg("外部交易订单号为空");
            return true;
        }
        return false;
    }

}
