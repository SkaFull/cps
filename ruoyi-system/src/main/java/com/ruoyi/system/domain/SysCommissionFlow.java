package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 佣金流水对象 sys_commission_flow
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
public class SysCommissionFlow extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 流水ID */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 流水类型(income_self:自购收入 income_promotion:推广收入 withdraw:提现 refund:退款) */
    @Excel(name = "流水类型", readConverterExp = "income_self=自购收入,income_promotion=推广收入,withdraw=提现,refund=退款")
    private String flowType;

    /** 变动金额 */
    @Excel(name = "变动金额")
    private BigDecimal amount;

    /** 变动前余额 */
    @Excel(name = "变动前余额")
    private BigDecimal balanceBefore;

    /** 变动后余额 */
    @Excel(name = "变动后余额")
    private BigDecimal balanceAfter;

    /** 关联订单佣金记录ID */
    @Excel(name = "关联订单佣金记录ID")
    private Long orderCommissionId;

    /** 关联订单ID */
    @Excel(name = "关联订单ID")
    private String relatedOrderId;

    /** 流水描述 */
    @Excel(name = "流水描述")
    private String description;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setFlowType(String flowType) 
    {
        this.flowType = flowType;
    }

    public String getFlowType() 
    {
        return flowType;
    }

    public void setAmount(BigDecimal amount) 
    {
        this.amount = amount;
    }

    public BigDecimal getAmount() 
    {
        return amount;
    }

    public void setBalanceBefore(BigDecimal balanceBefore) 
    {
        this.balanceBefore = balanceBefore;
    }

    public BigDecimal getBalanceBefore() 
    {
        return balanceBefore;
    }

    public void setBalanceAfter(BigDecimal balanceAfter) 
    {
        this.balanceAfter = balanceAfter;
    }

    public BigDecimal getBalanceAfter() 
    {
        return balanceAfter;
    }

    public void setOrderCommissionId(Long orderCommissionId) 
    {
        this.orderCommissionId = orderCommissionId;
    }

    public Long getOrderCommissionId() 
    {
        return orderCommissionId;
    }

    public void setRelatedOrderId(String relatedOrderId) 
    {
        this.relatedOrderId = relatedOrderId;
    }

    public String getRelatedOrderId() 
    {
        return relatedOrderId;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("flowType", getFlowType())
            .append("amount", getAmount())
            .append("balanceBefore", getBalanceBefore())
            .append("balanceAfter", getBalanceAfter())
            .append("orderCommissionId", getOrderCommissionId())
            .append("relatedOrderId", getRelatedOrderId())
            .append("description", getDescription())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .toString();
    }
}
