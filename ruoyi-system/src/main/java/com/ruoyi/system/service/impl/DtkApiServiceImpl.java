package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.IDtkApiService;
import com.ruoyi.union.dtk.api.client.DtkApiClient;
import com.ruoyi.union.dtk.api.request.mastertool.DtkCreatTaokoulingRequest;
import com.ruoyi.union.dtk.api.request.mastertool.DtkGetPrivilegeLinkRequest;
import com.ruoyi.union.dtk.api.request.mastertool.DtkGetSuperCategoryRequest;
import com.ruoyi.union.dtk.api.request.putstorage.DtkGoodsDetailsRequest;
import com.ruoyi.union.dtk.api.request.search.DtkGetDtkSearchGoodsRequest;
import com.ruoyi.union.dtk.api.request.special.DtkGetRankingListRequest;
import com.ruoyi.union.dtk.api.request.special.DtkCarouselMapResponseRequest;
import com.ruoyi.union.dtk.api.response.base.DtkApiResponse;
import com.ruoyi.union.dtk.api.response.base.DtkSearchPageResponse;
import com.ruoyi.union.dtk.api.response.mastertool.DtkCreatTaokoulingResponse;
import com.ruoyi.union.dtk.api.response.mastertool.DtkGetPrivilegeLinkResponse;
import com.ruoyi.union.dtk.api.response.mastertool.DtkGetSuperCategoryResponse;
import com.ruoyi.union.dtk.api.response.putstorage.DtkGoodsDetailsResponse;
import com.ruoyi.union.dtk.api.response.search.DtkGetDtkSearchGoodsResponse1;
import com.ruoyi.union.dtk.api.response.special.DtkGetRankingListMergeResponse;
import com.ruoyi.union.dtk.api.response.special.DtkCarouselMapResponseResponse;
import com.ruoyi.union.dtk.config.DtkConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 大淘客API服务实现类
 * 
 * @author ruoyi
 */
@Slf4j
@Service
public class DtkApiServiceImpl implements IDtkApiService {

    @Autowired
    private DtkConfig dtkConfig;

    /**
     * 获取轮播图列表
     * 
     * @return 轮播图列表
     */
    @Override
    public JSONObject getBanners() {
        try {
            // 创建大淘客轮播图请求
            DtkCarouselMapResponseRequest request = new DtkCarouselMapResponseRequest();
            
            // 执行请求 - 这里需要从配置中获取appKey和appSecret
            // 由于轮播图是公开接口，可以使用系统默认配置
            DtkApiClient client = DtkApiClient.getInstance(getDefaultAppKey(), getDefaultAppSecret());
            DtkApiResponse<List<DtkCarouselMapResponseResponse>> response = client.execute(request);
            
            // 检查响应
            if (response == null || response.getCode() != 0) {
                log.error("获取轮播图失败: {}", response != null ? response.getMsg() : "响应为空");
                throw new ServiceException("获取轮播图失败");
            }
            
            // 转换为JSON返回
            JSONObject result = new JSONObject();
            result.put("code", 0);
            result.put("msg", "success");
            result.put("data", response.getData());
            
            return result;
        } catch (Exception e) {
            log.error("获取轮播图异常", e);
            throw new ServiceException("获取轮播图异常: " + e.getMessage());
        }
    }

    /**
     * 获取分类列表
     * 
     * @return 分类列表
     */
    @Override
    public JSONObject getCategories() {
        try {
            // 创建大淘客超级分类请求
            DtkGetSuperCategoryRequest request = new DtkGetSuperCategoryRequest();
            
            // 执行请求
            DtkApiClient client = DtkApiClient.getInstance(getDefaultAppKey(), getDefaultAppSecret());
            DtkApiResponse<List<DtkGetSuperCategoryResponse>> response = client.execute(request);
            
            // 检查响应
            if (response == null || response.getCode() != 0) {
                log.error("获取分类列表失败: {}", response != null ? response.getMsg() : "响应为空");
                throw new ServiceException("获取分类列表失败");
            }
            
            // 转换为JSON返回
            JSONObject result = new JSONObject();
            result.put("code", 0);
            result.put("msg", "success");
            result.put("data", response.getData());
            
            return result;
        } catch (Exception e) {
            log.error("获取分类列表异常", e);
            throw new ServiceException("获取分类列表异常: " + e.getMessage());
        }
    }

    /**
     * 搜索商品
     * @param keyword 关键词
     * @param categoryId 分类ID
     * @param page 页码
     * @param pageSize 每页数量
     * @param sort 排序方式
     * @param appKey 应用密钥
     * @param appSecret 应用密钥
     * @return 商品列表
     */
    @Override
    public JSONObject searchGoods(String keyword, Long categoryId, Integer page, Integer pageSize, 
                                 String sort, String appKey, String appSecret) {
        try {
            // 创建大淘客搜索请求
            DtkGetDtkSearchGoodsRequest request = new DtkGetDtkSearchGoodsRequest();
            if(StringUtils.isNotEmpty(keyword)){
                request.setKeyWords(keyword);
            }else {
                request.setKeyWords(dtkConfig.getKeyWords()); // 默认关键字
            }
            // 设置分类ID（如果有）
            if (categoryId != null) {
                request.setCids(String.valueOf(categoryId));
            }
            // 设置分页参数
            request.setPageId(page != null ? page.toString() : "1");
            request.setPageSize(pageSize != null ? pageSize: 20);
            // 设置排序方式
            if (StringUtils.isNotEmpty(sort)) {
                request.setSort(sort);
            }
            // 执行请求
            DtkApiClient client = DtkApiClient.getInstance(
                StringUtils.isNotEmpty(appKey) ? appKey : getDefaultAppKey(),
                StringUtils.isNotEmpty(appSecret) ? appSecret : getDefaultAppSecret()
            );
            DtkApiResponse<DtkSearchPageResponse<DtkGetDtkSearchGoodsResponse1>> response = client.execute(request);
            
            // 检查响应
            if (response == null || response.getCode() != 0) {
                log.error("搜索商品失败: {}", response != null ? response.getMsg() : "响应为空");
                throw new ServiceException("搜索商品失败");
            }
            
            // 转换为JSON返回
            JSONObject result = new JSONObject();
            result.put("code", 0);
            result.put("msg", "success");
            
            DtkSearchPageResponse<DtkGetDtkSearchGoodsResponse1> data = response.getData();
            if (data != null) {
                result.put("totalResults", data.getTotalNum());
                result.put("resultList", data.getList());
            } else {
                result.put("totalResults", 0);
                result.put("resultList", new java.util.ArrayList<>());
            }
            return result;
        } catch (Exception e) {
            log.error("搜索商品异常", e);
            throw new ServiceException("搜索商品异常: " + e.getMessage());
        }
    }

    /**
     * 获取商品详情
     * @param goodsId 商品ID
     * @param appKey 应用密钥
     * @param appSecret 应用密钥
     * @return 商品详情
     */
    @Override
    public JSONObject getGoodsDetail(String goodsId, String appKey, String appSecret) {
        try {
            // 创建大淘客商品详情请求
            DtkGoodsDetailsRequest request = new DtkGoodsDetailsRequest();
            request.setGoodsId(goodsId);
            
            // 执行请求
            DtkApiClient client = DtkApiClient.getInstance(
                StringUtils.isNotEmpty(appKey) ? appKey : getDefaultAppKey(),
                StringUtils.isNotEmpty(appSecret) ? appSecret : getDefaultAppSecret()
            );
            DtkApiResponse<DtkGoodsDetailsResponse> response = client.execute(request);
            
            // 检查响应
            if (response == null || response.getCode() != 0) {
                log.error("获取商品详情失败: {}", response != null ? response.getMsg() : "响应为空");
                throw new ServiceException("获取商品详情失败");
            }
            
            // 转换为JSON返回
            JSONObject result = new JSONObject();
            result.put("code", 0);
            result.put("msg", "success");
            result.put("data", response.getData());
            
            return result;
        } catch (Exception e) {
            log.error("获取商品详情异常", e);
            throw new ServiceException("获取商品详情异常: " + e.getMessage());
        }
    }

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
    @Override
    public JSONObject getHotGoods(Integer page, Integer pageSize, String keyword, 
                                 String appKey, String appSecret) {
        try {
            // 如果有关键词，则使用搜索接口
            if (StringUtils.isNotEmpty(keyword)) {
                return searchGoods(keyword, null, page, pageSize, "2", appKey, appSecret);
            }
            
            // 使用排行榜接口获取热门商品
            DtkGetRankingListRequest request = new DtkGetRankingListRequest();
            request.setRankType(1); // 1：实时榜
            request.setCid(0); // 0表示所有分类
            
            // 执行请求
            DtkApiClient client = DtkApiClient.getInstance(
                StringUtils.isNotEmpty(appKey) ? appKey : getDefaultAppKey(),
                StringUtils.isNotEmpty(appSecret) ? appSecret : getDefaultAppSecret()
            );
            DtkApiResponse<List<DtkGetRankingListMergeResponse>> response = client.execute(request);
            
            // 检查响应
            if (response == null || response.getCode() != 0) {
                log.error("获取热门商品失败: {}", response != null ? response.getMsg() : "响应为空");
                throw new ServiceException("获取热门商品失败");
            }
            
            // 转换为JSON返回
            JSONObject result = new JSONObject();
            result.put("code", 0);
            result.put("msg", "success");
            
            List<DtkGetRankingListMergeResponse> data = response.getData();
            if (data != null && !data.isEmpty()) {
                // 简单分页处理
                int start = (page - 1) * pageSize;
                int end = Math.min(start + pageSize, data.size());
                
                if (start < data.size()) {
                    result.put("totalResults", data.size());
                    result.put("resultList", data.subList(start, end));
                } else {
                    result.put("totalResults", 0);
                    result.put("resultList", new java.util.ArrayList<>());
                }
            } else {
                result.put("totalResults", 0);
                result.put("resultList", new java.util.ArrayList<>());
            }
            
            return result;
        } catch (Exception e) {
            log.error("获取热门商品异常", e);
            throw new ServiceException("获取热门商品异常: " + e.getMessage());
        }
    }

    /**
     * 高效转链（获取淘口令）
     * @param goodsId   商品ID
     * @param couponId  优惠券ID（可选）
     * @param pid       推广位ID（可选）
     * @param goodsName 商品名称（可选）
     * @param appKey    应用密钥
     * @param appSecret 应用密钥
     * @return 包含淘口令、高佣链接等信息
     */
    @Override
    public JSONObject getPrivilegeLink(String goodsId, String couponId, String pid,
                                       String goodsName, String appKey, String appSecret) {
        try {
            DtkApiClient client = DtkApiClient.getInstance(
                StringUtils.isNotEmpty(appKey) ? appKey : getDefaultAppKey(),
                StringUtils.isNotEmpty(appSecret) ? appSecret : getDefaultAppSecret()
            );
            
            // 第一步：尝试调用高效转链接口
            DtkGetPrivilegeLinkResponse linkData = null;
            String tpwd = null;
            String longTpwd = null;
            boolean privilegeLinkSuccess = false;
            
            try {
                DtkGetPrivilegeLinkRequest privilegeRequest = new DtkGetPrivilegeLinkRequest();
                privilegeRequest.setGoodsId(goodsId);
                if (StringUtils.isNotEmpty(couponId)) {
                    privilegeRequest.setCouponId(couponId);
                }
                if (StringUtils.isNotEmpty(pid)) {
                    privilegeRequest.setPid(pid);
                }

                // 如果传入了商品名称，使用商品名称；否则使用系统默认配置
                if (StringUtils.isNotEmpty(goodsName)) {
                    privilegeRequest.setRightSymbol(goodsName);
                } else {
                    privilegeRequest.setRightSymbol(dtkConfig.getRightSymbol());
                }

                DtkApiResponse<DtkGetPrivilegeLinkResponse> privilegeResponse = client.execute(privilegeRequest);

                if (privilegeResponse != null && privilegeResponse.getCode() == 0 && privilegeResponse.getData() != null) {
                    linkData = privilegeResponse.getData();
                    tpwd = linkData.getTpwd();
                    longTpwd = linkData.getLongTpwd();
                    privilegeLinkSuccess = true;
                    log.info("高效转链成功: goodsId={}", goodsId);
                } else {
                    String errMsg = privilegeResponse != null ? privilegeResponse.getMsg() : "响应为空";
                    log.warn("高效转链失败（将使用备选方案）: goodsId={}, error={}", goodsId, errMsg);
                }
            } catch (Exception e) {
                log.warn("高效转链异常（将使用备选方案）: goodsId={}, error={}", goodsId, e.getMessage());
            }

            // 如果高效转链失败或未返回淘口令，尝试备选方案
            if (!privilegeLinkSuccess || StringUtils.isEmpty(tpwd)) {
                log.info("尝试备选方案生成淘口令: goodsId={}", goodsId);
                
                // 备选方案1：如果高效转链成功但没有淘口令，使用转链后的URL生成淘口令
                if (privilegeLinkSuccess && linkData != null) {
                    String targetUrl = StringUtils.isNotEmpty(linkData.getCouponClickUrl())
                            ? linkData.getCouponClickUrl()
                            : linkData.getItemUrl();

                    if (StringUtils.isNotEmpty(targetUrl)) {
                        DtkCreatTaokoulingRequest taokoulingRequest = new DtkCreatTaokoulingRequest();
                        taokoulingRequest.setText("点击查看详情");
                        taokoulingRequest.setUrl(targetUrl);

                        DtkApiResponse<DtkCreatTaokoulingResponse> taokoulingResponse = client.execute(taokoulingRequest);
                        if (taokoulingResponse != null && taokoulingResponse.getCode() == 0
                                && taokoulingResponse.getData() != null) {
                            tpwd = taokoulingResponse.getData().getModel();
                            longTpwd = taokoulingResponse.getData().getLongTpwd();
                            log.info("使用转链URL生成淘口令成功: goodsId={}", goodsId);
                        }
                    }
                } 
                // 备选方案2：如果高效转链完全失败，直接构造商品链接后生成淘口令
                else {
                    try {
                        // 直接使用商品ID构造淘宝商品链接
                        String goodsUrl = "https://item.taobao.com/item.htm?id=" + goodsId;
                        
                        // 生成淘口令
                        DtkCreatTaokoulingRequest taokoulingRequest = new DtkCreatTaokoulingRequest();
                        taokoulingRequest.setText("点击查看详情");
                        taokoulingRequest.setUrl(goodsUrl);

                        DtkApiResponse<DtkCreatTaokoulingResponse> taokoulingResponse = client.execute(taokoulingRequest);
                        if (taokoulingResponse != null && taokoulingResponse.getCode() == 0
                                && taokoulingResponse.getData() != null) {
                            tpwd = taokoulingResponse.getData().getModel();
                            longTpwd = taokoulingResponse.getData().getLongTpwd();
                            
                            // 使用基本信息填充linkData
                            linkData = new DtkGetPrivilegeLinkResponse();
                            linkData.setItemUrl(goodsUrl);
                            
                            log.info("使用商品链接生成淘口令成功: goodsId={}", goodsId);
                        }
                    } catch (Exception e) {
                        log.error("备选方案失败: goodsId={}, error={}", goodsId, e.getMessage());
                    }
                }
            }

            // 如果所有方案都失败，抛出异常
            if (StringUtils.isEmpty(tpwd)) {
                throw new ServiceException("获取淘口令失败，所有方案均已尝试");
            }

            // 构建返回结果
            JSONObject result = new JSONObject();
            result.put("code", 0);
            result.put("msg", "success");

            JSONObject data = new JSONObject();
            data.put("goodsId", goodsId);
            data.put("tpwd", tpwd);
            data.put("longTpwd", StringUtils.isNotEmpty(longTpwd) ? longTpwd : tpwd);
            
            // 如果有linkData，填充其他字段
            if (linkData != null) {
                data.put("couponClickUrl", linkData.getCouponClickUrl());
                data.put("itemUrl", linkData.getItemUrl());
                data.put("shortUrl", linkData.getShortUrl());
                data.put("kuaiZhanUrl", linkData.getKuaiZhanUrl());
                data.put("maxCommissionRate", linkData.getMaxCommissionRate());
                data.put("actualPrice", linkData.getActualPrice());
                data.put("originalPrice", linkData.getOriginalPrice());
                data.put("couponInfo", linkData.getCouponInfo());
                data.put("couponStartTime", linkData.getCouponStartTime());
                data.put("couponEndTime", linkData.getCouponEndTime());
            }
            
            result.put("data", data);

            log.info("淘口令获取成功: goodsId={}, method={}", goodsId, 
                    privilegeLinkSuccess ? "高效转链" : "备选方案");
            return result;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("获取淘口令异常: goodsId={}", goodsId, e);
            throw new ServiceException("获取淘口令异常: " + e.getMessage());
        }
    }

    /**
     * 获取默认的AppKey
     * 从DtkConfig配置中读取
     * 
     * @return AppKey
     */
    private String getDefaultAppKey() {
        return dtkConfig.getAppKey();
    }

    /**
     * 获取默认的AppSecret
     * 从DtkConfig配置中读取
     * 
     * @return AppSecret
     */
    private String getDefaultAppSecret() {
        return dtkConfig.getAppSecret();
    }
}
