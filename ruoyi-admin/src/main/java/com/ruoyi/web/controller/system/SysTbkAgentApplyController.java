package com.ruoyi.web.controller.system;

import cn.hutool.json.JSONObject;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysTbkAgentApply;
import com.ruoyi.system.domain.SysUnionPlatform;
import com.ruoyi.system.service.ISysTbkAgentApplyService;
import com.ruoyi.system.service.ISysAgentUnionRelationService;
import com.ruoyi.system.service.ISysUnionPlatformService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 淘宝客代理申请管理Controller
 * 
 * @author ruoyi
 */
@Tag(name = "淘宝客代理申请管理")
@RestController
@RequestMapping("/system/tbkApply")
public class SysTbkAgentApplyController extends BaseController {

    @Autowired
    private ISysTbkAgentApplyService tbkAgentApplyService;

    @Autowired
    private ISysAgentUnionRelationService agentUnionRelationService;

    @Autowired
    private ISysUnionPlatformService unionPlatformService;

    /**
     * 查询代理申请列表
     */
    @Operation(summary = "查询代理申请列表")
    @PreAuthorize("@ss.hasPermi('system:tbkApply:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysTbkAgentApply apply) {
        startPage();
        List<SysTbkAgentApply> list = tbkAgentApplyService.selectSysTbkAgentApplyList(apply);
        return getDataTable(list);
    }

    /**
     * 获取代理申请详细信息
     */
    @Operation(summary = "获取代理申请详细信息")
    @PreAuthorize("@ss.hasPermi('system:tbkApply:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        SysTbkAgentApply apply = tbkAgentApplyService.selectSysTbkAgentApplyById(id);
        if (apply == null) {
            return error("申请信息不存在");
        }
        
        // 获取代理等级（默认为1级）
        Integer agentLevel = apply.getAgentLevel() != null ? apply.getAgentLevel() : 1;
        
        // 获取邀请上限
        int inviteLimit = tbkAgentApplyService.getSubordinateLimitByLevel(agentLevel);
        
        // 获取已邀请人数
        int invitedCount = 0;
        if (apply.getInvitationCode() != null && !apply.getInvitationCode().isEmpty()) {
            invitedCount = tbkAgentApplyService.countSubordinatesByInvitationCode(apply.getInvitationCode());
        }
        
        // 计算剩余邀请个数
        int remainingInvites = inviteLimit - invitedCount;
        if (remainingInvites < 0) {
            remainingInvites = 0;
        }
        
        // 构建返回结果
        AjaxResult result = success(apply);
        result.put("agentLevel", agentLevel);
        result.put("inviteLimit", inviteLimit);
        result.put("invitedCount", invitedCount);
        result.put("remainingInvites", remainingInvites);
        
        return result;
    }

    /**
     * 审核代理申请
     */
    @Operation(summary = "审核代理申请")
    @PreAuthorize("@ss.hasPermi('system:tbkApply:audit')")
    @Log(title = "代理申请审核", businessType = BusinessType.UPDATE)
    @PutMapping("/audit/{id}")
    public AjaxResult audit(@PathVariable Long id, @RequestParam String status, @RequestParam(required = false) String remark) {
        SysTbkAgentApply apply = new SysTbkAgentApply();
        apply.setId(id);
        apply.setStatus(status);
        apply.setAuditBy(getUsername());
        apply.setAuditRemark(remark);  // 修复：使用 auditRemark 字段而不是 remark
        
        int result = tbkAgentApplyService.auditApply(apply);
        return toAjax(result);
    }

    /**
     * 修改代理申请状态
     */
    @Operation(summary = "修改代理申请状态")
    @PreAuthorize("@ss.hasPermi('system:tbkApply:edit')")
    @Log(title = "代理申请", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysTbkAgentApply apply) {
        return toAjax(tbkAgentApplyService.changeStatus(apply.getId(), apply.getStatus()));
    }

    /**
     * 删除代理申请
     */
    @Operation(summary = "删除代理申请")
    @PreAuthorize("@ss.hasPermi('system:tbkApply:remove')")
    @Log(title = "代理申请", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tbkAgentApplyService.deleteSysTbkAgentApplyByIds(ids));
    }

    /**
     * 分配联盟信息给代理申请
     */
    @Operation(summary = "分配联盟信息")
    @PreAuthorize("@ss.hasPermi('system:tbkApply:assign')")
    @Log(title = "分配联盟", businessType = BusinessType.UPDATE)
    @PostMapping("/assign/{applyId}")
    public AjaxResult assignUnionPlatforms(@PathVariable Long applyId, @RequestBody Long[] unionPlatformIds) {
        // 查询申请信息获取用户ID
        SysTbkAgentApply apply = tbkAgentApplyService.selectSysTbkAgentApplyById(applyId);
        if (apply == null) {
            return error("申请信息不存在");
        }
        if (unionPlatformIds == null || unionPlatformIds.length == 0) {
            return error("请选择要分配的联盟信息");
        }
        
        int result = agentUnionRelationService.assignUnionPlatforms(applyId, apply.getUserId(), unionPlatformIds);
        return toAjax(result);
    }

    /**
     * 取消代理申请的联盟分配
     */
    @Operation(summary = "取消联盟分配")
    @PreAuthorize("@ss.hasPermi('system:tbkApply:cancel')")
    @Log(title = "取消联盟分配", businessType = BusinessType.UPDATE)
    @DeleteMapping("/cancel/{applyId}")
    public AjaxResult cancelAssignment(@PathVariable Long applyId) {
        int result = agentUnionRelationService.cancelAssignment(applyId);
        return toAjax(result);
    }

    /**
     * 查询代理申请已分配的联盟信息
     */
    @Operation(summary = "查询已分配联盟")
    @PreAuthorize("@ss.hasPermi('system:tbkApply:query')")
    @GetMapping("/assigned/{applyId}")
    public AjaxResult getAssignedUnions(@PathVariable Long applyId) {
        return success(agentUnionRelationService.selectByApplyId(applyId));
    }

    /**
     * 根据申请ID查询可分配的联盟账号
     * 只显示申请中包含的联盟类型且未被引用的账号
     */
    @Operation(summary = "查询可分配联盟")
    @PreAuthorize("@ss.hasPermi('system:tbkApply:query')")
    @GetMapping("/available/{applyId}")
    public AjaxResult getAvailableUnions(@PathVariable Long applyId) {
        // 1. 查询申请信息
        SysTbkAgentApply apply = tbkAgentApplyService.selectSysTbkAgentApplyById(applyId);
        if (apply == null) {
            return error("申请记录不存在");
        }
        
        // 2. 获取申请的联盟类型列表
        List<String> platformTypes = apply.getApplyPlatformTypeList();
        if (platformTypes.isEmpty()) {
            return error("该申请未选择联盟类型");
        }
        
        // 3. 查询可用联盟账号（申请类型 + 未被引用）
        List<SysUnionPlatform> availableUnions = unionPlatformService.selectAvailableByTypes(platformTypes);
        
        return success(availableUnions);
    }

    /**
     * PDD授权备案
     */
    @Operation(summary = "PDD授权备案")
    @PreAuthorize("@ss.hasPermi('system:tbkApply:pddAuth')")
    @Log(title = "PDD授权备案", businessType = BusinessType.UPDATE)
    @PostMapping("/pddAuth/{relationId}")
    public AjaxResult pddAuthRecord(@PathVariable Long relationId) {
        try {
            String authUrl = agentUnionRelationService.pddAuthRecord(relationId);
            JSONObject data = new JSONObject();
            data.put("authUrl", authUrl);
            return success(data);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }
}
