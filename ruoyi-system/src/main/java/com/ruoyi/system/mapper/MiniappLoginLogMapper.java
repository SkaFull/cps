package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.MiniappLoginLog;

import java.util.List;

/**
 * 小程序登录日志 数据层
 *
 * @author ruoyi
 */
public interface MiniappLoginLogMapper {

    /**
     * 根据日志ID查询登录日志
     *
     * @param logId 日志ID
     * @return 登录日志
     */
    MiniappLoginLog selectMiniappLoginLogById(Long logId);

    /**
     * 查询登录日志列表
     *
     * @param loginLog 登录日志
     * @return 登录日志集合
     */
    List<MiniappLoginLog> selectMiniappLoginLogList(MiniappLoginLog loginLog);

    /**
     * 根据用户ID查询登录日志
     *
     * @param userId 用户ID
     * @return 登录日志集合
     */
    List<MiniappLoginLog> selectMiniappLoginLogByUserId(Long userId);

    /**
     * 根据 openId 查询登录日志
     *
     * @param openId 微信openId
     * @return 登录日志集合
     */
    List<MiniappLoginLog> selectMiniappLoginLogByOpenId(String openId);

    /**
     * 新增登录日志
     *
     * @param loginLog 登录日志
     * @return 结果
     */
    int insertMiniappLoginLog(MiniappLoginLog loginLog);

    /**
     * 根据日志ID删除登录日志
     *
     * @param logId 日志ID
     * @return 结果
     */
    int deleteMiniappLoginLogById(Long logId);

    /**
     * 批量删除登录日志
     *
     * @param logIds 需要删除的日志ID数组
     * @return 结果
     */
    int deleteMiniappLoginLogByIds(Long[] logIds);

    /**
     * 清空登录日志
     *
     * @return 结果
     */
    int cleanMiniappLoginLog();
}
