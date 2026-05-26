package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysCommissionConfig;

/**
 * 佣金配置Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
public interface SysCommissionConfigMapper 
{
    /**
     * 查询佣金配置
     * 
     * @param id 佣金配置主键
     * @return 佣金配置
     */
    public SysCommissionConfig selectSysCommissionConfigById(Long id);

    /**
     * 根据配置键查询佣金配置
     * 
     * @param configKey 配置键
     * @return 佣金配置
     */
    public SysCommissionConfig selectSysCommissionConfigByKey(String configKey);

    /**
     * 查询佣金配置列表
     * 
     * @param sysCommissionConfig 佣金配置
     * @return 佣金配置集合
     */
    public List<SysCommissionConfig> selectSysCommissionConfigList(SysCommissionConfig sysCommissionConfig);

    /**
     * 新增佣金配置
     * 
     * @param sysCommissionConfig 佣金配置
     * @return 结果
     */
    public int insertSysCommissionConfig(SysCommissionConfig sysCommissionConfig);

    /**
     * 修改佣金配置
     * 
     * @param sysCommissionConfig 佣金配置
     * @return 结果
     */
    public int updateSysCommissionConfig(SysCommissionConfig sysCommissionConfig);

    /**
     * 删除佣金配置
     * 
     * @param id 佣金配置主键
     * @return 结果
     */
    public int deleteSysCommissionConfigById(Long id);

    /**
     * 批量删除佣金配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysCommissionConfigByIds(Long[] ids);

    /**
     * 根据配置键删除佣金配置
     * 
     * @param configKey 配置键
     * @return 结果
     */
    public int deleteSysCommissionConfigByKey(String configKey);

    /**
     * 根据配置键批量删除佣金配置
     * 
     * @param configKeys 配置键集合
     * @return 结果
     */
    public int deleteSysCommissionConfigByKeys(String[] configKeys);
}
