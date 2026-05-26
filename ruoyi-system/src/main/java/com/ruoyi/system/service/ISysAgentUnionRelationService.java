package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysAgentUnionRelation;

/**
 * 代理联盟关联Service接口
 *
 * @author ruoyi
 */
public interface ISysAgentUnionRelationService {
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
     * 分配联盟信息给代理申请
     * 该方法会：1.创建代理联盟关联记录 2.更新联盟平台状态为已使用
     *
     * @param applyId 申请ID
     * @param userId 用户ID
     * @param unionPlatformIds 联盟平台ID列表
     * @return 结果
     */
    public int assignUnionPlatforms(Long applyId, Long userId, Long[] unionPlatformIds);

    /**
     * 取消代理联盟关联
     * 该方法会：1.删除代理联盟关联记录 2.将联盟平台状态重置为未被应用
     *
     * @param applyId 申请ID
     * @return 结果
     */
    public int cancelAssignment(Long applyId);

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
     * @param ids 需要删除的代理联盟关联主键集合
     * @return 结果
     */
    public int deleteSysAgentUnionRelationByIds(Long[] ids);

    /**
     * PDD授权备案
     * 
     * @param relationId 关联ID
     * @return 授权URL
     */
    public String pddAuthRecord(Long relationId);
}
