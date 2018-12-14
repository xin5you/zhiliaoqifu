package com.ebeijia.zl.facade.account.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

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
 * @Date 2018-12-03
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
     * 交易描述
     */
    @TableField("trans_desc")
    private String transDesc;
    
    
    /**
     * 交易数量
     */
    @TableField("trans_number")
    private int transNumber=1;
 
    /**
     * 交易类型代码
     */
    @TableField("trans_id")
    private String transId;
 
    /**
     * 0:未处理
     * 1:已处理      
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
    @TableField("pri_b_id")
    private String priBId;
 
    /**
     * 用户操作类型
     */
    @TableField("user_type")
    private String userType;
 
    /**
     * 平台唯一用户标识
     */
    @TableField("user_id")
    private String userId;
    
    
    /**
     * 用户渠道
     */
    @TableField("user_chnl")
    private String userChnl;
    
    
    /**
     * 用户渠道id
     */
    @TableField("user_chnl_id")
    private String userChnlId;
 
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
    @TableField("tfr_in_user_id")
    private String tfrInUserId;
    
    
    /**
     * 转入账户类型
     */
    @TableField("tfr_in_b_id")
    private String tfrInBId;
 
    /**
     * 转出账户
     */
    @TableField("tfr_out_user_id")
    private String tfrOutUserId;
 
    /**
     * 转出账户类型
     */
    @TableField("tfr_out_b_id")
    private String tfrOutBId;
    
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

    /**
     *  业务数据
     *  
     *  专项账户类型
     */
    @TableField(exist=false)
    private Set<String> bIds;

    @Override
    protected Serializable pkVal() { 
        return this.itfPrimaryKey;
    }
}
