package com.ruoyi.system.service;

import com.ruoyi.system.domain.MiniappSession;

/**
 * 小程序会话Service接口
 *
 * @author ruoyi
 */
public interface IMiniappSessionService {

    /**
     * 根据会话ID查询会话信息
     *
     * @param sessionId 会话ID
     * @return 会话信息
     */
    MiniappSession selectMiniappSessionById(String sessionId);

    /**
     * 根据用户ID查询最新会话信息
     *
     * @param userId 用户ID
     * @return 会话信息
     */
    MiniappSession selectMiniappSessionByUserId(Long userId);

    /**
     * 根据 openId 查询最新会话信息
     *
     * @param openId 微信openId
     * @return 会话信息
     */
    MiniappSession selectMiniappSessionByOpenId(String openId);

    /**
     * 根据访问Token查询会话信息（仅返回未过期的）
     *
     * @param accessToken 访问Token
     * @return 会话信息
     */
    MiniappSession selectMiniappSessionByAccessToken(String accessToken);

    /**
     * 根据刷新Token查询会话信息（仅返回未过期的）
     *
     * @param refreshToken 刷新Token
     * @return 会话信息
     */
    MiniappSession selectMiniappSessionByRefreshToken(String refreshToken);

    /**
     * 新增会话信息
     *
     * @param session 会话信息
     * @return 结果
     */
    int insertMiniappSession(MiniappSession session);

    /**
     * 修改会话信息
     *
     * @param session 会话信息
     * @return 结果
     */
    int updateMiniappSession(MiniappSession session);

    /**
     * 根据会话ID删除会话信息
     *
     * @param sessionId 会话ID
     * @return 结果
     */
    int deleteMiniappSessionById(String sessionId);

    /**
     * 根据用户ID删除会话信息
     *
     * @param userId 用户ID
     * @return 结果
     */
    int deleteMiniappSessionByUserId(Long userId);

    /**
     * 删除已过期的会话
     *
     * @return 结果
     */
    int deleteExpiredSessions();
}
