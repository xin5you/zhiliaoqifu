package com.cn.thinkx.wecard.facade.telrecharge.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * 分销商订单明细表
 *
 * @User zhuqi
 * @Date 2018-12-10
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_retail_chnl_order_inf")
public class RetailChnlOrderInf extends Model<RetailChnlOrderInf> {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 3162740953743544278L;

	/**
     * chnel_order_id
     */
    @TableId(value = "channel_order_id" ,type = IdType.UUID)
    private String channelOrderId;
 
    /**
     * 分销商ID
     */
    @TableField("channel_id")
    private String channelId;
 
    /**
     * 外部ID
     */
    @TableField("outer_tid")
    private String outerTid;
 
    /**
     * 充值的手机
     */
    @TableField("recharge_phone")
    private String rechargePhone;
 
    /**
     * 1:话费
            2：流量
     */
    @TableField("recharge_type")
    private String rechargeType;
 
    /**
     * 单位元
     */
    @TableField("recharge_value")
    private BigDecimal rechargeValue;
 
    /**
     * 充值的数量
     */
    @TableField("item_num")
    private Integer itemNum;
 
    /**
     * 支付的价格
     */
    @TableField("pay_amt")
    private BigDecimal payAmt;
 
    /**
     * 0:待扣款
            1:已扣款
            2:已退款
            5:退款中
     */
    @TableField("order_stat")
    private String orderStat;
 
    /**
     * 1：处理中
            2：处理失败
            3：处理成功
     */
    @TableField("notify_stat")
    private String notifyStat;
 
    /**
     * 0:通知，1不通知
     */
    @TableField("notify_flag")
    private String notifyFlag;
 
    /**
     * 回调地址
     */
    @TableField("notify_url")
    private String notifyUrl;
 
    /**
     * app_version
     */
    @TableField("app_version")
    private String appVersion;
 
    /**
     * product_id
     */
    @TableField("product_id")
    private String productId;
 
    /**
     * channel_rate
     */
    @TableField("channel_rate")
    private BigDecimal channelRate;
 
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
    @TableField("lock_version")
    private Integer lockVersion;
    
    @TableField(exist=false)
	private String channelName; // 分销商名称
    
    @TableField(exist=false)
	private String operName;//商品名称
    
    @TableField(exist=false)
	private String phoneNo;
    
    @TableField(exist=false)
	private String startDate;
    
    @TableField(exist=false)
	private String endDate;
    
    @TableField(exist=false)
	private String rechargeState;
    
    @TableField(exist=false)
	private String orderCount;
    
    @TableField(exist=false)
	private BigDecimal orderRechargeAmt;
    
    @TableField(exist=false)
	private BigDecimal orderPayAmt;


    @Override
    protected Serializable pkVal() { 
        return this.channelOrderId;
    }
}
