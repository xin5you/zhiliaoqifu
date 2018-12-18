package com.ebeijia.zl.web.oms.batchOrder.model;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_batch_order_list")
public class BatchOrderList extends Model<BatchOrder>{
	/**
     * 订单明细_id
     */
    @TableId(value = "order_list_id" ,type = IdType.UUID)
	private String orderListId;                     	 
    
    /**
     * 订单号
     */
    @TableField("order_id")
	private String orderId;                          
    
    /**
     * 用户名
     */
    @TableField("user_name")
	private String userName;                       
    
    /**
     * 手机号
     */
    @TableField("phone_no")
	private String phoneNo;                       
    
    /**
     * 身份证号
     */
    @TableField("user_card_no")
	private String userCardNo;                  
    
    /**
     * 金额
     */
    @TableField("amount")
	private BigDecimal amount;                         
    
    /**
     * 账户类型
     */
    @TableField("account_type")
	private String accountType;				
    
    /**
     * 专项类型（九大类）
     */
    @TableField("biz_type")
	private String bizType;						
    
    /**
     * 订单状态
     */
    @TableField("order_stat")
	private String orderStat;                   
    
    /**
     * 订单描述
     */
    @TableField("order_desc")
	private String orderDesc;                  
    
    /**
     * 订单名称
     */
    @TableField("tfr_in_id")
	private String tfrInId;
    
    /**
     * 订单名称
     */
    @TableField("tfr_in_bid")
	private String tfrInBid;
    
    /**
     * 订单名称
     */
    @TableField("tfr_out_id")
	private String tfrOutId;
    
    /**
     * 订单名称
     */
    @TableField("tfr_out_bid")
	private String tfrOutBid;
    
    /**
     * 备用字段1
     */
    @TableField("resv1")
	private String resv1;                            
    
    /**
     * 用户名
     */
    @TableField("resv2")
	private String resv2;                            
    
    /**
     * 备用字段3
     */
    @TableField("resv3")
	private String resv3;                           
    
    /**
     * 备用字段4
     */
    @TableField("resv4")
	private String resv4;                             
    
    /**
     * 备用字段5
     */
    @TableField("resv5")
	private String resv5;                            
    
    /**
     * 备用字段6
     */
    @TableField("resv6")
	private String resv6;     
    
    /**
     * 状态
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
	
    @TableField(exist = false)
	private String puId;
	
    @TableField(exist = false)
	private String orderStat2;
    
    @TableField(exist = false)
	private String orderStat3;
    
    @TableField(exist = false)
	private String orderStat4;
	
    @TableField(exist = false)
	private String companyId;
    
    @TableField(exist = false)
	private String accountTypeName;
    
    @TableField(exist = false)
	private String bizTypeName;

}
