package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysUnionPlatform;

/**
 * 联盟信息Service接口
 *
 * @author ruoyi
 */
public interface ISysUnionPlatformService {
    /**
     * 查询联盟信息
     *
     * @param id 联盟信息主键
     * @return 联盟信息
     */
    public SysUnionPlatform selectSysUnionPlatformById(Long id);

    /**
     * 根据联盟类型查询可用的联盟信息列表
     *
     * @param platformType 联盟类型
     * @return 联盟信息列表
     */
    public List<SysUnionPlatform> selectAvailableByPlatformType(String platformType);

    /**
     * 根据多个联盟类型查询可用的联盟信息列表
     *
     * @param platformTypes 联盟类型列表
     * @return 联盟信息列表
     */
    public List<SysUnionPlatform> selectAvailableByTypes(List<String> platformTypes);

    /**
     * 查询联盟信息列表
     *
     * @param sysUnionPlatform 联盟信息
     * @return 联盟信息集合
     */
    public List<SysUnionPlatform> selectSysUnionPlatformList(SysUnionPlatform sysUnionPlatform);

    /**
     * 新增联盟信息
     *
     * @param sysUnionPlatform 联盟信息
     * @return 结果
     */
    public int insertSysUnionPlatform(SysUnionPlatform sysUnionPlatform);

    /**
     * 修改联盟信息
     *
     * @param sysUnionPlatform 联盟信息
     * @return 结果
     */
    public int updateSysUnionPlatform(SysUnionPlatform sysUnionPlatform);

    /**
     * 删除联盟信息
     *
     * @param id 联盟信息主键
     * @return 结果
     */
    public int deleteSysUnionPlatformById(Long id);

    /**
     * 批量删除联盟信息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysUnionPlatformByIds(Long[] ids);
}
