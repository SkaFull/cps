package com.ruoyi.common.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信小程序配置属性
 * 
 * @author ruoyi
 */
@Data
@Component
@ConfigurationProperties(prefix = "wx.miniapp")
public class WeChatMiniAppProperties {
    
    /** 小程序AppID */
    private String appId;
    
    /** 小程序AppSecret */
    private String appSecret;
    
    /** 是否启用 */
    private Boolean enabled = true;
    
    /** 限流配置 */
    private RateLimit rateLimit = new RateLimit();
    
    /** 缓存配置（秒） */
    private Integer userCacheExpire = 3600;
    private Integer tokenCacheExpire = 1800;
    
    /** 安全配置 */
    private Security security = new Security();
    
    /** API配置 */
    private Api api = new Api();
    
    /**
     * 限流配置
     */
    @Data
    public static class RateLimit {
        /** 单个IP每分钟最大请求次数 */
        private Integer maxRequestsPerIp = 10;
        /** 全局每分钟最大请求次数 */
        private Integer maxRequestsGlobal = 1000;
        /** 最大失败次数（超过后锁定） */
        private Integer maxFailures = 5;
        /** 锁定时长（分钟） */
        private Integer lockDuration = 30;
    }
    
    /**
     * 安全配置
     */
    @Data
    public static class Security {
        /** 是否要求HTTPS */
        private Boolean requireHttps = false;
        /** Token过期时间（分钟） */
        private Integer tokenExpire = 30;
        /** 刷新Token过期时间（天） */
        private Integer refreshTokenExpire = 7;
        /** 是否验证IP */
        private Boolean validateIp = false;
        /** 允许的IP列表（逗号分隔，为空表示不限制） */
        private String allowedIps;
        /** 是否启用登录日志 */
        private Boolean enableLoginLog = true;
        /** 是否启用设备指纹 */
        private Boolean enableDeviceFingerprint = false;
    }
    
    /**
     * API配置
     */
    @Data
    public static class Api {
        /** 微信API基础URL */
        private String baseUrl = "https://api.weixin.qq.com";
        /** 登录接口URL */
        private String loginUrl = "/sns/jscode2session";
        /** Token接口URL */
        private String tokenUrl = "/cgi-bin/token";
        /** 请求超时时间（毫秒） */
        private Integer timeout = 5000;
        /** 重试次数 */
        private Integer retryCount = 3;
    }
}
