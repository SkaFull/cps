package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 代理佣金账户对象 sys_agent_commission_account
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
public class SysAgentCommissionAccount extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 账户ID */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 累计收益 */
    @Excel(name = "累计收益")
    private BigDecimal totalEarned;

    /** 自购累计收益 */
    @Excel(name = "自购累计收益")
    private BigDecimal selfEarned;

    /** 推广累计收益 */
    @Excel(name = "推广累计收益")
    private BigDecimal promotionEarned;

    /** 已提现金额 */
    @Excel(name = "已提现金额")
    private BigDecimal withdrawn;

    /** 账户余额 */
    @Excel(name = "账户余额")
    private BigDecimal balance;

    /** 冻结金额 */
    @Excel(name = "冻结金额")
    private BigDecimal frozenAmount;

    /** 状态(0:正常 1:冻结) */
    @Excel(name = "状态", readConverterExp = "0=正常,1=冻结")
    private String status;

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

    public void setTotalEarned(BigDecimal totalEarned) 
    {
        this.totalEarned = totalEarned;
    }

    public BigDecimal getTotalEarned() 
    {
        return totalEarned;
    }

    public void setSelfEarned(BigDecimal selfEarned) 
    {
        this.selfEarned = selfEarned;
    }

    public BigDecimal getSelfEarned() 
    {
        return selfEarned;
    }

    public void setPromotionEarned(BigDecimal promotionEarned) 
    {
        this.promotionEarned = promotionEarned;
    }

    public BigDecimal getPromotionEarned() 
    {
        return promotionEarned;
    }

    public void setWithdrawn(BigDecimal withdrawn) 
    {
        this.withdrawn = withdrawn;
    }

    public BigDecimal getWithdrawn() 
    {
        return withdrawn;
    }

    public void setBalance(BigDecimal balance) 
    {
        this.balance = balance;
    }

    public BigDecimal getBalance() 
    {
        return balance;
    }

    public void setFrozenAmount(BigDecimal frozenAmount) 
    {
        this.frozenAmount = frozenAmount;
    }

    public BigDecimal getFrozenAmount() 
    {
        return frozenAmount;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("totalEarned", getTotalEarned())
            .append("selfEarned", getSelfEarned())
            .append("promotionEarned", getPromotionEarned())
            .append("withdrawn", getWithdrawn())
            .append("balance", getBalance())
            .append("frozenAmount", getFrozenAmount())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
