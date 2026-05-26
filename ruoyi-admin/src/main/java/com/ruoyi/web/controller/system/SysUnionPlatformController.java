package com.ruoyi.web.controller.system;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysUnionPlatform;
import com.ruoyi.system.domain.SysAgentUnionRelation;
import com.ruoyi.system.domain.SysTbkAgentApply;
import com.ruoyi.system.service.ISysUnionPlatformService;
import com.ruoyi.system.service.ISysAgentUnionRelationService;
import com.ruoyi.system.service.ISysTbkAgentApplyService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;

/**
 * 联盟信息管理Controller
 * 
 * @author ruoyi
 * @date 2026-04-03
 */
@RestController
@RequestMapping("/system/unionPlatform")
public class SysUnionPlatformController extends BaseController {
    
    @Autowired
    private ISysUnionPlatformService unionPlatformService;
    
    @Autowired
    private ISysAgentUnionRelationService agentUnionRelationService;
    
    @Autowired
    private ISysTbkAgentApplyService tbkAgentApplyService;

    /**
     * 查询联盟信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:unionPlatform:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUnionPlatform sysUnionPlatform) {
        startPage();
        List<SysUnionPlatform> list = unionPlatformService.selectSysUnionPlatformList(sysUnionPlatform);
        return getDataTable(list);
    }

    /**
     * 导出联盟信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:unionPlatform:export')")
    @Log(title = "联盟信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysUnionPlatform sysUnionPlatform) {
        List<SysUnionPlatform> list = unionPlatformService.selectSysUnionPlatformList(sysUnionPlatform);
        ExcelUtil<SysUnionPlatform> util = new ExcelUtil<SysUnionPlatform>(SysUnionPlatform.class);
        util.exportExcel(response, list, "联盟信息数据");
    }

    /**
     * 获取联盟信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:unionPlatform:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(unionPlatformService.selectSysUnionPlatformById(id));
    }

    /**
     * 根据联盟类型查询可用的联盟信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:unionPlatform:query')")
    @GetMapping("/available/{platformType}")
    public AjaxResult getAvailableByType(@PathVariable("platformType") String platformType) {
        List<SysUnionPlatform> list = unionPlatformService.selectAvailableByPlatformType(platformType);
        return success(list);
    }

    /**
     * 获取当前用户绑定的联盟平台列表
     * 用于首页数据筛选：代理用户只能看到自己绑定的联盟类型
     * 
     * 业务逻辑：
     * 1. 先通过userId查询sys_tbk_agent_apply表，获取已生效的代理申请记录
     * 2. 通过申请记录的apply_id查询sys_agent_union_relation表，获取绑定的联盟平台
     * 3. 返回已绑定且生效的联盟平台列表
     */
    @GetMapping("/userBound")
    public AjaxResult getUserBoundPlatforms() {
        Long userId = SecurityUtils.getUserId();
        logger.info("【获取用户绑定平台】当前用户ID: {}", userId);
        
        // 第一步：查询用户的代理申请记录
        SysTbkAgentApply agentApply = tbkAgentApplyService.selectByUserId(userId);
        logger.info("【获取用户绑定平台】代理申请记录: {}, status: {}", 
                agentApply != null ? agentApply.getId() : "null", 
                agentApply != null ? agentApply.getStatus() : "null");
        
        // 如果没有代理申请或申请状态不是"已生效"，返回空列表
        if (agentApply == null || !"1".equals(agentApply.getStatus())) {
            logger.warn("【获取用户绑定平台】没有代理申请或申请未生效，返回空列表");
            return success(new java.util.ArrayList<>());
        }
        
        // 第二步：通过申请ID查询绑定的联盟关联关系
        List<SysAgentUnionRelation> relations = agentUnionRelationService.selectByApplyId(agentApply.getId());
        logger.info("【获取用户绑定平台】联盟关联关系数量: {}", relations != null ? relations.size() : 0);
        
        // 如果没有绑定任何联盟，返回空列表
        if (relations == null || relations.isEmpty()) {
            logger.warn("【获取用户绑定平台】没有绑定任何联盟，返回空列表");
            return success(new java.util.ArrayList<>());
        }
        
        // 第三步：提取已绑定且生效的联盟平台ID列表
        List<Long> platformIds = relations.stream()
                .filter(r -> "1".equals(r.getStatus())) // 只取生效状态的关联
                .map(SysAgentUnionRelation::getUnionPlatformId)
                .distinct()
                .collect(java.util.stream.Collectors.toList());
        logger.info("【获取用户绑定平台】生效的联盟平台ID列表: {}", platformIds);
        
        // 第四步：根据平台ID列表查询联盟平台详情
        if (platformIds.isEmpty()) {
            logger.warn("【获取用户绑定平台】没有生效的联盟平台，返回空列表");
            return success(new java.util.ArrayList<>());
        }
        
        List<SysUnionPlatform> platforms = new java.util.ArrayList<>();
        for (Long platformId : platformIds) {
            SysUnionPlatform platform = unionPlatformService.selectSysUnionPlatformById(platformId);
            logger.info("【获取用户绑定平台】平台ID: {}, 平台信息: {}, is_quote: {}", 
                    platformId, 
                    platform != null ? platform.getPlatformName() : "null",
                    platform != null ? platform.getIsQuote() : "null");
            if (platform != null && "1".equals(platform.getIsQuote())) {
                platforms.add(platform);
            }
        }
        
        logger.info("【获取用户绑定平台】最终返回平台数量: {}", platforms.size());
        return success(platforms);
    }

    /**
     * 新增联盟信息
     */
    @PreAuthorize("@ss.hasPermi('system:unionPlatform:add')")
    @Log(title = "联盟信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysUnionPlatform sysUnionPlatform) {
        return toAjax(unionPlatformService.insertSysUnionPlatform(sysUnionPlatform));
    }

    /**
     * 修改联盟信息
     */
    @PreAuthorize("@ss.hasPermi('system:unionPlatform:edit')")
    @Log(title = "联盟信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysUnionPlatform sysUnionPlatform) {
        return toAjax(unionPlatformService.updateSysUnionPlatform(sysUnionPlatform));
    }

    /**
     * 修改联盟使用状态
     */
    @PreAuthorize("@ss.hasPermi('system:unionPlatform:edit')")
    @Log(title = "联盟信息-修改使用状态", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUnionPlatform sysUnionPlatform) {
        // 参数校验
        if (sysUnionPlatform.getId() == null) {
            return error("联盟ID不能为空");
        }
        if (sysUnionPlatform.getIsQuote() == null) {
            return error("使用状态不能为空");
        }
        
        // 状态值校验：只允许 0-未使用、1-使用中、2-失效
        String isQuote = sysUnionPlatform.getIsQuote();
        if (!"0".equals(isQuote) && !"1".equals(isQuote) && !"2".equals(isQuote)) {
            return error("使用状态值无效，只允许：0-未使用、1-使用中、2-失效");
        }
        
        // 查询联盟信息是否存在
        SysUnionPlatform existingPlatform = unionPlatformService.selectSysUnionPlatformById(sysUnionPlatform.getId());
        if (existingPlatform == null) {
            return error("联盟信息不存在");
        }
        
        // 只更新使用状态
        existingPlatform.setIsQuote(isQuote);
        return toAjax(unionPlatformService.updateSysUnionPlatform(existingPlatform));
    }

    /**
     * 删除联盟信息
     */
    @PreAuthorize("@ss.hasPermi('system:unionPlatform:remove')")
    @Log(title = "联盟信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(unionPlatformService.deleteSysUnionPlatformByIds(ids));
    }
}
