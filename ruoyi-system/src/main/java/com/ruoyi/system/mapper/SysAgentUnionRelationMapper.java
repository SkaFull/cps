package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysAgentUnionRelation;

/**
 * 代理联盟关联Mapper接口
 *
 * @author ruoyi
 */
public interface SysAgentUnionRelationMapper {
    /**
     * 查询代理联盟关联
     *
     * @param id 代理联盟关联主键
     * @return 代理联盟关联
     */
    public SysAgentUnionRelation selectSysAgentUnionRelationById(Long id);

    /**
     * 根据用户ID和联盟类型查询关联关系
     *
     * @param userId 用户ID
     * @param platformType 联盟类型
     * @return 代理联盟关联
     */
    public SysAgentUnionRelation selectByUserIdAndPlatformType(Long userId, String platformType);

    /**
     * 根据用户ID查询所有关联关系
     *
     * @param userId 用户ID
     * @return 代理联盟关联列表
     */
    public List<SysAgentUnionRelation> selectByUserId(Long userId);

    /**
     * 根据申请ID查询所有关联关系
     *
     * @param applyId 申请ID
     * @return 代理联盟关联列表
     */
    public List<SysAgentUnionRelation> selectByApplyId(Long applyId);

    /**
     * 查询代理联盟关联列表
     *
     * @param sysAgentUnionRelation 代理联盟关联
     * @return 代理联盟关联集合
     */
    public List<SysAgentUnionRelation> selectSysAgentUnionRelationList(SysAgentUnionRelation sysAgentUnionRelation);

    /**
     * 新增代理联盟关联
     *
     * @param sysAgentUnionRelation 代理联盟关联
     * @return 结果
     */
    public int insertSysAgentUnionRelation(SysAgentUnionRelation sysAgentUnionRelation);

    /**
     * 修改代理联盟关联
     *
     * @param sysAgentUnionRelation 代理联盟关联
     * @return 结果
     */
    public int updateSysAgentUnionRelation(SysAgentUnionRelation sysAgentUnionRelation);

    /**
     * 删除代理联盟关联
     *
     * @param id 代理联盟关联主键
     * @return 结果
     */
    public int deleteSysAgentUnionRelationById(Long id);

    /**
     * 批量删除代理联盟关联
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysAgentUnionRelationByIds(Long[] ids);

    /**
     * 根据申请ID删除关联关系
     *
     * @param applyId 申请ID
     * @return 结果
     */
    public int deleteSysAgentUnionRelationByApplyId(Long applyId);
}
