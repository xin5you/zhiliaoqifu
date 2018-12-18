package com.ebeijia.zl.web.oms.batchOrder.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_batch_order")
public class BatchOrder extends Model<BatchOrder> {
	/**
     * 订单号
     */
    @TableId(value = "order_id" ,type = IdType.UUID)
	private String orderId;                                
    
    /**
     * 订单名称
     */
    @TableField("order_name")
	private String orderName;                     
    
    /**
     * 订单类型
     */
    @TableField("order_type")
	private String orderType;                       
    
    /**
     * 订单日期
     */
    @TableField("order_date")
	private long orderDate;                          
    
    /**
     *订单状态
     */
    @TableField("order_stat")
	private String orderStat;                        
    
    /**
     * 企业ID
     */
    @TableField("company_id")
	private String companyId;              			
    
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
	private String orderCount;                     //订单数量
    
    @TableField(exist = false)
	private String sumAmount;                   //总金额
    
    @TableField(exist = false)
	private String startTime;                       //开始时间
    
    @TableField(exist = false)
	private String endTime;                       //结束时间
    
    @TableField(exist = false)
	private String disposeWait;                  //未处理
    
    @TableField(exist = false)
	private String disposeSuccess;             //处理成功
    
    @TableField(exist = false)
	private String disposeFail;                   //处理失败
	
    @TableField(exist = false)
	private String companyName;
	
}
