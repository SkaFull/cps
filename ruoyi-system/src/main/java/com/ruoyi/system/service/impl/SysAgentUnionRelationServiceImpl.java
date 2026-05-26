package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson2.JSONObject;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.pop.request.PddDdkRpPromUrlGenerateRequest;
import com.pdd.pop.sdk.http.api.pop.response.PddDdkRpPromUrlGenerateResponse;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.sign.Md5Utils;
import com.ruoyi.system.domain.SysAgentUnionRelation;
import com.ruoyi.system.domain.SysUnionPlatform;
import com.ruoyi.system.mapper.SysAgentUnionRelationMapper;
import com.ruoyi.system.mapper.SysUnionPlatformMapper;
import com.ruoyi.system.service.ISysAgentUnionRelationService;

/**
 * 代理联盟关联 Service 实现类
 */
@Service
public class SysAgentUnionRelationServiceImpl implements ISysAgentUnionRelationService {

    @Autowired
    private SysAgentUnionRelationMapper relationMapper;

    @Autowired
    private SysUnionPlatformMapper unionPlatformMapper;

    @Override
    public SysAgentUnionRelation selectSysAgentUnionRelationById(Long id) {
        return relationMapper.selectSysAgentUnionRelationById(id);
    }

    @Override
    public SysAgentUnionRelation selectByUserIdAndPlatformType(Long userId, String platformType) {
        return relationMapper.selectByUserIdAndPlatformType(userId, platformType);
    }

    @Override
    public List<SysAgentUnionRelation> selectByUserId(Long userId) {
        return relationMapper.selectByUserId(userId);
    }

    @Override
    public List<SysAgentUnionRelation> selectByApplyId(Long applyId) {
        return relationMapper.selectByApplyId(applyId);
    }

    @Override
    public List<SysAgentUnionRelation> selectSysAgentUnionRelationList(SysAgentUnionRelation relation) {
        return relationMapper.selectSysAgentUnionRelationList(relation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int assignUnionPlatforms(Long applyId, Long userId, Long[] unionPlatformIds) {
        if (unionPlatformIds == null || unionPlatformIds.length == 0) {
            return 0;
        }

        // 1. 查询该申请的旧关联关系，将旧联盟状态改为"未使用"
        List<SysAgentUnionRelation> oldRelations = relationMapper.selectByApplyId(applyId);
        if (oldRelations != null && !oldRelations.isEmpty()) {
            for (SysAgentUnionRelation oldRelation : oldRelations) {
                SysUnionPlatform oldUnionPlatform = unionPlatformMapper.selectSysUnionPlatformById(oldRelation.getUnionPlatformId());
                if (oldUnionPlatform != null) {
                    oldUnionPlatform.setIsQuote("0");
                    oldUnionPlatform.setUpdateBy(SecurityUtils.getUsername());
                    unionPlatformMapper.updateSysUnionPlatform(oldUnionPlatform);
                }
            }
        }

        // 2. 删除该申请的旧关联关系
        relationMapper.deleteSysAgentUnionRelationByApplyId(applyId);

        // 3. 批量创建新的关联关系
        List<SysAgentUnionRelation> relations = new ArrayList<>();
        for (Long unionPlatformId : unionPlatformIds) {
            // 查询联盟信息
            SysUnionPlatform unionPlatform = unionPlatformMapper.selectSysUnionPlatformById(unionPlatformId);
            if (unionPlatform == null) {
                throw new RuntimeException("联盟信息不存在：" + unionPlatformId);
            }

            // 检查是否已被其他用户使用（除了当前用户）
            if ("1".equals(unionPlatform.getIsQuote())) {
                // 查询是否被当前用户使用
                SysAgentUnionRelation existRelation = relationMapper.selectByUserIdAndPlatformType(
                    userId, unionPlatform.getPlatformType());
                if (existRelation != null && !existRelation.getUnionPlatformId().equals(unionPlatformId)) {
                    throw new RuntimeException("联盟信息已被其他用户使用：" + unionPlatform.getPlatformType());
                }
            }

            // 创建关联关系
            SysAgentUnionRelation relation = new SysAgentUnionRelation();
            relation.setUserId(userId);
            relation.setApplyId(applyId);
            relation.setUnionPlatformId(unionPlatformId);
            relation.setPlatformType(unionPlatform.getPlatformType());
            relation.setStatus("1");
            relation.setCreateBy(SecurityUtils.getUsername());
            relations.add(relation);

            // 更新联盟信息状态为"使用中"
            unionPlatform.setIsQuote("1");
            unionPlatform.setUpdateBy(SecurityUtils.getUsername());
            unionPlatformMapper.updateSysUnionPlatform(unionPlatform);
        }

        // 4. 批量插入关联关系
        int count = 0;
        for (SysAgentUnionRelation relation : relations) {
            count += relationMapper.insertSysAgentUnionRelation(relation);
        }

        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int cancelAssignment(Long applyId) {
        // 1. 查询该申请的所有关联关系
        List<SysAgentUnionRelation> relations = relationMapper.selectByApplyId(applyId);
        if (relations == null || relations.isEmpty()) {
            return 0;
        }

        // 2. 将关联的联盟信息状态改为"未使用"
        for (SysAgentUnionRelation relation : relations) {
            SysUnionPlatform unionPlatform = unionPlatformMapper.selectSysUnionPlatformById(relation.getUnionPlatformId());
            if (unionPlatform != null) {
                unionPlatform.setIsQuote("0");
                unionPlatform.setUpdateBy(SecurityUtils.getUsername());
                unionPlatformMapper.updateSysUnionPlatform(unionPlatform);
            }
        }

        // 3. 删除关联关系
        return relationMapper.deleteSysAgentUnionRelationByApplyId(applyId);
    }

    @Override
    public int insertSysAgentUnionRelation(SysAgentUnionRelation relation) {
        return relationMapper.insertSysAgentUnionRelation(relation);
    }

    @Override
    public int updateSysAgentUnionRelation(SysAgentUnionRelation relation) {
        return relationMapper.updateSysAgentUnionRelation(relation);
    }

    @Override
    public int deleteSysAgentUnionRelationById(Long id) {
        return relationMapper.deleteSysAgentUnionRelationById(id);
    }

    @Override
    public int deleteSysAgentUnionRelationByIds(Long[] ids) {
        return relationMapper.deleteSysAgentUnionRelationByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String pddAuthRecord(Long relationId) {
        try {
            // 1. 查询关联信息
            SysAgentUnionRelation relation = relationMapper.selectSysAgentUnionRelationById(relationId);
            if (relation == null) {
                throw new ServiceException("关联信息不存在");
            }

            // 2. 检查是否已授权
            if ("1".equals(relation.getAuthStatus())) {
                throw new ServiceException("该用户已授权，无需重复授权");
            }

            // 3. 查询联盟平台信息
            SysUnionPlatform unionPlatform = unionPlatformMapper.selectSysUnionPlatformById(relation.getUnionPlatformId());
            if (unionPlatform == null) {
                throw new ServiceException("联盟平台信息不存在");
            }

            // 4. 检查是否为PDD平台
            if (!"pdd".equals(unionPlatform.getPlatformType())) {
                throw new ServiceException("只有拼多多联盟支持授权备案");
            }

            // 5. 对用户ID进行MD5加密
            String userIdMd5 = Md5Utils.hash(relation.getUserId().toString());

            // 6. 构建自定义参数
            JSONObject customParams = new JSONObject();
            customParams.put("uid", userIdMd5);
            String customParametersJson = customParams.toJSONString();

            // 7. 创建PopHttpClient（PopClient的具体实现类）
            PopHttpClient client = new PopHttpClient(unionPlatform.getAppKey(), unionPlatform.getAppSecret());

            // 8. 构建请求
            PddDdkRpPromUrlGenerateRequest request = new PddDdkRpPromUrlGenerateRequest();
            request.setChannelType(10);
            request.setCustomParameters(customParametersJson);
            
            // 9. 设置推广位ID列表
            List<String> pIdList = new ArrayList<>();
            pIdList.add(unionPlatform.getAdzoneId());
            request.setPIdList(pIdList);

            // 10. 调用接口
            PddDdkRpPromUrlGenerateResponse response = client.syncInvoke(request);
            if (response == null || response.getErrorResponse() != null) {
                String errorMsg = response != null && response.getErrorResponse() != null 
                    ? response.getErrorResponse().getErrorMsg() 
                    : "调用授权备案接口失败";
                throw new ServiceException("授权备案失败：" + errorMsg);
            }

            // 11. 获取授权URL
            String authUrl = null;
            PddDdkRpPromUrlGenerateResponse.RpPromotionUrlGenerateResponse rpPromotionUrlGenerateResponse = response.getRpPromotionUrlGenerateResponse();
            if ( rpPromotionUrlGenerateResponse != null
                && rpPromotionUrlGenerateResponse.getUrlList() != null
                && !rpPromotionUrlGenerateResponse.getUrlList().isEmpty()) {
                authUrl = rpPromotionUrlGenerateResponse.getUrlList().get(0).getMobileUrl();
                if (authUrl == null || authUrl.isEmpty()) {
                    authUrl = rpPromotionUrlGenerateResponse.getUrlList().get(0).getUrl();
                }
            }

            if (authUrl == null || authUrl.isEmpty()) {
                throw new ServiceException("授权备案成功，但未获取到授权URL");
            }

            // 12. 更新数据库记录，保存授权状态、自定义参数和授权URL
            relation.setAuthStatus("1");
            relation.setAuthUrl(authUrl);
            relation.setCustomParameters(customParametersJson);
            relation.setUpdateBy(SecurityUtils.getUsername());
            relationMapper.updateSysAgentUnionRelation(relation);

            return authUrl;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("授权备案异常：" + e.getMessage());
        }
    }
}
