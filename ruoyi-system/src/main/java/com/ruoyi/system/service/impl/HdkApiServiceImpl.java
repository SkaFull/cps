package com.ruoyi.system.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import com.ruoyi.system.service.IHdkApiService;
import com.ruoyi.union.hdk.config.HdkConfig;
import com.ruoyi.union.hdk.module.HdkModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 好单库API服务实现类
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
@Service
public class HdkApiServiceImpl implements IHdkApiService {

    @Autowired
    private HdkModule hdkModule;

    @Autowired
    private HdkConfig hdkConfig;

    /**
     * 执行好单库API调用的通用方法
     * 
     * @param params 请求参数Map
     * @param apiKey 可选的应用密钥
     * @return API响应结果
     */
    @Override
    public JSON execute(Map<String, String> params, String apiKey) {
        Map<String, String> requestParams = new HashMap<>(params);
        
        // 如果提供了自定义apiKey，则临时替换配置中的apiKey
        if (StrUtil.isNotEmpty(apiKey)) {
            String originalApiKey = hdkConfig.getApiKey();
            try {
                hdkConfig.setApiKey(apiKey);
                return hdkModule.execute(requestParams);
            } finally {
                hdkConfig.setApiKey(originalApiKey);
            }
        }
        
        return hdkModule.execute(requestParams);
    }

    /**
     * 商品列表查询
     * 
     * @param params 查询参数
     * @param apiKey 可选的应用密钥
     * @return 商品列表结果
     */
    @Override
    public JSON getItemList(Map<String, String> params, String apiKey) {
        Map<String, String> requestParams = new HashMap<>(params);
        requestParams.put("apiMethodName", "item/list");
        requestParams.put("v", "v3");
        return execute(requestParams, apiKey);
    }

    /**
     * 商品详情查询
     * 
     * @param params 查询参数，需包含商品ID
     * @param apiKey 可选的应用密钥
     * @return 商品详情结果
     */
    @Override
    public JSON getItemDetail(Map<String, String> params, String apiKey) {
        Map<String, String> requestParams = new HashMap<>(params);
        requestParams.put("apiMethodName", "item/detail");
        requestParams.put("v", "v3");
        return execute(requestParams, apiKey);
    }

    /**
     * 9.9包邮商品查询
     * 
     * @param params 查询参数
     * @param apiKey 可选的应用密钥
     * @return 9.9包邮商品列表
     */
    @Override
    public JSON getNineNine(Map<String, String> params, String apiKey) {
        Map<String, String> requestParams = new HashMap<>(params);
        requestParams.put("apiMethodName", "nineblock");
        requestParams.put("v", "v2");
        return execute(requestParams, apiKey);
    }

    /**
     * 超值大牌商品查询
     * 
     * @param params 查询参数
     * @param apiKey 可选的应用密钥
     * @return 超值大牌商品列表
     */
    @Override
    public JSON getBrandList(Map<String, String> params, String apiKey) {
        Map<String, String> requestParams = new HashMap<>(params);
        requestParams.put("apiMethodName", "brand/list");
        requestParams.put("v", "v3");
        return execute(requestParams, apiKey);
    }

    /**
     * 热搜记录查询
     * 
     * @param params 查询参数
     * @param apiKey 可选的应用密钥
     * @return 热搜记录列表
     */
    @Override
    public JSON getHotSearch(Map<String, String> params, String apiKey) {
        Map<String, String> requestParams = new HashMap<>(params);
        requestParams.put("apiMethodName", "top/search");
        requestParams.put("v", "v2");
        return execute(requestParams, apiKey);
    }

    /**
     * 分类ID查询
     * 
     * @param params 查询参数
     * @param apiKey 可选的应用密钥
     * @return 分类列表
     */
    @Override
    public JSON getCategoryList(Map<String, String> params, String apiKey) {
        Map<String, String> requestParams = new HashMap<>(params);
        requestParams.put("apiMethodName", "cid/list");
        requestParams.put("v", "v2");
        return execute(requestParams, apiKey);
    }
}
