package com.ruoyi.web.controller.union;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.ITbkSdkService;
import com.ruoyi.union.tbk.constants.TbkApiConstants;
import com.ruoyi.union.tbk.module.TbkModule;
import com.taobao.api.TaobaoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 参数配置 信息操作处理
 * @author ruoyi
 */
@Slf4j
@RestController
@RequestMapping("/union/tbk")
public class TbkController extends BaseController {

    @Autowired
    private TbkModule tbkModule;

    @Autowired
    private ITbkSdkService tbkSdkService;

    /**
     * 店铺搜索
     */
    @PreAuthorize("@ss.hasPermi('union:tbk:shop')")
    @GetMapping("/getShop")
    public AjaxResult getShop(@RequestParam Map<String, String> params) {
        params.put("apiMethodName", TbkApiConstants.API_METHOD_NAME_GET_SHOP);  // 调用方法
        params.put("fields","user_id,shop_title,shop_type,seller_nick,pict_url,shop_url"); // 返回字段信息
        params.put("pageNo",  params.get("pageNum"));
        TaobaoResponse taobaoResponse = tbkModule.execute(params);
        return success(taobaoResponse);
    }

    /**
     * 权益物料精选
     */
    @PreAuthorize("@ss.hasPermi('union:tbk:dg:optimus:promotion')")
    @GetMapping("/dgOptimusPromotion")
    public AjaxResult dgOptimusPromotion(@RequestParam Map<String, String> params) {
        params.put("apiMethodName", TbkApiConstants.API_METHOD_NAME_PROMOTION_DG_OPTIMUS);  // 调用方法
        params.put("page_no",  params.get("pageNum"));
        params.put("page_size",  params.get("pageSize"));
        TaobaoResponse taobaoResponse = tbkModule.execute(params);
        return success(taobaoResponse);
    }

    /**
     * 物料id列表查询
     */
    @PreAuthorize("@ss.hasPermi('union:tbk:optimus:tou:material:ids:get')")
    @GetMapping("/optimusTouMaterialIdsGet")
    public AjaxResult optimusTouMaterialIdsGet(@RequestParam Map<String, String> params) {
        params.put("apiMethodName", TbkApiConstants.API_METHOD_NAME_GET_OPTIMUS_TOU_MATERIAL_IDS);  // 调用方法
        String materialQueryReq = params.get("materialQuery");
        JSONObject materialQueryJObj = JSONObject.parseObject(materialQueryReq);
        materialQueryJObj.put("page_no",  params.get("pageNum"));
        materialQueryJObj.put("page_size",  params.get("pageSize"));
        String materialQuery = tbkModule.toJsonString(materialQueryJObj);
        params.put("materialQuery",materialQuery);
        TaobaoResponse taobaoResponse = tbkModule.execute(params);
        log.info("optimusTouMaterialIdsGet taobaoResponse >>> ",JSONObject.toJSONString(taobaoResponse));
        return success(taobaoResponse);
    }

    /**
     * 物料精选升级版
     */
    @PreAuthorize("@ss.hasPermi('union:tbk:dg:material:recommend')")
    @GetMapping("/dgMaterialRecommend")
    public AjaxResult dgMaterialRecommend(@RequestParam Map<String, String> params) {
        params.put("apiMethodName", TbkApiConstants.API_METHOD_NAME_RECOMMEND_DG_MATERIAL);  // 调用方法
        TaobaoResponse taobaoResponse = tbkModule.execute(params);
        AjaxResult ajaxResult = success(taobaoResponse);
        log.info("dgMaterialRecommend taobaoResponse >>> ",JSONObject.toJSONString(ajaxResult));
        return ajaxResult;
    }

    /**
     * 物料搜索升级版
     */
    @PreAuthorize("@ss.hasPermi('union:tbk:dg:material:optional:upgrade')")
    @GetMapping("/dgMaterialOptionalUpgrade")
    public AjaxResult dgMaterialOptionalUpgrade(@RequestParam Map<String, String> params) {
        JSONObject resultOb = tbkSdkService.dgMaterialOptionalUpgrade(params);
        return success(resultOb);
    }

    /**
     * 获取淘口令
     */
    @PreAuthorize("@ss.hasPermi('union:tbk:tpwd')")
    @GetMapping("/getTpwd")
    public AjaxResult getTpwd(@RequestParam Map<String, String> params) {
        params.put("apiMethodName", TbkApiConstants.API_METHOD_NAME_CREATE_TPWD);
        Map<String, Object> map = tbkModule.getTpwd(params);
        return success(map);
    }

    /**
     * 获取短连接
     */
    @PreAuthorize("@ss.hasPermi('union:tbk:short:url')")
    @GetMapping(value = "getShortUrl")
    public AjaxResult getShortUrl(@RequestParam Map<String, String> params) {
        Map<String, Object> map = tbkModule.getShortUrl(params);
        return success(map);
    }
}
