package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysCommissionConfig;

/**
 * 佣金配置Service接口
 * 
 * @author ruoyi
 */
public interface ISysCommissionConfigService 
{
    /**
     * 查询佣金配置
     * 
     * @param configKey 佣金配置主键
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
     * 批量删除佣金配置
     * 
     * @param configKeys 需要删除的佣金配置主键集合
     * @return 结果
     */
    public int deleteSysCommissionConfigByKeys(String[] configKeys);

    /**
     * 删除佣金配置信息
     * 
     * @param configKey 佣金配置主键
     * @return 结果
     */
    public int deleteSysCommissionConfigByKey(String configKey);
}
