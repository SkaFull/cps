package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.InvitationCodeUtil;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.mapper.SysTbkAgentApplyMapper;
import com.ruoyi.system.domain.SysTbkAgentApply;
import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.system.service.ISysTbkAgentApplyService;
import com.ruoyi.system.service.ISysDictTypeService;

/**
 * 淘宝客代理申请Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-13
 */
@Service
public class SysTbkAgentApplyServiceImpl implements ISysTbkAgentApplyService 
{
    @Autowired
    private SysTbkAgentApplyMapper sysTbkAgentApplyMapper;

    @Autowired
    private ISysDictTypeService dictTypeService;

    /**
     * 查询淘宝客代理申请
     * 
     * @param id 淘宝客代理申请主键
     * @return 淘宝客代理申请
     */
    @Override
    public SysTbkAgentApply selectSysTbkAgentApplyById(Long id)
    {
        return sysTbkAgentApplyMapper.selectSysTbkAgentApplyById(id);
    }

    /**
     * 根据用户ID查询代理申请
     * 
     * @param userId 用户ID
     * @return 淘宝客代理申请
     */
    @Override
    public SysTbkAgentApply selectByUserId(Long userId)
    {
        return sysTbkAgentApplyMapper.selectByUserId(userId);
    }

    /**
     * 根据小程序AppID查询代理申请
     * 
     * @param miniAppId 小程序AppID
     * @return 淘宝客代理申请
     */
    @Override
    public SysTbkAgentApply selectByMiniAppId(String miniAppId)
    {
        return sysTbkAgentApplyMapper.selectByMiniAppId(miniAppId);
    }

    /**
     * 根据邀请码查询代理申请
     * 
     * @param invitationCode 邀请码
     * @return 淘宝客代理申请
     */
    @Override
    public SysTbkAgentApply selectByInvitationCode(String invitationCode)
    {
        return sysTbkAgentApplyMapper.selectByInvitationCode(invitationCode);
    }

    /**
     * 查询淘宝客代理申请列表
     * 
     * @param sysTbkAgentApply 淘宝客代理申请
     * @return 淘宝客代理申请
     */
    @Override
    public List<SysTbkAgentApply> selectSysTbkAgentApplyList(SysTbkAgentApply sysTbkAgentApply)
    {
        return sysTbkAgentApplyMapper.selectSysTbkAgentApplyList(sysTbkAgentApply);
    }

    /**
     * 新增淘宝客代理申请
     * 重点：自动生成唯一邀请码
     * 
     * @param sysTbkAgentApply 淘宝客代理申请
     * @return 结果
     */
    @Override
    @Transactional
    public int insertSysTbkAgentApply(SysTbkAgentApply sysTbkAgentApply)
    {
        // 1. 检查用户是否已有申请记录
        SysTbkAgentApply existingApply = sysTbkAgentApplyMapper.selectByUserId(sysTbkAgentApply.getUserId());
        if (existingApply != null)
        {
            throw new ServiceException("您已提交过申请，无需重复提交");
        }

        // 2. 检查小程序AppID是否已被使用
        SysTbkAgentApply existingMiniApp = sysTbkAgentApplyMapper.selectByMiniAppId(sysTbkAgentApply.getMiniAppId());
        if (existingMiniApp != null)
        {
            throw new ServiceException("该小程序AppID已被其他用户使用");
        }

        // 3. 自动生成唯一邀请码（核心逻辑）
        String invitationCode = generateUniqueInvitationCode();
        sysTbkAgentApply.setInvitationCode(invitationCode);

        // 4. 设置默认状态为待审核
        sysTbkAgentApply.setStatus("0");

        // 5. 设置创建时间
        sysTbkAgentApply.setCreateTime(DateUtils.getNowDate());

        // 6. 注意：AppSecret应在Controller层或此处进行加密存储
        // 为了安全，这里建议加密存储AppSecret
        // sysTbkAgentApply.setAppSecret(encryptAppSecret(sysTbkAgentApply.getAppSecret()));

        return sysTbkAgentApplyMapper.insertSysTbkAgentApply(sysTbkAgentApply);
    }

    /**
     * 生成唯一邀请码
     * 使用InvitationCodeUtil工具类生成，并检查数据库唯一性
     * 
     * @return 唯一邀请码
     */
    private String generateUniqueInvitationCode()
    {
        int maxRetry = 10;
        int retry = 0;

        while (retry < maxRetry)
        {
            // 生成随机邀请码
            String code = InvitationCodeUtil.generateCode();

            // 检查数据库中是否已存在
            SysTbkAgentApply existingApply = sysTbkAgentApplyMapper.selectByInvitationCode(code);

            if (existingApply == null)
            {
                // 邀请码唯一，返回
                return code;
            }

            retry++;
        }

        // 达到最大重试次数仍未生成唯一邀请码
        throw new ServiceException("邀请码生成失败，请稍后重试");
    }

    /**
     * 修改淘宝客代理申请
     * 
     * @param sysTbkAgentApply 淘宝客代理申请
     * @return 结果
     */
    @Override
    public int updateSysTbkAgentApply(SysTbkAgentApply sysTbkAgentApply)
    {
        sysTbkAgentApply.setUpdateTime(DateUtils.getNowDate());
        return sysTbkAgentApplyMapper.updateSysTbkAgentApply(sysTbkAgentApply);
    }

    /**
     * 批量删除淘宝客代理申请
     * 
     * @param ids 需要删除的淘宝客代理申请主键
     * @return 结果
     */
    @Override
    public int deleteSysTbkAgentApplyByIds(Long[] ids)
    {
        return sysTbkAgentApplyMapper.deleteSysTbkAgentApplyByIds(ids);
    }

    /**
     * 删除淘宝客代理申请信息
     * 
     * @param id 淘宝客代理申请主键
     * @return 结果
     */
    @Override
    public int deleteSysTbkAgentApplyById(Long id)
    {
        return sysTbkAgentApplyMapper.deleteSysTbkAgentApplyById(id);
    }

    /**
     * 审核代理申请
     * 
     * @param sysTbkAgentApply 代理申请对象（包含id、status、auditBy、remark）
     * @return 结果
     */
    @Override
    @Transactional
    public int auditApply(SysTbkAgentApply sysTbkAgentApply)
    {
        // 1. 查询申请记录
        SysTbkAgentApply apply = sysTbkAgentApplyMapper.selectSysTbkAgentApplyById(sysTbkAgentApply.getId());
        if (apply == null)
        {
            throw new ServiceException("申请记录不存在");
        }

        // 2. 检查申请状态
        if (!"0".equals(apply.getStatus()))
        {
            throw new ServiceException("该申请已审核，无需重复操作");
        }

        // 3. 更新审核信息
        apply.setStatus(sysTbkAgentApply.getStatus());
        apply.setAuditRemark(sysTbkAgentApply.getAuditRemark());
        apply.setAuditTime(DateUtils.getNowDate());
        apply.setAuditBy(sysTbkAgentApply.getAuditBy());
        apply.setUpdateTime(DateUtils.getNowDate());

        return sysTbkAgentApplyMapper.updateSysTbkAgentApply(apply);
    }

    /**
     * 更改代理状态(启用/禁用)
     * 
     * @param id 申请ID
     * @param status 状态(1-启用,2-禁用)
     * @return 结果
     */
    @Override
    @Transactional
    public int changeStatus(Long id, String status)
    {
        // 1. 查询申请记录
        SysTbkAgentApply apply = sysTbkAgentApplyMapper.selectSysTbkAgentApplyById(id);
        if (apply == null)
        {
            throw new ServiceException("申请记录不存在");
        }

        // 2. 只有已生效或已失效的记录才能修改状态
        if ("0".equals(apply.getStatus()))
        {
            throw new ServiceException("待审核的申请不能修改状态");
        }

        // 3. 更新状态
        apply.setStatus(status);
        apply.setUpdateTime(DateUtils.getNowDate());

        return sysTbkAgentApplyMapper.updateSysTbkAgentApply(apply);
    }

    /**
     * 获取指定代理级别的下级数量限制
     * 
     * @param agentLevel 代理级别(1-10)
     * @return 下级数量限制
     */
    @Override
    public int getSubordinateLimitByLevel(Integer agentLevel)
    {
        if (agentLevel == null || agentLevel < 1 || agentLevel > 10)
        {
            throw new ServiceException("代理级别必须在1-10之间");
        }

        // 从字典中获取该级别对应的下级数量限制
        List<SysDictData> dictDataList = dictTypeService.selectDictDataByType("agent_level_subordinate_limit");
        if (dictDataList != null && !dictDataList.isEmpty())
        {
            // 查找匹配的字典数据（dictValue = 级别）
            for (SysDictData dictData : dictDataList)
            {
                if (String.valueOf(agentLevel).equals(dictData.getDictValue()))
                {
                    try
                    {
                        return Integer.parseInt(dictData.getDictLabel());
                    }
                    catch (NumberFormatException e)
                    {
                        // 如果配置值无法解析为数字，返回默认值
                        return agentLevel * 10;
                    }
                }
            }
        }
        
        // 如果字典中没有配置，返回默认值：级别 * 10
        return agentLevel * 10;
    }

    /**
     * 统计指定邀请码的下级代理数量
     * 
     * @param invitationCode 邀请码
     * @return 下级代理数量
     */
    @Override
    public int countSubordinatesByInvitationCode(String invitationCode)
    {
        if (invitationCode == null || invitationCode.trim().isEmpty())
        {
            return 0;
        }
        return sysTbkAgentApplyMapper.countSubordinatesByInvitationCode(invitationCode);
    }

    /**
     * 验证是否可以添加下级代理
     * 
     * @param invitationCode 上级邀请码
     * @return true-可以添加，false-已达上限
     */
    @Override
    public boolean canAddSubordinate(String invitationCode)
    {
        if (invitationCode == null || invitationCode.trim().isEmpty())
        {
            return false;
        }

        // 1. 查询上级代理信息
        SysTbkAgentApply referrer = sysTbkAgentApplyMapper.selectByInvitationCode(invitationCode);
        if (referrer == null)
        {
            throw new ServiceException("上级邀请码不存在");
        }

        // 2. 检查上级代理状态是否为启用
        if (!"1".equals(referrer.getStatus()))
        {
            throw new ServiceException("上级代理未启用，无法添加下级");
        }

        // 3. 检查两级代理限制：如果上级有推荐人，则不能再发展下级
        if (referrer.getReferrerInvitationCode() != null && !referrer.getReferrerInvitationCode().trim().isEmpty())
        {
            throw new ServiceException("系统仅支持两级代理，该代理已有上级，无法再发展下级");
        }

        // 4. 获取上级的级别对应的下级数量限制
        Integer agentLevel = referrer.getAgentLevel();
        if (agentLevel == null)
        {
            agentLevel = 1; // 默认为1级
        }
        int limit = getSubordinateLimitByLevel(agentLevel);

        // 5. 统计当前下级数量
        int currentCount = countSubordinatesByInvitationCode(invitationCode);

        // 6. 判断是否已达上限
        return currentCount < limit;
    }

    /**
     * 调整代理级别
     * 
     * @param id 申请ID
     * @param newLevel 新级别(1-10)
     * @return 结果
     */
    @Override
    @Transactional
    public int adjustAgentLevel(Long id, Integer newLevel)
    {
        if (newLevel == null || newLevel < 1 || newLevel > 10)
        {
            throw new ServiceException("代理级别必须在1-10之间");
        }

        // 1. 查询申请记录
        SysTbkAgentApply apply = sysTbkAgentApplyMapper.selectSysTbkAgentApplyById(id);
        if (apply == null)
        {
            throw new ServiceException("申请记录不存在");
        }

        // 2. 检查申请状态，只有启用状态的代理才能调整级别
        if (!"1".equals(apply.getStatus()))
        {
            throw new ServiceException("只有启用状态的代理才能调整级别");
        }

        // 3. 检查是否是降级（只允许升级）
        Integer currentLevel = apply.getAgentLevel();
        if (currentLevel == null)
        {
            currentLevel = 1;
        }
        if (newLevel <= currentLevel)
        {
            throw new ServiceException("只允许提高代理级别，不允许降级");
        }

        // 4. 更新级别
        apply.setAgentLevel(newLevel);
        apply.setUpdateTime(DateUtils.getNowDate());

        return sysTbkAgentApplyMapper.updateSysTbkAgentApply(apply);
    }
}
