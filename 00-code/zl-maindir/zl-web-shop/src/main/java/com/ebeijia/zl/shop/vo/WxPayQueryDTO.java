package com.ebeijia.zl.shop.vo;

import com.ebeijia.zl.shop.utils.CalculateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Data
public class WxPayQueryDTO {
    @NotBlank
    @Length(max = 32)
    @ApiModelProperty("商户订单号-长度32")
    private String out_trade_no;

    @NotNull
    @ApiModelProperty("商户号-不能为空")
    private Integer mch_id;

    @NotBlank
    @Length(max = 32)
    @ApiModelProperty("随机字符串-长度32")
    private String noncestr;

    @NotBlank
    @Length(max = 32)
    @ApiModelProperty("签名-长度32")
    private String sign;

    /**
     * 参数转换成Map便于效验
     *
     * @return
     */
    public Map toParams() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("out_trade_no", out_trade_no);
        map.put("mch_id", String.valueOf(mch_id));
        map.put("noncestr", noncestr);
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


    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public Integer getMch_id() {
        return mch_id;
    }

    public void setMch_id(Integer mch_id) {
        this.mch_id = mch_id;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
