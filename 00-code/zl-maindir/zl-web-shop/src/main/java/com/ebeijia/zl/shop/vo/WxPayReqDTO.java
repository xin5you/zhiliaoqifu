package com.ebeijia.zl.shop.vo;

import com.ebeijia.zl.shop.utils.CalculateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Data
@ApiModel("申请芸付")
public class WxPayReqDTO {
    @NotNull
    @ApiModelProperty("商户号-不能为空")
    public Integer mch_id;

    @NotBlank
    @Length(max = 32)
    @ApiModelProperty("随机字符串-长度32")
    public String nonce_str;

    @NotBlank
    @Length(max = 32)
    @ApiModelProperty("商户订单号-长度32")
    public String out_trade_no;

    @NotNull
    @DecimalMin(value = "0", inclusive = false)
    @ApiModelProperty("订单金额-必须大于0")
    public Integer total_fee;

    @NotBlank
    @Length(max = 14, min = 14)
    @ApiModelProperty("订单时间-yyyyMMddHHmmss-长度必须为14")
    public String order_time;

    @NotBlank
    @URL
    @ApiModelProperty("订单成功回掉地址-必须为URL")
    public String notify_url;

    @NotBlank
    @URL
    @ApiModelProperty("支付成功跳转页面-必须为URL")
    public String success_url;


    @NotBlank
    @Length(max = 32)
    @ApiModelProperty("签名-长度32")
    public String sign;


    /**
     * 参数转换成Map便于效验
     *
     * @return
     */
    public Map toParams() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("mch_id", String.valueOf(mch_id));
        map.put("nonce_str", String.valueOf(nonce_str));
        map.put("out_trade_no", String.valueOf(out_trade_no));
        map.put("total_fee", String.valueOf(total_fee));
        map.put("order_time", String.valueOf(order_time));
        map.put("notify_url", String.valueOf(notify_url));
        map.put("success_url", String.valueOf(success_url));
        return map;
    }

    /**
     * 校验请求的数据结果
     *
     * @return
     */
    public boolean checkParameter() {
        Map<String, String> params = toParams();

        String sortSplice = CalculateUtil.sortSplice(params);
        String paramMD5 = DigestUtils.md5Hex(sortSplice);

        return paramMD5.equalsIgnoreCase(sign);
    }

    public Integer getMch_id() {
        return mch_id;
    }

    public void setMch_id(Integer mch_id) {
        this.mch_id = mch_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public Integer getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(Integer total_fee) {
        this.total_fee = total_fee;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getSuccess_url() {
        return success_url;
    }

    public void setSuccess_url(String success_url) {
        this.success_url = success_url;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "WxPayReqDTO{" +
                "mch_id=" + mch_id +
                ", nonce_str='" + nonce_str + '\'' +
                ", out_trade_no='" + out_trade_no + '\'' +
                ", total_fee=" + total_fee +
                ", order_time='" + order_time + '\'' +
                ", notify_url='" + notify_url + '\'' +
                ", success_url='" + success_url + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
