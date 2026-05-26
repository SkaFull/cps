package com.ruoyi.web.controller.miniapp;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysAgentUnionRelation;
import com.ruoyi.system.domain.SysTbkAgentApply;
import com.ruoyi.system.service.ISysAgentUnionRelationService;
import com.ruoyi.system.service.ISysTbkAgentApplyService;
import com.ruoyi.system.service.ITbkSdkService;
import com.ruoyi.union.tbk.config.TbkConfig;
import com.ruoyi.union.tbk.constants.TbkApiConstants;
import com.ruoyi.union.tbk.module.TbkModule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小程序淘宝客代理Controller
 * 
 * @author ruoyi
 */
@Tag(name = "小程序淘宝客代理")
@RestController
@RequestMapping("/miniapp/tbk")
public class MiniappTbkController extends BaseController {

    @Autowired
    private ISysTbkAgentApplyService tbkAgentApplyService;

    @Autowired
    private ISysAgentUnionRelationService agentUnionRelationService;

    @Autowired
    private ITbkSdkService tbkSdkService;

    @Value("${wx.miniapp.appid}")
    private String miniAppId;

    @Resource
    private TbkConfig tbkConfig;

    /**
     * 提交代理申请
     */
    @Operation(summary = "提交代理申请")
    @PostMapping("/agent/apply")
    public AjaxResult applyAgent(@RequestBody SysTbkAgentApply apply) {
        SysUser user = getLoginUser().getUser();
        apply.setUserId(user.getUserId());
        apply.setMiniAppId(miniAppId);
        
        // 验证上级邀请码（如果填写了）
        if (StringUtils.isNotEmpty(apply.getReferrerInvitationCode())) {
            String referrerCode = apply.getReferrerInvitationCode().trim();
            SysTbkAgentApply referrerApply = tbkAgentApplyService.selectByInvitationCode(referrerCode);
            
            // 检查邀请码是否存在
            if (referrerApply == null) {
                return AjaxResult.error("上级邀请码不存在，请检查后重新输入");
            }
            
            // 检查上级代理是否已通过审核（status='1'表示已通过）
            if (!"1".equals(referrerApply.getStatus())) {
                return AjaxResult.error("该邀请码对应的代理尚未通过审核，暂时无法使用");
            }
            
            // 不能填写自己的邀请码
            if (referrerApply.getUserId().equals(user.getUserId())) {
                return AjaxResult.error("不能填写自己的邀请码");
            }
            
            // 两级代理控制：检查上级是否已有上级（系统只支持两级代理）
            if (StringUtils.isNotEmpty(referrerApply.getReferrerInvitationCode())) {
                return AjaxResult.error("系统仅支持两级代理，该代理已有上级，无法再发展下级");
            }
            
            // 检查上级代理的下级数量是否已达上限
            try {
                boolean canAdd = tbkAgentApplyService.canAddSubordinate(referrerCode);
                if (!canAdd) {
                    // 获取上级的级别和限制信息
                    Integer agentLevel = referrerApply.getAgentLevel() != null ? referrerApply.getAgentLevel() : 1;
                    int limit = tbkAgentApplyService.getSubordinateLimitByLevel(agentLevel);
                    return AjaxResult.error("该邀请码的下级代理数量已达上限(" + limit + "个)，无法继续添加");
                }
            } catch (Exception e) {
                return AjaxResult.error(e.getMessage());
            }
            
            apply.setReferrerInvitationCode(referrerCode);
        }
        
        // 设置默认代理级别为1级
        apply.setAgentLevel(1);
        
        // 检查是否已申请
        SysTbkAgentApply existApply = tbkAgentApplyService.selectByUserId(user.getUserId());
        if (existApply != null) {
            // 如果申请被拒绝（status='2'），允许重新提交，更新申请信息
            if ("2".equals(existApply.getStatus())) {
                apply.setId(existApply.getId());
                apply.setInvitationCode(existApply.getInvitationCode()); // 保留原邀请码
                apply.setStatus("0"); // 重置为待审核状态
                apply.setAuditBy(null); // 清空审核人
                apply.setAuditTime(null); // 清空审核时间
                apply.setAuditRemark(null); // 清空审核备注
                
                int result = tbkAgentApplyService.updateSysTbkAgentApply(apply);
                if (result > 0) {
                    AjaxResult ajax = AjaxResult.success("申请已重新提交，请等待审核");
                    ajax.put("invitationCode", existApply.getInvitationCode());
                    return ajax;
                }
                return AjaxResult.error("重新提交失败");
            }
            // 其他状态（待审核或已通过）不允许重复提交
            return AjaxResult.error("您已提交过申请，请勿重复提交");
        }
        
        // 首次申请，新增记录（默认级别为1级）
        apply.setAgentLevel(1);
        int result = tbkAgentApplyService.insertSysTbkAgentApply(apply);
        if (result > 0) {
            // 返回生成的邀请码和级别信息
            SysTbkAgentApply savedApply = tbkAgentApplyService.selectByUserId(user.getUserId());
            AjaxResult ajax = AjaxResult.success("申请提交成功，请等待审核");
            ajax.put("invitationCode", savedApply.getInvitationCode());
            ajax.put("agentLevel", savedApply.getAgentLevel());
            return ajax;
        }
        return AjaxResult.error("申请提交失败");
    }
    
    /**
     * 查询代理申请状态
     */
    @Operation(summary = "查询代理申请状态")
    @GetMapping("/agent/status")
    public AjaxResult getAgentStatus() {
        SysUser user = getLoginUser().getUser();
        SysTbkAgentApply apply = tbkAgentApplyService.selectByUserId(user.getUserId());
        
        if (apply == null) {
            return AjaxResult.success(null);
        }
        
        // 返回申请状态信息
        Map<String, Object> statusMap = new HashMap<>();
        statusMap.put("realName", apply.getRealName());
        statusMap.put("phone", apply.getPhone());
        statusMap.put("wechat", apply.getWechat());
        statusMap.put("remark", apply.getRemark());
        statusMap.put("invitationCode", apply.getInvitationCode());
        statusMap.put("applyPlatformTypes", apply.getApplyPlatformTypes());
        
        // 状态转换：0-待审核(pending), 1-已通过(approved), 2-已拒绝(rejected)
        String status = "pending";
        if ("1".equals(apply.getStatus())) {
            status = "approved";
        } else if ("2".equals(apply.getStatus())) {
            status = "rejected";
        }
        statusMap.put("status", status);
        
        if ("2".equals(apply.getStatus()) && apply.getAuditRemark() != null) {
            statusMap.put("auditRemark", apply.getAuditRemark());
        }
        
        // 添加代理级别信息
        Integer agentLevel = apply.getAgentLevel() != null ? apply.getAgentLevel() : 1;
        statusMap.put("agentLevel", agentLevel);
        
        // 添加邀请下级相关信息（仅对已通过审核的代理）
        if ("1".equals(apply.getStatus()) && StringUtils.isNotEmpty(apply.getInvitationCode())) {
            // 获取该级别的下级数量限制
            int inviteLimit = tbkAgentApplyService.getSubordinateLimitByLevel(agentLevel);
            statusMap.put("inviteLimit", inviteLimit);
            
            // 统计当前已邀请的下级数量
            int invitedCount = tbkAgentApplyService.countSubordinatesByInvitationCode(apply.getInvitationCode());
            statusMap.put("invitedCount", invitedCount);
            
            // 计算剩余可邀请数量
            int remainingInvites = inviteLimit - invitedCount;
            statusMap.put("remainingInvites", remainingInvites > 0 ? remainingInvites : 0);
        }
        
        return AjaxResult.success(statusMap);
    }

    /**
     * 查询当前用户的代理申请信息
     */
    @Operation(summary = "查询代理申请信息")
    @GetMapping("/apply")
    public AjaxResult getApply() {
        SysUser user = getLoginUser().getUser();
        SysTbkAgentApply apply = tbkAgentApplyService.selectByUserId(user.getUserId());
        return AjaxResult.success(apply);
    }

    /**
     * 根据邀请码查询代理信息（公开接口，用于商品查询时确定佣金归属）
     */
    @Operation(summary = "查询邀请码对应的代理信息")
    @GetMapping("/agent/{invitationCode}")
    public AjaxResult getAgentByInvitationCode(@PathVariable String invitationCode) {
        SysTbkAgentApply apply = tbkAgentApplyService.selectByInvitationCode(invitationCode);
        if (apply == null) {
            return AjaxResult.error("邀请码无效");
        }
        
        // 查询已分配的淘宝客联盟信息
        List<SysAgentUnionRelation> relations = agentUnionRelationService.selectByApplyId(apply.getId());
        SysAgentUnionRelation tbkRelation = relations.stream()
                .filter(r -> "tbk".equals(r.getPlatformType()) && "1".equals(r.getStatus()))
                .findFirst()
                .orElse(null);
        
        if (tbkRelation == null || tbkRelation.getUnionPlatform() == null) {
            return AjaxResult.error("该代理未分配淘宝客联盟信息");
        }
        
        // 只返回必要的信息，不返回敏感信息
        AjaxResult ajax = AjaxResult.success();
        ajax.put("appKey", tbkRelation.getUnionPlatform().getAppKey());
        ajax.put("adzoneId", tbkRelation.getUnionPlatform().getAdzoneId());
        ajax.put("status", apply.getStatus());
        return ajax;
    }

    /**
     * 检查当前用户是否为代理
     */
    @Operation(summary = "检查代理状态")
    @GetMapping("/checkAgent")
    public AjaxResult checkAgent() {
        SysUser user = getLoginUser().getUser();
        SysTbkAgentApply apply = tbkAgentApplyService.selectByUserId(user.getUserId());
        
        AjaxResult ajax = AjaxResult.success();
        if (apply == null) {
            ajax.put("isAgent", false);
            ajax.put("message", "未申请代理");
        } else if ("0".equals(apply.getStatus())) {
            ajax.put("isAgent", false);
            ajax.put("message", "代理申请审核中");
            ajax.put("invitationCode", apply.getInvitationCode());
        } else if ("1".equals(apply.getStatus())) {
            ajax.put("isAgent", true);
            ajax.put("message", "已通过代理审核");
            ajax.put("invitationCode", apply.getInvitationCode());
        } else {
            ajax.put("isAgent", false);
            ajax.put("message", "代理申请已拒绝");
        }
        return ajax;
    }

    /**
     * 获取当前用户的代理详细信息
     */
    @Operation(summary = "获取代理信息")
    @GetMapping("/agent/info")
    public AjaxResult getAgentInfo() {
        SysUser user = getLoginUser().getUser();
        SysTbkAgentApply apply = tbkAgentApplyService.selectByUserId(user.getUserId());
        
        if (apply == null) {
            return AjaxResult.error("未申请代理");
        }
        
        return AjaxResult.success(apply);
    }

    /**
     * 获取轮播图列表（公开接口）
     */
    @Operation(summary = "获取轮播图列表")
    @GetMapping("/banners")
    public AjaxResult getBanners() {
        // TODO: 从数据库查询轮播图数据
        // 使用占位图片展示，实际使用时应替换为真实淘宝客物料或本地上传的图片
        List<Map<String, Object>> banners = new ArrayList<>();
        
        // 轮播图1 - 女装促销场景（750x360）
        Map<String, Object> banner1 = new HashMap<>();
        banner1.put("id", 1);
        banner1.put("imageUrl", "https://gd-hbimg.huaban.com/aef32007d9e58d6f91bb9d8fe42e920d96c0cb1c1a5e9-pczwZ7_fw658webp");
        banner1.put("linkType", "category");
        banner1.put("linkId", "16");
        banner1.put("sort", 1);
        banners.add(banner1);
        
        // 轮播图2 - 数码家电场景（750x360）
        Map<String, Object> banner2 = new HashMap<>();
        banner2.put("id", 2);
        banner2.put("imageUrl", "https://gd-hbimg.huaban.com/9f8e1facf36e5f8b9c5f7a7c5b5e8f5e8a3c5e8a1f3e5-jKzX9Y_fw658webp");
        banner2.put("linkType", "category");
        banner2.put("linkId", "50013864");
        banner2.put("sort", 2);
        banners.add(banner2);
        
        // 轮播图3 - 美妆个护场景（750x360）
        Map<String, Object> banner3 = new HashMap<>();
        banner3.put("id", 3);
        banner3.put("imageUrl", "https://gd-hbimg.huaban.com/1a5f3d2e4c5b6a7d8e9f0a1b2c3d4e5f6a7b8c9d0e1f2-mNpQrS_fw658webp");
        banner3.put("linkType", "category");
        banner3.put("linkId", "50010788");
        banner3.put("sort", 3);
        banners.add(banner3);
        
        return AjaxResult.success(banners);
    }

    /**
     * 获取分类列表（公开接口）
     */
    @Operation(summary = "获取分类列表")
    @GetMapping("/categories")
    public AjaxResult getCategories() {
        // TODO: 从数据库查询分类数据
        // 返回分类数据，图标路径由前端管理
        List<Map<String, Object>> categories = new ArrayList<>();
        
        // 分类ID和名称
        int[] categoryIds = {16, 30, 50010788, 50013864, 21, 50016422, 50014812, 50013199};
        String[] categoryNames = {"女装", "男装", "美妆", "数码", "家居", "美食", "母婴", "运动"};
        
        for (int i = 0; i < categoryNames.length; i++) {
            Map<String, Object> category = new HashMap<>();
            category.put("id", categoryIds[i]);
            category.put("name", categoryNames[i]);
            category.put("sort", i + 1);
            categories.add(category);
        }
        return AjaxResult.success(categories);
    }

    /**
     * 获取热门商品列表（公开接口）
     */
    @Operation(summary = "获取热门商品列表")
    @GetMapping("/goods/hot")
    public AjaxResult getHotGoods(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String invitationCode,
            @RequestParam(required = false) Long userId) {

        // 1、根据邀请码查询具体的代理申请信息（必须是已审核通过的）
        SysTbkAgentApply agentApply = null;
        if (invitationCode != null && !invitationCode.trim().isEmpty()) {
            agentApply = tbkAgentApplyService.selectByInvitationCode(invitationCode);
            // 验证代理状态：只有审核通过（status=1）的代理才有效
            if (agentApply != null && !"1".equals(agentApply.getStatus())) {
                agentApply = null; // 未通过审核的代理视为无效
            }
        } else if (userId != null) {
            // 1.1、如果没有邀请码，则根据微信小程序传入的用户ID查询代理申请信息（必须是已审核通过的）
            SysTbkAgentApply userApply = tbkAgentApplyService.selectByUserId(userId);
            // 验证代理状态：只有审核通过（status=1）的代理才有效
            if (userApply != null && "1".equals(userApply.getStatus())) {
                agentApply = userApply;
            }
        }
        // 2、构建请求参数 - 获取热门商品（女装+男装分类，有优惠券，销量排序）
        Map<String, String> params = new HashMap<>();
        
        // 如果有搜索关键词，使用关键词搜索；否则使用分类筛选
        if (keyword != null && !keyword.trim().isEmpty()) {
            params.put("q", keyword.trim()); // 搜索关键词
        } else {
            params.put("cat", "16,30,50008165,1624,50006842,50011740,50010788,1801,50013864"); // 女装和男装分类
        }
        
        params.put("hasCoupon", "true"); // 只要有优惠券的商品
        params.put("sort", "total_sales_des"); // 按销量降序
        params.put("pageNum", page.toString());
        params.put("pageSize", pageSize.toString());
        
        // 3、调用淘宝客API
        if (agentApply != null) {
            // 查询代理的淘宝客联盟信息
            String adzoneId = getTbkAdzoneId(agentApply.getId());
            if (adzoneId != null) {
                params.put("adzoneId", adzoneId);
            } else {
                params.put("adzoneId", tbkConfig.getAdzoneId().toString());
            }
        } else {
            params.put("adzoneId", tbkConfig.getAdzoneId().toString());
        }
        JSONObject resultOb = tbkSdkService.dgMaterialOptionalUpgrade(params);
        
        // 4、返回结果（前端会根据API返回的数据结构进行适配）
        return success(resultOb);
    }

    /**
     * 获取商品详情（公开接口）
     */
    @Operation(summary = "获取商品详情")
    @GetMapping("/goods/{id}")
    public AjaxResult getGoodsDetail(
            @PathVariable String id,
            @RequestParam(required = false) String invitationCode,
            @RequestParam(required = false) Long userId) {
        
        // 1、根据邀请码或用户ID查询代理信息
        SysTbkAgentApply agentApply = null;
        if (invitationCode != null && !invitationCode.trim().isEmpty()) {
            agentApply = tbkAgentApplyService.selectByInvitationCode(invitationCode);
            // 验证代理状态：只有审核通过（status=1）的代理才有效
            if (agentApply != null && !"1".equals(agentApply.getStatus())) {
                agentApply = null;
            }
        } else if (userId != null) {
            SysTbkAgentApply userApply = tbkAgentApplyService.selectByUserId(userId);
            // 验证代理状态：只有审核通过（status=1）的代理才有效
            if (userApply != null && "1".equals(userApply.getStatus())) {
                agentApply = userApply;
            }
        }
        
        // 2、先调用getItemInfo获取商品基本信息（包括标题）
        JSONObject itemInfo;
        if (agentApply != null) {
            // 查询代理的淘宝客联盟信息
            SysAgentUnionRelation tbkRelation = getTbkRelation(agentApply.getId());
            if (tbkRelation != null && tbkRelation.getUnionPlatform() != null) {
                itemInfo = tbkSdkService.getItemInfo(id, 
                    tbkRelation.getUnionPlatform().getAppKey(), 
                    tbkRelation.getUnionPlatform().getAppSecret());
            } else {
                itemInfo = tbkSdkService.getItemInfo(id);
            }
        } else {
            itemInfo = tbkSdkService.getItemInfo(id);
        }
        
        // 3、检查商品是否存在
        if (itemInfo == null || itemInfo.isEmpty()) {
            return AjaxResult.error("商品不存在或已下架");
        }
        
        String title = itemInfo.getString("title");
        if (title == null || title.trim().isEmpty()) {
            return AjaxResult.error("商品信息不完整");
        }
        
        // 4、使用商品标题作为关键词调用dgMaterialOptionalUpgrade获取推广链接
        Map<String, String> params = new HashMap<>();
        params.put("q", title); // 使用商品标题作为搜索关键词
        params.put("pageSize", "20"); // 获取多个结果以便匹配
        
        if (agentApply != null) {
            // 查询代理的淘宝客联盟信息
            String adzoneId = getTbkAdzoneId(agentApply.getId());
            if (adzoneId != null) {
                params.put("adzoneId", adzoneId);
            } else {
                params.put("adzoneId", tbkConfig.getAdzoneId().toString());
            }
        } else {
            params.put("adzoneId", tbkConfig.getAdzoneId().toString());
        }
        JSONObject resultOb = tbkSdkService.dgMaterialOptionalUpgrade(params);
        
        // 5、在返回结果中查找匹配的商品（通过itemId匹配）
        JSONObject matchedItem = null;
        if (resultOb != null && resultOb.containsKey("resultList")) {
            JSONArray resultList = resultOb.getJSONArray("resultList");
            if (resultList != null && !resultList.isEmpty()) {
                // 遍历结果，找到itemId匹配的商品
                for (int i = 0; i < resultList.size(); i++) {
                    JSONObject item = resultList.getJSONObject(i);
                    String itemId = item.getString("itemId");
                    if (id.equals(itemId)) {
                        matchedItem = item;
                        break;
                    }
                }
            }
        }
        
        // 6、如果找到了匹配的商品（含推广链接），返回该商品
        if (matchedItem != null) {
            return AjaxResult.success(matchedItem);
        }
        
        // 7、如果没找到匹配的商品（可能是该商品不支持推广），返回基本信息
        // 将getItemInfo的数据结构转换为与dgMaterialOptionalUpgrade相同的结构
        JSONObject result = new JSONObject();
        result.put("itemId", itemInfo.getString("numIid"));
        
        // 构建itemBasicInfo
        JSONObject itemBasicInfo = new JSONObject();
        itemBasicInfo.put("title", itemInfo.getString("title"));
        itemBasicInfo.put("pictUrl", itemInfo.getString("pictUrl"));
        
        // 处理smallImages：从JSONArray转换为逗号分隔的字符串
        JSONArray smallImagesArray = itemInfo.getJSONArray("smallImages");
        String smallImagesStr = "";
        if (smallImagesArray != null && smallImagesArray.size() > 0) {
            List<String> imageList = new ArrayList<>();
            for (int i = 0; i < smallImagesArray.size(); i++) {
                imageList.add(smallImagesArray.getString(i));
            }
            smallImagesStr = String.join(",", imageList);
        }
        itemBasicInfo.put("smallImages", smallImagesStr);
        
        itemBasicInfo.put("shopTitle", itemInfo.getString("shopTitle"));
        itemBasicInfo.put("categoryName", itemInfo.getString("categoryName"));
        itemBasicInfo.put("provcity", itemInfo.getString("provcity"));
        result.put("itemBasicInfo", itemBasicInfo);
        
        // 构建pricePromotionInfo
        JSONObject pricePromotionInfo = new JSONObject();
        pricePromotionInfo.put("zkFinalPrice", itemInfo.getString("zkFinalPrice"));
        pricePromotionInfo.put("finalPromotionPrice", itemInfo.getString("zkFinalPrice"));
        result.put("pricePromotionInfo", pricePromotionInfo);
        
        // 构建publishInfo（没有推广链接）
        JSONObject publishInfo = new JSONObject();
        publishInfo.put("clickUrl", itemInfo.getString("itemUrl")); // 使用普通商品链接
        result.put("publishInfo", publishInfo);
        
        return AjaxResult.success(result);
    }

    /**
     * 搜索商品（公开接口）
     */
    @Operation(summary = "搜索商品")
    @GetMapping("/goods/search")
    public AjaxResult searchGoods(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) String invitationCode) {
        
        // 1、根据邀请码查询代理信息
        SysTbkAgentApply agentApply = null;
        if (invitationCode != null && !invitationCode.trim().isEmpty()) {
            agentApply = tbkAgentApplyService.selectByInvitationCode(invitationCode);
            if (agentApply != null && !"1".equals(agentApply.getStatus())) {
                agentApply = null;
            }
        }

        // 2、构建请求参数 - 根据关键词搜索商品
        Map<String, String> params = new HashMap<>();
        params.put("q", keyword); // 搜索关键词
        params.put("hasCoupon", "true"); // 只要有优惠券的商品
        params.put("sort", "total_sales_des"); // 按销量降序
        params.put("pageNum", page.toString());
        params.put("pageSize", pageSize.toString());
        
        // 3、调用淘宝客API
        if (agentApply != null) {
            // 查询代理的淘宝客联盟信息
            String adzoneId = getTbkAdzoneId(agentApply.getId());
            if (adzoneId != null) {
                params.put("adzoneId", adzoneId);
            } else {
                params.put("adzoneId", tbkConfig.getAdzoneId().toString());
            }
        } else {
            params.put("adzoneId", tbkConfig.getAdzoneId().toString());
        }
        JSONObject resultOb = tbkSdkService.dgMaterialOptionalUpgrade(params);
        
        // 4、返回结果（前端会根据API返回的数据结构进行适配）
        return success(resultOb);
    }

    /**
     * 根据分类获取商品列表（公开接口）
     */
    @Operation(summary = "根据分类或者关键字获取商品")
    @GetMapping("/goods/list")
    public AjaxResult getGoodsByCategory(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) String invitationCode,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "total_sales_des") String sort) {
        // 1、根据邀请码查询具体的代理申请信息（必须是已审核通过的）
        SysTbkAgentApply agentApply = null;
        if (invitationCode != null && !invitationCode.trim().isEmpty()) {
            agentApply = tbkAgentApplyService.selectByInvitationCode(invitationCode);
            // 验证代理状态：只有审核通过（status=1）的代理才有效
            if (agentApply != null && !"1".equals(agentApply.getStatus())) {
                agentApply = null; // 未通过审核的代理视为无效
            }
        } else if (userId != null) {
            // 1.1、如果没有邀请码，则根据微信小程序传入的用户ID查询代理申请信息（必须是已审核通过的）
            SysTbkAgentApply userApply = tbkAgentApplyService.selectByUserId(userId);
            // 验证代理状态：只有审核通过（status=1）的代理才有效
            if (userApply != null && "1".equals(userApply.getStatus())) {
                agentApply = userApply;
            }
        }

        // 2、构建请求参数
        Map<String, String> params = new HashMap<>();
        // 设置分类ID（cat参数：商品筛选-后台类目ID，用逗号分割，最大10个）
        if (categoryId != null) {
            params.put("cat", categoryId.toString());
        }

        if (StringUtils.isNotEmpty(keyword)) {
            params.put("q", keyword);
        }

        if (categoryId == null && StringUtils.isEmpty(keyword)) {
            params.put("cat", "16,30,50008165,1624,50006842,50011740,50010788,1801,50013864");
        }

        // 设置分页参数和排序参数
        params.put("sort", sort);
        params.put("hasCoupon", "true");
        params.put("pageNum", page.toString());
        params.put("pageSize", pageSize.toString());
        // 3、调用淘宝客SDK
        if (agentApply != null) {
            // 查询代理的淘宝客联盟信息
            String adzoneId = getTbkAdzoneId(agentApply.getId());
            if (adzoneId != null) {
                params.put("adzoneId", adzoneId);
            } else {
                params.put("adzoneId", tbkConfig.getAdzoneId().toString());
            }
        } else {
            // 使用系统默认配置
            params.put("adzoneId", tbkConfig.getAdzoneId().toString());
        }
        JSONObject resultOb = tbkSdkService.dgMaterialOptionalUpgrade(params);
        // 4、返回结果（后端返回结构不变，前端根据返回内容进行适配）
        return success(resultOb);
    }

    /**
     * 获取淘口令（公开接口）
     */
    @Operation(summary = "获取淘口令")
    @GetMapping("/getTpwd")
    public AjaxResult getTpwd(
            @RequestParam String text,
            @RequestParam String url,
            @RequestParam(required = false) String logo,
            @RequestParam(required = false) String invitationCode,
            @RequestParam(required = false) Long userId) {

        // 1、根据邀请码查询具体的代理申请信息（必须是已审核通过的）
        SysTbkAgentApply agentApply = null;
        if (invitationCode != null && !invitationCode.trim().isEmpty()) {
            agentApply = tbkAgentApplyService.selectByInvitationCode(invitationCode);
            // 验证代理状态：只有审核通过（status=1）的代理才有效
            if (agentApply != null && !"1".equals(agentApply.getStatus())) {
                agentApply = null; // 未通过审核的代理视为无效
            }
        } else if (userId != null) {
            // 1.1、如果没有邀请码，则根据微信小程序传入的用户ID查询代理申请信息（必须是已审核通过的）
            SysTbkAgentApply userApply = tbkAgentApplyService.selectByUserId(userId);
            // 验证代理状态：只有审核通过（status=1）的代理才有效
            if (userApply != null && "1".equals(userApply.getStatus())) {
                agentApply = userApply;
            }
        }
        
        // 2、构建淘口令请求参数
        Map<String, String> params = new HashMap<>();
        params.put("apiMethodName", TbkApiConstants.API_METHOD_NAME_CREATE_TPWD);
        params.put("text", text);
        params.put("url", url);
        if (logo != null && !logo.isEmpty()) {
            params.put("logo", logo);
        }

        // 3、根据代理信息选择调用方式
        Map<String, Object> result = tbkSdkService.getTpwd(params);
        return success(result);
    }

    /**
     * 获取代理的淘宝客联盟关联信息
     *
     * @param applyId 申请ID
     * @return 淘宝客联盟关联信息，如果未分配则返回null
     */
    private SysAgentUnionRelation getTbkRelation(Long applyId) {
        List<SysAgentUnionRelation> relations = agentUnionRelationService.selectByApplyId(applyId);
        return relations.stream()
                .filter(r -> "tbk".equals(r.getPlatformType()) && "1".equals(r.getStatus()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取代理的淘宝客推广位ID
     *
     * @param applyId 申请ID
     * @return 推广位ID，如果未分配则返回null
     */
    private String getTbkAdzoneId(Long applyId) {
        SysAgentUnionRelation tbkRelation = getTbkRelation(applyId);
        if (tbkRelation != null && tbkRelation.getUnionPlatform() != null) {
            return tbkRelation.getUnionPlatform().getAdzoneId();
        }
        return null;
    }
}
