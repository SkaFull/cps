package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysWechatUser;

/**
 * 微信用户关联Service接口
 * 
 * @author ruoyi
 * @date 2026-03-13
 */
public interface ISysWechatUserService 
{
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
    public SysWechatUser selectByOpenid(String openid);

    /**
     * 根据用户ID查询微信用户
     * 
     * @param userId 用户ID
     * @return 微信用户关联
     */
    public SysWechatUser selectByUserId(Long userId);

    /**
     * 查询微信用户关联列表
     * 
     * @param sysWechatUser 微信用户关联
     * @return 微信用户关联集合
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
     * 批量删除微信用户关联
     * 
     * @param ids 需要删除的微信用户关联主键集合
     * @return 结果
     */
    public int deleteSysWechatUserByIds(Long[] ids);

    /**
     * 删除微信用户关联信息
     * 
     * @param id 微信用户关联主键
     * @return 结果
     */
    public int deleteSysWechatUserById(Long id);
}
