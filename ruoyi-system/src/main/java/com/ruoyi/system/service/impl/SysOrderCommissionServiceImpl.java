package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysOrderCommissionMapper;
import com.ruoyi.system.domain.SysOrderCommission;
import com.ruoyi.system.service.ISysOrderCommissionService;

/**
 * 订单佣金Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysOrderCommissionServiceImpl implements ISysOrderCommissionService 
{
    @Autowired
    private SysOrderCommissionMapper sysOrderCommissionMapper;

    /**
     * 查询订单佣金
     * 
     * @param id 订单佣金主键
     * @return 订单佣金
     */
    @Override
    public SysOrderCommission selectSysOrderCommissionById(Long id)
    {
        return sysOrderCommissionMapper.selectSysOrderCommissionById(id);
    }

    /**
     * 根据订单ID和平台类型查询订单佣金
     * 
     * @param orderId 订单ID
     * @param platformType 平台类型
     * @return 订单佣金
     */
    @Override
    public SysOrderCommission selectSysOrderCommissionByOrderIdAndPlatformType(String orderId, String platformType)
    {
        return sysOrderCommissionMapper.selectSysOrderCommissionByOrderIdAndPlatformType(orderId, platformType);
    }

    /**
     * 查询订单佣金列表
     * 
     * @param sysOrderCommission 订单佣金
     * @return 订单佣金
     */
    @Override
    public List<SysOrderCommission> selectSysOrderCommissionList(SysOrderCommission sysOrderCommission)
    {
        return sysOrderCommissionMapper.selectSysOrderCommissionList(sysOrderCommission);
    }

    /**
     * 新增订单佣金
     * 
     * @param sysOrderCommission 订单佣金
     * @return 结果
     */
    @Override
    public int insertSysOrderCommission(SysOrderCommission sysOrderCommission)
    {
        return sysOrderCommissionMapper.insertSysOrderCommission(sysOrderCommission);
    }

    /**
     * 修改订单佣金
     * 
     * @param sysOrderCommission 订单佣金
     * @return 结果
     */
    @Override
    public int updateSysOrderCommission(SysOrderCommission sysOrderCommission)
    {
        return sysOrderCommissionMapper.updateSysOrderCommission(sysOrderCommission);
    }

    /**
     * 批量删除订单佣金
     * 
     * @param ids 需要删除的订单佣金主键
     * @return 结果
     */
    @Override
    public int deleteSysOrderCommissionByIds(Long[] ids)
    {
        return sysOrderCommissionMapper.deleteSysOrderCommissionByIds(ids);
    }

    /**
     * 删除订单佣金信息
     * 
     * @param id 订单佣金主键
     * @return 结果
     */
    @Override
    public int deleteSysOrderCommissionById(Long id)
    {
        return sysOrderCommissionMapper.deleteSysOrderCommissionById(id);
    }
}
