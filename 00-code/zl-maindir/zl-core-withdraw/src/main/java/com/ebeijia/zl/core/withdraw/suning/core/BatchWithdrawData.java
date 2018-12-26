package com.ebeijia.zl.core.withdraw.suning.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ebeijia.zl.common.utils.tools.DateUtil;
import com.ebeijia.zl.common.utils.tools.SnowFlake;
import com.ebeijia.zl.core.withdraw.suning.config.YFBWithdrawConfig;
import com.ebeijia.zl.core.withdraw.suning.utils.HttpClientUtil;
import com.ebeijia.zl.core.withdraw.suning.vo.WithdrawBodyVO;
import com.ebeijia.zl.core.withdraw.suning.vo.WithdrawDetailDataVO;
import com.suning.epps.codec.Digest;
import com.suning.epps.codec.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;


import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

/**
 * 批量转出到卡
 */
@Configuration
public class BatchWithdrawData {

    @Autowired
    private YFBWithdrawConfig yfbWithdrawConfig;
/*
    private String publicKeyIndex = "0001";
    private String signAlgorithm = "RSA";
    private String merchantNo = "70235957";
    private String inputCharset = "UTF-8";*/



    /**
     * 请求转账网关
     *
     * @return signature 签名
     * @throws UnsupportedEncodingException
     * @throws java.security.InvalidKeyException
     * @throws KeyManagementException
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public String batchWithDraw(WithdrawBodyVO body) throws InvalidKeyException, NoSuchAlgorithmException,
            InvalidKeySpecException, SignatureException, UnsupportedEncodingException,
            java.security.InvalidKeyException, KeyManagementException {

        String bussinessParam = createBatchData(body).toJSONString();
        String singnature = calculateSign(bussinessParam);
        String responseStr = HttpClientUtil.post(yfbWithdrawConfig.getPublicKeyIndex(),
                "RSA",
                yfbWithdrawConfig.getMerchantNo(),
                "UTF-8",
                yfbWithdrawConfig.getBathWithdrawUrl(),
                singnature,
                bussinessParam);
        return responseStr;
    }

    /**
     * 计算签名
     *
     * @param body 请求业务数据
     * @return signature 签名
     * @throws java.security.InvalidKeyException
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public String calculateSign(String body) throws NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidKeyException, SignatureException, java.security.InvalidKeyException {
        Map<String, String> signMap = new HashMap<String, String>();
        signMap.put("merchantNo",yfbWithdrawConfig.getMerchantNo());
        signMap.put("publicKeyIndex",yfbWithdrawConfig.getPublicKeyIndex());
        signMap.put("inputCharset", "UTF-8");
        signMap.put("body", body);
        String digest = Digest.digest(Digest.mapToString(Digest.treeMap(signMap)));
        PrivateKey privateKey = RSAUtil.resolvePrivateKey(yfbWithdrawConfig.getPrivateKeyUrl());
        String signature = RSAUtil.sign(digest, privateKey);
        return signature;
    }

    /**
     * 生成请求wag多个批次的json数据方法
     *
     *
     * @return JSONArray
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public JSONArray createBatchData(WithdrawBodyVO body) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(bulidBatchContentJosn(body));
        return jsonArray;
    }

    /**
     * 生成body批次数据
     *
     * @param
     * @return JSONObject
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public JSONObject bulidBatchContentJosn(WithdrawBodyVO body) {
        JSONObject contentObject = new JSONObject();
        contentObject.put("batchNo", SnowFlake.getInstance().nextId());
        contentObject.put("merchantNo", yfbWithdrawConfig.getMerchantNo());// 70057241;70056575
        contentObject.put("productCode",yfbWithdrawConfig.getProductCode() );
        contentObject.put("totalNum", body.getTotalNum());
        contentObject.put("totalAmount", body.getTotalAmount());
        contentObject.put("currency", "CNY");
        contentObject.put("payDate", DateUtil.getCurrentDateStr());
        contentObject.put("goodsType", yfbWithdrawConfig.getGoodsType());
        contentObject.put("detailData", bulidDetailContentJosn(body.getDetailData()));
        contentObject.put("notifyUrl", yfbWithdrawConfig.getYfbNotifyUrl());
        return contentObject;
    }

    /**
     * 生成body明细数据
     *
     * @param
     * @return JSONObject
     * @see [相关类/方法]（可选）
     * @since [产品/模块版本] （可选）
     */
    public JSONArray bulidDetailContentJosn(List<WithdrawDetailDataVO> detailData) {
        JSONArray detailArray = new JSONArray();
        JSONObject detailObject=null;
        for (WithdrawDetailDataVO vo: detailData) {
            detailObject = new JSONObject();
            detailObject.put("serialNo", vo.getSerialNo());
            detailObject.put("receiverType", vo.getReceiverType());
            detailObject.put("receiverCurrency", "CNY");
            detailObject.put("receiverName", vo.getReceiverName());
            detailObject.put("amount", vo.getAmount());
            detailObject.put("orderName", vo.getOrderName());
            detailObject.put("bankName", vo.getBankName());
            detailObject.put("receiverCardNo", vo.getReceiverCardNo());
            detailObject.put("bankCode", vo.getBankCode());
            detailObject.put("bankProvince", vo.getBankProvince());
            detailObject.put("bankCity", vo.getBankCity());
            detailObject.put("payeeBankLinesNo", vo.getPayeeBankLinesNo());
            detailArray.add(detailObject);
        }
        return detailArray;

    }
}
