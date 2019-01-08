package com.ebeijia.zl.shop.vo;

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
import java.math.BigDecimal;

/**
 *
 * 企业员工在平台账户类型
 *
 * @Date 2018-12-18
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("tb_billing_type")
@ApiModel("企业员工在平台账户类型")
public class BillingType extends Model<BillingType> {

    /**
     * 专项类型id
     */
    @TableId(value = "b_id" ,type = IdType.UUID)
    @ApiModelProperty(value = "专项类型id")
    private String bId;

    /**
     * 专项名称
     */
    @TableField("b_name")
    @ApiModelProperty(value = "专项名称")
    private String bName;

    /**
     * A类账户
     B类账户
     C类账户
     */
    @TableField("code")
    @ApiModelProperty(value = "A类账户            B类账户            C类账户")
    private String code;

    /**
     * 数据状态
     */
    @TableField("data_stat")
    @ApiModelProperty(value = "数据状态")
    private String dataStat;

    /**
     * 备注
     */
    @TableField("remarks")
    @ApiModelProperty(value = "备注")
    private String remarks;

    /**
     * 创建人
     */
    @TableField("create_user")
    @ApiModelProperty(value = "创建人")
    private String createUser;

    /**
     * 更新人
     */
    @TableField("update_user")
    @ApiModelProperty(value = "更新人")
    private String updateUser;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    @ApiModelProperty(value = "更新时间")
    private Long updateTime;

    /**
     * 乐观锁版本
     */
    @TableField("lock_version")
    @ApiModelProperty(value = "乐观锁版本")
    private Integer lockVersion;

    /**
     * 折损率
     */
    @TableField("lose_fee")
    @ApiModelProperty(value = "折损率")
    private BigDecimal loseFee;

    /**
     * 可购率
     */
    @TableField("buy_fee")
    @ApiModelProperty(value = "可购率")
    private BigDecimal buyFee;

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDataStat() {
        return dataStat;
    }

    public void setDataStat(String dataStat) {
        this.dataStat = dataStat;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getLockVersion() {
        return lockVersion;
    }

    public void setLockVersion(Integer lockVersion) {
        this.lockVersion = lockVersion;
    }

    public BigDecimal getLoseFee() {
        return loseFee;
    }

    public void setLoseFee(BigDecimal loseFee) {
        this.loseFee = loseFee;
    }

    public BigDecimal getBuyFee() {
        return buyFee;
    }

    public void setBuyFee(BigDecimal buyFee) {
        this.buyFee = buyFee;
    }

    @Override
    protected Serializable pkVal() {
        return this.bId;
    }
}
