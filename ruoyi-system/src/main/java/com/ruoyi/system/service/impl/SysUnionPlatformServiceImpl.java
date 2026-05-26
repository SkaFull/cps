package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.SysUnionPlatform;
import com.ruoyi.system.mapper.SysUnionPlatformMapper;
import com.ruoyi.system.service.ISysUnionPlatformService;

/**
 * 联盟信息 Service 实现类
 */
@Service
public class SysUnionPlatformServiceImpl implements ISysUnionPlatformService {

    @Autowired
    private SysUnionPlatformMapper unionPlatformMapper;

    @Override
    public SysUnionPlatform selectSysUnionPlatformById(Long id) {
        return unionPlatformMapper.selectSysUnionPlatformById(id);
    }

    @Override
    public List<SysUnionPlatform> selectAvailableByPlatformType(String platformType) {
        return unionPlatformMapper.selectAvailableByPlatformType(platformType);
    }

    @Override
    public List<SysUnionPlatform> selectAvailableByTypes(List<String> platformTypes) {
        if (platformTypes == null || platformTypes.isEmpty()) {
            return new ArrayList<>();
        }
        // 查询所有未被引用的联盟账号（is_quote=0表示未被应用）
        SysUnionPlatform query = new SysUnionPlatform();
        query.setIsQuote("0");
        List<SysUnionPlatform> allAvailable = unionPlatformMapper.selectSysUnionPlatformList(query);
        // 过滤出申请的联盟类型
        return allAvailable.stream()
                .filter(platform -> platformTypes.contains(platform.getPlatformType()))
                .collect(Collectors.toList());
    }

    @Override
    public List<SysUnionPlatform> selectSysUnionPlatformList(SysUnionPlatform sysUnionPlatform) {
        return unionPlatformMapper.selectSysUnionPlatformList(sysUnionPlatform);
    }

    @Override
    public int insertSysUnionPlatform(SysUnionPlatform sysUnionPlatform) {
        return unionPlatformMapper.insertSysUnionPlatform(sysUnionPlatform);
    }

    @Override
    public int updateSysUnionPlatform(SysUnionPlatform sysUnionPlatform) {
        return unionPlatformMapper.updateSysUnionPlatform(sysUnionPlatform);
    }

    @Override
    public int deleteSysUnionPlatformById(Long id) {
        return unionPlatformMapper.deleteSysUnionPlatformById(id);
    }

    @Override
    public int deleteSysUnionPlatformByIds(Long[] ids) {
        return unionPlatformMapper.deleteSysUnionPlatformByIds(ids);
    }
}
