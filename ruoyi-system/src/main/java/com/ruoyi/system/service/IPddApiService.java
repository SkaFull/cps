package com.ruoyi.system.service;

import com.alibaba.fastjson2.JSONObject;

/**
 * 拼多多API服务接口
 * 
 * @author ruoyi
 */
public interface IPddApiService {

    /**
     * 搜索商品
     * 
     * @param keyword 搜索关键词
     * @param page 页码
     * @param pageSize 每页数量
     * @param userId 用户ID（用于构建custom_parameters）
     * @param appKey 应用Key（可选，为空时使用默认配置）
     * @param appSecret 应用密钥（可选，为空时使用默认配置）
     * @return 搜索结果
     */
    JSONObject searchGoods(String keyword, Integer page, Integer pageSize, Long userId, String appKey, String appSecret);

    /**
     * 获取商品详情
     * 
     * @param goodsIdList 商品ID列表（逗号分隔）
     * @param appKey 应用Key（可选，为空时使用默认配置）
     * @param appSecret 应用密钥（可选，为空时使用默认配置）
     * @return 商品详情
     */
    JSONObject getGoodsDetail(String goodsIdList, String appKey, String appSecret);

    /**
     * 获取推荐商品
     * 
     * @param page 页码
     * @param pageSize 每页数量
     * @param userId 用户ID（用于构建custom_parameters）
     * @param appKey 应用Key（可选，为空时使用默认配置）
     * @param appSecret 应用密钥（可选，为空时使用默认配置）
     * @return 推荐商品列表
     */
    JSONObject getRecommendGoods(Integer page, Integer pageSize, Long userId, String appKey, String appSecret);

    /**
     * 生成推广链接
     * @param goodsSign 商品ID
     * @param userId 用户ID（用于构建custom_parameters）
     * @param appKey 应用Key（可选，为空时使用默认配置）
     * @param appSecret 应用密钥（可选，为空时使用默认配置）
     * @return 推广链接信息
     */
    JSONObject generatePromotionUrl(String goodsSign, Long userId, String appKey, String appSecret);

    /**
     * 生成营销工具推广链接
     * 
     * @param resourceType 资源类型
     * @param url 目标URL
     * @param appKey 应用Key（可选，为空时使用默认配置）
     * @param appSecret 应用密钥（可选，为空时使用默认配置）
     * @return 推广链接信息
     */
    JSONObject generateRpPromUrl(Integer resourceType, String url, String appKey, String appSecret);

    /**
     * 生成商城推广链接
     * 
     * @param channelType 频道类型
     * @param appKey 应用Key（可选，为空时使用默认配置）
     * @param appSecret 应用密钥（可选，为空时使用默认配置）
     * @return 推广链接信息
     */
    JSONObject generateCmsPromUrl(Integer channelType, String appKey, String appSecret);
}
