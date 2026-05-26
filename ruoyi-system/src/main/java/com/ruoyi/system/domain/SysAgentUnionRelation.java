package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 代理联盟关联对象 sys_agent_union_relation
 *
 * @author ruoyi
 */
public class SysAgentUnionRelation extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 关联ID */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 申请ID */
    @Excel(name = "申请ID")
    private Long applyId;

    /** 联盟平台ID */
    @Excel(name = "联盟平台ID")
    private Long unionPlatformId;

    /** 联盟类型（冗余字段，方便查询） */
    @Excel(name = "联盟类型", readConverterExp = "tbk=淘宝客,jd=京东,pdd=拼多多,dtk=大淘客")
    private String platformType;

    /** 关联状态（0-失效，1-生效） */
    @Excel(name = "关联状态", readConverterExp = "0=失效,1=生效")
    private String status;

    /** 授权状态（0-未授权，1-已授权） */
    @Excel(name = "授权状态", readConverterExp = "0=未授权,1=已授权")
    private String authStatus;

    /** 授权URL */
    @Excel(name = "授权URL")
    private String authUrl;

    /** 自定义参数（JSON格式） */
    @Excel(name = "自定义参数")
    private String customParameters;

    // ====== 关联查询字段（非数据库字段） ======

    /** 联盟信息（关联查询用） */
    private SysUnionPlatform unionPlatform;

    /** 用户昵称（关联查询用） */
    private String userName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public Long getUnionPlatformId() {
        return unionPlatformId;
    }

    public void setUnionPlatformId(Long unionPlatformId) {
        this.unionPlatformId = unionPlatformId;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SysUnionPlatform getUnionPlatform() {
        return unionPlatform;
    }

    public void setUnionPlatform(SysUnionPlatform unionPlatform) {
        this.unionPlatform = unionPlatform;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    public String getCustomParameters() {
        return customParameters;
    }

    public void setCustomParameters(String customParameters) {
        this.customParameters = customParameters;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("applyId", getApplyId())
                .append("unionPlatformId", getUnionPlatformId())
                .append("platformType", getPlatformType())
                .append("status", getStatus())
                .append("authStatus", getAuthStatus())
                .append("authUrl", getAuthUrl())
                .append("customParameters", getCustomParameters())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
