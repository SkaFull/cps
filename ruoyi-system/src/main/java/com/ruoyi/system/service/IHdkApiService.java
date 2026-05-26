package com.ruoyi.system.service;

import cn.hutool.json.JSON;

import java.util.Map;

/**
 * 好单库API服务接口
 * 提供好单库商品查询、转链等功能
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
public interface IHdkApiService {
    
    /**
     * 执行好单库API调用的通用方法
     * 
     * @param params 请求参数Map，必须包含apiMethodName
     * @param apiKey 可选的应用密钥，若为null则使用默认配置
     * @return 好单库API响应结果
     */
    JSON execute(Map<String, String> params, String apiKey);
    
    /**
     * 商品列表查询
     * 
     * @param params 查询参数
     * @param apiKey 可选的应用密钥
     * @return 商品列表结果
     */
    JSON getItemList(Map<String, String> params, String apiKey);
    
    /**
     * 商品详情查询
     * 
     * @param params 查询参数，需包含商品ID
     * @param apiKey 可选的应用密钥
     * @return 商品详情结果
     */
    JSON getItemDetail(Map<String, String> params, String apiKey);
    
    /**
     * 9.9包邮商品查询
     * 
     * @param params 查询参数
     * @param apiKey 可选的应用密钥
     * @return 9.9包邮商品列表
     */
    JSON getNineNine(Map<String, String> params, String apiKey);
    
    /**
     * 超值大牌商品查询
     * 
     * @param params 查询参数
     * @param apiKey 可选的应用密钥
     * @return 超值大牌商品列表
     */
    JSON getBrandList(Map<String, String> params, String apiKey);
    
    /**
     * 热搜记录查询
     * 
     * @param params 查询参数
     * @param apiKey 可选的应用密钥
     * @return 热搜记录列表
     */
    JSON getHotSearch(Map<String, String> params, String apiKey);
    
    /**
     * 分类ID查询
     * 
     * @param params 查询参数
     * @param apiKey 可选的应用密钥
     * @return 分类列表
     */
    JSON getCategoryList(Map<String, String> params, String apiKey);
}
