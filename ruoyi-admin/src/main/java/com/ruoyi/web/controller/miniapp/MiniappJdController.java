package com.ruoyi.web.controller.miniapp;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.SysTbkAgentApply;
import com.ruoyi.system.service.IJdSdkService;
import com.ruoyi.system.service.ISysTbkAgentApplyService;
import com.ruoyi.union.jd.config.JdConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小程序京东联盟Controller
 * 
 * @author ruoyi
 */
@Tag(name = "小程序京东联盟")
@RestController
@RequestMapping("/miniapp/jd")
public class MiniappJdController extends BaseController {

    @Autowired
    private IJdSdkService jdSdkService;

    @Resource
    private JdConfig jdConfig;

    @Autowired
    private ISysTbkAgentApplyService tbkAgentApplyService;

    /**
     * 获取热门商品列表（公开接口）
     */
    @Operation(summary = "获取热门商品列表")
    @GetMapping("/goods/hot")
    public AjaxResult getHotGoods(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) String keyword) {

        // 构建请求参数
        Map<String, String> params = new HashMap<>();
        
        // 京东联盟热销榜参数（参考官方文档：https://union.jd.com/openplatform/api/v2?apiName=jd.union.open.goods.rank.query）
        // rankId: 榜单ID（必填）- 榜单ID（200000：全部，200001：食品酒水，200002：家庭清洁，200003：个护美妆，200004：医药保健，200005：生鲜，200006：数码家电，200007：家居日用，200008：时尚生活）
        // sortType: 排序类型（必填）- 1：2小时，2：高佣，3：24小时
        params.put("rankGoodsReq.rankId", "200000"); // 食品酒水实时榜
        params.put("rankGoodsReq.sortType", "2"); // 高佣排序
        
        params.put("rankGoodsReq.pageIndex", page.toString());
        params.put("rankGoodsReq.pageSize", String.valueOf(Math.min(pageSize, 20))); // 单页数最大20
        
        // 调用京东联盟SDK
        JSONObject resultOb = jdSdkService.queryGoodsRank(params);
        
        // 直接返回Service层返回的完整JSONObject（与淘宝客保持一致）
        return success(resultOb);
    }

    /**
     * 获取商品详情（公开接口）- 支持invitationCode和userId参数
     */
    @Operation(summary = "获取商品详情")
    @GetMapping("/getGoodsDetail")
    public AjaxResult getGoodsDetail(
            @RequestParam String skuIds,
            @RequestParam(required = false) String invitationCode,
            @RequestParam(required = false) Long userId) {

        // 1、根据邀请码或用户ID查询代理信息
        SysTbkAgentApply agentApply = null;
        if (invitationCode != null && !invitationCode.trim().isEmpty()) {
            agentApply = tbkAgentApplyService.selectByInvitationCode(invitationCode);
            // 验证代理状态：只有审核通过（status=1）的代理才有效
            if (agentApply != null && !"1".equals(agentApply.getStatus())) {
                agentApply = null;
            }
        } else if (userId != null) {
            SysTbkAgentApply userApply = tbkAgentApplyService.selectByUserId(userId);
            // 验证代理状态：只有审核通过（status=1）的代理才有效
            if (userApply != null && "1".equals(userApply.getStatus())) {
                agentApply = userApply;
            }
        }
        
        // 调用京东联盟SDK查询商品详情
        JSONObject resultOb = jdSdkService.queryGoodsBigField(skuIds);
        
        if (resultOb == null || resultOb.isEmpty()) {
            return AjaxResult.error("商品不存在或已下架");
        }
        
        // 获取推广链接
        Map<String, String> promotionParams = new HashMap<>();
        promotionParams.put("materialId", skuIds);
        promotionParams.put("positionId", jdConfig.getPositionId().toString()); // 需要看代理商商有没有
        
        JSONObject promotionResult = jdSdkService.getPromotionLink(promotionParams);
        if (promotionResult != null && promotionResult.containsKey("clickURL")) {
            resultOb.put("clickURL", promotionResult.getString("clickURL"));
            resultOb.put("shortUrl", promotionResult.getString("shortURL"));
        }

        logger.info("getGoodsDetail >>>> " +  resultOb.toString());

        return AjaxResult.success(resultOb);
    }

    /**
     * 搜索商品(公开接口)
     */
    @Operation(summary = "搜索商品")
    @GetMapping("/goods/search")
    public AjaxResult searchGoods(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        
        // 京东联盟暂不支持关键词搜索的公开API
        // 这里使用热销榜作为替代方案
        Map<String, String> params = new HashMap<>();
        
        // 使用正确的参数名（参考官方文档）
        params.put("rankGoodsReq.rankId", "200000"); // 全部榜单
        params.put("rankGoodsReq.sortType", "2"); // 高佣排序
        params.put("rankGoodsReq.pageIndex", page.toString());
        params.put("rankGoodsReq.pageSize", String.valueOf(Math.min(pageSize, 20))); // 单页数最大20
        
        JSONObject resultOb = jdSdkService.queryGoodsRank(params);
        
        // 直接返回Service层返回的完整JSONObject（与淘宝客保持一致）
        return success(resultOb);
    }

    /**
     * 根据分类获取商品列表(公开接口)
     */
    @Operation(summary = "根据分类获取商品")
    @GetMapping("/goods/list")
    public AjaxResult getGoodsByCategory(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long cid1,
            @RequestParam(required = false) Long cid2,
            @RequestParam(required = false) Long cid3,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "4") String rankType,
            @RequestParam(required = false) String sort) {

        // 构建请求参数
        Map<String, String> params = new HashMap<>();
        
        // 京东联盟榜单参数（参考官方文档）
        // rankId: 榜单ID（必填）- 200000：全部
        // sortType: 排序类型（必填）- 1：2小时，2：高佣，3：24小时
        // 根据传入的rankType参数转换为rankId（保持接口兼容性）
        String rankId = "200000"; // 默认全部榜单
        if ("1".equals(rankType)) {
            rankId = "200000"; // 销量榜->全部榜单
        } else if ("2".equals(rankType)) {
            rankId = "200000"; // 佣金榜->全部榜单
        } else if ("3".equals(rankType)) {
            rankId = "200000"; // 新品榜->全部榜单
        } else if ("4".equals(rankType)) {
            rankId = "200000"; // 热门榜->全部榜单
        }
        
        params.put("rankGoodsReq.rankId", rankId);
        params.put("rankGoodsReq.sortType", "2"); // 高佣排序
        
        // 类目ID（可选）
        if (cid1 != null) {
            params.put("rankGoodsReq.cid1", cid1.toString());
        }
        if (cid2 != null) {
            params.put("rankGoodsReq.cid2", cid2.toString());
        }
        if (cid3 != null) {
            params.put("rankGoodsReq.cid3", cid3.toString());
        }
        
        params.put("rankGoodsReq.pageIndex", page.toString());
        params.put("rankGoodsReq.pageSize", String.valueOf(Math.min(pageSize, 20))); // 单页数最大20
        
        // 调用京东联盟SDK
        JSONObject resultOb = jdSdkService.queryGoodsRank(params);
        
        // 直接返回Service层返回的完整JSONObject（与淘宝客保持一致）
        return success(resultOb);
    }

    /**
     * 获取分类列表（公开接口）
     */
    @Operation(summary = "获取分类列表")
    @GetMapping("/categories")
    public AjaxResult getCategories() {
        // TODO: 从数据库查询分类数据
        // 京东联盟常用分类ID
        List<Map<String, Object>> categories = new ArrayList<>();
        
        // 分类ID和名称（京东一级分类）
        long[] categoryIds = {9987L, 9988L, 1315L, 6994L, 737L, 794L, 1672L, 11729L};
        String[] categoryNames = {"手机", "电脑", "家电", "服饰", "美妆", "图书", "食品", "运动"};
        
        for (int i = 0; i < categoryNames.length; i++) {
            Map<String, Object> category = new HashMap<>();
            category.put("id", categoryIds[i]);
            category.put("name", categoryNames[i]);
            category.put("sort", i + 1);
            categories.add(category);
        }
        
        return AjaxResult.success(categories);
    }

    /**
     * 获取推广链接（公开接口）
     */
    @Operation(summary = "获取推广链接")
    @GetMapping("/getPromotionUrl")
    public AjaxResult getPromotionUrl(
            @RequestParam String itemId,
            @RequestParam(required = false) String positionId,
            @RequestParam(required = false) String sceneId,
            @RequestParam(required = false) String siteId) {

        // 构建请求参数
        Map<String, String> params = new HashMap<>();
        params.put("materialId", itemId);
        
        if (positionId != null && !positionId.isEmpty()) {
            params.put("positionId", positionId);
        }
        
        if (sceneId != null && !sceneId.isEmpty()) {
            params.put("promotionCodeReq.sceneId", sceneId);
        }
        
        if (siteId != null && !siteId.isEmpty()) {
            params.put("promotionCodeReq.siteId", siteId);
        }
        
        // 调用京东联盟SDK
        JSONObject result = jdSdkService.getPromotionLink(params);
        logger.info("getPromotionUrl result >>>> " +  result.toJSONString());
        return success(result);
    }

    /**
     * 查询优惠券信息（公开接口）
     */
    @Operation(summary = "查询优惠券信息")
    @GetMapping("/coupon/query")
    public AjaxResult queryCoupon(@RequestParam String couponUrls) {
        
        // 调用京东联盟SDK
        JSONObject result = jdSdkService.queryCoupon(couponUrls);
        
        return success(result);
    }

    /**
     * 获取轮播图列表（公开接口）
     */
    @Operation(summary = "获取轮播图列表")
    @GetMapping("/banners")
    public AjaxResult getBanners() {
        // TODO: 从数据库查询轮播图数据
        List<Map<String, Object>> banners = new ArrayList<>();
        
        // 轮播图1 - 数码促销场景
        Map<String, Object> banner1 = new HashMap<>();
        banner1.put("id", 1);
        banner1.put("imageUrl", "https://img14.360buyimg.com/pop/jfs/t1/123456/7/12345/123456/5f123456E123456/123456.jpg");
        banner1.put("linkType", "category");
        banner1.put("linkId", "9987");
        banner1.put("sort", 1);
        banners.add(banner1);
        
        // 轮播图2 - 家电促销场景
        Map<String, Object> banner2 = new HashMap<>();
        banner2.put("id", 2);
        banner2.put("imageUrl", "https://img14.360buyimg.com/pop/jfs/t1/234567/8/23456/234567/5f234567E234567/234567.jpg");
        banner2.put("linkType", "category");
        banner2.put("linkId", "1315");
        banner2.put("sort", 2);
        banners.add(banner2);
        
        // 轮播图3 - 服饰促销场景
        Map<String, Object> banner3 = new HashMap<>();
        banner3.put("id", 3);
        banner3.put("imageUrl", "https://img14.360buyimg.com/pop/jfs/t1/345678/9/34567/345678/5f345678E345678/345678.jpg");
        banner3.put("linkType", "category");
        banner3.put("linkId", "6994");
        banner3.put("sort", 3);
        banners.add(banner3);
        
        return AjaxResult.success(banners);
    }
}
