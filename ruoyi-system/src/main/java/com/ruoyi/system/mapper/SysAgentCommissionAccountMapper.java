package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysAgentCommissionAccount;

/**
 * 代理佣金账户Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
public interface SysAgentCommissionAccountMapper 
{
    /**
     * 查询代理佣金账户
     * 
     * @param id 代理佣金账户主键
     * @return 代理佣金账户
     */
    public SysAgentCommissionAccount selectSysAgentCommissionAccountById(Long id);

    /**
     * 根据用户ID查询代理佣金账户
     * 
     * @param userId 用户ID
     * @return 代理佣金账户
     */
    public SysAgentCommissionAccount selectSysAgentCommissionAccountByUserId(Long userId);

    /**
     * 根据用户ID查询代理佣金账户（别名方法）
     * 
     * @param userId 用户ID
     * @return 代理佣金账户
     */
    public SysAgentCommissionAccount selectByUserId(Long userId);

    /**
     * 查询代理佣金账户列表
     * 
     * @param sysAgentCommissionAccount 代理佣金账户
     * @return 代理佣金账户集合
     */
    public List<SysAgentCommissionAccount> selectSysAgentCommissionAccountList(SysAgentCommissionAccount sysAgentCommissionAccount);

    /**
     * 新增代理佣金账户
     * 
     * @param sysAgentCommissionAccount 代理佣金账户
     * @return 结果
     */
    public int insertSysAgentCommissionAccount(SysAgentCommissionAccount sysAgentCommissionAccount);

    /**
     * 修改代理佣金账户
     * 
     * @param sysAgentCommissionAccount 代理佣金账户
     * @return 结果
     */
    public int updateSysAgentCommissionAccount(SysAgentCommissionAccount sysAgentCommissionAccount);

    /**
     * 删除代理佣金账户
     * 
     * @param id 代理佣金账户主键
     * @return 结果
     */
    public int deleteSysAgentCommissionAccountById(Long id);

    /**
     * 批量删除代理佣金账户
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysAgentCommissionAccountByIds(Long[] ids);
}
