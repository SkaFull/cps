package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysAgentCommissionAccountMapper;
import com.ruoyi.system.domain.SysAgentCommissionAccount;
import com.ruoyi.system.service.ISysAgentCommissionAccountService;

/**
 * 佣金账户Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysAgentCommissionAccountServiceImpl implements ISysAgentCommissionAccountService 
{
    @Autowired
    private SysAgentCommissionAccountMapper sysAgentCommissionAccountMapper;

    /**
     * 查询佣金账户
     * 
     * @param id 佣金账户主键
     * @return 佣金账户
     */
    @Override
    public SysAgentCommissionAccount selectSysAgentCommissionAccountById(Long id)
    {
        return sysAgentCommissionAccountMapper.selectSysAgentCommissionAccountById(id);
    }

    /**
     * 根据用户ID查询佣金账户
     * 
     * @param userId 用户ID
     * @return 佣金账户
     */
    @Override
    public SysAgentCommissionAccount selectByUserId(Long userId)
    {
        return sysAgentCommissionAccountMapper.selectByUserId(userId);
    }

    /**
     * 查询佣金账户列表
     * 
     * @param sysAgentCommissionAccount 佣金账户
     * @return 佣金账户
     */
    @Override
    public List<SysAgentCommissionAccount> selectSysAgentCommissionAccountList(SysAgentCommissionAccount sysAgentCommissionAccount)
    {
        return sysAgentCommissionAccountMapper.selectSysAgentCommissionAccountList(sysAgentCommissionAccount);
    }

    /**
     * 新增佣金账户
     * 
     * @param sysAgentCommissionAccount 佣金账户
     * @return 结果
     */
    @Override
    public int insertSysAgentCommissionAccount(SysAgentCommissionAccount sysAgentCommissionAccount)
    {
        return sysAgentCommissionAccountMapper.insertSysAgentCommissionAccount(sysAgentCommissionAccount);
    }

    /**
     * 修改佣金账户
     * 
     * @param sysAgentCommissionAccount 佣金账户
     * @return 结果
     */
    @Override
    public int updateSysAgentCommissionAccount(SysAgentCommissionAccount sysAgentCommissionAccount)
    {
        return sysAgentCommissionAccountMapper.updateSysAgentCommissionAccount(sysAgentCommissionAccount);
    }

    /**
     * 批量删除佣金账户
     * 
     * @param ids 需要删除的佣金账户主键
     * @return 结果
     */
    @Override
    public int deleteSysAgentCommissionAccountByIds(Long[] ids)
    {
        return sysAgentCommissionAccountMapper.deleteSysAgentCommissionAccountByIds(ids);
    }

    /**
     * 删除佣金账户信息
     * 
     * @param id 佣金账户主键
     * @return 结果
     */
    @Override
    public int deleteSysAgentCommissionAccountById(Long id)
    {
        return sysAgentCommissionAccountMapper.deleteSysAgentCommissionAccountById(id);
    }
}
