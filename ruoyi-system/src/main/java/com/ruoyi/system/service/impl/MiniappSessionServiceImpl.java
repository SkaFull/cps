package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.MiniappSession;
import com.ruoyi.system.mapper.MiniappSessionMapper;
import com.ruoyi.system.service.IMiniappSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 小程序会话Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class MiniappSessionServiceImpl implements IMiniappSessionService {

    @Autowired
    private MiniappSessionMapper miniappSessionMapper;

    /**
     * 根据会话ID查询会话信息
     *
     * @param sessionId 会话ID
     * @return 会话信息
     */
    @Override
    public MiniappSession selectMiniappSessionById(String sessionId) {
        return miniappSessionMapper.selectMiniappSessionById(sessionId);
    }

    /**
     * 根据用户ID查询最新会话信息
     *
     * @param userId 用户ID
     * @return 会话信息
     */
    @Override
    public MiniappSession selectMiniappSessionByUserId(Long userId) {
        return miniappSessionMapper.selectMiniappSessionByUserId(userId);
    }

    /**
     * 根据 openId 查询最新会话信息
     *
     * @param openId 微信openId
     * @return 会话信息
     */
    @Override
    public MiniappSession selectMiniappSessionByOpenId(String openId) {
        return miniappSessionMapper.selectMiniappSessionByOpenId(openId);
    }

    /**
     * 根据访问Token查询会话信息（仅返回未过期的）
     *
     * @param accessToken 访问Token
     * @return 会话信息
     */
    @Override
    public MiniappSession selectMiniappSessionByAccessToken(String accessToken) {
        return miniappSessionMapper.selectMiniappSessionByAccessToken(accessToken);
    }

    /**
     * 根据刷新Token查询会话信息（仅返回未过期的）
     *
     * @param refreshToken 刷新Token
     * @return 会话信息
     */
    @Override
    public MiniappSession selectMiniappSessionByRefreshToken(String refreshToken) {
        return miniappSessionMapper.selectMiniappSessionByRefreshToken(refreshToken);
    }

    /**
     * 新增会话信息
     *
     * @param session 会话信息
     * @return 结果
     */
    @Override
    public int insertMiniappSession(MiniappSession session) {
        return miniappSessionMapper.insertMiniappSession(session);
    }

    /**
     * 修改会话信息
     *
     * @param session 会话信息
     * @return 结果
     */
    @Override
    public int updateMiniappSession(MiniappSession session) {
        return miniappSessionMapper.updateMiniappSession(session);
    }

    /**
     * 根据会话ID删除会话信息
     *
     * @param sessionId 会话ID
     * @return 结果
     */
    @Override
    public int deleteMiniappSessionById(String sessionId) {
        return miniappSessionMapper.deleteMiniappSessionById(sessionId);
    }

    /**
     * 根据用户ID删除会话信息
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public int deleteMiniappSessionByUserId(Long userId) {
        return miniappSessionMapper.deleteMiniappSessionByUserId(userId);
    }

    /**
     * 删除已过期的会话
     *
     * @return 结果
     */
    @Override
    public int deleteExpiredSessions() {
        return miniappSessionMapper.deleteExpiredSessions();
    }
}
