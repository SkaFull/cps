package com.ruoyi.system.service;

import com.alibaba.fastjson2.JSONObject;

import java.util.Map;

/**
 * 淘宝客SDK封装Service接口
 * @author ruoyi
 * @date 2026-03-13
 */
public interface ITbkSdkService {

    /**
     * 物料搜索升级版（使用系统默认配置）
     * @param params 请求参数
     * @return 商品列表结果
     */
    JSONObject dgMaterialOptionalUpgrade(Map<String, String> params);

    /**
     * 物料搜索升级版（使用指定的代理配置）
     * @param params 请求参数
     * @param appKey 代理的appKey
     * @param appSecret 代理的appSecret
     * @param adzoneId 代理的推广位ID
     * @return 商品列表结果
     */
    JSONObject dgMaterialOptionalUpgrade(Map<String, String> params, String appKey, String appSecret, Long adzoneId);

    /**
     * 生成淘口令（使用系统默认配置）
     * @param params 请求参数（包含text、url、logo等）
     * @return 淘口令结果
     */
    Map<String, Object> getTpwd(Map<String, String> params);

    /**
     * 生成淘口令（使用指定的代理配置）
     * @param params 请求参数（包含text、url、logo等）
     * @param appKey 代理的appKey
     * @param appSecret 代理的appSecret
     * @return 淘口令结果
     */
    Map<String, Object> getTpwd(Map<String, String> params, String appKey, String appSecret);

    /**
     * 获取商品详情（使用系统默认配置）
     * @param numIids 商品ID，多个用逗号分隔，最多40个
     * @return 商品详情结果
     */
    JSONObject getItemInfo(String numIids);

    /**
     * 获取商品详情（使用指定的代理配置）
     * @param numIids 商品ID，多个用逗号分隔，最多40个
     * @param appKey 代理的appKey
     * @param appSecret 代理的appSecret
     * @return 商品详情结果
     */
    JSONObject getItemInfo(String numIids, String appKey, String appSecret);

}
