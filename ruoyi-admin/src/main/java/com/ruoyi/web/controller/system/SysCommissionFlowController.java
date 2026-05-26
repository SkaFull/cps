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
import com.ruoyi.system.domain.SysCommissionFlow;
import com.ruoyi.system.service.ISysCommissionFlowService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 佣金流水Controller
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
@RestController
@RequestMapping("/system/commissionFlow")
public class SysCommissionFlowController extends BaseController
{
    @Autowired
    private ISysCommissionFlowService sysCommissionFlowService;

    /**
     * 查询佣金流水列表
     */
    @PreAuthorize("@ss.hasPermi('system:commissionFlow:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysCommissionFlow sysCommissionFlow)
    {
        startPage();
        List<SysCommissionFlow> list = sysCommissionFlowService.selectSysCommissionFlowList(sysCommissionFlow);
        return getDataTable(list);
    }

    /**
     * 导出佣金流水列表
     */
    @PreAuthorize("@ss.hasPermi('system:commissionFlow:export')")
    @Log(title = "佣金流水", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysCommissionFlow sysCommissionFlow)
    {
        List<SysCommissionFlow> list = sysCommissionFlowService.selectSysCommissionFlowList(sysCommissionFlow);
        ExcelUtil<SysCommissionFlow> util = new ExcelUtil<SysCommissionFlow>(SysCommissionFlow.class);
        util.exportExcel(response, list, "佣金流水数据");
    }

    /**
     * 获取佣金流水详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:commissionFlow:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sysCommissionFlowService.selectSysCommissionFlowById(id));
    }

    /**
     * 新增佣金流水
     */
    @PreAuthorize("@ss.hasPermi('system:commissionFlow:add')")
    @Log(title = "佣金流水", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysCommissionFlow sysCommissionFlow)
    {
        return toAjax(sysCommissionFlowService.insertSysCommissionFlow(sysCommissionFlow));
    }

    /**
     * 修改佣金流水
     */
    @PreAuthorize("@ss.hasPermi('system:commissionFlow:edit')")
    @Log(title = "佣金流水", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysCommissionFlow sysCommissionFlow)
    {
        return toAjax(sysCommissionFlowService.updateSysCommissionFlow(sysCommissionFlow));
    }

    /**
     * 删除佣金流水
     */
    @PreAuthorize("@ss.hasPermi('system:commissionFlow:remove')")
    @Log(title = "佣金流水", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysCommissionFlowService.deleteSysCommissionFlowByIds(ids));
    }
}
