package com.ebeijia.zl.facade.telrecharge.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * 供应商订单明细表
 *
 * @User zhuqi
 * @Date 2018-12-10
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_provider_order_inf")
public class ProviderOrderInf extends Model<ProviderOrderInf> {
 
    /**
     * reg_order_id
     */
    @TableId(value = "reg_order_id" ,type = IdType.UUID)
    private String regOrderId;
 
    /**
     * provider_id
     */
    @TableField("provider_id")
    private String providerId;
 
    /**
     * 渠道订单
     */
    @TableField("channel_order_id")
    private String channelOrderId;
 
    /**
     * 单位元
     */
    @TableField("reg_order_amt")
    private BigDecimal regOrderAmt;

    /**
     * 交易扣款金额
     */
    @TableField("reg_txn_amt")
    private BigDecimal regTxnAmt;

    /**
     * 系统交易订单状态
     */
    @TableField("order_state")
    private String orderState;

    /**
     * 系统交易流水
     */
    @TableField("itf_primary_key")
    private String itfPrimaryKey;
 
    /**
     * 订单处理时间
     */
    @TableField("operate_time")
    private Long operateTime;
 
    /**
     * 1:已扣款
            2:已退款
            暂不使用该字段标识
     */
    @TableField("pay_state")
    private String payState;
 
    /**
     * 0:充值中
            1:成功 
            3:充值失败
            8:待充值
            9:撤销
     */
    @TableField("recharge_state")
    private String rechargeState;

    /**
     * 预计成本价
     */
    @TableField("item_cost")
    private BigDecimal itemCost;

    /**
     * 实际成本价
     */
    @TableField("trans_cost")
    private BigDecimal transCost;

    /**
     * 撤销原因
     */
    @TableField("revoke_message")
    private String revokeMessage;
 
    /**
     * 供应商流水ID
     */
    @TableField("bill_id")
    private String billId;
 
    /**
     * 处理次数
     */
    @TableField("oper_num")
    private Integer operNum;
    
    /**
     * RESV1
     */
    @TableField("resv1")
    private String resv1;
 
    /**
     * RESV2
     */
    @TableField("resv2")
    private String resv2;
 
    /**
     * RESV3
     */
    @TableField("resv3")
    private String resv3;
 
    /**
     * RESV4
     */
    @TableField("resv4")
    private String resv4;
 
    /**
     * RESV5
     */
    @TableField("resv5")
    private String resv5;
 
    /**
     * RESV6
     */
    @TableField("resv6")
    private String resv6;
 
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
     * 修改人
     */
    @TableField("update_user")
    private String updateUser;
 
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Long createTime;
 
    /**
     * 修改时间
     */
    @TableField("update_time")
    private Long updateTime;
 
    /**
     * 乐观锁版本
     */
    @Version
    @TableField("lock_version")
    private Integer lockVersion;

    @TableField(exist = false)
    private String providerName;

    @Override
    protected Serializable pkVal() { 
        return this.regOrderId;
    }
}
