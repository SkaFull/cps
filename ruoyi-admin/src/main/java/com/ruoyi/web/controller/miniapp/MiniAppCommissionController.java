package com.ruoyi.web.controller.miniapp;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.domain.SysAgentCommissionAccount;
import com.ruoyi.system.domain.SysCommissionFlow;
import com.ruoyi.system.service.ISysAgentCommissionAccountService;
import com.ruoyi.system.service.ISysCommissionFlowService;

/**
 * 小程序佣金API Controller
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
@RestController
@RequestMapping("/miniapp/commission")
public class MiniAppCommissionController extends BaseController
{
    @Autowired
    private ISysAgentCommissionAccountService commissionAccountService;

    @Autowired
    private ISysCommissionFlowService commissionFlowService;

    /**
     * 获取当前用户的佣金账户信息
     */
    @GetMapping("/account")
    public AjaxResult getMyAccount()
    {
        Long userId = SecurityUtils.getUserId();
        SysAgentCommissionAccount account = commissionAccountService.selectByUserId(userId);
        
        if (account == null)
        {
            // 如果账户不存在，返回空账户信息
            account = new SysAgentCommissionAccount();
            account.setUserId(userId);
            account.setTotalEarned(java.math.BigDecimal.ZERO);
            account.setSelfEarned(java.math.BigDecimal.ZERO);
            account.setPromotionEarned(java.math.BigDecimal.ZERO);
            account.setBalance(java.math.BigDecimal.ZERO);
            account.setFrozenAmount(java.math.BigDecimal.ZERO);
            account.setWithdrawn(java.math.BigDecimal.ZERO);
        }
        
        return success(account);
    }

    /**
     * 获取当前用户的佣金流水列表
     */
    @GetMapping("/flow/list")
    public TableDataInfo getMyFlowList(SysCommissionFlow commissionFlow)
    {
        Long userId = SecurityUtils.getUserId();
        commissionFlow.setUserId(userId);
        
        startPage();
        List<SysCommissionFlow> list = commissionFlowService.selectSysCommissionFlowList(commissionFlow);
        return getDataTable(list);
    }

    /**
     * 获取当前用户的佣金统计信息
     */
    @GetMapping("/statistics")
    public AjaxResult getMyStatistics()
    {
        Long userId = SecurityUtils.getUserId();
        SysAgentCommissionAccount account = commissionAccountService.selectByUserId(userId);
        
        if (account == null)
        {
            return success(new java.util.HashMap<String, Object>() {{
                put("totalEarned", java.math.BigDecimal.ZERO);
                put("selfEarned", java.math.BigDecimal.ZERO);
                put("promotionEarned", java.math.BigDecimal.ZERO);
                put("availableBalance", java.math.BigDecimal.ZERO);
                put("frozenBalance", java.math.BigDecimal.ZERO);
                put("withdrawnAmount", java.math.BigDecimal.ZERO);
                put("selfRate", 40);
                put("promotionRate", 5);
            }});
        }
        
        // 构建统计信息
        java.util.Map<String, Object> statistics = new java.util.HashMap<>();
        statistics.put("totalEarned", account.getTotalEarned());
        statistics.put("selfEarned", account.getSelfEarned());
        statistics.put("promotionEarned", account.getPromotionEarned());
        statistics.put("availableBalance", account.getBalance());
        statistics.put("frozenBalance", account.getFrozenAmount());
        statistics.put("withdrawnAmount", account.getWithdrawn());
        statistics.put("selfRate", 40); // 自购佣金比例40%
        statistics.put("promotionRate", 5); // 推广佣金比例5%
        
        return success(statistics);
    }

    /**
     * 获取当前用户的收益趋势（最近7天）
     */
    @GetMapping("/trend")
    public AjaxResult getMyTrend()
    {
        Long userId = SecurityUtils.getUserId();
        
        // 查询最近7天的佣金流水
        SysCommissionFlow queryFlow = new SysCommissionFlow();
        queryFlow.setUserId(userId);
        
        List<SysCommissionFlow> flowList = commissionFlowService.selectSysCommissionFlowList(queryFlow);
        
        // 按日期分组统计
        java.util.Map<String, java.math.BigDecimal> dailyEarnings = new java.util.TreeMap<>();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        
        for (SysCommissionFlow flow : flowList)
        {
            if ("income".equals(flow.getFlowType()) && flow.getCreateTime() != null)
            {
                String dateKey = dateFormat.format(flow.getCreateTime());
                dailyEarnings.merge(dateKey, flow.getAmount(), java.math.BigDecimal::add);
            }
        }
        
        return success(dailyEarnings);
    }
}
