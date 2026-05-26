package com.ruoyi.system.service;

import com.alibaba.fastjson2.JSONObject;

import java.util.Map;

/**
 * 京东联盟SDK封装Service接口
 * @author ruoyi
 * @date 2026-03-31
 */
public interface IJdSdkService {

    /**
     * 查询热销商品排行榜
     * @param params 请求参数
     * @return 商品列表结果
     */
    JSONObject queryGoodsRank(Map<String, String> params);

    /**
     * 查询商品详情
     * @param skuIds 商品SKU ID列表，多个用逗号分隔
     * @return 商品详情结果
     */
    JSONObject queryGoodsBigField(String skuIds);

    /**
     * 获取推广链接
     * @param params 请求参数（包含materialId等）
     * @return 推广链接结果
     */
    JSONObject getPromotionLink(Map<String, String> params);

    /**
     * 查询优惠券信息
     * @param couponUrls 优惠券链接列表
     * @return 优惠券信息结果
     */
    JSONObject queryCoupon(String couponUrls);

}
