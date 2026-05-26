package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysOrderCommission;

/**
 * 订单佣金Service接口
 * 
 * @author ruoyi
 */
public interface ISysOrderCommissionService 
{
    /**
     * 查询订单佣金
     * 
     * @param id 订单佣金主键
     * @return 订单佣金
     */
    public SysOrderCommission selectSysOrderCommissionById(Long id);

    /**
     * 根据订单ID和平台类型查询订单佣金
     * 
     * @param orderId 订单ID
     * @param platformType 平台类型
     * @return 订单佣金
     */
    public SysOrderCommission selectSysOrderCommissionByOrderIdAndPlatformType(String orderId, String platformType);

    /**
     * 查询订单佣金列表
     * 
     * @param sysOrderCommission 订单佣金
     * @return 订单佣金集合
     */
    public List<SysOrderCommission> selectSysOrderCommissionList(SysOrderCommission sysOrderCommission);

    /**
     * 新增订单佣金
     * 
     * @param sysOrderCommission 订单佣金
     * @return 结果
     */
    public int insertSysOrderCommission(SysOrderCommission sysOrderCommission);

    /**
     * 修改订单佣金
     * 
     * @param sysOrderCommission 订单佣金
     * @return 结果
     */
    public int updateSysOrderCommission(SysOrderCommission sysOrderCommission);

    /**
     * 批量删除订单佣金
     * 
     * @param ids 需要删除的订单佣金主键集合
     * @return 结果
     */
    public int deleteSysOrderCommissionByIds(Long[] ids);

    /**
     * 删除订单佣金信息
     * 
     * @param id 订单佣金主键
     * @return 结果
     */
    public int deleteSysOrderCommissionById(Long id);
}
