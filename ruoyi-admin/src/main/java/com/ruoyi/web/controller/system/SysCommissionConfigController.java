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
import com.ruoyi.system.domain.SysCommissionConfig;
import com.ruoyi.system.service.ISysCommissionConfigService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 佣金配置Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/commissionConfig")
public class SysCommissionConfigController extends BaseController
{
    @Autowired
    private ISysCommissionConfigService sysCommissionConfigService;

    /**
     * 查询佣金配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:commissionConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysCommissionConfig sysCommissionConfig)
    {
        startPage();
        List<SysCommissionConfig> list = sysCommissionConfigService.selectSysCommissionConfigList(sysCommissionConfig);
        return getDataTable(list);
    }

    /**
     * 导出佣金配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:commissionConfig:export')")
    @Log(title = "佣金配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysCommissionConfig sysCommissionConfig)
    {
        List<SysCommissionConfig> list = sysCommissionConfigService.selectSysCommissionConfigList(sysCommissionConfig);
        ExcelUtil<SysCommissionConfig> util = new ExcelUtil<SysCommissionConfig>(SysCommissionConfig.class);
        util.exportExcel(response, list, "佣金配置数据");
    }

    /**
     * 获取佣金配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:commissionConfig:query')")
    @GetMapping(value = "/{configKey}")
    public AjaxResult getInfo(@PathVariable("configKey") String configKey)
    {
        return success(sysCommissionConfigService.selectSysCommissionConfigByKey(configKey));
    }

    /**
     * 新增佣金配置
     */
    @PreAuthorize("@ss.hasPermi('system:commissionConfig:add')")
    @Log(title = "佣金配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysCommissionConfig sysCommissionConfig)
    {
        return toAjax(sysCommissionConfigService.insertSysCommissionConfig(sysCommissionConfig));
    }

    /**
     * 修改佣金配置
     */
    @PreAuthorize("@ss.hasPermi('system:commissionConfig:edit')")
    @Log(title = "佣金配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysCommissionConfig sysCommissionConfig)
    {
        return toAjax(sysCommissionConfigService.updateSysCommissionConfig(sysCommissionConfig));
    }

    /**
     * 删除佣金配置
     */
    @PreAuthorize("@ss.hasPermi('system:commissionConfig:remove')")
    @Log(title = "佣金配置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{configKeys}")
    public AjaxResult remove(@PathVariable String[] configKeys)
    {
        return toAjax(sysCommissionConfigService.deleteSysCommissionConfigByKeys(configKeys));
    }
}
