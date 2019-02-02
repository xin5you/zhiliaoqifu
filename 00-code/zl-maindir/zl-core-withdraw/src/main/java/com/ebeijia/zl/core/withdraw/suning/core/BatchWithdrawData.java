package com.ebeijia.zl.core.withdraw.suning.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ebeijia.zl.common.utils.tools.DateUtil;
import com.ebeijia.zl.common.utils.tools.SnowFlake;
import com.ebeijia.zl.common.utils.tools.StringUtil;
import com.ebeijia.zl.core.withdraw.suning.config.YFBWithdrawConfig;
import com.ebeijia.zl.core.withdraw.suning.utils.HttpClientUtil;
import com.ebeijia.zl.core.withdraw.suning.vo.WithdrawBodyVO;
import com.ebeijia.zl.core.withdraw.suning.vo.WithdrawDetailDataVO;
import com.suning.epps.codec.Digest;
import com.suning.epps.codec.RSAUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private YFBWithdrawConfig yfbWithdrawConfig;
    private String publicKeyIndex = "0001";
    private String signAlgorithm = "RSA";
    private String merchantNo = "70235957";
    private String inputCharset = "UTF-8";
    private String productCode="01070000042";



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
        String responseStr = HttpClientUtil.post(publicKeyIndex,
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
        signMap.put("publicKeyIndex",publicKeyIndex);
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
        contentObject.put("batchNo", body.getBatchNo());
        contentObject.put("merchantNo", yfbWithdrawConfig.getMerchantNo());// 70057241;70056575
        contentObject.put("productCode",productCode );
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

    /**
     * 验证代付业务签名
     *
     * @param data 业务JSON参数
     * @param signature 业务参数签名值
     * @return
     */
    public  boolean verifySignature(String data, String signature) {
        if (StringUtil.isNullOrEmpty(data)) {
            logger.error("## 验证签名data参数为空");
            return false;
        }
        // 生成MD5摘要
        Map<String, String> signMap = new HashMap<>();
        signMap.put("content", data);
        String signData = Digest.digest(Digest.mapToString(Digest.treeMap(signMap)));

        if (signData == null || signData.length() == 0) {
            logger.error("## 原数据字符串为空,return->false");
            return false;
        }
        if (signature == null || signature.length() == 0) {
            logger.error("## 签名字符串为空,return->false");
            return false;
        }
        try {
            // 获取根据公钥字符串获取私钥
            PublicKey pubKey = RSAUtil.getPublicKey(yfbWithdrawConfig.getWagKeyString());
            // 返回验证结果
            return RSAUtil.vertiy(signData, signature, pubKey);
        } catch (Exception e) {
            logger.error("## 验证签名异常{},return->false",e);
            return false;
        }
    }
}
