package com.ruoyi.union.dtk.config;

import lombok.Data;

/**
 * 大淘客API的参数
 * 该类用于存储调用大淘客 API 所需的配置信息
 */
@Data
public class DtkConfig {

    /**
     * 大淘客 API 的应用密钥
     * 用于标识调用 API 的应用身份，是调用大淘客 API 时必需的参数
     */
    private String appKey;
    /**
     * 大淘客 API 的应用密钥对应的密钥
     * 用于对请求进行签名验证，确保请求的安全性，与 appKey 配合使用
     */
    private String appSecret;

    /**
     * 淘宝联盟PID
     * mm_xxx_xxx_12345678（mm+账号ID+媒体ID+推广位ID）三段式
     * 用于标识具体的推广位置，在进行推广相关的API请求时会用到。
     */
    private String adzoneId;

    /**
     * 关键词搜索，如果没有传，就使用系统默认的
     */
    private String keyWords;

    /**
     * 关淘口令右边自定义符号,默认￥
     */
    private String rightSymbol;

    /**
     * 是否开启调试模式
     */
    private boolean debug = false;
}
