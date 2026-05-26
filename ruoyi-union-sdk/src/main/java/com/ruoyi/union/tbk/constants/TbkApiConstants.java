package com.ruoyi.union.tbk.constants;

import com.taobao.api.TaobaoRequest;
import com.taobao.api.request.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 该类用于定义淘宝客API相关的常量，主要包含一个映射表，用于存储API名称和对应的请求类。
 * @author ruoyi
 * @since 2024/12/28 12:33
 */
public class TbkApiConstants {

    //==== 公用 ====
    public static final String API_METHOD_NAME_GET_ITEM_INFO = "taobao.tbk.item.info.get";
    public static final String API_METHOD_NAME_GET_SHOP_RECOMMEND = "taobao.tbk.shop.recommend.get";
    public static final String API_METHOD_NAME_GET_SPREAD = "taobao.tbk.spread.get";
    public static final String API_METHOD_NAME_GET_CPUPON = "taobao.tbk.coupon.get";
    public static final String API_METHOD_NAME_CREATE_TPWD = "taobao.tbk.tpwd.create";
    public static final String API_METHOD_NAME_SAVE_SC_PUBLISHER_INFO = "taobao.tbk.sc.publisher.info.save";
    public static final String API_METHOD_NAME_GET_SC_PUBLISHER_INFO = "taobao.tbk.sc.publisher.info.get";
    public static final String API_METHOD_NAME_GET_SC_INVITECODE = "taobao.tbk.sc.invitecode.get";

    //==== 推广者 ====
    public static final String API_METHOD_NAME_GET_SHOP = "taobao.tbk.shop.get";
    public static final String API_METHOD_NAME_GET_DG_NEWUSER_ORDER = "taobao.tbk.dg.newuser.order.get";
    public static final String API_METHOD_NAME_SUM_DG_NEWUSER_ORDER = "taobao.tbk.dg.newuser.order.sum";
    public static final String API_METHOD_NAME_CREATE_DG_VEGAS_TLJ = "taobao.tbk.dg.vegas.tlj.create";
    public static final String API_METHOD_NAME_GET_DG_PUNISH_ORDER = "taobao.tbk.dg.punish.order.get";
    public static final String API_METHOD_NAME_REPORT_DG_VEGAS_SEND = "taobao.tbk.dg.vegas.send.report";
    public static final String API_METHOD_NAME_GET_ACTIVITY_INFO = "taobao.tbk.activity.info.get";
    public static final String API_METHOD_NAME_PROMOTION_DG_OPTIMUS = "taobao.tbk.dg.optimus.promotion";
    public static final String API_METHOD_NAME_STATUS_DG_VEGAS_SEND = "taobao.tbk.dg.vegas.send.status";
    public static final String API_METHOD_NAME_DETAIL_DG_CPA_ACTIVITY = "taobao.tbk.dg.cpa.activity.detail";
    public static final String API_METHOD_NAME_GET_DG_TPWD_REPORT = "taobao.tbk.dg.tpwd.report.get";
    public static final String API_METHOD_NAME_REPORT_DG_CPA_ACTIVITY = "taobao.tbk.dg.cpa.activity.report";
    public static final String API_METHOD_NAME_STOP_DG_VEGAS_TLJ = "taobao.tbk.dg.vegas.tlj.stop";
    public static final String API_METHOD_NAME_REPORT_DG_VEGAS_TLJ = "taobao.tbk.dg.vegas.tlj.report";
    public static final String API_METHOD_NAME_REPORT_DG_TPWD_RISK = "taobao.tbk.dg.tpwd.risk.report";
    public static final String API_METHOD_NAME_RECOMMEND_DG_MATERIAL = "taobao.tbk.dg.material.recommend";
    public static final String API_METHOD_NAME_GET_OPTIMUS_TOU_MATERIAL_IDS = "taobao.tbk.optimus.tou.material.ids.get";
    public static final String API_METHOD_NAME_UPGRADE_DG_MATERIAL_OPTIONAL = "taobao.tbk.dg.material.optional.upgrade";

    /**
     * 淘宝客API文档链接，可通过该链接查看API详细信息
     * 宝客API名称与对应请求类的映射表。
     * 键为API名称，值为继承自TaobaoRequest的请求类的Class对象。
     */
    public final static Map<String, Class<? extends TaobaoRequest>> REQUEST_MAP = new HashMap<>();

    static {
        //==== 公用 ====
        //淘宝客-公用-淘宝客商品详情查询(简版)
        REQUEST_MAP.put( API_METHOD_NAME_GET_ITEM_INFO, TbkItemInfoGetRequest.class );
        //淘宝客-公用-店铺关联推荐
        REQUEST_MAP.put( API_METHOD_NAME_GET_SHOP_RECOMMEND, TbkShopRecommendGetRequest.class );
        //淘宝客-公用-长链转短链
        REQUEST_MAP.put( API_METHOD_NAME_GET_SPREAD, TbkSpreadGetRequest.class );
        //淘宝客-公用-阿里妈妈推广券详情查询
        REQUEST_MAP.put( API_METHOD_NAME_GET_CPUPON, TbkCouponGetRequest.class );
        //淘宝客-公用-淘口令生成
        REQUEST_MAP.put( API_METHOD_NAME_CREATE_TPWD, TbkTpwdCreateRequest.class );
        //淘宝客-公用-私域用户备案
        REQUEST_MAP.put( API_METHOD_NAME_SAVE_SC_PUBLISHER_INFO, TbkScPublisherInfoSaveRequest.class );
        //淘宝客-公用-私域用户备案信息查询
        REQUEST_MAP.put( API_METHOD_NAME_GET_SC_PUBLISHER_INFO, TbkScPublisherInfoGetRequest.class );
        //淘宝客-公用-私域用户邀请码生成
        REQUEST_MAP.put( API_METHOD_NAME_GET_SC_INVITECODE, TbkScInvitecodeGetRequest.class );

        //==== 推广者 ====
        //淘宝客-推广者-店铺搜索
        REQUEST_MAP.put( API_METHOD_NAME_GET_SHOP, TbkShopGetRequest.class );
        //淘宝客-推广者-新用户订单明细查询
        REQUEST_MAP.put( API_METHOD_NAME_GET_DG_NEWUSER_ORDER, TbkDgNewuserOrderGetRequest.class );
        //淘宝客-推广者-拉新活动对应数据查询
        REQUEST_MAP.put( API_METHOD_NAME_SUM_DG_NEWUSER_ORDER, TbkDgNewuserOrderSumRequest.class );
        //淘宝客-推广者-淘礼金创建
        REQUEST_MAP.put( API_METHOD_NAME_CREATE_DG_VEGAS_TLJ, TbkDgVegasTljCreateRequest.class );
        //淘宝客-推广者-处罚订单查询
        REQUEST_MAP.put( API_METHOD_NAME_GET_DG_PUNISH_ORDER, TbkDgPunishOrderGetRequest.class );
        //淘宝客-推广者-查询红包发放个数
        REQUEST_MAP.put( API_METHOD_NAME_REPORT_DG_VEGAS_SEND, TbkDgVegasSendReportRequest.class );
        //淘宝客-推广者-官方活动转链
        REQUEST_MAP.put( API_METHOD_NAME_GET_ACTIVITY_INFO, TbkActivityInfoGetRequest.class );
        //淘宝客-推广者-权益物料精选
        REQUEST_MAP.put( API_METHOD_NAME_PROMOTION_DG_OPTIMUS, TbkDgOptimusPromotionRequest.class );
        //淘宝客-推广者-红包领取状态查询
        REQUEST_MAP.put( API_METHOD_NAME_STATUS_DG_VEGAS_SEND, TbkDgVegasSendStatusRequest.class );
        //淘宝客-推广者-CPA活动执行明细
        REQUEST_MAP.put( API_METHOD_NAME_DETAIL_DG_CPA_ACTIVITY, TbkDgCpaActivityDetailRequest.class );
        //淘宝客-推广者-淘口令回流数据查询
        REQUEST_MAP.put( API_METHOD_NAME_GET_DG_TPWD_REPORT, TbkDgTpwdReportGetRequest.class );
        //淘宝客-推广者-任务奖励效果报表
        REQUEST_MAP.put( API_METHOD_NAME_REPORT_DG_CPA_ACTIVITY, TbkDgCpaActivityReportRequest.class );
        //淘宝客-推广者-淘礼金暂停发放
        REQUEST_MAP.put( API_METHOD_NAME_STOP_DG_VEGAS_TLJ, TbkDgVegasTljStopRequest.class );
        //淘宝客-推广者-淘礼金效果数据
        REQUEST_MAP.put( API_METHOD_NAME_REPORT_DG_VEGAS_TLJ, TbkDgVegasTljReportRequest.class );
        //淘宝客-推广者-物料精选升级版
        REQUEST_MAP.put( API_METHOD_NAME_REPORT_DG_TPWD_RISK, TbkDgTpwdRiskReportRequest.class );
        //淘宝客-推广者-物料精选升级版
        REQUEST_MAP.put( API_METHOD_NAME_RECOMMEND_DG_MATERIAL, TbkDgMaterialRecommendRequest.class );
        //淘宝客-推广者-物料id列表查询
        REQUEST_MAP.put( API_METHOD_NAME_GET_OPTIMUS_TOU_MATERIAL_IDS, TbkOptimusTouMaterialIdsGetRequest.class );
        //淘宝客-推广者-物料搜索升级版
        REQUEST_MAP.put( API_METHOD_NAME_UPGRADE_DG_MATERIAL_OPTIONAL, TbkDgMaterialOptionalUpgradeRequest.class );
    }

}
