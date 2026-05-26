package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.jd.open.api.sdk.response.kplunion.*;
import com.ruoyi.system.service.IJdSdkService;
import com.ruoyi.union.jd.config.JdConfig;
import com.ruoyi.union.jd.constants.JdApiConstants;
import com.ruoyi.union.jd.module.JdModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 京东联盟SDK封装Service实现
 * @author ruoyi
 * @date 2026-03-31
 */
@Service
@Slf4j
public class JdSdkService implements IJdSdkService {

    @Autowired
    private JdModule jdModule;

    @Autowired
    private JdConfig jdConfig;

    /**
     * 查询热销商品排行榜
     * @param params 请求参数
     * @return 商品列表结果
     */
    @Override
    public JSONObject queryGoodsRank(Map<String, String> params) {
        params.put("apiMethodName", JdApiConstants.API_METHOD_NAME_GOODS_RANK_QUERY);
        
        // 调用京东联盟API
        UnionOpenGoodsRankQueryResponse response = jdModule.execute(params);
        
        // 解析响应
        return parseGoodsRankResponse(response);
    }

    /**
     * 查询商品详情
     * @param itemIds 商品SKU ID列表，多个用逗号分隔
     * @return 商品详情结果
     */
    @Override
    public JSONObject queryGoodsBigField(String itemIds) {
        Map<String, String> params = new HashMap<>();
        params.put("apiMethodName", JdApiConstants.API_METHOD_NAME_GOODS_BIGFIELD_QUERY);
        // 构建业务参数goodsReq
        // 注意：使用"goodsReq."前缀表示这是业务参数对象的属性
        params.put("goodsReq.itemIds", itemIds);
        params.put("goodsReq.sceneId", "1");

        // 调用京东联盟API
        UnionOpenGoodsBigfieldQueryResponse response = jdModule.execute(params);
        
        // 解析响应
        return parseGoodsBigFieldResponse(response);
    }

    /**
     * 获取推广链接（淘口令功能）
     * @param params 请求参数，支持以下参数：
     *               - materialId: 推广物料url（必填），如商品链接、联盟商品ID等
     *               - siteId: 网站ID（可选），默认从配置获取
     *               - sceneId: 场景ID（可选），默认为1
     *               - positionId: 推广位ID（可选），默认从配置获取
     * @return 推广链接结果
     */
    @Override
    public JSONObject getPromotionLink(Map<String, String> params) {
        params.put("apiMethodName", JdApiConstants.API_METHOD_NAME_PROMOTION_COMMON_GET);
        
        // 获取materialId（前端传入的itemId）
        String materialId = params.get("materialId");
        if (materialId == null || materialId.isEmpty()) {
            throw new RuntimeException("参数materialId不能为空");
        }
        
        // 构建业务参数promotionCodeReq
        // 注意：使用"promotionCodeReq."前缀表示这是业务参数对象的属性
        params.put("promotionCodeReq.materialId", materialId);
        
        // 设置siteId（网站ID/流量媒体ID），从配置获取
        if (!params.containsKey("promotionCodeReq.siteId") && jdConfig.getSiteId() != null) {
            params.put("promotionCodeReq.siteId", jdConfig.getSiteId().toString());
        }
        
        // 设置sceneId（场景ID），默认为1
        if (!params.containsKey("promotionCodeReq.sceneId")) {
            params.put("promotionCodeReq.sceneId", "1");
        }
        
        // 设置positionId（推广位ID），从配置获取
        if (!params.containsKey("positionId") && jdConfig.getPositionId() != null) {
            params.put("positionId", jdConfig.getPositionId().toString());
        }
        
        // 移除materialId，避免与业务参数冲突
        params.remove("materialId");
        
        // 调用京东联盟API
        UnionOpenPromotionCommonGetResponse response = jdModule.execute(params);
        
        // 解析响应
        return parsePromotionLinkResponse(response);
    }

    /**
     * 查询优惠券信息
     * @param couponUrls 优惠券链接列表
     * @return 优惠券信息结果
     */
    @Override
    public JSONObject queryCoupon(String couponUrls) {
        Map<String, String> params = new HashMap<>();
        params.put("apiMethodName", JdApiConstants.API_METHOD_NAME_COUPON_QUERY);
        params.put("couponUrls", couponUrls);
        
        // 调用京东联盟API
        UnionOpenCouponQueryResponse response = jdModule.execute(params);
        
        // 解析响应
        return parseCouponResponse(response);
    }

    /**
     * 解析商品排行榜响应
     */
    private JSONObject parseGoodsRankResponse(UnionOpenGoodsRankQueryResponse response) {
        JSONObject result = new JSONObject();
        
        // 京东SDK响应对象没有isSuccess()方法，需要通过检查响应内容判断是否成功
        if (response == null) {
            result.put("resultList", new JSONArray());
            result.put("totalResults", 0);
            return result;
        }

        // 将response对象转换为JSON字符串，再解析为JSONObject
        String responseStr = JSONObject.toJSONString(response);
        System.out.println("=== 京东API原始响应 ===");
        System.out.println(responseStr);
        
        JSONObject responseJson = JSONObject.parseObject(responseStr);
        
        // 检查是否有错误码（京东API错误时会有code字段，且code不为0）
        if (responseJson.containsKey("code")) {
            Integer code = responseJson.getInteger("code");
            // code存在且不为0表示有错误
            if (code != null && code != 0) {
                result.put("resultList", new JSONArray());
                result.put("totalResults", 0);
                result.put("errorCode", responseJson.getString("code"));
                result.put("errorMsg", responseJson.getString("msg"));
                return result;
            }
        }
        
        // queryResult是JSON字符串，需要先解析为JSON对象
        String queryResultStr = responseJson.getString("queryResult");
        
        System.out.println("=== queryResult字符串 ===");
        System.out.println(queryResultStr);
        
        if (queryResultStr == null || queryResultStr.isEmpty()) {
            result.put("resultList", new JSONArray());
            result.put("totalResults", 0);
            return result;
        }
        
        JSONObject queryResult = JSONObject.parseObject(queryResultStr);
        
        if (queryResult == null) {
            result.put("resultList", new JSONArray());
            result.put("totalResults", 0);
            return result;
        }

        JSONArray dataArray = queryResult.getJSONArray("data");
        JSONArray resultArray = new JSONArray();
        
        if (dataArray != null && !dataArray.isEmpty()) {
            for (int i = 0; i < dataArray.size(); i++) {
                JSONObject item = dataArray.getJSONObject(i);
                JSONObject resultItem = new JSONObject();
                
                // 提取关键字段 - 根据真实API返回报文映射，确保字段名与适配器期望一致
                // 商品ID - itemId是字符串类型（加密ID）
                String itemId = item.getString("itemId");
                if (itemId == null || itemId.isEmpty()) {
                    itemId = item.getString("skuId"); // 兼容可能的字段名变化
                }
                resultItem.put("skuId", itemId);
                resultItem.put("id", itemId); // 兼容字段
                resultItem.put("itemId", itemId); // 保留原始字段名
                
                // 商品名称
                String skuName = item.getString("skuName");
                resultItem.put("skuName", skuName);
                resultItem.put("name", skuName); // 兼容字段
                resultItem.put("title", skuName); // 兼容字段
                
                // 商品主图
                String imageUrl = item.getString("imageUrl");
                resultItem.put("imageUrl", imageUrl);
                resultItem.put("mainPic", imageUrl); // 兼容字段
                resultItem.put("pictUrl", imageUrl); // 兼容字段
                
                // 价格信息 - 优先从purchasePriceInfo.purchasePrice获取
                JSONObject priceInfo = item.getJSONObject("purchasePriceInfo");
                Object priceValue = null;
                if (priceInfo != null && priceInfo.getBigDecimal("purchasePrice") != null) {
                    priceValue = priceInfo.getBigDecimal("purchasePrice");
                } else if (item.getBigDecimal("wlprice") != null) {
                    priceValue = item.getBigDecimal("wlprice");
                } else {
                    priceValue = 0; // 默认值
                }
                resultItem.put("price", priceValue);
                resultItem.put("actualPrice", priceValue); // 兼容字段
                resultItem.put("zkFinalPrice", priceValue); // 兼容淘宝客字段
                resultItem.put("reservePrice", priceValue); // 兼容淘宝客字段
                
                // 原价 - 尝试从wlprice获取作为原价
                Object originalPrice = item.getBigDecimal("wlprice");
                if (originalPrice == null) {
                    originalPrice = priceValue; // 如果没有原价，使用现价
                }
                resultItem.put("originalPrice", originalPrice);
                
                // 佣金信息
                Object commissionValue = item.getBigDecimal("commission");
                Object commissionShareValue = item.getBigDecimal("commissionShare");
                resultItem.put("commission", commissionValue != null ? commissionValue : 0);
                resultItem.put("commissionShare", commissionShareValue != null ? commissionShareValue : 0);
                resultItem.put("commissionRate", commissionShareValue != null ? commissionShareValue : 0); // 兼容字段
                resultItem.put("zkFinalPriceWap", commissionValue != null ? commissionValue : 0); // 兼容淘宝客字段（佣金）
                
                // 30天引单数量 - 映射为monthSales
                Integer inOrderCount = item.getInteger("inOrderCount30Days");
                resultItem.put("inOrderCount30Days", inOrderCount != null ? inOrderCount : 0);
                resultItem.put("monthSales", inOrderCount != null ? inOrderCount : 0); // 兼容字段
                resultItem.put("volume", inOrderCount != null ? inOrderCount : 0); // 兼容淘宝客字段
                
                // 店铺名称
                String shopName = item.getString("shopName");
                resultItem.put("shopName", shopName);
                resultItem.put("shopTitle", shopName); // 兼容字段
                resultItem.put("nick", shopName); // 兼容淘宝客字段
                
                // 品牌名称
                resultItem.put("brandName", item.getString("brandName"));
                
                // 评论相关信息
                Long comments = item.getLong("comments");
                resultItem.put("comments", comments != null ? comments : 0);
                resultItem.put("goodComments", item.getLong("goodComments"));
                resultItem.put("goodCommentsShare", item.getBigDecimal("goodCommentsShare"));
                
                // 图片列表
                JSONArray imgList = item.getJSONArray("imgList");
                if (imgList != null && imgList.size() > 0) {
                    resultItem.put("imgList", imgList);
                    resultItem.put("smallImages", imgList); // 兼容字段
                    resultItem.put("small_images", imgList); // 兼容淘宝客字段
                } else {
                    // 如果没有图片列表，创建一个包含主图的数组
                    JSONArray singleImageArray = new JSONArray();
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        singleImageArray.add(imageUrl);
                    }
                    resultItem.put("imgList", singleImageArray);
                    resultItem.put("smallImages", singleImageArray);
                    resultItem.put("small_images", singleImageArray);
                }
                
                // 标签信息
                resultItem.put("skuTagList", item.getJSONArray("skuTagList"));
                
                // 优惠券信息
                JSONObject couponInfo = item.getJSONObject("couponInfo");
                resultItem.put("couponInfo", couponInfo);
                
                // 分类信息
                resultItem.put("categoryName", item.getString("categoryName"));
                
                // 自营标识
                resultItem.put("owner", item.getString("owner"));
                
                // 推广链接相关
                String materialUrl = item.getString("materialUrl");
                resultItem.put("materialUrl", materialUrl);
                resultItem.put("clickUrl", materialUrl); // 兼容字段
                resultItem.put("couponClickUrl", materialUrl); // 兼容淘宝客字段
                resultItem.put("url", materialUrl); // 兼容字段
                
                resultArray.add(resultItem);
            }
        }
        
        result.put("resultList", resultArray);
        result.put("totalResults", queryResult.getInteger("totalCount"));
        
        return result;
    }

    /**
     * 解析商品详情响应
     */
    private JSONObject parseGoodsBigFieldResponse(UnionOpenGoodsBigfieldQueryResponse response) {
        JSONObject result = new JSONObject();
        
        // 京东SDK响应对象没有isSuccess()方法，需要通过检查响应内容判断是否成功
        if (response == null) {
            return result;
        }

        String responseStr = JSONObject.toJSONString(response);
        log.info("=== 京东商品详情API原始响应 ===");
        log.info(responseStr);
        
        JSONObject responseJson = JSONObject.parseObject(responseStr);
        
        // 检查是否有错误码
        if (responseJson.containsKey("code") && responseJson.getInteger("code") != null) {
            Integer code = responseJson.getInteger("code");
            if (code != 0) {
                result.put("errorCode", responseJson.getString("code"));
                result.put("errorMsg", responseJson.getString("msg"));
                return result;
            }
        }
        
        // queryResult是JSON字符串，需要先解析
        String queryResultStr = responseJson.getString("queryResult");
        if (queryResultStr == null || queryResultStr.isEmpty()) {
            return result;
        }
        
        JSONObject queryResult = JSONObject.parseObject(queryResultStr);
        if (queryResult == null) {
            return result;
        }

        JSONArray dataArray = queryResult.getJSONArray("data");
        
        if (dataArray != null && !dataArray.isEmpty()) {
            // 返回第一个商品的详情
            JSONObject item = dataArray.getJSONObject(0);
            
            // 提取各部分信息
            JSONObject skuInfo = item.getJSONObject("skuInfo");
            JSONObject priceInfo = item.getJSONObject("priceInfo");
            JSONObject commissionInfo = item.getJSONObject("commissionInfo");
            JSONObject couponInfo = item.getJSONObject("couponInfo");
            JSONObject categoryInfo = item.getJSONObject("categoryInfo");
            
            // 构建前端期望的数据结构
            if (skuInfo != null) {
                result.put("skuId", skuInfo.getString("skuId"));
                result.put("skuName", skuInfo.getString("skuName"));
                result.put("imageUrl", skuInfo.getString("imageUrl"));
                
                // 图片列表
                JSONArray imageList = skuInfo.getJSONArray("imageList");
                if (imageList != null && !imageList.isEmpty()) {
                    result.put("images", imageList);
                } else {
                    // 如果没有图片列表，使用主图
                    JSONArray singleImage = new JSONArray();
                    String imageUrl = skuInfo.getString("imageUrl");
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        singleImage.add(imageUrl);
                    }
                    result.put("images", singleImage);
                }
                
                // 店铺信息
                JSONObject shopInfo = skuInfo.getJSONObject("shopInfo");
                if (shopInfo != null) {
                    result.put("shopName", shopInfo.getString("shopName"));
                }
                
                result.put("brandName", skuInfo.getString("brandName"));
                
                // 30天销量
                result.put("inOrderCount30Days", skuInfo.getString("inOrderCount30Days"));
            }
            
            // 价格信息
            if (priceInfo != null) {
                JSONObject priceInfoData = new JSONObject();
                priceInfoData.put("price", priceInfo.getString("price"));
                priceInfoData.put("lowestPrice", priceInfo.getString("lowestPrice"));
                priceInfoData.put("lowestCouponPrice", priceInfo.getString("lowestCouponPrice"));
                result.put("priceInfo", priceInfoData);
            }
            
            // 佣金信息
            if (commissionInfo != null) {
                JSONObject commissionInfoData = new JSONObject();
                commissionInfoData.put("commission", commissionInfo.getString("commission"));
                commissionInfoData.put("commissionShare", commissionInfo.getString("commissionShare"));
                result.put("commissionInfo", commissionInfoData);
            }
            
            // 优惠券信息
            if (couponInfo != null) {
                JSONObject couponInfoData = new JSONObject();
                couponInfoData.put("discount", couponInfo.getString("discount"));
                couponInfoData.put("link", couponInfo.getString("link"));
                result.put("couponInfo", couponInfoData);
            }
            
            // 分类信息
            if (categoryInfo != null) {
                JSONObject categoryInfoData = new JSONObject();
                categoryInfoData.put("cid1", categoryInfo.getString("cid1"));
                categoryInfoData.put("cid1Name", categoryInfo.getString("cid1Name"));
                categoryInfoData.put("cid2", categoryInfo.getString("cid2"));
                categoryInfoData.put("cid2Name", categoryInfo.getString("cid2Name"));
                categoryInfoData.put("cid3", categoryInfo.getString("cid3"));
                categoryInfoData.put("cid3Name", categoryInfo.getString("cid3Name"));
                result.put("categoryInfo", categoryInfoData);
            }
            
            // 提取商品详情相关字段（优先从baseBigFieldInfo获取）
            JSONObject baseBigFieldInfo = item.getJSONObject("baseBigFieldInfo");
            
            // 处理商品详情图片列表
            if (baseBigFieldInfo != null) {
                String detailImages = baseBigFieldInfo.getString("detailImages");
                if (detailImages != null && !detailImages.isEmpty()) {
                    // 将逗号分隔的字符串转换为数组
                    String[] imageUrls = detailImages.split(",");
                    JSONArray detailImageArray = new JSONArray();
                    for (String url : imageUrls) {
                        if (url != null && !url.trim().isEmpty()) {
                            detailImageArray.add(url.trim());
                        }
                    }
                    result.put("detailImages", detailImageArray);
                    log.info("解析detailImages成功，数量: {}", detailImageArray.size());
                }
                
                // 商品详情HTML内容 - wdis字段（包含视频等多媒体信息）
                String wdis = baseBigFieldInfo.getString("wdis");
                if (wdis != null && !wdis.isEmpty()) {
                    result.put("detailHtml", wdis);
                    log.info("获取detailHtml成功，长度: {}", wdis.length());
                }
                
                // 商品属性信息 - propGroups（JSON字符串格式）
                String propGroups = baseBigFieldInfo.getString("propGroups");
                if (propGroups != null && !propGroups.isEmpty()) {
                    try {
                        JSONArray propGroupsArray = JSONArray.parseArray(propGroups);
                        result.put("propGroups", propGroupsArray);
                        log.info("解析propGroups成功，分组数量: {}", propGroupsArray.size());
                    } catch (Exception e) {
                        log.warn("解析propGroups失败: {}", e.getMessage());
                    }
                }
                
                // 处理imageInfo - 包含轮播图和视频信息
                JSONObject imageInfo = baseBigFieldInfo.getJSONObject("imageInfo");
                if (imageInfo != null) {
                    JSONArray imageList = imageInfo.getJSONArray("imageList");
                    
                    if (imageList != null && !imageList.isEmpty()) {
                        // 提取视频信息
                        JSONArray videoList = new JSONArray();
                        for (int i = 0; i < imageList.size(); i++) {
                            JSONObject imgItem = imageList.getJSONObject(i);
                            if (imgItem != null && imgItem.containsKey("videoUrl")) {
                                String videoUrl = imgItem.getString("videoUrl");
                                if (videoUrl != null && !videoUrl.isEmpty()) {
                                    JSONObject video = new JSONObject();
                                    video.put("url", videoUrl);
                                    video.put("videoUrl", videoUrl);
                                    video.put("cover", imgItem.getString("url")); // 视频封面
                                    videoList.add(video);
                                }
                            }
                        }
                        
                        if (!videoList.isEmpty()) {
                            result.put("videos", videoList);
                            log.info("提取视频成功，数量: {}", videoList.size());
                        }
                    }
                }
            }
            
            // 保存原始数据供调试
            result.put("sourceData", item);
        }
        
        return result;
    }

    /**
     * 解析推广链接响应
     */
    private JSONObject parsePromotionLinkResponse(UnionOpenPromotionCommonGetResponse response) {
        JSONObject result = new JSONObject();
        
        // 京东SDK响应对象没有isSuccess()方法，需要通过检查响应内容判断是否成功
        if (response == null) {
            return result;
        }

        JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(response));
        
        // 打印原始响应以便调试
        log.info("=== 京东推广链接API原始响应 ===");
        log.info(responseJson.toJSONString());
        
        // 检查是否有错误码
        if (responseJson.containsKey("code") && responseJson.getInteger("code") != null) {
            Integer code = responseJson.getInteger("code");
            // code为200表示成功，其他值表示失败
            if (code != 0) {
                result.put("errorCode", responseJson.getString("code"));
                result.put("errorMsg", responseJson.getString("msg"));
                return result;
            }
        }
        
        JSONObject getResult = responseJson.getJSONObject("getResult");
        
        if (getResult != null) {
            // 检查getResult中的code
            Integer getResultCode = getResult.getInteger("code");
            if (getResultCode != null && getResultCode != 200) {
                result.put("errorCode", getResult.getString("code"));
                result.put("errorMsg", getResult.getString("message"));
                return result;
            }
            
            // clickURL和shortURL可能直接在getResult中，也可能在data中
            String clickURL = getResult.getString("clickURL");
            String jCommand = getResult.getString("jCommand");
            
            // 如果getResult中没有，尝试从data中获取
            if (clickURL == null || clickURL.isEmpty()) {
                JSONObject data = getResult.getJSONObject("data");
                if (data != null) {
                    clickURL = data.getString("clickURL");
                    jCommand = data.getString("jCommand");
                }
            }
            
            // 设置返回结果
            if (clickURL != null && !clickURL.isEmpty()) {
                result.put("clickURL", clickURL);
                result.put("shortURL", jCommand != null ? jCommand : "");
                result.put("sourceData", getResult);
            }
        }
        
        return result;
    }

    /**
     * 解析优惠券响应
     */
    private JSONObject parseCouponResponse(UnionOpenCouponQueryResponse response) {
        JSONObject result = new JSONObject();
        
        // 京东SDK响应对象没有isSuccess()方法，需要通过检查响应内容判断是否成功
        if (response == null) {
            return result;
        }

        JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(response));
        
        // 检查是否有错误码
        if (responseJson.containsKey("code") && responseJson.getInteger("code") != null) {
            result.put("errorCode", responseJson.getString("code"));
            result.put("errorMsg", responseJson.getString("msg"));
            return result;
        }
        
        JSONObject queryResult = responseJson.getJSONObject("queryResult");
        
        if (queryResult != null) {
            JSONArray dataArray = queryResult.getJSONArray("data");
            result.put("coupons", dataArray);
        }
        
        return result;
    }
}
