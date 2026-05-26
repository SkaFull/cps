package com.ruoyi.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.pdd.pop.sdk.http.PopBaseHttpRequest;
import com.pdd.pop.sdk.http.PopBaseHttpResponse;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.sign.Md5Utils;
import com.ruoyi.system.service.IPddApiService;
import com.ruoyi.union.pdd.config.PddConfig;
import com.ruoyi.union.pdd.constants.PddApiConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.*;

/**
 * 拼多多API服务实现
 * 参考 PddModule 的通用execute方式，通过Map参数调用PDD SDK
 * 
 * @author ruoyi
 */
@Slf4j
@Service
public class PddApiServiceImpl implements IPddApiService {

    @Autowired
    private PddConfig pddConfig;

    /**
     * 通用执行方法，支持指定clientId/clientSecret（代理模式）
     */
    @SuppressWarnings("unchecked")
    private <T extends PopBaseHttpResponse> T execute(Map<String, Object> params, String clientId, String clientSecret) {
        // 获取API方法名
        String apiMethodName = (String) params.get("apiMethodName");
        if (StringUtils.isEmpty(apiMethodName)) {
            throw new ServiceException("参数【apiMethodName】方法名称为空！");
        }

        log.info("execute params >>>> " + params.toString());

        // 获取请求类
        Class<? extends PopBaseHttpRequest<T>> requestClass =
            (Class<? extends PopBaseHttpRequest<T>>) PddApiConstants.REQUEST_MAP.get(apiMethodName);
        if (requestClass == null) {
            throw new ServiceException(apiMethodName + "方法未集成！");
        }

        // 构建请求对象，通过BeanUtil填充参数
        PopBaseHttpRequest<T> request = ReflectUtil.newInstance(requestClass);
        BeanUtil.fillBeanWithMap(params, request, true);

        // 确定使用哪个Client
        String useClientId = StringUtils.isNotEmpty(clientId) ? clientId : pddConfig.getClientId();
        String useClientSecret = StringUtils.isNotEmpty(clientSecret) ? clientSecret : pddConfig.getClientSecret();

        try {
            // 使用PopHttpClient（PopClient的具体实现）
            PopHttpClient popClient = new PopHttpClient(useClientId, useClientSecret);
            T rsp = popClient.syncInvoke(request);
            if (rsp == null) {
                throw new ServiceException("调用拼多多接口 [" + apiMethodName + "] 失败：响应为空");
            }
            return rsp;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("调用拼多多接口 [{}] 失败", apiMethodName, e);
            throw new ServiceException("调用拼多多接口失败：" + e.getMessage());
        }
    }

    @Override
    public JSONObject searchGoods(String keyword, Integer page, Integer pageSize, Long userId, String appKey, String appSecret) {
        try {
            // 构建请求参数（参考PDD官方文档：pdd.ddk.goods.search）
            Map<String, Object> params = new HashMap<>();
            params.put("apiMethodName", "pdd.ddk.goods.search");
            params.put("keyword", keyword);
            params.put("pid", pddConfig.getStringPositionId());
            params.put("page", page);
            params.put("pageSize", pageSize);
            // 按佣金比率降序排序
            params.put("sortType", 2);
            // 只返回有佣金的商品
            params.put("withCoupon", false);
            
            // 构建custom_parameters参数（必填，用于授权备案）
            if (userId != null) {
                String customParameters = buildCustomParameters(userId);
                params.put("customParameters", customParameters);
            }
            
            Object response = execute(params, appKey, appSecret);
            // 解析响应并转换字段格式
            return parseGoodsSearchResponse(response);
        } catch (Exception e) {
            log.error("搜索商品失败", e);
            throw new ServiceException("搜索商品失败：" + e.getMessage());
        }
    }

    @Override
    public JSONObject getGoodsDetail(String goodsIdList, String appKey, String appSecret) {
        try {
            // 构建请求参数（参考PDD官方文档：pdd.ddk.goods.detail）
            Map<String, Object> params = new HashMap<>();
            params.put("apiMethodName", "pdd.ddk.goods.detail");
            params.put("goodsIdList", goodsIdList);

            Object response = execute(params, appKey, appSecret);

            // 包装为JSONObject返回
            JSONObject result = new JSONObject();
            result.put("response", response);
            return result;
        } catch (Exception e) {
            log.error("获取商品详情失败", e);
            throw new ServiceException("获取商品详情失败：" + e.getMessage());
        }
    }

    @Override
    public JSONObject getRecommendGoods(Integer page, Integer pageSize, Long userId, String appKey, String appSecret) {
        try {
            // 构建请求参数（参考PDD官方文档：pdd.ddk.goods.recommend.get）
            Map<String, Object> params = new HashMap<>();
            params.put("apiMethodName", "pdd.ddk.goods.recommend.get");
            params.put("pageNum", page);
            params.put("pageSize", pageSize);
            // 构建custom_parameters参数（必填，用于授权备案）
            if (userId != null) {
                String customParameters = buildCustomParameters(userId);
                params.put("customParameters", customParameters);
            }
            Object response = execute(params, appKey, appSecret);
            // 解析响应并转换字段格式
            return parseGoodsSearchResponse(response);
        } catch (Exception e) {
            log.error("获取推荐商品失败", e);
            throw new ServiceException("获取推荐商品失败：" + e.getMessage());
        }
    }

    @Override
    public JSONObject generatePromotionUrl(String goodsSign, Long userId, String appKey, String appSecret) {
        try {
            // 构建请求参数（参考PDD官方文档：pdd.ddk.goods.promotion.url.generate）
            Map<String, Object> params = new HashMap<>();
            params.put("apiMethodName", "pdd.ddk.goods.promotion.url.generate");
            List<String> goodsSignList = new ArrayList<>();
            goodsSignList.add(goodsSign);
            params.put("goods_sign_list", goodsSignList);
            params.put("p_id", pddConfig.getStringPositionId());
            // 构建custom_parameters参数（必填，用于授权备案）
            if (userId != null) {
                String customParameters = buildCustomParameters(userId);
                params.put("customParameters", customParameters);
            }
            Object response = execute(params, appKey, appSecret);
            
            // 解析响应并提取短链
            return parsePromotionUrlResponse(response);
        } catch (Exception e) {
            log.error("生成推广链接失败", e);
            throw new ServiceException("生成推广链接失败：" + e.getMessage());
        }
    }

    @Override
    public JSONObject generateRpPromUrl(Integer resourceType, String url, String appKey, String appSecret) {
        try {
            // 构建请求参数（参考PDD官方文档：pdd.ddk.rp.prom.url.generate）
            Map<String, Object> params = new HashMap<>();
            params.put("apiMethodName", "pdd.ddk.rp.prom.url.generate");
            if (resourceType != null) {
                params.put("resourceType", resourceType);
            }
            if (StringUtils.isNotEmpty(url)) {
                params.put("url", url);
            }

            Object response = execute(params, appKey, appSecret);

            // 包装为JSONObject返回
            JSONObject result = new JSONObject();
            result.put("response", response);
            return result;
        } catch (Exception e) {
            log.error("生成营销工具推广链接失败", e);
            throw new ServiceException("生成营销工具推广链接失败：" + e.getMessage());
        }
    }

    @Override
    public JSONObject generateCmsPromUrl(Integer channelType, String appKey, String appSecret) {
        try {
            // 构建请求参数（参考PDD官方文档：pdd.ddk.cms.prom.url.generate）
            Map<String, Object> params = new HashMap<>();
            params.put("apiMethodName", "pdd.ddk.cms.prom.url.generate");
            if (channelType != null) {
                params.put("channelType", channelType);
            }

            Object response = execute(params, appKey, appSecret);

            // 包装为JSONObject返回
            JSONObject result = new JSONObject();
            result.put("response", response);
            return result;
        } catch (Exception e) {
            log.error("生成商城推广链接失败", e);
            throw new ServiceException("生成商城推广链接失败：" + e.getMessage());
        }
    }

    /**
     * 构建custom_parameters参数
     * 格式：{"uid":"用户ID","sid":"sessionId"}
     * 用于拼多多授权备案，uid为必填项
     * @param userId 用户ID
     * @return URL编码后的JSON字符串
     */
    private String buildCustomParameters(Long userId) {
        try {
            // 构建JSON对象
            JSONObject customParams = new JSONObject();
            // 5. 对用户ID进行MD5加密
            String userIdMd5 = Md5Utils.hash(userId.toString());
            customParams.put("uid", userIdMd5);
            // sid可以使用timestamp或其他标识，这里使用userId+timestamp
            //customParams.put("sid", userId + "_" + System.currentTimeMillis());
            // 转换为JSON字符串
            String jsonStr = customParams.toJSONString();
            log.debug("构建custom_parameters: {}", jsonStr);
            return jsonStr;
        } catch (Exception e) {
            log.error("构建custom_parameters失败", e);
            throw new ServiceException("构建custom_parameters失败: " + e.getMessage());
        }
    }

    /**
     * 解析拼多多商品搜索/推荐API响应
     * 将PDD API原始字段转换为前端适配器期望的统一格式
     * 参考JD的parseGoodsRankResponse方法实现
     */
    private JSONObject parseGoodsSearchResponse(Object response) {
        JSONObject result = new JSONObject();
        
        if (response == null) {
            result.put("response", createEmptyGoodsSearchResponse());
            return result;
        }

        // 将response对象转换为JSON字符串，再解析为JSONObject
        String responseStr = JSONObject.toJSONString(response);
        log.info("=== 拼多多API原始响应 ===");
        log.info(responseStr);
        
        JSONObject responseJson = JSONObject.parseObject(responseStr);
        
        // 检查是否有错误（PDD API错误时errorResponse不为空）
        JSONObject errorResponse = responseJson.getJSONObject("errorResponse");
        if (errorResponse != null) {
            result.put("response", createEmptyGoodsSearchResponse());
            result.put("errorCode", errorResponse.getString("errorCode"));
            result.put("errorMsg", errorResponse.getString("errorMsg"));
            log.error("拼多多API返回错误: {}", errorResponse.toJSONString());
            return result;
        }
        
        // 获取goodsSearchResponse（商品搜索接口）或goodsRecommendGetResponse（推荐接口）
        JSONObject dataResponse = responseJson.getJSONObject("goodsSearchResponse");
        if (dataResponse == null) {
            dataResponse = responseJson.getJSONObject("goodsRecommendGetResponse");
        }
        
        if (dataResponse == null) {
            result.put("response", createEmptyGoodsSearchResponse());
            log.warn("拼多多API响应中未找到goodsSearchResponse或goodsRecommendGetResponse");
            return result;
        }

        JSONArray goodsList = dataResponse.getJSONArray("goodsList");
        JSONArray resultArray = new JSONArray();
        
        if (goodsList != null && !goodsList.isEmpty()) {
            for (int i = 0; i < goodsList.size(); i++) {
                JSONObject item = goodsList.getJSONObject(i);
                JSONObject resultItem = transformGoodsItem(item);
                resultArray.add(resultItem);
            }
        }
        
        // 构建符合前端适配器期望的响应结构
        JSONObject goodsSearchResponse = new JSONObject();
        goodsSearchResponse.put("goodsList", resultArray);
        goodsSearchResponse.put("totalCount", dataResponse.getInteger("totalCount"));
        goodsSearchResponse.put("searchId", dataResponse.getString("searchId"));
        
        JSONObject responseWrapper = new JSONObject();
        responseWrapper.put("goodsSearchResponse", goodsSearchResponse);
        
        result.put("response", responseWrapper);
        
        log.info("成功解析拼多多商品列表，数量: {}", resultArray.size());
        return result;
    }

    /**
     * 转换单个商品数据项
     * 将PDD API字段映射为前端适配器期望的字段名
     */
    private JSONObject transformGoodsItem(JSONObject item) {
        JSONObject resultItem = new JSONObject();
        
        // ===== 商品基本信息 =====
        // 商品ID - goodsSign作为唯一标识
        String goodsSign = item.getString("goodsSign");
        Long goodsId = item.getLong("goodsId");
        resultItem.put("goodsSign", goodsSign);
        resultItem.put("goodsId", goodsId);
        resultItem.put("id", goodsSign != null ? goodsSign : String.valueOf(goodsId)); // 统一ID字段
        
        // 商品名称
        String goodsName = item.getString("goodsName");
        resultItem.put("goodsName", goodsName);
        resultItem.put("title", goodsName); // 兼容字段
        resultItem.put("skuName", goodsName); // 兼容JD字段
        
        // 商品图片
        String goodsThumbnailUrl = item.getString("goodsThumbnailUrl");
        String goodsImageUrl = item.getString("goodsImageUrl");
        resultItem.put("goodsThumbnailUrl", goodsThumbnailUrl);
        resultItem.put("goodsImageUrl", goodsImageUrl);
        resultItem.put("mainPic", goodsThumbnailUrl != null ? goodsThumbnailUrl : goodsImageUrl); // 统一主图字段
        resultItem.put("imageUrl", goodsImageUrl); // 兼容JD字段
        resultItem.put("pictUrl", goodsImageUrl); // 兼容TBK字段
        
        // 商品图片列表
        JSONArray goodsGalleryUrls = item.getJSONArray("goodsGalleryUrls");
        if (goodsGalleryUrls != null && !goodsGalleryUrls.isEmpty()) {
            resultItem.put("goodsGalleryUrls", goodsGalleryUrls);
            resultItem.put("smallImages", goodsGalleryUrls); // 兼容字段
            resultItem.put("imgList", goodsGalleryUrls); // 兼容JD字段
        } else {
            // 如果没有图片列表，创建包含主图的数组
            JSONArray singleImageArray = new JSONArray();
            if (goodsImageUrl != null && !goodsImageUrl.isEmpty()) {
                singleImageArray.add(goodsImageUrl);
            }
            resultItem.put("goodsGalleryUrls", singleImageArray);
            resultItem.put("smallImages", singleImageArray);
            resultItem.put("imgList", singleImageArray);
        }
        
        // ===== 价格信息（单位：分 → 元）=====
        Long minGroupPrice = item.getLong("minGroupPrice"); // 最低拼团价（分）
        Long minNormalPrice = item.getLong("minNormalPrice"); // 最低单买价（分）
        Long couponDiscount = item.getLong("couponDiscount"); // 优惠券面额（分）
        if (couponDiscount == null) {
            couponDiscount = item.getLong("extraCouponAmount"); // 额外优惠券（分）
        }
        if (couponDiscount == null) {
            couponDiscount = 0L;
        }
        
        // 转换为元（保留2位小数）
        BigDecimal minGroupPriceYuan = minGroupPrice != null ? 
            new BigDecimal(minGroupPrice).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        BigDecimal minNormalPriceYuan = minNormalPrice != null ? 
            new BigDecimal(minNormalPrice).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        BigDecimal couponDiscountYuan = new BigDecimal(couponDiscount).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
        
        // 券后价 = 拼团价 - 优惠券
        BigDecimal finalPrice = minGroupPriceYuan.subtract(couponDiscountYuan);
        
        resultItem.put("minGroupPrice", minGroupPrice); // 原始分单位
        resultItem.put("minNormalPrice", minNormalPrice); // 原始分单位
        resultItem.put("couponDiscount", couponDiscount); // 原始分单位
        resultItem.put("price", finalPrice); // 券后价（元）
        resultItem.put("actualPrice", finalPrice); // 统一券后价字段
        resultItem.put("originalPrice", minNormalPriceYuan); // 原价（元）
        resultItem.put("zkFinalPrice", finalPrice); // 兼容TBK字段
        resultItem.put("reservePrice", minNormalPriceYuan); // 兼容TBK字段
        
        // ===== 佣金信息（千分比 → 百分比）=====
        Long promotionRate = item.getLong("promotionRate"); // 佣金比例（千分比）
        if (promotionRate == null) {
            promotionRate = 0L;
        }
        
        // 转换为百分比（保留2位小数）
        BigDecimal promotionRatePercent = new BigDecimal(promotionRate).divide(new BigDecimal(10), 2, RoundingMode.HALF_UP);
        
        // 计算预估佣金 = 券后价 * 佣金比例
        BigDecimal commission = finalPrice.multiply(promotionRatePercent).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
        
        resultItem.put("promotionRate", promotionRate); // 原始千分比
        resultItem.put("commission", commission); // 预估佣金（元）
        resultItem.put("commissionRate", promotionRatePercent); // 佣金比例（百分比）
        resultItem.put("commissionShare", promotionRatePercent); // 兼容JD字段
        resultItem.put("zkFinalPriceWap", commission); // 兼容TBK字段（用佣金替代）
        
        // ===== 销量信息 =====
        String salesTip = item.getString("salesTip"); // 销量文案，如"8.4万+"
        Integer monthSales = parseSalesTip(salesTip);
        resultItem.put("salesTip", salesTip);
        resultItem.put("monthSales", monthSales); // 统一月销量字段
        resultItem.put("volume", monthSales); // 兼容TBK字段
        resultItem.put("inOrderCount30Days", monthSales); // 兼容JD字段
        
        // ===== 店铺信息 =====
        String mallName = item.getString("mallName");
        resultItem.put("mallName", mallName);
        resultItem.put("shopTitle", mallName); // 统一店铺名称字段
        resultItem.put("shopName", mallName); // 兼容JD字段
        resultItem.put("nick", mallName); // 兼容TBK字段
        
        // ===== 优惠券信息 =====
        resultItem.put("couponAmount", couponDiscountYuan); // 优惠券金额（元）
        resultItem.put("hasCoupon", item.getBoolean("hasCoupon"));
        resultItem.put("couponStartTime", item.getLong("couponStartTime"));
        resultItem.put("couponEndTime", item.getLong("couponEndTime"));
        resultItem.put("couponRemainQuantity", item.getLong("couponRemainQuantity"));
        resultItem.put("couponTotalQuantity", item.getLong("couponTotalQuantity"));
        resultItem.put("couponMinOrderAmount", item.getLong("couponMinOrderAmount"));
        
        // 构建优惠券描述文本
        String couponInfo = buildCouponInfo(couponDiscountYuan, item.getLong("couponMinOrderAmount"));
        resultItem.put("couponInfo", couponInfo);
        
        // ===== 分类信息 =====
        resultItem.put("optId", item.getLong("optId"));
        resultItem.put("optName", item.getString("optName"));
        resultItem.put("categoryName", item.getString("optName")); // 统一分类名称字段
        resultItem.put("catIds", item.getJSONArray("catIds"));
        
        // ===== 品牌信息 =====
        resultItem.put("brandName", item.getString("brandName"));
        
        // ===== 商家类型 =====
        resultItem.put("merchantType", item.getInteger("merchantType"));
        resultItem.put("mallCps", item.getLong("mallCps"));
        
        // ===== 标签信息 =====
        resultItem.put("serviceTags", item.getJSONArray("serviceTags"));
        resultItem.put("unifiedTags", item.getJSONArray("unifiedTags"));
        resultItem.put("activityTags", item.getJSONArray("activityTags"));
        resultItem.put("skuTagList", item.getJSONArray("serviceTags")); // 兼容JD字段
        
        // ===== 推广链接（需要单独调用接口生成）=====
        resultItem.put("materialUrl", ""); // 推广链接（需通过promotion/url/generate接口获取）
        resultItem.put("clickUrl", ""); // 统一推广链接字段
        resultItem.put("url", ""); // 兼容字段
        resultItem.put("couponClickUrl", ""); // 兼容TBK字段
        
        // ===== 其他信息 =====
        resultItem.put("descTxt", item.getString("descTxt")); // 商品描述
        resultItem.put("lgstTxt", item.getString("lgstTxt")); // 物流文案
        resultItem.put("servTxt", item.getString("servTxt")); // 服务文案
        
        return resultItem;
    }

    /**
     * 解析销量文案
     * @param salesTip 销量文案，如"8.4万+"、"1722"、"2000+"
     * @return 销量数值
     */
    private Integer parseSalesTip(String salesTip) {
        if (salesTip == null || salesTip.isEmpty()) {
            return 0;
        }
        
        // 去除"+"号
        String salesStr = salesTip.replace("+", "").trim();
        
        // 处理"万"的情况
        if (salesStr.contains("万")) {
            try {
                double num = Double.parseDouble(salesStr.replace("万", ""));
                return (int) (num * 10000);
            } catch (NumberFormatException e) {
                log.warn("解析销量文案失败: {}", salesTip);
                return 0;
            }
        }
        
        // 直接转换数字
        try {
            return Integer.parseInt(salesStr);
        } catch (NumberFormatException e) {
            log.warn("解析销量文案失败: {}", salesTip);
            return 0;
        }
    }

    /**
     * 构建优惠券信息文本
     */
    private String buildCouponInfo(BigDecimal couponDiscountYuan, Long couponMinOrderAmount) {
        if (couponDiscountYuan == null || couponDiscountYuan.compareTo(BigDecimal.ZERO) <= 0) {
            return "";
        }
        
        if (couponMinOrderAmount != null && couponMinOrderAmount > 0) {
            BigDecimal minOrderAmountYuan = new BigDecimal(couponMinOrderAmount)
                .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
            return String.format("满%s元减%s元", minOrderAmountYuan, couponDiscountYuan);
        }
        
        return String.format("%s元优惠券", couponDiscountYuan);
    }

    /**
     * 创建空的商品搜索响应
     */
    private JSONObject createEmptyGoodsSearchResponse() {
        JSONObject goodsSearchResponse = new JSONObject();
        goodsSearchResponse.put("goodsList", new JSONArray());
        goodsSearchResponse.put("totalCount", 0);
        goodsSearchResponse.put("searchId", "");
        
        JSONObject responseWrapper = new JSONObject();
        responseWrapper.put("goodsSearchResponse", goodsSearchResponse);
        
        return responseWrapper;
    }

    /**
     * 解析拼多多推广链接生成API响应
     * 提取mobileShortUrl短链字段，适配前端需求
     */
    private JSONObject parsePromotionUrlResponse(Object response) {
        JSONObject result = new JSONObject();
        
        if (response == null) {
            log.error("拼多多推广链接生成API响应为空");
            throw new ServiceException("生成推广链接失败：响应为空");
        }

        // 将response对象转换为JSON字符串，再解析为JSONObject
        String responseStr = JSONObject.toJSONString(response);
        log.info("=== 拼多多推广链接生成API原始响应 ===");
        log.info(responseStr);
        
        JSONObject responseJson = JSONObject.parseObject(responseStr);
        
        // 检查是否有错误
        JSONObject errorResponse = responseJson.getJSONObject("errorResponse");
        if (errorResponse != null) {
            String errorMsg = errorResponse.getString("errorMsg");
            log.error("拼多多推广链接生成API返回错误: {}", errorResponse.toJSONString());
            throw new ServiceException("生成推广链接失败：" + errorMsg);
        }
        
        // 获取goodsPromotionUrlGenerateResponse
        JSONObject urlGenerateResponse = responseJson.getJSONObject("goodsPromotionUrlGenerateResponse");
        if (urlGenerateResponse == null) {
            log.error("拼多多推广链接生成API响应中未找到goodsPromotionUrlGenerateResponse");
            throw new ServiceException("生成推广链接失败：响应格式异常");
        }

        // 获取推广链接列表
        JSONArray goodsPromotionUrlList = urlGenerateResponse.getJSONArray("goodsPromotionUrlList");
        if (goodsPromotionUrlList == null || goodsPromotionUrlList.isEmpty()) {
            log.error("拼多多推广链接列表为空");
            throw new ServiceException("生成推广链接失败：链接列表为空");
        }
        
        // 获取第一个推广链接数据
        JSONObject urlData = goodsPromotionUrlList.getJSONObject(0);
        if (urlData == null) {
            log.error("拼多多推广链接数据为空");
            throw new ServiceException("生成推广链接失败：链接数据为空");
        }
        
        // 提取各种链接字段
        String mobileShortUrl = urlData.getString("mobileShortUrl");
        String mobileUrl = urlData.getString("mobileUrl");
        String shortUrl = urlData.getString("shortUrl");
        String url = urlData.getString("url");
        String weAppWebViewUrl = urlData.getString("weAppWebViewUrl");
        String weAppWebViewShortUrl = urlData.getString("weAppWebViewShortUrl");
        String schemaUrl = urlData.getString("schemaUrl");
        String qrcodeUrl = urlData.getString("qrcodeUrl");
        
        // 构建适配后的响应数据
        // 优先使用mobileShortUrl作为主要推广链接（适配小程序场景）
        JSONObject adaptedUrlData = new JSONObject();
        adaptedUrlData.put("goodsSign", urlData.getString("goodsSign"));
        adaptedUrlData.put("promotionUrl", mobileShortUrl != null ? mobileShortUrl : 
                                          (mobileUrl != null ? mobileUrl : 
                                          (shortUrl != null ? shortUrl : url)));
        adaptedUrlData.put("mobileShortUrl", mobileShortUrl);
        adaptedUrlData.put("mobileUrl", mobileUrl);
        adaptedUrlData.put("shortUrl", shortUrl);
        adaptedUrlData.put("url", url);
        adaptedUrlData.put("weAppWebViewUrl", weAppWebViewUrl);
        adaptedUrlData.put("weAppWebViewShortUrl", weAppWebViewShortUrl);
        adaptedUrlData.put("schemaUrl", schemaUrl);
        adaptedUrlData.put("qrcodeUrl", qrcodeUrl);
        
        // 包装响应数据，保持与原有结构兼容
        JSONObject goodsPromotionUrlGenerateResponse = new JSONObject();
        goodsPromotionUrlGenerateResponse.put("goodsPromotionUrlList", goodsPromotionUrlList);
        goodsPromotionUrlGenerateResponse.put("adaptedData", adaptedUrlData);
        
        JSONObject responseWrapper = new JSONObject();
        responseWrapper.put("goodsPromotionUrlGenerateResponse", goodsPromotionUrlGenerateResponse);
        
        result.put("response", responseWrapper);
        
        log.info("成功解析拼多多推广链接，mobileShortUrl: {}", mobileShortUrl);
        return result;
    }
}
