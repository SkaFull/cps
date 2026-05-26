package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysOrderCommission;

/**
 * 订单佣金记录Mapper接口
 * 
 * @author ruoyi
 * @date 2026-04-08
 */
public interface SysOrderCommissionMapper 
{
    /**
     * 查询订单佣金记录
     * 
     * @param id 订单佣金记录主键
     * @return 订单佣金记录
     */
    public SysOrderCommission selectSysOrderCommissionById(Long id);

    /**
     * 根据订单ID和平台类型查询订单佣金记录
     * 
     * @param orderId 订单ID
     * @param platformType 平台类型
     * @return 订单佣金记录
     */
    public SysOrderCommission selectSysOrderCommissionByOrderIdAndPlatformType(String orderId, String platformType);

    /**
     * 查询订单佣金记录列表
     * 
     * @param sysOrderCommission 订单佣金记录
     * @return 订单佣金记录集合
     */
    public List<SysOrderCommission> selectSysOrderCommissionList(SysOrderCommission sysOrderCommission);

    /**
     * 查询用户的订单佣金记录列表
     * 
     * @param userId 用户ID
     * @return 订单佣金记录集合
     */
    public List<SysOrderCommission> selectSysOrderCommissionListByUserId(Long userId);

    /**
     * 新增订单佣金记录
     * 
     * @param sysOrderCommission 订单佣金记录
     * @return 结果
     */
    public int insertSysOrderCommission(SysOrderCommission sysOrderCommission);

    /**
     * 修改订单佣金记录
     * 
     * @param sysOrderCommission 订单佣金记录
     * @return 结果
     */
    public int updateSysOrderCommission(SysOrderCommission sysOrderCommission);

    /**
     * 删除订单佣金记录
     * 
     * @param id 订单佣金记录主键
     * @return 结果
     */
    public int deleteSysOrderCommissionById(Long id);

    /**
     * 批量删除订单佣金记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysOrderCommissionByIds(Long[] ids);
}
