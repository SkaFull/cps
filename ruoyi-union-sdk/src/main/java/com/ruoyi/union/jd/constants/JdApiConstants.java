package com.ruoyi.union.jd.constants;

import com.jd.open.api.sdk.request.kplunion.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 京东联盟API常量
 * 参考文档：https://union.jd.com/openplatform/api
 * @author mengqing
 * @since 2024/12/28 12:33
 */
public class JdApiConstants {
    
    /**
     * API方法名称常量
     */
    // 工具商营销线报接口
    public static final String API_METHOD_NAME_PROMOTION_TOOLS_INTELLIGENCE_QUERY = "jd.union.open.promotion.tools.intelligence.query";
    
    // 优惠券领取情况查询接口
    public static final String API_METHOD_NAME_COUPON_QUERY = "jd.union.open.coupon.query";
    
    // 商品详情查询接口
    public static final String API_METHOD_NAME_GOODS_BIGFIELD_QUERY = "jd.union.open.goods.bigfield.query";
    
    // 招商团长活动商品列表查询
    public static final String API_METHOD_NAME_CP_ACTIVITY_GOODS_QUERY = "jd.union.open.cp.activity.goods.query";
    
    // 网站/APP获取推广链接接口
    public static final String API_METHOD_NAME_PROMOTION_COMMON_GET = "jd.union.open.promotion.common.get";
    
    // 邀请码获取接口
    public static final String API_METHOD_NAME_CHANNEL_INVITECODE_GET = "jd.union.open.channel.invitecode.get";
    
    // 活动查询接口
    public static final String API_METHOD_NAME_ACTIVITY_QUERY = "jd.union.open.activity.query";
    
    // 联盟商品ID获取接口
    public static final String API_METHOD_NAME_GOODS_ITEMID_GET = "jd.union.open.goods.itemid.get";
    
    // 联盟实时热销榜商品接口
    public static final String API_METHOD_NAME_GOODS_RANK_QUERY = "jd.union.open.goods.rank.query";

    /**
     * 存储京东联盟API名称与对应请求类的映射表。
     * 键为API名称，值为对应的请求类的Class对象。
     * 该映射表用于根据API名称快速找到对应的请求类，方便后续调用京东联盟的API。
     */
    public final static Map<String, Class> REQUEST_MAP = new HashMap<>();

    static {
        // 初始化映射表，将各个API名称与对应的请求类关联起来，使用常量名称进行映射
        
        // 工具商营销线报接口
        REQUEST_MAP.put(API_METHOD_NAME_PROMOTION_TOOLS_INTELLIGENCE_QUERY, UnionOpenPromotionToolsIntelligenceQueryRequest.class);
        
        // 优惠券领取情况查询接口
        REQUEST_MAP.put(API_METHOD_NAME_COUPON_QUERY, UnionOpenCouponQueryRequest.class);
        
        // 商品详情查询接口
        REQUEST_MAP.put(API_METHOD_NAME_GOODS_BIGFIELD_QUERY, UnionOpenGoodsBigfieldQueryRequest.class);
        
        // 招商团长活动商品列表查询
        REQUEST_MAP.put(API_METHOD_NAME_CP_ACTIVITY_GOODS_QUERY, UnionOpenCpActivityGoodsQueryRequest.class);
        
        // 网站/APP获取推广链接接口
        REQUEST_MAP.put(API_METHOD_NAME_PROMOTION_COMMON_GET, UnionOpenPromotionCommonGetRequest.class);
        
        // 邀请码获取接口
        REQUEST_MAP.put(API_METHOD_NAME_CHANNEL_INVITECODE_GET, UnionOpenChannelInvitecodeGetRequest.class);
        
        // 活动查询接口
        REQUEST_MAP.put(API_METHOD_NAME_ACTIVITY_QUERY, UnionOpenActivityQueryRequest.class);
        
        // 联盟商品ID获取接口
        REQUEST_MAP.put(API_METHOD_NAME_GOODS_ITEMID_GET, UnionOpenGoodsItemidGetRequest.class);
        
        // 联盟实时热销榜商品接口
        REQUEST_MAP.put(API_METHOD_NAME_GOODS_RANK_QUERY, UnionOpenGoodsRankQueryRequest.class);
    }

}
