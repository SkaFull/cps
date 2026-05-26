package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysTbkAgentApply;

/**
 * 淘宝客代理申请Service接口
 * 
 * @author ruoyi
 * @date 2026-03-13
 */
public interface ISysTbkAgentApplyService 
{
    /**
     * 查询淘宝客代理申请
     * 
     * @param id 淘宝客代理申请主键
     * @return 淘宝客代理申请
     */
    public SysTbkAgentApply selectSysTbkAgentApplyById(Long id);

    /**
     * 根据用户ID查询代理申请
     * 
     * @param userId 用户ID
     * @return 淘宝客代理申请
     */
    public SysTbkAgentApply selectByUserId(Long userId);

    /**
     * 根据小程序AppID查询代理申请
     * 
     * @param miniAppId 小程序AppID
     * @return 淘宝客代理申请
     */
    public SysTbkAgentApply selectByMiniAppId(String miniAppId);

    /**
     * 根据邀请码查询代理申请
     * 
     * @param invitationCode 邀请码
     * @return 淘宝客代理申请
     */
    public SysTbkAgentApply selectByInvitationCode(String invitationCode);

    /**
     * 查询淘宝客代理申请列表
     * 
     * @param sysTbkAgentApply 淘宝客代理申请
     * @return 淘宝客代理申请集合
     */
    public List<SysTbkAgentApply> selectSysTbkAgentApplyList(SysTbkAgentApply sysTbkAgentApply);

    /**
     * 新增淘宝客代理申请
     * 注意：会自动生成唯一邀请码
     * 
     * @param sysTbkAgentApply 淘宝客代理申请
     * @return 结果
     */
    public int insertSysTbkAgentApply(SysTbkAgentApply sysTbkAgentApply);

    /**
     * 修改淘宝客代理申请
     * 
     * @param sysTbkAgentApply 淘宝客代理申请
     * @return 结果
     */
    public int updateSysTbkAgentApply(SysTbkAgentApply sysTbkAgentApply);

    /**
     * 批量删除淘宝客代理申请
     * 
     * @param ids 需要删除的淘宝客代理申请主键集合
     * @return 结果
     */
    public int deleteSysTbkAgentApplyByIds(Long[] ids);

    /**
     * 删除淘宝客代理申请信息
     * 
     * @param id 淘宝客代理申请主键
     * @return 结果
     */
    public int deleteSysTbkAgentApplyById(Long id);

    /**
     * 审核代理申请
     * 
     * @param sysTbkAgentApply 代理申请对象（包含id、status、auditBy、remark）
     * @return 结果
     */
    public int auditApply(SysTbkAgentApply sysTbkAgentApply);

    /**
     * 更改代理状态(启用/禁用)
     * 
     * @param id 申请ID
     * @param status 状态(1-启用,2-禁用)
     * @return 结果
     */
    public int changeStatus(Long id, String status);

    /**
     * 获取指定代理级别的下级数量限制
     * 
     * @param agentLevel 代理级别(1-10)
     * @return 下级数量限制
     */
    public int getSubordinateLimitByLevel(Integer agentLevel);

    /**
     * 统计指定邀请码的下级代理数量
     * 
     * @param invitationCode 邀请码
     * @return 下级代理数量
     */
    public int countSubordinatesByInvitationCode(String invitationCode);

    /**
     * 验证是否可以添加下级代理
     * 
     * @param invitationCode 上级邀请码
     * @return true-可以添加，false-已达上限
     */
    public boolean canAddSubordinate(String invitationCode);

    /**
     * 调整代理级别
     * 
     * @param id 申请ID
     * @param newLevel 新级别(1-10)
     * @return 结果
     */
    public int adjustAgentLevel(Long id, Integer newLevel);
}
