package com.ruoyi.web.controller.miniapp;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.SysAgentUnionRelation;
import com.ruoyi.system.domain.SysTbkAgentApply;
import com.ruoyi.system.service.IDtkApiService;
import com.ruoyi.system.service.ISysAgentUnionRelationService;
import com.ruoyi.system.service.ISysTbkAgentApplyService;
import com.ruoyi.union.dtk.config.DtkConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 小程序大淘客Controller
 * 
 * @author ruoyi
 */
@Slf4j
@Tag(name = "小程序大淘客")
@RestController
@RequestMapping("/miniapp/dtk")
public class MiniappDtkController extends BaseController {

    @Autowired
    private ISysTbkAgentApplyService tbkAgentApplyService;

    @Autowired
    private ISysAgentUnionRelationService agentUnionRelationService;

    @Autowired
    private IDtkApiService dtkApiService;

    @Autowired
    private DtkConfig dtkConfig;

    /**
     * 获取轮播图（公开接口）
     */
    @Operation(summary = "获取轮播图")
    @GetMapping("/banners")
    public AjaxResult getBanners(
            @RequestParam(required = false) String invitationCode,
            @RequestParam(required = false) Long userId) {
        
        // 1、根据邀请码或用户ID查询代理信息
        SysTbkAgentApply agentApply = getAgentApply(invitationCode, userId);
        
        // 2、验证代理用户是否绑定了大淘客平台，并获取代理的AppKey和AppSecret
        String appKey = null;
        String appSecret = null;
        if (agentApply != null) {
            SysAgentUnionRelation dtkRelation = getDtkRelation(agentApply.getId());
            if (dtkRelation == null || dtkRelation.getUnionPlatform() == null) {
                return AjaxResult.error("该代理未分配大淘客联盟信息");
            }
            appKey = dtkRelation.getUnionPlatform().getAppKey();
            appSecret = dtkRelation.getUnionPlatform().getAppSecret();
        }
        
        // 3、调用大淘客API获取轮播图
        try {
            JSONObject result = dtkApiService.getBanners();
            return AjaxResult.success(result);
        } catch (Exception e) {
            log.error("获取轮播图失败", e);
            return AjaxResult.error("获取轮播图失败: " + e.getMessage());
        }
    }

    /**
     * 获取分类列表（公开接口）
     */
    @Operation(summary = "获取分类列表")
    @GetMapping("/categories")
    public AjaxResult getCategories(
            @RequestParam(required = false) String invitationCode,
            @RequestParam(required = false) Long userId) {
        
        // 1、根据邀请码或用户ID查询代理信息
        SysTbkAgentApply agentApply = getAgentApply(invitationCode, userId);
        
        // 2、验证代理用户是否绑定了大淘客平台，并获取代理的AppKey和AppSecret
        String appKey = null;
        String appSecret = null;
        if (agentApply != null) {
            SysAgentUnionRelation dtkRelation = getDtkRelation(agentApply.getId());
            if (dtkRelation == null || dtkRelation.getUnionPlatform() == null) {
                return AjaxResult.error("该代理未分配大淘客联盟信息");
            }
            appKey = dtkRelation.getUnionPlatform().getAppKey();
            appSecret = dtkRelation.getUnionPlatform().getAppSecret();
        }
        
        // 3、调用大淘客API获取分类列表
        try {
            JSONObject result = dtkApiService.getCategories();
            return AjaxResult.success(result);
        } catch (Exception e) {
            log.error("获取分类列表失败", e);
            return AjaxResult.error("获取分类列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取商品列表（公开接口）
     */
    @Operation(summary = "获取商品列表")
    @GetMapping("/goods/list")
    public AjaxResult getGoodsList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String categoryId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "0") String sort,
            @RequestParam(required = false) String invitationCode,
            @RequestParam(required = false) Long userId) {
        
        // 1、根据邀请码或用户ID查询代理信息
        SysTbkAgentApply agentApply = getAgentApply(invitationCode, userId);
        
        // 2、验证代理用户是否绑定了大淘客平台，并获取代理的AppKey和AppSecret
        String appKey = null;
        String appSecret = null;
        if (agentApply != null) {
            SysAgentUnionRelation dtkRelation = getDtkRelation(agentApply.getId());
            if (dtkRelation == null || dtkRelation.getUnionPlatform() == null) {
                return AjaxResult.error("该代理未分配大淘客联盟信息");
            }
            appKey = dtkRelation.getUnionPlatform().getAppKey();
            appSecret = dtkRelation.getUnionPlatform().getAppSecret();
        }
        
        // 3、调用大淘客API获取商品列表
        try {
            Long categoryIdLong = null;
            if (categoryId != null && !categoryId.trim().isEmpty()) {
                try {
                    categoryIdLong = Long.parseLong(categoryId);
                } catch (NumberFormatException e) {
                    log.warn("分类ID格式错误: {}", categoryId);
                }
            }
            JSONObject result = dtkApiService.searchGoods(keyword, categoryIdLong, page, pageSize, sort, appKey, appSecret);
            log.info("获取商品列表结果: {}", result);
            return AjaxResult.success(result);
        } catch (Exception e) {
            log.error("获取商品列表失败", e);
            return AjaxResult.error("获取商品列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取商品详情（公开接口）
     */
    @Operation(summary = "获取商品详情")
    @GetMapping("/goods/{id}")
    public AjaxResult getGoodsDetail(
            @PathVariable String id,
            @RequestParam(required = false) String invitationCode,
            @RequestParam(required = false) Long userId) {
        
        // 1、根据邀请码或用户ID查询代理信息
        SysTbkAgentApply agentApply = getAgentApply(invitationCode, userId);
        
        // 2、验证代理用户是否绑定了大淘客平台，并获取代理的AppKey和AppSecret
        String appKey = null;
        String appSecret = null;
        if (agentApply != null) {
            SysAgentUnionRelation dtkRelation = getDtkRelation(agentApply.getId());
            if (dtkRelation == null || dtkRelation.getUnionPlatform() == null) {
                return AjaxResult.error("该代理未分配大淘客联盟信息");
            }
            appKey = dtkRelation.getUnionPlatform().getAppKey();
            appSecret = dtkRelation.getUnionPlatform().getAppSecret();
        }
        
        // 3、调用大淘客API获取商品详情
        try {
            JSONObject result = dtkApiService.getGoodsDetail(id, appKey, appSecret);
            return AjaxResult.success(result);
        } catch (Exception e) {
            log.error("获取商品详情失败: id={}", id, e);
            return AjaxResult.error("获取商品详情失败: " + e.getMessage());
        }
    }

    /**
     * 获取热门商品列表（公开接口）
     */
    @Operation(summary = "获取热门商品列表")
    @GetMapping("/goods/hot")
    public AjaxResult getHotGoods(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String invitationCode,
            @RequestParam(required = false) Long userId) {

        // 1、根据邀请码或用户ID查询代理申请信息
        SysTbkAgentApply agentApply = getAgentApply(invitationCode, userId);

        // 2、验证代理用户是否绑定了大淘客平台，并获取代理的AppKey和AppSecret
        String appKey = null;
        String appSecret = null;
        if (agentApply != null) {
            SysAgentUnionRelation dtkRelation = getDtkRelation(agentApply.getId());
            if (dtkRelation == null || dtkRelation.getUnionPlatform() == null) {
                return AjaxResult.error("该代理未分配大淘客联盟信息");
            }
            appKey = dtkRelation.getUnionPlatform().getAppKey();
            appSecret = dtkRelation.getUnionPlatform().getAppSecret();
        }

        // 3、调用大淘客API获取热门商品
        try {
            JSONObject result = dtkApiService.getHotGoods(page, pageSize, keyword, appKey, appSecret);
            return AjaxResult.success(result);
        } catch (Exception e) {
            log.error("获取热门商品失败", e);
            return AjaxResult.error("获取热门商品失败: " + e.getMessage());
        }
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
        
        // 1、根据邀请码或用户ID查询代理信息
        SysTbkAgentApply agentApply = getAgentApply(invitationCode, userId);

        // 2、验证代理用户是否绑定了大淘客平台，并获取代理的AppKey和AppSecret
        String appKey = null;
        String appSecret = null;
        if (agentApply != null) {
            SysAgentUnionRelation dtkRelation = getDtkRelation(agentApply.getId());
            if (dtkRelation == null || dtkRelation.getUnionPlatform() == null) {
                return AjaxResult.error("该代理未分配大淘客联盟信息");
            }
            appKey = dtkRelation.getUnionPlatform().getAppKey();
            appSecret = dtkRelation.getUnionPlatform().getAppSecret();
        }

        // 3、调用大淘客API搜索商品
        try {
            JSONObject result = dtkApiService.searchGoods(keyword, null, page, pageSize, "4", appKey, appSecret);
            return AjaxResult.success(result);
        } catch (Exception e) {
            log.error("搜索商品失败: keyword={}", keyword, e);
            return AjaxResult.error("搜索商品失败: " + e.getMessage());
        }
    }

    /**
     * 高效转链获取淘口令（公开接口）
     */
    @Operation(summary = "高效转链获取淘口令")
    @GetMapping("/goods/tpwd")
    public AjaxResult getPrivilegeLink(
            @RequestParam String goodsId,
            @RequestParam(required = false) String couponId,
            @RequestParam(required = false) String pid,
            @RequestParam(required = false) String goodsName,
            @RequestParam(required = false) String invitationCode,
            @RequestParam(required = false) Long userId) {
        
        // 1、根据邀请码或用户ID查询代理信息
        SysTbkAgentApply agentApply = getAgentApply(invitationCode, userId);

        // 2、验证代理用户是否绑定了大淘客平台，并获取代理的AppKey、AppSecret和PID
        String appKey = null;
        String appSecret = null;
        String agentPid = null;
        if (agentApply != null) {
            SysAgentUnionRelation dtkRelation = getDtkRelation(agentApply.getId());
            if (dtkRelation == null || dtkRelation.getUnionPlatform() == null) {
                return AjaxResult.error("该代理未分配大淘客联盟信息");
            }
            appKey = dtkRelation.getUnionPlatform().getAppKey();
            appSecret = dtkRelation.getUnionPlatform().getAppSecret();
            agentPid = dtkRelation.getUnionPlatform().getAdzoneId(); // 获取代理的推广位ID作为PID
        }

        // 3、如果没有传入pid，使用代理的pid
        if (pid == null || pid.trim().isEmpty()) {
            pid = agentPid;
        }

        // 如果还是为空，则取系统默认
        if (pid == null || pid.trim().isEmpty()) {
            pid = dtkConfig.getAdzoneId().toString();
        }

        // 4、调用大淘客API高效转链获取淘口令
        try {
            JSONObject result = dtkApiService.getPrivilegeLink(goodsId, couponId, pid, goodsName, appKey, appSecret);
            return AjaxResult.success(result);
        } catch (Exception e) {
            log.error("获取淘口令失败: goodsId={}, couponId={}, pid={}, goodsName={}", goodsId, couponId, pid, goodsName, e);
            return AjaxResult.error("获取淘口令失败: " + e.getMessage());
        }
    }

    /**
     * 根据邀请码或用户ID获取代理申请信息
     * @param invitationCode 邀请码
     * @param userId 用户ID
     * @return 代理申请信息，如果未找到或状态非审核通过则返回null
     */
    private SysTbkAgentApply getAgentApply(String invitationCode, Long userId) {
        SysTbkAgentApply agentApply = null;
        
        // 优先使用邀请码查询
        if (invitationCode != null && !invitationCode.trim().isEmpty()) {
            agentApply = tbkAgentApplyService.selectByInvitationCode(invitationCode);
            // 检查代理状态是否为审核通过（状态值为"1"）
            if (agentApply != null && !"1".equals(agentApply.getStatus())) {
                agentApply = null;
            }
        } 
        // 其次使用用户ID查询
        else if (userId != null) {
            SysTbkAgentApply userApply = tbkAgentApplyService.selectByUserId(userId);
            if (userApply != null && "1".equals(userApply.getStatus())) {
                agentApply = userApply;
            }
        }
        
        return agentApply;
    }

    /**
     * 获取代理的大淘客联盟关联信息
     *
     * @param applyId 申请ID
     * @return 大淘客联盟关联信息，如果未分配则返回null
     */
    private SysAgentUnionRelation getDtkRelation(Long applyId) {
        List<SysAgentUnionRelation> relations = agentUnionRelationService.selectByApplyId(applyId);
        return relations.stream()
                .filter(r -> "dtk".equals(r.getPlatformType()) && "1".equals(r.getStatus()))
                .findFirst()
                .orElse(null);
    }
}
