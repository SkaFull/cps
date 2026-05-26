package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysTbkAgentApply;

/**
 * 淘宝客代理申请Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-13
 */
public interface SysTbkAgentApplyMapper {
    /**
     * 查询淘宝客代理申请
     * 
     * @param id 淘宝客代理申请主键
     * @return 淘宝客代理申请
     */
    public SysTbkAgentApply selectSysTbkAgentApplyById(Long id);

    /**
     * 根据用户ID查询淘宝客代理申请
     * 
     * @param userId 用户ID
     * @return 淘宝客代理申请
     */
    public SysTbkAgentApply selectSysTbkAgentApplyByUserId(Long userId);

    /**
     * 根据用户ID查询淘宝客代理申请（简化方法名）
     * 
     * @param userId 用户ID
     * @return 淘宝客代理申请
     */
    public SysTbkAgentApply selectByUserId(Long userId);

    /**
     * 根据邀请码查询淘宝客代理申请
     * 
     * @param invitationCode 邀请码
     * @return 淘宝客代理申请
     */
    public SysTbkAgentApply selectSysTbkAgentApplyByInvitationCode(String invitationCode);

    /**
     * 根据邀请码查询淘宝客代理申请（简化方法名）
     * 
     * @param invitationCode 邀请码
     * @return 淘宝客代理申请
     */
    public SysTbkAgentApply selectByInvitationCode(String invitationCode);

    /**
     * 根据小程序AppID查询淘宝客代理申请
     * 
     * @param miniAppId 小程序AppID
     * @return 淘宝客代理申请
     */
    public SysTbkAgentApply selectSysTbkAgentApplyByMiniAppId(String miniAppId);

    /**
     * 根据小程序AppID查询淘宝客代理申请（简化方法名）
     * 
     * @param miniAppId 小程序AppID
     * @return 淘宝客代理申请
     */
    public SysTbkAgentApply selectByMiniAppId(String miniAppId);

    /**
     * 查询淘宝客代理申请列表
     * 
     * @param sysTbkAgentApply 淘宝客代理申请
     * @return 淘宝客代理申请集合
     */
    public List<SysTbkAgentApply> selectSysTbkAgentApplyList(SysTbkAgentApply sysTbkAgentApply);

    /**
     * 新增淘宝客代理申请
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
     * 删除淘宝客代理申请
     * 
     * @param id 淘宝客代理申请主键
     * @return 结果
     */
    public int deleteSysTbkAgentApplyById(Long id);

    /**
     * 批量删除淘宝客代理申请
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysTbkAgentApplyByIds(Long[] ids);

    /**
     * 统计指定邀请码的下级代理数量
     * 
     * @param invitationCode 邀请码
     * @return 下级代理数量
     */
    public int countSubordinatesByInvitationCode(String invitationCode);
}
