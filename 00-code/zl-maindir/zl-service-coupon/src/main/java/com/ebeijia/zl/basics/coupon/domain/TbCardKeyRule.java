package com.ebeijia.zl.basics.coupon.domain;

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
 * 卡密池
 *
 * @User J
 * @Date 2018-12-13
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_card_key_rule")
@ApiModel("卡密池")
public class TbCardKeyRule extends Model<TbCardKeyRule> {
 
    /**
     * 创建时间
     */
    @TableField("create_time")
    @ApiModelProperty(value = "创建时间")
    private Long createTime;
 
    /**
     * 创建人
     */
    @TableField("create_user")
    @ApiModelProperty(value = "创建人")
    private String createUser;
 
    /**
     * 数据状态
     */
    @TableField("data_stat")
    @ApiModelProperty(value = "数据状态")
    private String dataStat;
 
    /**
     * 说明
     */
    @TableField("description")
    @ApiModelProperty(value = "说明")
    private String description;
 
    /**
     * 加密规则id
     */
    @TableId(value = "key_rule_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "加密规则id")
    private String keyRuleId;
 
    /**
     * 乐观锁版本
     */
    @TableField("lock_version")
    @ApiModelProperty(value = "乐观锁版本")
    private Integer lockVersion;
 
    /**
     * 加密逻辑
     */
    @TableField("rule_key")
    @ApiModelProperty(value = "加密逻辑")
    private String ruleKey;
 
    /**
     * 更新时间
     */
    @TableField("update_time")
    @ApiModelProperty(value = "更新时间")
    private Long updateTime;
 
    /**
     * 更新人
     */
    @TableField("update_user")
    @ApiModelProperty(value = "更新人")
    private String updateUser;


    @Override
    protected Serializable pkVal() { 
        return this.keyRuleId;
    }
}
