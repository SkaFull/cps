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
import com.ruoyi.system.domain.SysOrderCommission;
import com.ruoyi.system.service.ISysOrderCommissionService;
import com.ruoyi.system.service.ICommissionCalculateService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import java.math.BigDecimal;

/**
 * 订单佣金Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/orderCommission")
public class SysOrderCommissionController extends BaseController
{
    @Autowired
    private ISysOrderCommissionService sysOrderCommissionService;

    @Autowired
    private ICommissionCalculateService commissionCalculateService;

    /**
     * 查询订单佣金列表
     */
    @PreAuthorize("@ss.hasPermi('system:orderCommission:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysOrderCommission sysOrderCommission)
    {
        startPage();
        List<SysOrderCommission> list = sysOrderCommissionService.selectSysOrderCommissionList(sysOrderCommission);
        return getDataTable(list);
    }

    /**
     * 导出订单佣金列表
     */
    @PreAuthorize("@ss.hasPermi('system:orderCommission:export')")
    @Log(title = "订单佣金", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysOrderCommission sysOrderCommission)
    {
        List<SysOrderCommission> list = sysOrderCommissionService.selectSysOrderCommissionList(sysOrderCommission);
        ExcelUtil<SysOrderCommission> util = new ExcelUtil<SysOrderCommission>(SysOrderCommission.class);
        util.exportExcel(response, list, "订单佣金数据");
    }

    /**
     * 获取订单佣金详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:orderCommission:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sysOrderCommissionService.selectSysOrderCommissionById(id));
    }

    /**
     * 新增订单佣金
     */
    @PreAuthorize("@ss.hasPermi('system:orderCommission:add')")
    @Log(title = "订单佣金", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysOrderCommission sysOrderCommission)
    {
        return toAjax(sysOrderCommissionService.insertSysOrderCommission(sysOrderCommission));
    }

    /**
     * 修改订单佣金
     */
    @PreAuthorize("@ss.hasPermi('system:orderCommission:edit')")
    @Log(title = "订单佣金", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysOrderCommission sysOrderCommission)
    {
        return toAjax(sysOrderCommissionService.updateSysOrderCommission(sysOrderCommission));
    }

    /**
     * 删除订单佣金
     */
    @PreAuthorize("@ss.hasPermi('system:orderCommission:remove')")
    @Log(title = "订单佣金", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysOrderCommissionService.deleteSysOrderCommissionByIds(ids));
    }

    /**
     * 订单结算
     */
    @PreAuthorize("@ss.hasPermi('system:orderCommission:settle')")
    @Log(title = "订单结算", businessType = BusinessType.UPDATE)
    @PostMapping("/settle/{id}")
    public AjaxResult settle(@PathVariable Long id, @RequestBody SysOrderCommission orderCommission)
    {
        try
        {
            BigDecimal actualCommission = orderCommission.getActualCommission();
            if (actualCommission == null || actualCommission.compareTo(BigDecimal.ZERO) <= 0)
            {
                return error("实际佣金金额必须大于0");
            }
            boolean result = commissionCalculateService.settleOrder(id, actualCommission);
            return result ? success("结算成功") : error("结算失败");
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }

    /**
     * 订单退款
     */
    @PreAuthorize("@ss.hasPermi('system:orderCommission:refund')")
    @Log(title = "订单退款", businessType = BusinessType.UPDATE)
    @PostMapping("/refund/{id}")
    public AjaxResult refund(@PathVariable Long id)
    {
        try
        {
            boolean result = commissionCalculateService.refundOrder(id);
            return result ? success("退款处理成功") : error("退款处理失败");
        }
        catch (Exception e)
        {
            return error(e.getMessage());
        }
    }
}
