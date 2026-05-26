package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 代理佣金配置对象 sys_agent_commission_config
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
public class SysAgentCommissionConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 配置ID */
    private Long id;

    /** 代理层级(1:一级代理 2:二级代理) */
    @Excel(name = "代理层级", readConverterExp = "1=一级代理,2=二级代理")
    private Integer agentLevel;

    /** 自购佣金比例(%) */
    @Excel(name = "自购佣金比例(%)")
    private BigDecimal selfCommissionRate;

    /** 推广佣金比例(%) */
    @Excel(name = "推广佣金比例(%)")
    private BigDecimal promotionCommissionRate;

    /** 状态(0:正常 1:停用) */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setAgentLevel(Integer agentLevel) 
    {
        this.agentLevel = agentLevel;
    }

    public Integer getAgentLevel() 
    {
        return agentLevel;
    }

    public void setSelfCommissionRate(BigDecimal selfCommissionRate) 
    {
        this.selfCommissionRate = selfCommissionRate;
    }

    public BigDecimal getSelfCommissionRate() 
    {
        return selfCommissionRate;
    }

    public void setPromotionCommissionRate(BigDecimal promotionCommissionRate) 
    {
        this.promotionCommissionRate = promotionCommissionRate;
    }

    public BigDecimal getPromotionCommissionRate() 
    {
        return promotionCommissionRate;
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
            .append("agentLevel", getAgentLevel())
            .append("selfCommissionRate", getSelfCommissionRate())
            .append("promotionCommissionRate", getPromotionCommissionRate())
            .append("status", getStatus())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
