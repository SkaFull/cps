package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 订单佣金记录对象 sys_order_commission
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
public class SysOrderCommission extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 记录ID */
    private Long id;

    /** 订单ID */
    @Excel(name = "订单ID")
    private String orderId;

    /** 平台类型(tbk:淘宝客 jd:京东 pdd:拼多多) */
    @Excel(name = "平台类型", readConverterExp = "tbk=淘宝客,jd=京东,pdd=拼多多")
    private String platformType;

    /** 商品ID */
    @Excel(name = "商品ID")
    private String itemId;

    /** 商品标题 */
    @Excel(name = "商品标题")
    private String itemTitle;

    /** 订单金额 */
    @Excel(name = "订单金额")
    private BigDecimal orderAmount;

    /** 订单总佣金 */
    @Excel(name = "订单总佣金")
    private BigDecimal totalCommission;

    /** 下单代理用户ID */
    @Excel(name = "下单代理用户ID")
    private Long agentUserId;

    /** 自购佣金比例(%) */
    @Excel(name = "自购佣金比例(%)")
    private BigDecimal selfCommissionRate;

    /** 自购佣金金额 */
    @Excel(name = "自购佣金金额")
    private BigDecimal selfCommissionAmount;

    /** 是否有推荐人(0:无 1:有) */
    @Excel(name = "是否有推荐人", readConverterExp = "0=无,1=有")
    private String hasReferrer;

    /** 推荐人用户ID */
    @Excel(name = "推荐人用户ID")
    private Long referrerUserId;

    /** 推广佣金比例(%) */
    @Excel(name = "推广佣金比例(%)")
    private BigDecimal promotionCommissionRate;

    /** 推广佣金金额 */
    @Excel(name = "推广佣金金额")
    private BigDecimal promotionCommissionAmount;

    /** 平台收入 */
    @Excel(name = "平台收入")
    private BigDecimal platformAmount;

    /** 订单状态(pending:待结算 settled:已结算 cancelled:已取消) */
    @Excel(name = "订单状态", readConverterExp = "pending=待结算,settled=已结算,cancelled=已取消")
    private String orderStatus;

    /** 结算时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "结算时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date settleTime;

    /** 预估佣金 */
    @Excel(name = "预估佣金")
    private BigDecimal estimatedCommission;

    /** 佣金比例(%) */
    @Excel(name = "佣金比例(%)")
    private BigDecimal commissionRate;

    /** 买家用户ID */
    @Excel(name = "买家用户ID")
    private Long buyerUserId;

    /** 下单时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "下单时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date orderTime;

    /** 结算状态(0:未结算 1:已结算) */
    @Excel(name = "结算状态", readConverterExp = "0=未结算,1=已结算")
    private String settleStatus;

    /** 实际佣金 */
    @Excel(name = "实际佣金")
    private BigDecimal actualCommission;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setOrderId(String orderId) 
    {
        this.orderId = orderId;
    }

    public String getOrderId() 
    {
        return orderId;
    }

    public void setPlatformType(String platformType) 
    {
        this.platformType = platformType;
    }

    public String getPlatformType() 
    {
        return platformType;
    }

    public void setItemId(String itemId) 
    {
        this.itemId = itemId;
    }

    public String getItemId() 
    {
        return itemId;
    }

    public void setItemTitle(String itemTitle) 
    {
        this.itemTitle = itemTitle;
    }

    public String getItemTitle() 
    {
        return itemTitle;
    }

    public void setOrderAmount(BigDecimal orderAmount) 
    {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getOrderAmount() 
    {
        return orderAmount;
    }

    public void setTotalCommission(BigDecimal totalCommission) 
    {
        this.totalCommission = totalCommission;
    }

    public BigDecimal getTotalCommission() 
    {
        return totalCommission;
    }

    public void setAgentUserId(Long agentUserId) 
    {
        this.agentUserId = agentUserId;
    }

    public Long getAgentUserId() 
    {
        return agentUserId;
    }

    public void setSelfCommissionRate(BigDecimal selfCommissionRate) 
    {
        this.selfCommissionRate = selfCommissionRate;
    }

    public BigDecimal getSelfCommissionRate() 
    {
        return selfCommissionRate;
    }

    public void setSelfCommissionAmount(BigDecimal selfCommissionAmount) 
    {
        this.selfCommissionAmount = selfCommissionAmount;
    }

    public BigDecimal getSelfCommissionAmount() 
    {
        return selfCommissionAmount;
    }

    public void setHasReferrer(String hasReferrer) 
    {
        this.hasReferrer = hasReferrer;
    }

    public String getHasReferrer() 
    {
        return hasReferrer;
    }

    public void setReferrerUserId(Long referrerUserId) 
    {
        this.referrerUserId = referrerUserId;
    }

    public Long getReferrerUserId() 
    {
        return referrerUserId;
    }

    public void setPromotionCommissionRate(BigDecimal promotionCommissionRate) 
    {
        this.promotionCommissionRate = promotionCommissionRate;
    }

    public BigDecimal getPromotionCommissionRate() 
    {
        return promotionCommissionRate;
    }

    public void setPromotionCommissionAmount(BigDecimal promotionCommissionAmount) 
    {
        this.promotionCommissionAmount = promotionCommissionAmount;
    }

    public BigDecimal getPromotionCommissionAmount() 
    {
        return promotionCommissionAmount;
    }

    public void setPlatformAmount(BigDecimal platformAmount) 
    {
        this.platformAmount = platformAmount;
    }

    public BigDecimal getPlatformAmount() 
    {
        return platformAmount;
    }

    public void setOrderStatus(String orderStatus) 
    {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() 
    {
        return orderStatus;
    }

    public void setSettleTime(Date settleTime) 
    {
        this.settleTime = settleTime;
    }

    public Date getSettleTime() 
    {
        return settleTime;
    }

    public void setEstimatedCommission(BigDecimal estimatedCommission) 
    {
        this.estimatedCommission = estimatedCommission;
    }

    public BigDecimal getEstimatedCommission() 
    {
        return estimatedCommission;
    }

    public void setCommissionRate(BigDecimal commissionRate) 
    {
        this.commissionRate = commissionRate;
    }

    public BigDecimal getCommissionRate() 
    {
        return commissionRate;
    }

    public void setBuyerUserId(Long buyerUserId) 
    {
        this.buyerUserId = buyerUserId;
    }

    public Long getBuyerUserId() 
    {
        return buyerUserId;
    }

    public void setOrderTime(Date orderTime) 
    {
        this.orderTime = orderTime;
    }

    public Date getOrderTime() 
    {
        return orderTime;
    }

    public void setSettleStatus(String settleStatus) 
    {
        this.settleStatus = settleStatus;
    }

    public String getSettleStatus() 
    {
        return settleStatus;
    }

    public void setActualCommission(BigDecimal actualCommission) 
    {
        this.actualCommission = actualCommission;
    }

    public BigDecimal getActualCommission() 
    {
        return actualCommission;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orderId", getOrderId())
            .append("platformType", getPlatformType())
            .append("itemId", getItemId())
            .append("itemTitle", getItemTitle())
            .append("orderAmount", getOrderAmount())
            .append("totalCommission", getTotalCommission())
            .append("agentUserId", getAgentUserId())
            .append("selfCommissionRate", getSelfCommissionRate())
            .append("selfCommissionAmount", getSelfCommissionAmount())
            .append("hasReferrer", getHasReferrer())
            .append("referrerUserId", getReferrerUserId())
            .append("promotionCommissionRate", getPromotionCommissionRate())
            .append("promotionCommissionAmount", getPromotionCommissionAmount())
            .append("platformAmount", getPlatformAmount())
            .append("orderStatus", getOrderStatus())
            .append("settleTime", getSettleTime())
            .append("estimatedCommission", getEstimatedCommission())
            .append("commissionRate", getCommissionRate())
            .append("buyerUserId", getBuyerUserId())
            .append("orderTime", getOrderTime())
            .append("settleStatus", getSettleStatus())
            .append("actualCommission", getActualCommission())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
