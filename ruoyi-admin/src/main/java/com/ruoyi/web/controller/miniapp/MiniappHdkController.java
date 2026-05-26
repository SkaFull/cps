package com.ruoyi.web.controller.miniapp;

import cn.hutool.json.JSON;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.SysAgentUnionRelation;
import com.ruoyi.system.domain.SysTbkAgentApply;
import com.ruoyi.system.service.IHdkApiService;
import com.ruoyi.system.service.ISysAgentUnionRelationService;
import com.ruoyi.system.service.ISysTbkAgentApplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小程序好单库Controller
 * 
 * @author ruoyi
 * @date 2026-04-07
 */
@Tag(name = "小程序好单库")
@RestController
@RequestMapping("/miniapp/hdk")
public class MiniappHdkController extends BaseController {

    @Autowired
    private ISysTbkAgentApplyService tbkAgentApplyService;

    @Autowired
    private ISysAgentUnionRelationService agentUnionRelationService;

    @Autowired
    private IHdkApiService hdkApiService;

    /**
     * 获取分类列表（公开接口）
     */
    @Operation(summary = "获取分类列表")
    @GetMapping("/categories")
    public AjaxResult getCategories() {
        try {
            Map<String, String> params = new HashMap<>();
            JSON result = hdkApiService.getCategoryList(params, null);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("获取分类列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取商品列表
     */
    @Operation(summary = "获取商品列表")
    @GetMapping("/goods/list")
    public AjaxResult getGoodsList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String cid,
            @RequestParam(defaultValue = "1") String min_id,
            @RequestParam(required = false) String invitationCode,
            @RequestParam(required = false) Long userId) {
        
        // 1、根据邀请码或用户ID查询代理申请信息
        SysTbkAgentApply agentApply = getAgentApply(invitationCode, userId);

        // 2、验证代理用户是否绑定了好单库平台，并获取代理的ApiKey
        String apiKey = null;
        if (agentApply != null) {
            SysAgentUnionRelation hdkRelation = getHdkRelation(agentApply.getId());
            if (hdkRelation == null || hdkRelation.getUnionPlatform() == null) {
                return AjaxResult.error("该代理未分配好单库联盟信息");
            }
            apiKey = hdkRelation.getUnionPlatform().getAppKey();
        }

        // 3、调用好单库API获取商品列表
        try {
            Map<String, String> params = new HashMap<>();
            if (keyword != null && !keyword.trim().isEmpty()) {
                params.put("keyword", keyword);
            }
            if (cid != null && !cid.trim().isEmpty()) {
                params.put("cid", cid);
            }
            params.put("min_id", min_id);
            
            JSON result = hdkApiService.getItemList(params, apiKey);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("获取商品列表失败: " + e.getMessage());
        }
    }

    /**
     * 获取商品详情
     */
    @Operation(summary = "获取商品详情")
    @GetMapping("/goods/{id}")
    public AjaxResult getGoodsDetail(
            @PathVariable String id,
            @RequestParam(required = false) String invitationCode,
            @RequestParam(required = false) Long userId) {
        
        // 1、根据邀请码或用户ID查询代理信息
        SysTbkAgentApply agentApply = getAgentApply(invitationCode, userId);
        
        // 2、验证代理用户是否绑定了好单库平台，并获取代理的ApiKey
        String apiKey = null;
        if (agentApply != null) {
            SysAgentUnionRelation hdkRelation = getHdkRelation(agentApply.getId());
            if (hdkRelation == null || hdkRelation.getUnionPlatform() == null) {
                return AjaxResult.error("该代理未分配好单库联盟信息");
            }
            apiKey = hdkRelation.getUnionPlatform().getAppKey();
        }
        
        // 3、调用好单库API获取商品详情
        try {
            Map<String, String> params = new HashMap<>();
            params.put("id", id);
            
            JSON result = hdkApiService.getItemDetail(params, apiKey);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("获取商品详情失败: " + e.getMessage());
        }
    }

    /**
     * 获取9.9包邮商品
     */
    @Operation(summary = "获取9.9包邮商品")
    @GetMapping("/goods/nine")
    public AjaxResult getNineNineGoods(
            @RequestParam(defaultValue = "1") String min_id,
            @RequestParam(required = false) String invitationCode,
            @RequestParam(required = false) Long userId) {

        // 1、根据邀请码或用户ID查询代理申请信息
        SysTbkAgentApply agentApply = getAgentApply(invitationCode, userId);

        // 2、验证代理用户是否绑定了好单库平台，并获取代理的ApiKey
        String apiKey = null;
        if (agentApply != null) {
            SysAgentUnionRelation hdkRelation = getHdkRelation(agentApply.getId());
            if (hdkRelation == null || hdkRelation.getUnionPlatform() == null) {
                return AjaxResult.error("该代理未分配好单库联盟信息");
            }
            apiKey = hdkRelation.getUnionPlatform().getAppKey();
        }

        // 3、调用好单库API获取9.9包邮商品
        try {
            Map<String, String> params = new HashMap<>();
            params.put("min_id", min_id);
            
            JSON result = hdkApiService.getNineNine(params, apiKey);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("获取9.9包邮商品失败: " + e.getMessage());
        }
    }

    /**
     * 获取超值大牌商品
     */
    @Operation(summary = "获取超值大牌商品")
    @GetMapping("/goods/brand")
    public AjaxResult getBrandGoods(
            @RequestParam(defaultValue = "1") String min_id,
            @RequestParam(required = false) String invitationCode,
            @RequestParam(required = false) Long userId) {

        // 1、根据邀请码或用户ID查询代理申请信息
        SysTbkAgentApply agentApply = getAgentApply(invitationCode, userId);

        // 2、验证代理用户是否绑定了好单库平台，并获取代理的ApiKey
        String apiKey = null;
        if (agentApply != null) {
            SysAgentUnionRelation hdkRelation = getHdkRelation(agentApply.getId());
            if (hdkRelation == null || hdkRelation.getUnionPlatform() == null) {
                return AjaxResult.error("该代理未分配好单库联盟信息");
            }
            apiKey = hdkRelation.getUnionPlatform().getAppKey();
        }

        // 3、调用好单库API获取超值大牌商品
        try {
            Map<String, String> params = new HashMap<>();
            params.put("min_id", min_id);
            
            JSON result = hdkApiService.getBrandList(params, apiKey);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("获取超值大牌商品失败: " + e.getMessage());
        }
    }

    /**
     * 获取热搜记录
     */
    @Operation(summary = "获取热搜记录")
    @GetMapping("/search/hot")
    public AjaxResult getHotSearch() {
        try {
            Map<String, String> params = new HashMap<>();
            JSON result = hdkApiService.getHotSearch(params, null);
            return AjaxResult.success(result);
        } catch (Exception e) {
            return AjaxResult.error("获取热搜记录失败: " + e.getMessage());
        }
    }

    /**
     * 根据邀请码或用户ID获取代理申请信息
     *
     * @param invitationCode 邀请码
     * @param userId 用户ID
     * @return 代理申请信息，如果未找到或状态不为已审核则返回null
     */
    private SysTbkAgentApply getAgentApply(String invitationCode, Long userId) {
        if (invitationCode != null && !invitationCode.trim().isEmpty()) {
            SysTbkAgentApply agentApply = tbkAgentApplyService.selectByInvitationCode(invitationCode);
            if (agentApply != null && "1".equals(agentApply.getStatus())) {
                return agentApply;
            }
        } else if (userId != null) {
            SysTbkAgentApply userApply = tbkAgentApplyService.selectByUserId(userId);
            if (userApply != null && "1".equals(userApply.getStatus())) {
                return userApply;
            }
        }
        return null;
    }

    /**
     * 获取代理的好单库联盟关联信息
     *
     * @param applyId 申请ID
     * @return 好单库联盟关联信息，如果未分配则返回null
     */
    private SysAgentUnionRelation getHdkRelation(Long applyId) {
        List<SysAgentUnionRelation> relations = agentUnionRelationService.selectByApplyId(applyId);
        return relations.stream()
                .filter(r -> "hdk".equals(r.getPlatformType()) && "1".equals(r.getStatus()))
                .findFirst()
                .orElse(null);
    }
}
