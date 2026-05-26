package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysOrderCommission;
import java.math.BigDecimal;

/**
 * 佣金计算服务接口
 * 
 * @author ruoyi
 */
public interface ICommissionCalculateService
{
    /**
     * 计算订单佣金
     * 根据订单信息和用户推荐关系计算自购佣金和推广佣金
     * 
     * @param orderId 订单ID
     * @param platformType 平台类型(taobao/jd/pdd等)
     * @param itemId 商品ID
     * @param itemTitle 商品标题
     * @param orderAmount 订单金额
     * @param estimatedCommission 预估佣金(平台返回)
     * @param commissionRate 佣金率
     * @param buyerUserId 购买用户ID
     * @param orderTime 下单时间
     * @return 计算后的订单佣金对象
     */
    SysOrderCommission calculateCommission(
        String orderId,
        String platformType,
        String itemId,
        String itemTitle,
        BigDecimal orderAmount,
        BigDecimal estimatedCommission,
        BigDecimal commissionRate,
        Long buyerUserId,
        String orderTime
    );

    /**
     * 订单结算
     * 当订单确认收货后，将预估佣金转为实际佣金，并更新用户佣金账户
     * 
     * @param orderCommissionId 订单佣金记录ID
     * @param actualCommission 实际佣金(平台结算)
     * @return 是否结算成功
     */
    boolean settleOrder(Long orderCommissionId, BigDecimal actualCommission);

    /**
     * 订单退款处理
     * 处理订单退款，扣减已入账的佣金
     * 
     * @param orderCommissionId 订单佣金记录ID
     * @return 是否处理成功
     */
    boolean refundOrder(Long orderCommissionId);

    /**
     * 获取自购佣金比例
     * 
     * @return 自购佣金比例(百分比)
     */
    BigDecimal getSelfCommissionRate();

    /**
     * 获取推广佣金比例
     * 
     * @return 推广佣金比例(百分比)
     */
    BigDecimal getPromotionCommissionRate();

    /**
     * 更新佣金配置
     * 
     * @param configKey 配置键(self_rate/promotion_rate)
     * @param configValue 配置值
     * @return 是否更新成功
     */
    boolean updateCommissionConfig(String configKey, BigDecimal configValue);
}
