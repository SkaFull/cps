package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.MiniappLoginLog;
import com.ruoyi.system.mapper.MiniappLoginLogMapper;
import com.ruoyi.system.service.IMiniappLoginLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 小程序登录日志Service业务层处理
 *
 * @author ruoyi
 */
@Service
public class MiniappLoginLogServiceImpl implements IMiniappLoginLogService {

    @Autowired
    private MiniappLoginLogMapper miniappLoginLogMapper;

    /**
     * 根据日志ID查询登录日志
     *
     * @param logId 日志ID
     * @return 登录日志
     */
    @Override
    public MiniappLoginLog selectMiniappLoginLogById(Long logId) {
        return miniappLoginLogMapper.selectMiniappLoginLogById(logId);
    }

    /**
     * 查询登录日志列表
     *
     * @param loginLog 登录日志
     * @return 登录日志集合
     */
    @Override
    public List<MiniappLoginLog> selectMiniappLoginLogList(MiniappLoginLog loginLog) {
        return miniappLoginLogMapper.selectMiniappLoginLogList(loginLog);
    }

    /**
     * 根据用户ID查询登录日志
     *
     * @param userId 用户ID
     * @return 登录日志集合
     */
    @Override
    public List<MiniappLoginLog> selectMiniappLoginLogByUserId(Long userId) {
        return miniappLoginLogMapper.selectMiniappLoginLogByUserId(userId);
    }

    /**
     * 根据 openId 查询登录日志
     *
     * @param openId 微信openId
     * @return 登录日志集合
     */
    @Override
    public List<MiniappLoginLog> selectMiniappLoginLogByOpenId(String openId) {
        return miniappLoginLogMapper.selectMiniappLoginLogByOpenId(openId);
    }

    /**
     * 新增登录日志
     *
     * @param loginLog 登录日志
     * @return 结果
     */
    @Override
    public int insertMiniappLoginLog(MiniappLoginLog loginLog) {
        return miniappLoginLogMapper.insertMiniappLoginLog(loginLog);
    }

    /**
     * 根据日志ID删除登录日志
     *
     * @param logId 日志ID
     * @return 结果
     */
    @Override
    public int deleteMiniappLoginLogById(Long logId) {
        return miniappLoginLogMapper.deleteMiniappLoginLogById(logId);
    }

    /**
     * 批量删除登录日志
     *
     * @param logIds 需要删除的日志ID数组
     * @return 结果
     */
    @Override
    public int deleteMiniappLoginLogByIds(Long[] logIds) {
        return miniappLoginLogMapper.deleteMiniappLoginLogByIds(logIds);
    }

    /**
     * 清空登录日志
     *
     * @return 结果
     */
    @Override
    public int cleanMiniappLoginLog() {
        return miniappLoginLogMapper.cleanMiniappLoginLog();
    }
}
