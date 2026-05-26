package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysCommissionFlow;

/**
 * 佣金流水Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
public interface SysCommissionFlowMapper 
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
     * 查询用户的佣金流水列表
     * 
     * @param userId 用户ID
     * @return 佣金流水集合
     */
    public List<SysCommissionFlow> selectSysCommissionFlowListByUserId(Long userId);

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
     * 删除佣金流水
     * 
     * @param id 佣金流水主键
     * @return 结果
     */
    public int deleteSysCommissionFlowById(Long id);

    /**
     * 批量删除佣金流水
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysCommissionFlowByIds(Long[] ids);
}
