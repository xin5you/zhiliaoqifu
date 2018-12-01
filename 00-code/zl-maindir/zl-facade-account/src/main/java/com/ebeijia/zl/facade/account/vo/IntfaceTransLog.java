package com.ebeijia.zl.facade.account.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * itf接口平台流水表
 *
 * @User zhuqi
 * @Date 2018-11-30
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_intface_trans_log")
public class IntfaceTransLog extends Model<IntfaceTransLog> {
 
    /**
     * 接口平台流水号
     */
    @TableId(value = "itf_primary_key" ,type = IdType.UUID)
    private String itfPrimaryKey;
 
    /**
     * 清算日期
     */
    @TableField("settle_date")
    private String settleDate;
 
    /**
     * 原接口平台流水号
     */
    @TableField("org_itf_primary_key")
    private String orgItfPrimaryKey;
 
    /**
     * 订单号
     */
    @TableField("dms_related_key")
    private String dmsRelatedKey;
 
    /**
     * 原订单号
     */
    @TableField("org_dms_related_key")
    private String orgDmsRelatedKey;
 
    /**
     * 交易类型代码
     */
    @TableField("trans_id")
    private String transId;
 
    /**
     * 0x0001：是否应答
            0x0002：是否被撤消
            0x0004：是否被冲正
            0x0010：是否被退货
            0x0020：是否被差错
            
     */
    @TableField("trans_st")
    private String transSt;
 
    /**
     * 机构号
     */
    @TableField("ins_code")
    private String insCode;
 
    /**
     * 商户号
     */
    @TableField("mchnt_code")
    private String mchntCode;
 
    /**
     * 门店号
     */
    @TableField("shop_code")
    private String shopCode;
 
    /**
     * 表示交易结果
     */
    @TableField("resp_code")
    private String respCode;
 
    /**
     * 主账号
     */
    @TableField("pri_acct_no")
    private String priAcctNo;
 
    /**
     * 卡号
     */
    @TableField("card_no")
    private String cardNo;
 
    /**
     * 用户名
     */
    @TableField("user_inf_user_name")
    private String userInfUserName;
 
    /**
     * 产品号
     */
    @TableField("product_code")
    private String productCode;
 
    /**
     * 实际交易金额
     */
    @TableField("trans_amt")
    private BigDecimal transAmt;
 
    /**
     * 上送金额
     */
    @TableField("upload_amt")
    private BigDecimal uploadAmt;
 
    /**
     * 交易货币代码
     */
    @TableField("trans_curr_cd")
    private String transCurrCd;
 
    /**
     * 交易渠道
     */
    @TableField("trans_chnl")
    private String transChnl;
 
    /**
     * 手续费
     */
    @TableField("trans_fee")
    private BigDecimal transFee;
 
    /**
     * 手续费类型
     */
    @TableField("trans_fee_type")
    private String transFeeType;
 
    /**
     * 转入账户
     */
    @TableField("tfr_in_acct_no")
    private String tfrInAcctNo;
 
    /**
     * 转出账户
     */
    @TableField("tfr_out_acct_no")
    private String tfrOutAcctNo;
 
    /**
     * 附加信息
     */
    @TableField("additional_info")
    private String additionalInfo;
 
    /**
     * 数据状态
     */
    @TableField("data_stat")
    private String dataStat;
 
    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;
 
    /**
     * 创建人
     */
    @TableField("create_user")
    private String createUser;
 
    /**
     * 更新人
     */
    @TableField("update_user")
    private String updateUser;
 
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;
 
    /**
     * 更新时间
     */
    @TableField("update_time")
    private Long updateTime;
 
    /**
     * 乐观锁版本
     */
    @Version
    @TableField("lock_version")
    private Integer lockVersion;


    @Override
    protected Serializable pkVal() { 
        return this.itfPrimaryKey;
    }
}
