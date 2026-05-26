package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysCommissionConfigMapper;
import com.ruoyi.system.domain.SysCommissionConfig;
import com.ruoyi.system.service.ISysCommissionConfigService;

/**
 * 佣金配置Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysCommissionConfigServiceImpl implements ISysCommissionConfigService 
{
    @Autowired
    private SysCommissionConfigMapper sysCommissionConfigMapper;

    /**
     * 查询佣金配置
     * 
     * @param configKey 佣金配置主键
     * @return 佣金配置
     */
    @Override
    public SysCommissionConfig selectSysCommissionConfigByKey(String configKey)
    {
        return sysCommissionConfigMapper.selectSysCommissionConfigByKey(configKey);
    }

    /**
     * 查询佣金配置列表
     * 
     * @param sysCommissionConfig 佣金配置
     * @return 佣金配置
     */
    @Override
    public List<SysCommissionConfig> selectSysCommissionConfigList(SysCommissionConfig sysCommissionConfig)
    {
        return sysCommissionConfigMapper.selectSysCommissionConfigList(sysCommissionConfig);
    }

    /**
     * 新增佣金配置
     * 
     * @param sysCommissionConfig 佣金配置
     * @return 结果
     */
    @Override
    public int insertSysCommissionConfig(SysCommissionConfig sysCommissionConfig)
    {
        return sysCommissionConfigMapper.insertSysCommissionConfig(sysCommissionConfig);
    }

    /**
     * 修改佣金配置
     * 
     * @param sysCommissionConfig 佣金配置
     * @return 结果
     */
    @Override
    public int updateSysCommissionConfig(SysCommissionConfig sysCommissionConfig)
    {
        return sysCommissionConfigMapper.updateSysCommissionConfig(sysCommissionConfig);
    }

    /**
     * 批量删除佣金配置
     * 
     * @param configKeys 需要删除的佣金配置主键
     * @return 结果
     */
    @Override
    public int deleteSysCommissionConfigByKeys(String[] configKeys)
    {
        return sysCommissionConfigMapper.deleteSysCommissionConfigByKeys(configKeys);
    }

    /**
     * 删除佣金配置信息
     * 
     * @param configKey 佣金配置主键
     * @return 结果
     */
    @Override
    public int deleteSysCommissionConfigByKey(String configKey)
    {
        return sysCommissionConfigMapper.deleteSysCommissionConfigByKey(configKey);
    }
}
