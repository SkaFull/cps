package com.ruoyi.web.controller.miniapp;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import java.util.Collections;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.domain.SysWechatUser;
import com.ruoyi.system.domain.SysTbkAgentApply;
import com.ruoyi.system.domain.SysUnionPlatform;
import com.ruoyi.system.domain.SysAgentUnionRelation;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.ISysWechatUserService;
import com.ruoyi.system.service.ISysTbkAgentApplyService;
import com.ruoyi.system.service.ISysUnionPlatformService;
import com.ruoyi.system.service.ISysAgentUnionRelationService;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 小程序登录认证Controller
 * 
 * @author ruoyi
 */
@Tag(name = "小程序登录认证")
@RestController
@RequestMapping("/miniapp/auth")
public class MiniappAuthController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(MiniappAuthController.class);

    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private ISysWechatUserService wechatUserService;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ISysTbkAgentApplyService tbkAgentApplyService;

    @Autowired
    private ISysUnionPlatformService unionPlatformService;

    @Autowired
    private ISysAgentUnionRelationService agentUnionRelationService;

    /**
     * 小程序登录接口
     * 
     * @param code 小程序登录凭证code
     * @param encryptedData 加密的用户信息
     * @param iv 加密算法的初始向量
     * @return 登录结果，包含token和用户信息
     */
    @Operation(summary = "小程序登录")
    @PostMapping("/login")
    public AjaxResult login(@RequestParam String code,
                           @RequestParam(required = false) String encryptedData,
                           @RequestParam(required = false) String iv) {
        try {
            // 1. 调用微信接口获取session_key和openid
            WxMaJscode2SessionResult session = wxMaService.getUserService().getSessionInfo(code);
            String openid = session.getOpenid();
            String sessionKey = session.getSessionKey();

            if (StringUtils.isEmpty(openid)) {
                return AjaxResult.error("获取openid失败");
            }

            log.info("小程序登录，openid: {}", openid);

            // 2. 查询是否已存在该微信用户
            SysWechatUser wechatUser = wechatUserService.selectByOpenid(openid);
            SysUser sysUser = null;

            if (wechatUser == null) {
                // 3. 首次登录，创建新用户
                sysUser = createNewUser(openid);
                
                // 4. 创建微信用户关联记录
                wechatUser = new SysWechatUser();
                wechatUser.setUserId(sysUser.getUserId());
                wechatUser.setOpenid(openid);
                
                // 5. 如果提供了加密数据，解密并保存用户信息
                if (StringUtils.isNotEmpty(encryptedData) && StringUtils.isNotEmpty(iv)) {
                    WxMaUserInfo userInfo = wxMaService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
                    wechatUser.setNickname(userInfo.getNickName());
                    wechatUser.setAvatar(userInfo.getAvatarUrl());
                    wechatUser.setGender(userInfo.getGender());
                    wechatUser.setProvince(userInfo.getProvince());
                    wechatUser.setCity(userInfo.getCity());
                    wechatUser.setCountry(userInfo.getCountry());
                    wechatUser.setUnionid(userInfo.getUnionId());
                    
                    // 同步微信昵称到系统用户表
                    if (StringUtils.isNotEmpty(userInfo.getNickName())) {
                        sysUser.setNickName(userInfo.getNickName());
                        userService.updateUser(sysUser);
                    }
                }
                
                wechatUserService.insertSysWechatUser(wechatUser);
            } else {
                // 6. 已存在用户，获取用户信息
                sysUser = userService.selectUserById(wechatUser.getUserId());
                
                // 7. 更新用户信息（如果提供了加密数据）
                if (StringUtils.isNotEmpty(encryptedData) && StringUtils.isNotEmpty(iv)) {
                    WxMaUserInfo userInfo = wxMaService.getUserService().getUserInfo(sessionKey, encryptedData, iv);
                    wechatUser.setNickname(userInfo.getNickName());
                    wechatUser.setAvatar(userInfo.getAvatarUrl());
                    wechatUser.setGender(userInfo.getGender());
                    wechatUser.setProvince(userInfo.getProvince());
                    wechatUser.setCity(userInfo.getCity());
                    wechatUser.setCountry(userInfo.getCountry());
                    if (StringUtils.isNotEmpty(userInfo.getUnionId())) {
                        wechatUser.setUnionid(userInfo.getUnionId());
                    }
                    wechatUserService.updateSysWechatUser(wechatUser);
                    
                    // 同步微信昵称到系统用户表
                    if (StringUtils.isNotEmpty(userInfo.getNickName())) {
                        sysUser.setNickName(userInfo.getNickName());
                        userService.updateUser(sysUser);
                    }
                } else if (StringUtils.isNotEmpty(wechatUser.getNickname())) {
                    // 即使没有提供加密数据，也要同步已有的微信昵称到系统用户表
                    sysUser.setNickName(wechatUser.getNickname());
                    userService.updateUser(sysUser);
                }
            }

            // 8. 查询代理状态
            SysTbkAgentApply agentApply = tbkAgentApplyService.selectByUserId(sysUser.getUserId());
            boolean isAgent = agentApply != null && "1".equals(agentApply.getStatus());
            String invitationCode = (agentApply != null && isAgent) ? agentApply.getInvitationCode() : "";

            // 9. 查询代理用户绑定的联盟平台列表（优化：登录时一并返回，避免二次请求）
            List<SysUnionPlatform> boundPlatforms = new ArrayList<>();
            if (isAgent && agentApply != null) {
                log.info("【登录】代理用户，查询绑定的联盟平台，userId: {}, applyId: {}", sysUser.getUserId(), agentApply.getId());
                boundPlatforms = getUserBoundPlatforms(agentApply.getId());
                log.info("【登录】查询到 {} 个绑定平台", boundPlatforms.size());
            }

            // 10. 生成token
            LoginUser loginUser = new LoginUser(sysUser, Collections.emptySet());
            String token = tokenService.createToken(loginUser);

            // 11. 返回登录结果（包含代理状态和绑定平台列表）
            AjaxResult ajax = AjaxResult.success();
            ajax.put("token", token);
            ajax.put("user", sysUser);
            ajax.put("wechatUser", wechatUser);
            ajax.put("isAgent", isAgent);
            ajax.put("invitationCode", invitationCode);
            ajax.put("boundPlatforms", boundPlatforms); // 新增：绑定的联盟平台列表
            return ajax;

        } catch (Exception e) {
            log.error("小程序登录失败", e);
            return AjaxResult.error("登录失败：" + e.getMessage());
        }
    }

    /**
     * 创建新用户
     */
    private SysUser createNewUser(String openid) {
        SysUser user = new SysUser();
        // 使用openid后8位作为默认用户名
        user.setUserName("wx_" + openid.substring(openid.length() - 8));
        user.setNickName("微信用户");
        // 设置初始密码为123456，使用与PC端相同的加密方式
        user.setPassword(SecurityUtils.encryptPassword("123456"));
        user.setStatus("0"); // 正常状态
        user.setDelFlag("0"); // 未删除
        user.setCreateBy("miniapp");
        
        // 插入用户
        userService.insertUser(user);
        return user;
    }

    /**
     * 获取用户信息
     */
    @Operation(summary = "获取用户信息")
    @GetMapping("/userInfo")
    public AjaxResult getUserInfo() {
        SysUser user = getLoginUser().getUser();
        SysWechatUser wechatUser = wechatUserService.selectByUserId(user.getUserId());
        
        // 查询代理状态
        SysTbkAgentApply agentApply = tbkAgentApplyService.selectByUserId(user.getUserId());
        boolean isAgent = agentApply != null && "1".equals(agentApply.getStatus());
        String invitationCode = (agentApply != null && isAgent) ? agentApply.getInvitationCode() : "";
        
        // 查询代理用户绑定的联盟平台列表
        List<SysUnionPlatform> boundPlatforms = new ArrayList<>();
        if (isAgent && agentApply != null) {
            boundPlatforms = getUserBoundPlatforms(agentApply.getId());
        }
        
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("wechatUser", wechatUser);
        ajax.put("isAgent", isAgent);
        ajax.put("invitationCode", invitationCode);
        ajax.put("boundPlatforms", boundPlatforms);
        return ajax;
    }

    /**
     * 获取用户绑定的联盟平台列表（内部方法）
     * 
     * @param applyId 代理申请ID
     * @return 绑定的联盟平台列表
     */
    private List<SysUnionPlatform> getUserBoundPlatforms(Long applyId) {
        // 1. 查询绑定的联盟关联关系
        List<SysAgentUnionRelation> relations = agentUnionRelationService.selectByApplyId(applyId);
        if (relations == null || relations.isEmpty()) {
            log.info("【获取绑定平台】没有找到联盟关联关系，applyId: {}", applyId);
            return new ArrayList<>();
        }
        
        log.info("【获取绑定平台】找到 {} 条联盟关联关系", relations.size());
        
        // 2. 提取已生效的联盟平台ID列表
        List<Long> platformIds = relations.stream()
                .filter(r -> "1".equals(r.getStatus())) // 只取生效状态的关联
                .map(SysAgentUnionRelation::getUnionPlatformId)
                .distinct()
                .collect(Collectors.toList());
        
        if (platformIds.isEmpty()) {
            log.info("【获取绑定平台】没有生效的联盟关联关系");
            return new ArrayList<>();
        }
        
        log.info("【获取绑定平台】生效的联盟平台ID: {}", platformIds);
        
        // 3. 根据平台ID列表查询联盟平台详情，并过滤is_quote='1'的平台
        List<SysUnionPlatform> platforms = new ArrayList<>();
        for (Long platformId : platformIds) {
            SysUnionPlatform platform = unionPlatformService.selectSysUnionPlatformById(platformId);
            if (platform != null) {
                log.info("【获取绑定平台】平台ID: {}, name: {}, is_quote: {}", 
                        platformId, platform.getPlatformName(), platform.getIsQuote());
                if ("1".equals(platform.getIsQuote())) {
                    platforms.add(platform);
                }
            } else {
                log.warn("【获取绑定平台】未找到平台信息，platformId: {}", platformId);
            }
        }
        
        log.info("【获取绑定平台】最终返回 {} 个平台", platforms.size());
        return platforms;
    }
}
