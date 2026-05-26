package com.ruoyi.web.controller.miniapp;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysAgentUnionRelation;
import com.ruoyi.system.domain.SysTbkAgentApply;
import com.ruoyi.system.service.IPddApiService;
import com.ruoyi.system.service.ISysAgentUnionRelationService;
import com.ruoyi.system.service.ISysTbkAgentApplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小程序拼多多联盟Controller
 * 
 * @author ruoyi
 */
@Tag(name = "小程序拼多多联盟")
@RestController
@RequestMapping("/miniapp/pdd")
public class MiniappPddController extends BaseController {

    @Autowired
    private IPddApiService pddApiService;

    @Autowired
    private ISysTbkAgentApplyService tbkAgentApplyService;

    @Autowired
    private ISysAgentUnionRelationService agentUnionRelationService;

    /**
     * 获取代理的拼多多配置
     */
    private String[] getAgentPddConfig(String invitationCode, Long userId) {
        SysTbkAgentApply agentApply = null;
        
        // 根据邀请码或用户ID查询代理信息
        if (invitationCode != null && !invitationCode.trim().isEmpty()) {
            agentApply = tbkAgentApplyService.selectByInvitationCode(invitationCode);
            if (agentApply != null && !"1".equals(agentApply.getStatus())) {
                agentApply = null;
            }
        } else if (userId != null) {
            SysTbkAgentApply userApply = tbkAgentApplyService.selectByUserId(userId);
            if (userApply != null && "1".equals(userApply.getStatus())) {
                agentApply = userApply;
            }
        }
        
        if (agentApply == null) {
            return new String[]{null, null};
        }
        
        // 查询代理的拼多多联盟关系
        List<SysAgentUnionRelation> relations = agentUnionRelationService.selectByApplyId(agentApply.getId());
        SysAgentUnionRelation pddRelation = relations.stream()
            .filter(r -> "pdd".equals(r.getPlatformType()) && "1".equals(r.getStatus()))
            .findFirst()
            .orElse(null);
        
        if (pddRelation != null && pddRelation.getUnionPlatform() != null) {
            return new String[]{
                pddRelation.getUnionPlatform().getAppKey(),
                pddRelation.getUnionPlatform().getAppSecret()
            };
        }
        
        return new String[]{null, null};
    }

    /**
     * 获取商品列表（公开接口）
     * 根据关键词或分类获取商品列表
     */
    @Operation(summary = "获取商品列表")
    @GetMapping("/goods/list")
    public AjaxResult getGoodsList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String categoryId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) String invitationCode,
            @RequestParam(required = false) Long userId) {
        
        // 获取代理配置
        String[] config = getAgentPddConfig(invitationCode, userId);
        
        JSONObject result;
        
        // 如果有关键词，使用搜索接口
        if (keyword == null || keyword.trim().isEmpty()) {
            keyword = "家电";
        }
        result = pddApiService.searchGoods(keyword, page, pageSize, userId, config[0], config[1]);
//        else {
//            // 否则返回推荐商品
//            result = pddApiService.getRecommendGoods(page, pageSize, userId, config[0], config[1]);
//        }
        AjaxResult ajaxResult = success(result);
        logger.info("获取商品列表 getGoodsList >>>> " + ajaxResult.toString());
        return ajaxResult;
    }

    /**
     * 搜索商品（公开接口）
     */
    @Operation(summary = "搜索商品")
    @GetMapping("/goods/search")
    public AjaxResult searchGoods(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) String invitationCode,
            @RequestParam(required = false) Long userId) {
        
        // 获取代理配置
        String[] config = getAgentPddConfig(invitationCode, userId);
        
        // 调用服务层
        JSONObject result = pddApiService.searchGoods(keyword, page, pageSize, userId, config[0], config[1]);
        
        return success(result);
    }

    /**
     * 获取商品详情（公开接口）
     */
    @Operation(summary = "获取商品详情")
    @GetMapping("/goods/detail")
    public AjaxResult getGoodsDetail(
            @RequestParam String goodsIdList,
            @RequestParam(required = false) String invitationCode,
            @RequestParam(required = false) Long userId) {
        // 获取代理配置
        String[] config = getAgentPddConfig(invitationCode, userId);
        // 调用服务层
        JSONObject result = pddApiService.getGoodsDetail(goodsIdList, config[0], config[1]);
        return success(result);
    }

    /**
     * 获取推荐商品（公开接口）
     */
    @Operation(summary = "获取推荐商品")
    @GetMapping("/goods/recommend")
    public AjaxResult getRecommendGoods(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) String invitationCode,
            @RequestParam(required = false) Long userId) {
        
        // 获取代理配置
        String[] config = getAgentPddConfig(invitationCode, userId);
        
        // 调用服务层
        JSONObject result = pddApiService.getRecommendGoods(page, pageSize, userId, config[0], config[1]);
        
        return success(result);
    }

    /**
     * 获取热门商品列表（公开接口）
     */
    @Operation(summary = "获取热门商品列表")
    @GetMapping("/goods/hot")
    public AjaxResult getHotGoods(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) String invitationCode,
            @RequestParam(required = false) Long userId) {
        
        // 拼多多的热门商品使用推荐商品接口
        return getRecommendGoods(page, pageSize, invitationCode, userId);
    }

    /**
     * 生成推广链接（公开接口）
     * goodsSign
     */
    @Operation(summary = "生成推广链接")
    @GetMapping("/promotion/url/generate")
    public AjaxResult generatePromotionUrl(
            @RequestParam(required = false) String goodsIdList,
            @RequestParam(required = false) String goodsSign,
            @RequestParam(required = false, defaultValue = "false") Boolean generateShortUrl,
            @RequestParam(required = false) String invitationCode,
            @RequestParam(required = false) Long userId) {
        if (StringUtils.isEmpty(goodsSign)) {
            return error("goodsSign不能为空");
        }
        // 获取代理配置
        String[] config = getAgentPddConfig(invitationCode, userId);
        // 调用服务层
        JSONObject result = pddApiService.generatePromotionUrl(goodsSign, userId, config[0], config[1]);
        logger.info("生成推广链接 generatePromotionUrl >>>> " +  result.toString());
        return success(result);
    }

    /**
     * 生成营销工具推广链接（公开接口）
     */
    @Operation(summary = "生成营销工具推广链接")
    @GetMapping("/rp/prom/url/generate")
    public AjaxResult generateRpPromUrl(
            @RequestParam(required = false) Integer resourceType,
            @RequestParam(required = false) String url,
            @RequestParam(required = false) String invitationCode,
            @RequestParam(required = false) Long userId) {
        // 获取代理配置
        String[] config = getAgentPddConfig(invitationCode, userId);
        // 调用服务层
        JSONObject result = pddApiService.generateRpPromUrl(resourceType, url, config[0], config[1]);
        return success(result);
    }

    /**
     * 生成商城推广链接（公开接口）
     */
    @Operation(summary = "生成商城推广链接")
    @GetMapping("/cms/prom/url/generate")
    public AjaxResult generateCmsPromUrl(
            @RequestParam(required = false) Integer channelType,
            @RequestParam(required = false) String invitationCode,
            @RequestParam(required = false) Long userId) {
        // 获取代理配置
        String[] config = getAgentPddConfig(invitationCode, userId);
        // 调用服务层
        JSONObject result = pddApiService.generateCmsPromUrl(channelType, config[0], config[1]);
        return success(result);
    }

    /**
     * 获取分类列表（公开接口）
     */
    @Operation(summary = "获取分类列表")
    @GetMapping("/categories")
    public AjaxResult getCategories() {
        // TODO: 从数据库查询分类数据
        List<Map<String, Object>> categories = new ArrayList<>();
        
        // 拼多多常用分类
        String[] categoryNames = {"女装", "男装", "内衣", "美妆", "食品", "电器", "手机", "家居"};
        
        for (int i = 0; i < categoryNames.length; i++) {
            Map<String, Object> category = new HashMap<>();
            category.put("id", i + 1);
            category.put("name", categoryNames[i]);
            category.put("sort", i + 1);
            categories.add(category);
        }
        
        return success(categories);
    }
}
