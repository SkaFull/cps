package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.system.service.ITbkSdkService;
import com.ruoyi.union.tbk.config.TbkConfig;
import com.ruoyi.union.tbk.constants.TbkApiConstants;
import com.ruoyi.union.tbk.module.TbkModule;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.TaobaoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 淘宝客SDK封装Service接口
 */
@Service
public class TbkSdkService implements ITbkSdkService {

    @Autowired
    private TbkModule tbkModule;

    @Autowired
    private TbkConfig tbkConfig;

    /**
     * 物料搜索升级版（使用系统默认配置）
     * @param params 请求参数
     * @return 商品列表结果
     */
    @Override
    public JSONObject dgMaterialOptionalUpgrade(Map<String, String> params) {
        params.put("apiMethodName", TbkApiConstants.API_METHOD_NAME_UPGRADE_DG_MATERIAL_OPTIONAL);
        params.put("pageNo", params.get("pageNum"));
        TaobaoResponse taobaoResponse = tbkModule.execute(params);
        return parseResponse(taobaoResponse);
    }

    /**
     * 物料搜索升级版（使用指定的代理配置）
     * @param params 请求参数
     * @param appKey 代理的appKey
     * @param appSecret 代理的appSecret
     * @param adzoneId 代理的推广位ID
     * @return 商品列表结果
     */
    @Override
    public JSONObject dgMaterialOptionalUpgrade(Map<String, String> params, String appKey, String appSecret, Long adzoneId) {
        // 创建代理专用的TaobaoClient
        TaobaoClient agentClient = new DefaultTaobaoClient(tbkConfig.getUrl(), appKey, appSecret);
        
        params.put("apiMethodName", TbkApiConstants.API_METHOD_NAME_UPGRADE_DG_MATERIAL_OPTIONAL);
        params.put("pageNo", params.get("pageNum"));
        // 设置代理的推广位ID
        params.put("adzoneId", String.valueOf(adzoneId));
        
        // 使用代理的TaobaoClient执行请求
        TaobaoResponse taobaoResponse = tbkModule.execute(params, agentClient);
        return parseResponse(taobaoResponse);
    }

    /**
     * 生成淘口令（使用系统默认配置）
     * @param params 请求参数（包含text、url、logo等）
     * @return 淘口令结果
     */
    @Override
    public Map<String, Object> getTpwd(Map<String, String> params) {
        params.put("apiMethodName", TbkApiConstants.API_METHOD_NAME_CREATE_TPWD);
        return tbkModule.getTpwd(params);
    }

    /**
     * 生成淘口令（使用指定的代理配置）
     * @param params 请求参数（包含text、url、logo等）
     * @param appKey 代理的appKey
     * @param appSecret 代理的appSecret
     * @return 淘口令结果
     */
    @Override
    public Map<String, Object> getTpwd(Map<String, String> params, String appKey, String appSecret) {
        // 创建代理专用的TaobaoClient
        TaobaoClient agentClient = new DefaultTaobaoClient(tbkConfig.getUrl(), appKey, appSecret);
        
        params.put("apiMethodName", TbkApiConstants.API_METHOD_NAME_CREATE_TPWD);
        
        // 使用代理的TaobaoClient执行请求
        return tbkModule.getTpwd(params, agentClient);
    }

    /**
     * 获取商品详情（使用系统默认配置）
     * @param numIids 商品ID，多个用逗号分隔，最多40个
     * @return 商品详情结果
     */
    @Override
    public JSONObject getItemInfo(String numIids) {
        Map<String, String> params = new HashMap<>();
        params.put("apiMethodName", TbkApiConstants.API_METHOD_NAME_GET_ITEM_INFO);
        params.put("numIids", numIids);
        params.put("platform", "2"); // 1:PC, 2:无线
        
        TaobaoResponse taobaoResponse = tbkModule.execute(params);
        return parseItemInfoResponse(taobaoResponse);
    }

    /**
     * 获取商品详情（使用指定的代理配置）
     * @param numIids 商品ID，多个用逗号分隔，最多40个
     * @param appKey 代理的appKey
     * @param appSecret 代理的appSecret
     * @return 商品详情结果
     */
    @Override
    public JSONObject getItemInfo(String numIids, String appKey, String appSecret) {
        // 创建代理专用的TaobaoClient
        TaobaoClient agentClient = new DefaultTaobaoClient(tbkConfig.getUrl(), appKey, appSecret);
        
        Map<String, String> params = new HashMap<>();
        params.put("apiMethodName", TbkApiConstants.API_METHOD_NAME_GET_ITEM_INFO);
        params.put("numIids", numIids);
        params.put("platform", "2"); // 1:PC, 2:无线
        
        // 使用代理的TaobaoClient执行请求
        TaobaoResponse taobaoResponse = tbkModule.execute(params, agentClient);
        return parseItemInfoResponse(taobaoResponse);
    }

    /**
     * 解析商品详情API响应
     * @param taobaoResponse 淘宝客API响应
     * @return 解析后的JSON对象
     */
    private JSONObject parseItemInfoResponse(TaobaoResponse taobaoResponse) {
        JSONObject jsonObject = JSONObject.from(taobaoResponse);
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        
        if (jsonArray != null && jsonArray.size() > 0) {
            // 返回第一个商品的详情
            JSONObject itemInfo = jsonArray.getJSONObject(0);
            JSONObject result = new JSONObject();
            
            // 提取并重组商品信息
            result.put("numIid", itemInfo.getString("numIid"));
            result.put("title", itemInfo.getString("title"));
            result.put("pictUrl", itemInfo.getString("pictUrl"));
            result.put("smallImages", itemInfo.getJSONArray("smallImages"));
            result.put("reservePrice", itemInfo.getString("reservePrice")); // 商品一口价
            result.put("zkFinalPrice", itemInfo.getString("zkFinalPrice")); // 商品折扣价格
            result.put("volume", itemInfo.getString("volume")); // 30天销量
            result.put("nick", itemInfo.getString("nick")); // 卖家昵称
            result.put("shopTitle", itemInfo.getString("shopTitle")); // 店铺名称
            result.put("provcity", itemInfo.getString("provcity")); // 宝贝所在地
            result.put("itemUrl", itemInfo.getString("itemUrl")); // 商品链接
            result.put("categoryId", itemInfo.getString("categoryId")); // 类目ID
            result.put("categoryName", itemInfo.getString("categoryName")); // 类目名称
            result.put("levelOneCategoryId", itemInfo.getString("levelOneCategoryId")); // 一级类目ID
            result.put("levelOneCategoryName", itemInfo.getString("levelOneCategoryName")); // 一级类目名称
            result.put("shortTitle", itemInfo.getString("shortTitle")); // 商品短标题
            result.put("whiteImage", itemInfo.getString("whiteImage")); // 商品白底图
            result.put("subcategoryId", itemInfo.getString("subcategoryId")); // 子类目ID
            result.put("subcategoryName", itemInfo.getString("subcategoryName")); // 子类目名称
            result.put("tmallPlayActivityInfo", itemInfo.getString("tmallPlayActivityInfo")); // 天猫营销活动
            result.put("sourceData", itemInfo); // 保存原始数据
            
            return result;
        }
        
        return new JSONObject();
    }

    /**
     * 解析淘宝客API响应
     * @param taobaoResponse 淘宝客API响应
     * @return 解析后的JSON对象
     */
    private JSONObject parseResponse(TaobaoResponse taobaoResponse) {
        JSONObject jsonObject = JSONObject.from(taobaoResponse);
        JSONArray jsonArray = jsonObject.getJSONArray("resultList");
        JSONArray resultArray = new JSONArray();
        
        if (null != jsonArray && jsonArray.size() > 0) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                JSONObject resultObject = new JSONObject();
                
                resultObject.put("itemId", jsonObject1.getString("itemId"));
                resultObject.put("brandName", jsonObject1.getJSONObject("itemBasicInfo").getString("brandName"));
                resultObject.put("title", jsonObject1.getJSONObject("itemBasicInfo").getString("title"));
                resultObject.put("smallImages", jsonObject1.getJSONObject("itemBasicInfo").getString("smallImages"));
                resultObject.put("pictUrl", jsonObject1.getJSONObject("itemBasicInfo").getString("pictUrl"));
                resultObject.put("shopTitle", jsonObject1.getJSONObject("itemBasicInfo").getString("shopTitle"));
                resultObject.put("categoryName", jsonObject1.getJSONObject("itemBasicInfo").getString("categoryName"));
                resultObject.put("annualVol", jsonObject1.getJSONObject("itemBasicInfo").getString("annualVol"));
                resultObject.put("finalPromotionPrice", jsonObject1.getJSONObject("pricePromotionInfo").getString("finalPromotionPrice"));
                resultObject.put("zkFinalPrice", jsonObject1.getJSONObject("pricePromotionInfo").getString("zkFinalPrice"));
                resultObject.put("finalPromotionPathList", jsonObject1.getJSONObject("pricePromotionInfo").getJSONArray("finalPromotionPathList"));
                resultObject.put("promotionTagList", jsonObject1.getJSONObject("pricePromotionInfo").getJSONArray("promotionTagList"));
                resultObject.put("clickUrl", jsonObject1.getJSONObject("publishInfo").getString("clickUrl"));
                resultObject.put("couponShareUrl", jsonObject1.getJSONObject("publishInfo").getString("couponShareUrl"));
                resultObject.put("commissionAmount", jsonObject1.getJSONObject("publishInfo").getJSONObject("incomeInfo").getString("commissionAmount"));
                resultObject.put("sourceData", jsonObject1);
                
                resultArray.add(resultObject);
            }
        }
        
        JSONObject resultOb = new JSONObject();
        resultOb.put("resultList", resultArray);
        resultOb.put("totalResults", jsonObject.get("totalResults"));
        return resultOb;
    }
}
