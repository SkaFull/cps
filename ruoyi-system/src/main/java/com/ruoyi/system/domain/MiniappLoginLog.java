package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 小程序登录日志对象 sys_miniapp_login_log
 * 
 * @author ruoyi
 * @date 2026-03-13
 */
public class MiniappLoginLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 日志ID */
    private Long logId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 微信OpenID */
    @Excel(name = "微信OpenID")
    private String openId;

    /** 登录时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date loginTime;

    /** 登录IP */
    @Excel(name = "登录IP")
    private String loginIp;

    /** 登录地点 */
    @Excel(name = "登录地点")
    private String loginLocation;

    /** 浏览器类型 */
    @Excel(name = "浏览器类型")
    private String browser;

    /** 操作系统 */
    @Excel(name = "操作系统")
    private String os;

    /** 设备类型 */
    @Excel(name = "设备类型")
    private String deviceType;

    /** 设备品牌 */
    @Excel(name = "设备品牌")
    private String deviceBrand;

    /** 设备型号 */
    @Excel(name = "设备型号")
    private String deviceModel;

    /** 小程序版本 */
    @Excel(name = "小程序版本")
    private String miniAppVersion;

    /** 微信版本 */
    @Excel(name = "微信版本")
    private String wxVersion;

    /** 登录状态 */
    @Excel(name = "登录状态")
    private String status;

    /** 提示消息 */
    @Excel(name = "提示消息")
    private String msg;

    public void setLogId(Long logId) 
    {
        this.logId = logId;
    }

    public Long getLogId() 
    {
        return logId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setOpenId(String openId) 
    {
        this.openId = openId;
    }

    public String getOpenId() 
    {
        return openId;
    }

    public void setLoginTime(Date loginTime) 
    {
        this.loginTime = loginTime;
    }

    public Date getLoginTime() 
    {
        return loginTime;
    }

    public void setLoginIp(String loginIp) 
    {
        this.loginIp = loginIp;
    }

    public String getLoginIp() 
    {
        return loginIp;
    }

    public void setLoginLocation(String loginLocation) 
    {
        this.loginLocation = loginLocation;
    }

    public String getLoginLocation() 
    {
        return loginLocation;
    }

    public void setBrowser(String browser) 
    {
        this.browser = browser;
    }

    public String getBrowser() 
    {
        return browser;
    }

    public void setOs(String os) 
    {
        this.os = os;
    }

    public String getOs() 
    {
        return os;
    }

    public void setDeviceType(String deviceType) 
    {
        this.deviceType = deviceType;
    }

    public String getDeviceType() 
    {
        return deviceType;
    }

    public void setDeviceBrand(String deviceBrand) 
    {
        this.deviceBrand = deviceBrand;
    }

    public String getDeviceBrand() 
    {
        return deviceBrand;
    }

    public void setDeviceModel(String deviceModel) 
    {
        this.deviceModel = deviceModel;
    }

    public String getDeviceModel() 
    {
        return deviceModel;
    }

    public void setMiniAppVersion(String miniAppVersion) 
    {
        this.miniAppVersion = miniAppVersion;
    }

    public String getMiniAppVersion() 
    {
        return miniAppVersion;
    }

    public void setWxVersion(String wxVersion) 
    {
        this.wxVersion = wxVersion;
    }

    public String getWxVersion() 
    {
        return wxVersion;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setMsg(String msg) 
    {
        this.msg = msg;
    }

    public String getMsg() 
    {
        return msg;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("logId", getLogId())
            .append("userId", getUserId())
            .append("openId", getOpenId())
            .append("loginTime", getLoginTime())
            .append("loginIp", getLoginIp())
            .append("loginLocation", getLoginLocation())
            .append("browser", getBrowser())
            .append("os", getOs())
            .append("deviceType", getDeviceType())
            .append("deviceBrand", getDeviceBrand())
            .append("deviceModel", getDeviceModel())
            .append("miniAppVersion", getMiniAppVersion())
            .append("wxVersion", getWxVersion())
            .append("status", getStatus())
            .append("msg", getMsg())
            .toString();
    }
}
