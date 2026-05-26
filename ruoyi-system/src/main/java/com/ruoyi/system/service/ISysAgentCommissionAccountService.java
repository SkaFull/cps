package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysAgentCommissionAccount;

/**
 * 佣金账户Service接口
 * 
 * @author ruoyi
 */
public interface ISysAgentCommissionAccountService 
{
    /**
     * 查询佣金账户
     * 
     * @param id 佣金账户主键
     * @return 佣金账户
     */
    public SysAgentCommissionAccount selectSysAgentCommissionAccountById(Long id);

    /**
     * 根据用户ID查询佣金账户
     * 
     * @param userId 用户ID
     * @return 佣金账户
     */
    public SysAgentCommissionAccount selectByUserId(Long userId);

    /**
     * 查询佣金账户列表
     * 
     * @param sysAgentCommissionAccount 佣金账户
     * @return 佣金账户集合
     */
    public List<SysAgentCommissionAccount> selectSysAgentCommissionAccountList(SysAgentCommissionAccount sysAgentCommissionAccount);

    /**
     * 新增佣金账户
     * 
     * @param sysAgentCommissionAccount 佣金账户
     * @return 结果
     */
    public int insertSysAgentCommissionAccount(SysAgentCommissionAccount sysAgentCommissionAccount);

    /**
     * 修改佣金账户
     * 
     * @param sysAgentCommissionAccount 佣金账户
     * @return 结果
     */
    public int updateSysAgentCommissionAccount(SysAgentCommissionAccount sysAgentCommissionAccount);

    /**
     * 批量删除佣金账户
     * 
     * @param ids 需要删除的佣金账户主键集合
     * @return 结果
     */
    public int deleteSysAgentCommissionAccountByIds(Long[] ids);

    /**
     * 删除佣金账户信息
     * 
     * @param id 佣金账户主键
     * @return 结果
     */
    public int deleteSysAgentCommissionAccountById(Long id);
}
