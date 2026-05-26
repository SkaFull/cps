package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 联盟信息对象 sys_union_platform
 *
 * @author ruoyi
 */
public class SysUnionPlatform extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 联盟ID */
    private Long id;

    /** 联盟类型（tbk-淘宝客,jd-京东,pdd-拼多多,dtk-大淘客） */
    @Excel(name = "联盟类型", readConverterExp = "tbk=淘宝客,jd=京东,pdd=拼多多,dtk=大淘客")
    private String platformType;

    /** 联盟名称 */
    @Excel(name = "联盟名称")
    private String platformName;

    /** 应用钥匙 */
    @Excel(name = "App Key")
    private String appKey;

    /** 应用密匙 */
    private String appSecret;

    /** 站点ID */
    @Excel(name = "站点ID")
    private String siteId;

    /** 推广位ID */
    @Excel(name = "推广位ID")
    private String adzoneId;

    /** 访问令牌 */
    private String accessToken;

    /** 使用状态（0-未被应用，1-被使用，2-失效） */
    @Excel(name = "使用状态", readConverterExp = "0=未被应用,1=被使用,2=失效")
    private String isQuote;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlatformType() {
        return platformType;
    }

    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getAdzoneId() {
        return adzoneId;
    }

    public void setAdzoneId(String adzoneId) {
        this.adzoneId = adzoneId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getIsQuote() {
        return isQuote;
    }

    public void setIsQuote(String isQuote) {
        this.isQuote = isQuote;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("platformType", getPlatformType())
                .append("platformName", getPlatformName())
                .append("appKey", getAppKey())
                .append("appSecret", getAppSecret())
                .append("siteId", getSiteId())
                .append("adzoneId", getAdzoneId())
                .append("accessToken", getAccessToken())
                .append("isQuote", getIsQuote())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
