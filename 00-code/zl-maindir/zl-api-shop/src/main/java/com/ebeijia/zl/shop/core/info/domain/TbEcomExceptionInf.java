package com.ebeijia.zl.shop.core.info.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 *
 * 异常信息表
 *
 * @User J
 * @Date 2018-12-03
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_ecom_exception_inf")
@ApiModel("异常信息表")
public class TbEcomExceptionInf extends Model<TbEcomExceptionInf> {
 
    @TableId(value = "id" ,type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private String id;
 
    /**
     * 流水号包括：订单id、交易流水号等
     */
    @TableField("primary_key")
    @ApiModelProperty(value = "流水号包括：订单id、交易流水号等")
    private String primaryKey;
 
    /**
     * 调用接口的传入参数
     */
    @TableField("input_parameter")
    @ApiModelProperty(value = "调用接口的传入参数")
    private String inputParameter;
 
    /**
     * 1000：网易严选下订单
                        1001：网易严选 取消订单
                        1002：网易严选 订单确认收货
                        1003：网易严选 订单信息查询
                        1004：网易严选 获取物流轨迹信息
                        1005：订单取消回调
                        1006：订单包裹物流绑单回调
                        1007：订单异常回调
                        1008：SKU库存划拨回调
                        1009：SKU库存校准回调
                        1010：SKU低库存预警通知
                        1011：SKU再次开售通知
                        1012：退货地址回调
                        1013：严选拒绝退货回调
                        1014：退货包裹确认收货回调
                        1015：严选系统取消退货回调
                        1016：退款结果回调
                        1017：发起售后服务请求
                        1018：取消售后服务请求
                        1019：绑定售后寄回物流单号
                        1020：查询售后申请详情
     */
    @TableField("exception_type")
    @ApiModelProperty(value = "1000：网易严选下订单                        1001：网易严选 取消订单                        1002：网易严选 订单确认收货                        1003：网易严选 订单信息查询                        1004：网易严选 获取物流轨迹信息                        1005：订单取消回调                        1006：订单包裹物流绑单回调                        1007：订单异常回调                        1008：SKU库存划拨回调                        1009：SKU库存校准回调                        1010：SKU低库存预警通知                        1011：SKU再次开售通知                        1012：退货地址回调                        1013：严选拒绝退货回调                        1014：退货包裹确认收货回调                        1015：严选系统取消退货回调                        1016：退款结果回调                        1017：发起售后服务请求                        1018：取消售后服务请求                        1019：绑定售后寄回物流单号                        1020：查询售后申请详情")
    private String exceptionType;
 
    @TableField("exception_desc")
    @ApiModelProperty(value = "exception_desc")
    private String exceptionDesc;
 
    /**
     * 1：已处理、0：未处理（默认）
     */
    @TableField("exception_statc")
    @ApiModelProperty(value = "1：已处理、0：未处理（默认）")
    private String exceptionStatc;
 
    @TableField("process_times")
    @ApiModelProperty(value = "process_times")
    private Integer processTimes;
 
    @TableField("data_stat")
    @ApiModelProperty(value = "data_stat")
    private String dataStat;
 
    @TableField("remarks")
    @ApiModelProperty(value = "remarks")
    private String remarks;
 
    @TableField("create_user")
    @ApiModelProperty(value = "create_user")
    private String createUser;
 
    @TableField("update_user")
    @ApiModelProperty(value = "update_user")
    private String updateUser;
 
    @TableField("create_time")
    @ApiModelProperty(value = "create_time")
    private Long createTime;
 
    @TableField("update_time")
    @ApiModelProperty(value = "update_time")
    private Long updateTime;
 
    @TableField("lock_version")
    @ApiModelProperty(value = "lock_version")
    private Integer lockVersion;


    @Override
    protected Serializable pkVal() { 
        return this.id;
    }
}
