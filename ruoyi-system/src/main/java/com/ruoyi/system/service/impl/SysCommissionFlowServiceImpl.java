package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysCommissionFlowMapper;
import com.ruoyi.system.domain.SysCommissionFlow;
import com.ruoyi.system.service.ISysCommissionFlowService;

/**
 * 佣金流水Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysCommissionFlowServiceImpl implements ISysCommissionFlowService 
{
    @Autowired
    private SysCommissionFlowMapper sysCommissionFlowMapper;

    /**
     * 查询佣金流水
     * 
     * @param id 佣金流水主键
     * @return 佣金流水
     */
    @Override
    public SysCommissionFlow selectSysCommissionFlowById(Long id)
    {
        return sysCommissionFlowMapper.selectSysCommissionFlowById(id);
    }

    /**
     * 查询佣金流水列表
     * 
     * @param sysCommissionFlow 佣金流水
     * @return 佣金流水
     */
    @Override
    public List<SysCommissionFlow> selectSysCommissionFlowList(SysCommissionFlow sysCommissionFlow)
    {
        return sysCommissionFlowMapper.selectSysCommissionFlowList(sysCommissionFlow);
    }

    /**
     * 新增佣金流水
     * 
     * @param sysCommissionFlow 佣金流水
     * @return 结果
     */
    @Override
    public int insertSysCommissionFlow(SysCommissionFlow sysCommissionFlow)
    {
        return sysCommissionFlowMapper.insertSysCommissionFlow(sysCommissionFlow);
    }

    /**
     * 修改佣金流水
     * 
     * @param sysCommissionFlow 佣金流水
     * @return 结果
     */
    @Override
    public int updateSysCommissionFlow(SysCommissionFlow sysCommissionFlow)
    {
        return sysCommissionFlowMapper.updateSysCommissionFlow(sysCommissionFlow);
    }

    /**
     * 批量删除佣金流水
     * 
     * @param ids 需要删除的佣金流水主键
     * @return 结果
     */
    @Override
    public int deleteSysCommissionFlowByIds(Long[] ids)
    {
        return sysCommissionFlowMapper.deleteSysCommissionFlowByIds(ids);
    }

    /**
     * 删除佣金流水信息
     * 
     * @param id 佣金流水主键
     * @return 结果
     */
    @Override
    public int deleteSysCommissionFlowById(Long id)
    {
        return sysCommissionFlowMapper.deleteSysCommissionFlowById(id);
    }
}
