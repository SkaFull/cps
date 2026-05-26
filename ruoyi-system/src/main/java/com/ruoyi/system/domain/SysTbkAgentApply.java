package com.ruoyi.system.domain;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 淘宝客代理申请对象 sys_tbk_agent_apply
 * 
 * @author ruoyi
 * @date 2026-03-13
 */
public class SysTbkAgentApply extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 申请ID */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 微信小程序AppID */
    @Excel(name = "微信小程序AppID")
    private String miniAppId;

    /** 真实姓名 */
    @Excel(name = "真实姓名")
    private String realName;

    /** 手机号 */
    @Excel(name = "手机号")
    private String phone;

    /** 微信号 */
    @Excel(name = "微信号")
    private String wechat;

    /** 代理专属邀请码 */
    @Excel(name = "邀请码")
    private String invitationCode;

    /** 上级邀请码(推荐人邀请码) */
    @Excel(name = "上级邀请码")
    private String referrerInvitationCode;

    /** 代理级别(1-10级) */
    @Excel(name = "代理级别")
    private Integer agentLevel;

    /** 申请的联盟类型(多个逗号分隔,如:tbk,jd,pdd) */
    @Excel(name = "申请联盟类型")
    private String applyPlatformTypes;

    /** 申请状态(0-待审核,1-已生效,2-已失效) */
    @Excel(name = "申请状态", readConverterExp = "0=待审核,1=已生效,2=已失效")
    private String status;

    /** 审核人 */
    private String auditBy;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date auditTime;

    /** 审核备注 */
    private String auditRemark;

    /** 关联的用户信息 */
    private SysUser user;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setMiniAppId(String miniAppId) {
        this.miniAppId = miniAppId;
    }

    public String getMiniAppId() {
        return miniAppId;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRealName() {
        return realName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getWechat() {
        return wechat;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setReferrerInvitationCode(String referrerInvitationCode) {
        this.referrerInvitationCode = referrerInvitationCode;
    }

    public String getReferrerInvitationCode() {
        return referrerInvitationCode;
    }

    public void setAgentLevel(Integer agentLevel) {
        this.agentLevel = agentLevel;
    }

    public Integer getAgentLevel() {
        return agentLevel;
    }

    public void setApplyPlatformTypes(String applyPlatformTypes) {
        this.applyPlatformTypes = applyPlatformTypes;
    }

    public String getApplyPlatformTypes() {
        return applyPlatformTypes;
    }

    /**
     * 获取申请的联盟类型列表
     *
     * @return 联盟类型列表
     */
    public List<String> getApplyPlatformTypeList() {
        if (StringUtils.isEmpty(applyPlatformTypes)) {
            return new ArrayList<>();
        }
        return Arrays.asList(applyPlatformTypes.split(","));
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setAuditBy(String auditBy) {
        this.auditBy = auditBy;
    }

    public String getAuditBy() {
        return auditBy;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public SysUser getUser() {
        return user;
    }

    public void setUser(SysUser user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("miniAppId", getMiniAppId())
            .append("realName", getRealName())
            .append("phone", getPhone())
            .append("wechat", getWechat())
            .append("invitationCode", getInvitationCode())
            .append("referrerInvitationCode", getReferrerInvitationCode())
            .append("agentLevel", getAgentLevel())
            .append("applyPlatformTypes", getApplyPlatformTypes())
            .append("status", getStatus())
            .append("auditBy", getAuditBy())
            .append("auditTime", getAuditTime())
            .append("auditRemark", getAuditRemark())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
