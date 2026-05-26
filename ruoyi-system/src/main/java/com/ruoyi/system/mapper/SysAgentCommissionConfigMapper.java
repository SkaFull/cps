package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysAgentCommissionConfig;

/**
 * 代理佣金配置Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
public interface SysAgentCommissionConfigMapper 
{
    /**
     * 查询代理佣金配置
     * 
     * @param id 代理佣金配置主键
     * @return 代理佣金配置
     */
    public SysAgentCommissionConfig selectSysAgentCommissionConfigById(Long id);

    /**
     * 根据代理层级查询佣金配置
     * 
     * @param agentLevel 代理层级
     * @return 代理佣金配置
     */
    public SysAgentCommissionConfig selectSysAgentCommissionConfigByLevel(Integer agentLevel);

    /**
     * 查询代理佣金配置列表
     * 
     * @param sysAgentCommissionConfig 代理佣金配置
     * @return 代理佣金配置集合
     */
    public List<SysAgentCommissionConfig> selectSysAgentCommissionConfigList(SysAgentCommissionConfig sysAgentCommissionConfig);

    /**
     * 新增代理佣金配置
     * 
     * @param sysAgentCommissionConfig 代理佣金配置
     * @return 结果
     */
    public int insertSysAgentCommissionConfig(SysAgentCommissionConfig sysAgentCommissionConfig);

    /**
     * 修改代理佣金配置
     * 
     * @param sysAgentCommissionConfig 代理佣金配置
     * @return 结果
     */
    public int updateSysAgentCommissionConfig(SysAgentCommissionConfig sysAgentCommissionConfig);

    /**
     * 删除代理佣金配置
     * 
     * @param id 代理佣金配置主键
     * @return 结果
     */
    public int deleteSysAgentCommissionConfigById(Long id);

    /**
     * 批量删除代理佣金配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysAgentCommissionConfigByIds(Long[] ids);
}
