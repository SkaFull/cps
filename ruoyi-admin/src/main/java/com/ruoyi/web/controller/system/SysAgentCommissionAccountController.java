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
import com.ruoyi.system.domain.SysAgentCommissionAccount;
import com.ruoyi.system.service.ISysAgentCommissionAccountService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 代理佣金账户Controller
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
@RestController
@RequestMapping("/system/commissionAccount")
public class SysAgentCommissionAccountController extends BaseController
{
    @Autowired
    private ISysAgentCommissionAccountService sysAgentCommissionAccountService;

    /**
     * 查询代理佣金账户列表
     */
    @PreAuthorize("@ss.hasPermi('system:commissionAccount:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysAgentCommissionAccount sysAgentCommissionAccount)
    {
        startPage();
        List<SysAgentCommissionAccount> list = sysAgentCommissionAccountService.selectSysAgentCommissionAccountList(sysAgentCommissionAccount);
        return getDataTable(list);
    }

    /**
     * 导出代理佣金账户列表
     */
    @PreAuthorize("@ss.hasPermi('system:commissionAccount:export')")
    @Log(title = "代理佣金账户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysAgentCommissionAccount sysAgentCommissionAccount)
    {
        List<SysAgentCommissionAccount> list = sysAgentCommissionAccountService.selectSysAgentCommissionAccountList(sysAgentCommissionAccount);
        ExcelUtil<SysAgentCommissionAccount> util = new ExcelUtil<SysAgentCommissionAccount>(SysAgentCommissionAccount.class);
        util.exportExcel(response, list, "代理佣金账户数据");
    }

    /**
     * 获取代理佣金账户详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:commissionAccount:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sysAgentCommissionAccountService.selectSysAgentCommissionAccountById(id));
    }

    /**
     * 根据用户ID查询佣金账户
     */
    @PreAuthorize("@ss.hasPermi('system:commissionAccount:query')")
    @GetMapping(value = "/user/{userId}")
    public AjaxResult getInfoByUserId(@PathVariable("userId") Long userId)
    {
        return success(sysAgentCommissionAccountService.selectByUserId(userId));
    }

    /**
     * 新增代理佣金账户
     */
    @PreAuthorize("@ss.hasPermi('system:commissionAccount:add')")
    @Log(title = "代理佣金账户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysAgentCommissionAccount sysAgentCommissionAccount)
    {
        return toAjax(sysAgentCommissionAccountService.insertSysAgentCommissionAccount(sysAgentCommissionAccount));
    }

    /**
     * 修改代理佣金账户
     */
    @PreAuthorize("@ss.hasPermi('system:commissionAccount:edit')")
    @Log(title = "代理佣金账户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysAgentCommissionAccount sysAgentCommissionAccount)
    {
        return toAjax(sysAgentCommissionAccountService.updateSysAgentCommissionAccount(sysAgentCommissionAccount));
    }

    /**
     * 删除代理佣金账户
     */
    @PreAuthorize("@ss.hasPermi('system:commissionAccount:remove')")
    @Log(title = "代理佣金账户", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysAgentCommissionAccountService.deleteSysAgentCommissionAccountByIds(ids));
    }
}
