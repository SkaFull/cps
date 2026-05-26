package com.ruoyi.system.service;

import com.alibaba.fastjson2.JSONObject;

/**
 * 大淘客API服务接口
 * 
 * @author ruoyi
 */
public interface IDtkApiService {

    /**
     * 获取轮播图列表
     * 
     * @return 轮播图列表
     */
    JSONObject getBanners();

    /**
     * 获取分类列表
     * 
     * @return 分类列表
     */
    JSONObject getCategories();

    /**
     * 搜索商品
     * 
     * @param keyword 关键词
     * @param categoryId 分类ID
     * @param page 页码
     * @param pageSize 每页数量
     * @param sort 排序方式
     * @param appKey 应用密钥
     * @param appSecret 应用密钥
     * @return 商品列表
     */
    JSONObject searchGoods(String keyword, Long categoryId, Integer page, Integer pageSize, 
                          String sort, String appKey, String appSecret);

    /**
     * 获取商品详情
     * 
     * @param goodsId 商品ID
     * @param appKey 应用密钥
     * @param appSecret 应用密钥
     * @return 商品详情
     */
    JSONObject getGoodsDetail(String goodsId, String appKey, String appSecret);

    /**
     * 获取热门商品列表
     * 
     * @param page 页码
     * @param pageSize 每页数量
     * @param keyword 关键词（可选）
     * @param appKey 应用密钥
     * @param appSecret 应用密钥
     * @return 热门商品列表
     */
    JSONObject getHotGoods(Integer page, Integer pageSize, String keyword, 
                          String appKey, String appSecret);

    /**
     * 高效转链（获取淘口令）
     * 
     * @param goodsId 商品ID
     * @param couponId 优惠券ID（可选）
     * @param pid 推广位ID（可选）
     * @param goodsName 商品名称（可选）
     * @param appKey 应用密钥
     * @param appSecret 应用密钥
     * @return 包含淘口令、高佣链接等信息
     */
    JSONObject getPrivilegeLink(String goodsId, String couponId, String pid,
                               String goodsName, String appKey, String appSecret);
}
