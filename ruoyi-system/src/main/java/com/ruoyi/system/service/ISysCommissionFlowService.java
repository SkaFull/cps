package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysCommissionFlow;

/**
 * 佣金流水Service接口
 * 
 * @author ruoyi
 */
public interface ISysCommissionFlowService 
{
    /**
     * 查询佣金流水
     * 
     * @param id 佣金流水主键
     * @return 佣金流水
     */
    public SysCommissionFlow selectSysCommissionFlowById(Long id);

    /**
     * 查询佣金流水列表
     * 
     * @param sysCommissionFlow 佣金流水
     * @return 佣金流水集合
     */
    public List<SysCommissionFlow> selectSysCommissionFlowList(SysCommissionFlow sysCommissionFlow);

    /**
     * 新增佣金流水
     * 
     * @param sysCommissionFlow 佣金流水
     * @return 结果
     */
    public int insertSysCommissionFlow(SysCommissionFlow sysCommissionFlow);

    /**
     * 修改佣金流水
     * 
     * @param sysCommissionFlow 佣金流水
     * @return 结果
     */
    public int updateSysCommissionFlow(SysCommissionFlow sysCommissionFlow);

    /**
     * 批量删除佣金流水
     * 
     * @param ids 需要删除的佣金流水主键集合
     * @return 结果
     */
    public int deleteSysCommissionFlowByIds(Long[] ids);

    /**
     * 删除佣金流水信息
     * 
     * @param id 佣金流水主键
     * @return 结果
     */
    public int deleteSysCommissionFlowById(Long id);
}
