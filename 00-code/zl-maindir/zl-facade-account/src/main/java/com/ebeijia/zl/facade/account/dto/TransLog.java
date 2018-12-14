package com.ebeijia.zl.facade.account.dto;

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
 * 账户交易流水
 *
 * @User zhuqi
 * @Date 2018-12-03
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_trans_log")
public class TransLog extends Model<TransLog> {
 
    /**
     * 交易流水号
     */
    @TableId(value = "txn_primary_key" ,type = IdType.UUID)
    private String txnPrimaryKey;
 
    /**
     * 接口平台流水号
     */
    @TableField("itf_primary_key")
    private String itfPrimaryKey;
 
    /**
     * 原交易流水号
     */
    @TableField("org_txn_primary_key")
    private String orgTxnPrimaryKey;
 
    /**
     * 外部系统流水号
     */
    @TableField("dms_related_key")
    private String dmsRelatedKey;
 
    /**
     * 原外部系统交易流水号
     */
    @TableField("org_dms_related_key")
    private String orgDmsRelatedKey;
 
    /**
     * 交易类型代码
     */
    @TableField("trans_id")
    private String transId;
 
    /**
     * 交易渠道
     */
    @TableField("trans_chnl")
    private String transChnl;
 
    /**
     * 终端号
     */
    @TableField("term_code")
    private String termCode;
 
    /**
     * 门店
     */
    @TableField("shop_code")
    private String shopCode;
 
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
     * 主账号
     */
    @TableField("pri_b_id")
    private String priBId;
 
    /**
     * 卡号
     */
    @TableField("user_type")
    private String userType;
 
    /**
     * 用户名
     */
    @TableField("user_id")
    private String userId;
 
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
     * A 账户加
            D 账户减
            O 开户操作
            
     */
    @TableField("card_attr")
    private String cardAttr;
 
    /**
     * 附加信息
     */
    @TableField("additional_info")
    private String additionalInfo;
 
    /**
     * 表示交易结果:
            00:成功
            其他失败
     */
    @TableField("resp_code")
    private String respCode;
 
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

    /****业务代码***/
    /**
     * 执行顺序
     */
    @TableField(exist=false)
    private Integer order;

    @Override
    protected Serializable pkVal() { 
        return this.txnPrimaryKey;
    }
}
