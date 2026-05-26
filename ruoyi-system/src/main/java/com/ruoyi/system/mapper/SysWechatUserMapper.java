package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysWechatUser;
import java.util.List;

/**
 * 微信用户关联Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-13
 */
public interface SysWechatUserMapper {
    /**
     * 查询微信用户关联
     * 
     * @param id 微信用户关联主键
     * @return 微信用户关联
     */
    public SysWechatUser selectSysWechatUserById(Long id);

    /**
     * 根据openid查询微信用户
     * 
     * @param openid 微信openid
     * @return 微信用户关联
     */
    public SysWechatUser selectSysWechatUserByOpenid(String openid);

    /**
     * 根据openid查询微信用户（ServiceImpl中使用的方法名）
     * 
     * @param openid 微信openid
     * @return 微信用户关联
     */
    public SysWechatUser selectByOpenid(String openid);

    /**
     * 根据用户ID查询微信用户
     * 
     * @param userId 用户ID
     * @return 微信用户关联
     */
    public SysWechatUser selectSysWechatUserByUserId(Long userId);

    /**
     * 根据用户ID查询微信用户（ServiceImpl中使用的方法名）
     * 
     * @param userId 用户ID
     * @return 微信用户关联
     */
    public SysWechatUser selectByUserId(Long userId);

    /**
     * 根据邀请码查询微信用户
     * 
     * @param invitationCode 邀请码
     * @return 微信用户关联
     */
    public SysWechatUser selectSysWechatUserByInvitationCode(String invitationCode);

    /**
     * 查询微信用户列表
     * 
     * @param sysWechatUser 微信用户
     * @return 微信用户集合
     */
    public List<SysWechatUser> selectSysWechatUserList(SysWechatUser sysWechatUser);

    /**
     * 新增微信用户关联
     * 
     * @param sysWechatUser 微信用户关联
     * @return 结果
     */
    public int insertSysWechatUser(SysWechatUser sysWechatUser);

    /**
     * 修改微信用户关联
     * 
     * @param sysWechatUser 微信用户关联
     * @return 结果
     */
    public int updateSysWechatUser(SysWechatUser sysWechatUser);

    /**
     * 删除微信用户关联
     * 
     * @param id 微信用户关联主键
     * @return 结果
     */
    public int deleteSysWechatUserById(Long id);

    /**
     * 批量删除微信用户关联
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysWechatUserByIds(Long[] ids);
}
