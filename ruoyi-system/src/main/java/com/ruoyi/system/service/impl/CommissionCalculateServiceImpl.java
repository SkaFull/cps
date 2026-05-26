package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SysAgentCommissionAccount;
import com.ruoyi.system.domain.SysCommissionConfig;
import com.ruoyi.system.domain.SysCommissionFlow;
import com.ruoyi.system.domain.SysOrderCommission;
import com.ruoyi.system.domain.SysWechatUser;
import com.ruoyi.system.mapper.SysAgentCommissionAccountMapper;
import com.ruoyi.system.mapper.SysCommissionConfigMapper;
import com.ruoyi.system.mapper.SysCommissionFlowMapper;
import com.ruoyi.system.mapper.SysOrderCommissionMapper;
import com.ruoyi.system.mapper.SysWechatUserMapper;
import com.ruoyi.system.service.ICommissionCalculateService;

/**
 * 佣金计算服务实现
 * 
 * @author ruoyi
 */
@Service
public class CommissionCalculateServiceImpl implements ICommissionCalculateService
{
    @Autowired
    private SysCommissionConfigMapper commissionConfigMapper;

    @Autowired
    private SysOrderCommissionMapper orderCommissionMapper;

    @Autowired
    private SysAgentCommissionAccountMapper accountMapper;

    @Autowired
    private SysCommissionFlowMapper flowMapper;

    @Autowired
    private SysWechatUserMapper wechatUserMapper;

    @Autowired
    private com.ruoyi.system.mapper.SysTbkAgentApplyMapper agentApplyMapper;

    /**
     * 计算订单佣金
     */
    @Override
    @Transactional
    public SysOrderCommission calculateCommission(
        String orderId,
        String platformType,
        String itemId,
        String itemTitle,
        BigDecimal orderAmount,
        BigDecimal estimatedCommission,
        BigDecimal commissionRate,
        Long buyerUserId,
        String orderTime)
    {
        // 检查订单是否已存在
        SysOrderCommission existingOrder = orderCommissionMapper
            .selectSysOrderCommissionByOrderIdAndPlatformType(orderId, platformType);
        if (existingOrder != null)
        {
            return existingOrder;
        }

        // 创建订单佣金记录
        SysOrderCommission orderCommission = new SysOrderCommission();
        orderCommission.setOrderId(orderId);
        orderCommission.setPlatformType(platformType);
        orderCommission.setItemId(itemId);
        orderCommission.setItemTitle(itemTitle);
        orderCommission.setOrderAmount(orderAmount);
        orderCommission.setEstimatedCommission(estimatedCommission);
        orderCommission.setCommissionRate(commissionRate);
        orderCommission.setBuyerUserId(buyerUserId);
        // 将String类型的orderTime转换为Date类型
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            orderCommission.setOrderTime(sdf.parse(orderTime));
        } catch (java.text.ParseException e) {
            orderCommission.setOrderTime(new Date());
        }
        orderCommission.setOrderStatus("pending"); // 待结算
        orderCommission.setSettleStatus("unsettled"); // 未结算
        orderCommission.setCreateTime(new Date());

        // 查询购买用户的代理申请信息，判断是否有推荐人
        com.ruoyi.system.domain.SysTbkAgentApply buyerAgentApply = agentApplyMapper.selectByUserId(buyerUserId);
        if (buyerAgentApply == null)
        {
            throw new RuntimeException("用户不是代理，无法计算佣金");
        }

        // 获取佣金配置
        BigDecimal selfRate = getSelfCommissionRate();
        BigDecimal promotionRate = getPromotionCommissionRate();

        // 判断是否有推荐人（基于代理申请表的 referrer_invitation_code 字段）
        boolean hasReferrer = buyerAgentApply.getReferrerInvitationCode() != null 
            && !buyerAgentApply.getReferrerInvitationCode().isEmpty();
        
        orderCommission.setHasReferrer(hasReferrer ? "1" : "0");

        if (hasReferrer)
        {
            // 有推荐人：根据推荐人邀请码查找推荐人的代理申请信息
            com.ruoyi.system.domain.SysTbkAgentApply referrerAgentApply = agentApplyMapper
                .selectByInvitationCode(buyerAgentApply.getReferrerInvitationCode());
            
            if (referrerAgentApply != null)
            {
                orderCommission.setReferrerUserId(referrerAgentApply.getUserId());
                
                // 计算自购佣金（购买者获得）
                BigDecimal selfCommissionAmount = estimatedCommission
                    .multiply(selfRate)
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                
                // 计算推广佣金（推荐人获得）
                BigDecimal promotionCommissionAmount = estimatedCommission
                    .multiply(promotionRate)
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                
                orderCommission.setSelfCommissionRate(selfRate);
                orderCommission.setSelfCommissionAmount(selfCommissionAmount);
                orderCommission.setPromotionCommissionRate(promotionRate);
                orderCommission.setPromotionCommissionAmount(promotionCommissionAmount);
            }
            else
            {
                // 推荐人不存在，按无推荐人处理
                orderCommission.setHasReferrer("0");
                orderCommission.setReferrerUserId(null);
                
                BigDecimal selfCommissionAmount = estimatedCommission
                    .multiply(selfRate)
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                
                orderCommission.setSelfCommissionRate(selfRate);
                orderCommission.setSelfCommissionAmount(selfCommissionAmount);
                orderCommission.setPromotionCommissionRate(BigDecimal.ZERO);
                orderCommission.setPromotionCommissionAmount(BigDecimal.ZERO);
            }
        }
        else
        {
            // 无推荐人：只有自购佣金
            orderCommission.setReferrerUserId(null);
            
            BigDecimal selfCommissionAmount = estimatedCommission
                .multiply(selfRate)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            
            orderCommission.setSelfCommissionRate(selfRate);
            orderCommission.setSelfCommissionAmount(selfCommissionAmount);
            orderCommission.setPromotionCommissionRate(BigDecimal.ZERO);
            orderCommission.setPromotionCommissionAmount(BigDecimal.ZERO);
        }

        // 保存订单佣金记录
        orderCommissionMapper.insertSysOrderCommission(orderCommission);

        return orderCommission;
    }

    /**
     * 订单结算
     */
    @Override
    @Transactional
    public boolean settleOrder(Long orderCommissionId, BigDecimal actualCommission)
    {
        // 查询订单佣金记录
        SysOrderCommission orderCommission = orderCommissionMapper
            .selectSysOrderCommissionById(orderCommissionId);
        
        if (orderCommission == null)
        {
            throw new RuntimeException("订单佣金记录不存在");
        }

        if ("settled".equals(orderCommission.getSettleStatus()))
        {
            throw new RuntimeException("订单已结算，不能重复结算");
        }

        // 更新实际佣金
        orderCommission.setActualCommission(actualCommission);
        
        // 重新计算实际分成金额（按实际佣金比例分配）
        BigDecimal actualSelfAmount = actualCommission
            .multiply(orderCommission.getSelfCommissionRate())
            .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        
        BigDecimal actualPromotionAmount = BigDecimal.ZERO;
        if ("1".equals(orderCommission.getHasReferrer()) && orderCommission.getReferrerUserId() != null)
        {
            actualPromotionAmount = actualCommission
                .multiply(orderCommission.getPromotionCommissionRate())
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        }

        orderCommission.setSelfCommissionAmount(actualSelfAmount);
        orderCommission.setPromotionCommissionAmount(actualPromotionAmount);
        orderCommission.setOrderStatus("confirmed");
        orderCommission.setSettleStatus("settled");
        orderCommission.setSettleTime(DateUtils.getNowDate());
        orderCommission.setUpdateTime(new Date());
        
        orderCommissionMapper.updateSysOrderCommission(orderCommission);

        // 更新购买者的佣金账户（自购佣金）
        updateUserAccount(orderCommission.getBuyerUserId(), actualSelfAmount, 
            "income_self", orderCommission.getId(), "自购佣金入账");

        // 如果有推荐人，更新推荐人的佣金账户（推广佣金）
        if ("1".equals(orderCommission.getHasReferrer()) && orderCommission.getReferrerUserId() != null)
        {
            updateUserAccount(orderCommission.getReferrerUserId(), actualPromotionAmount, 
                "income_promotion", orderCommission.getId(), "推广佣金入账");
        }

        return true;
    }

    /**
     * 订单退款处理
     */
    @Override
    @Transactional
    public boolean refundOrder(Long orderCommissionId)
    {
        // 查询订单佣金记录
        SysOrderCommission orderCommission = orderCommissionMapper
            .selectSysOrderCommissionById(orderCommissionId);
        
        if (orderCommission == null)
        {
            throw new RuntimeException("订单佣金记录不存在");
        }

        if (!"settled".equals(orderCommission.getSettleStatus()))
        {
            throw new RuntimeException("订单未结算，无需退款处理");
        }

        if ("refunded".equals(orderCommission.getOrderStatus()))
        {
            throw new RuntimeException("订单已退款，不能重复处理");
        }

        // 扣减购买者的佣金（自购佣金）
        BigDecimal selfAmount = orderCommission.getSelfCommissionAmount();
        if (selfAmount != null && selfAmount.compareTo(BigDecimal.ZERO) > 0)
        {
            updateUserAccount(orderCommission.getBuyerUserId(), selfAmount.negate(), 
                "refund", orderCommission.getId(), "订单退款扣减佣金");
        }

        // 如果有推荐人，扣减推荐人的佣金（推广佣金）
        if ("1".equals(orderCommission.getHasReferrer()) && orderCommission.getReferrerUserId() != null)
        {
            BigDecimal promotionAmount = orderCommission.getPromotionCommissionAmount();
            if (promotionAmount != null && promotionAmount.compareTo(BigDecimal.ZERO) > 0)
            {
                updateUserAccount(orderCommission.getReferrerUserId(), promotionAmount.negate(), 
                    "refund", orderCommission.getId(), "订单退款扣减佣金");
            }
        }

        // 更新订单状态
        orderCommission.setOrderStatus("refunded");
        orderCommission.setUpdateTime(new Date());
        orderCommissionMapper.updateSysOrderCommission(orderCommission);

        return true;
    }

    /**
     * 更新用户佣金账户
     */
    private void updateUserAccount(Long userId, BigDecimal amount, String flowType, 
        Long relatedOrderId, String description)
    {
        // 查询或创建用户佣金账户
        SysAgentCommissionAccount account = accountMapper.selectByUserId(userId);
        if (account == null)
        {
            // 创建新账户
            account = new SysAgentCommissionAccount();
            account.setUserId(userId);
            account.setTotalEarned(BigDecimal.ZERO);
            account.setSelfEarned(BigDecimal.ZERO);
            account.setPromotionEarned(BigDecimal.ZERO);
            account.setBalance(BigDecimal.ZERO);
            account.setWithdrawn(BigDecimal.ZERO);
            account.setFrozenAmount(BigDecimal.ZERO);
            account.setCreateTime(new Date());
            accountMapper.insertSysAgentCommissionAccount(account);
        }

        // 记录变更前余额
        BigDecimal balanceBefore = account.getBalance();

        // 更新账户金额
        if ("income_self".equals(flowType))
        {
            // 自购佣金入账
            account.setTotalEarned(account.getTotalEarned().add(amount));
            account.setSelfEarned(account.getSelfEarned().add(amount));
            account.setBalance(account.getBalance().add(amount));
        }
        else if ("income_promotion".equals(flowType))
        {
            // 推广佣金入账
            account.setTotalEarned(account.getTotalEarned().add(amount));
            account.setPromotionEarned(account.getPromotionEarned().add(amount));
            account.setBalance(account.getBalance().add(amount));
        }
        else if ("refund".equals(flowType))
        {
            // 退款扣减
            account.setTotalEarned(account.getTotalEarned().add(amount)); // amount为负数
            account.setBalance(account.getBalance().add(amount)); // amount为负数
        }
        else if ("withdraw".equals(flowType))
        {
            // 提现
            account.setBalance(account.getBalance().add(amount)); // amount为负数
            account.setWithdrawn(account.getWithdrawn().subtract(amount)); // amount为负数，所以用减法
        }

        account.setUpdateTime(new Date());
        accountMapper.updateSysAgentCommissionAccount(account);

        // 记录佣金流水
        SysCommissionFlow flow = new SysCommissionFlow();
        flow.setUserId(userId);
        flow.setFlowType(flowType);
        flow.setAmount(amount);
        flow.setBalanceBefore(balanceBefore);
        flow.setBalanceAfter(account.getBalance());
        // 将Long类型的relatedOrderId转换为String类型
        flow.setRelatedOrderId(relatedOrderId != null ? relatedOrderId.toString() : null);
        flow.setDescription(description);
        flow.setCreateTime(new Date());
        flowMapper.insertSysCommissionFlow(flow);
    }

    /**
     * 获取自购佣金比例
     */
    @Override
    public BigDecimal getSelfCommissionRate()
    {
        SysCommissionConfig config = commissionConfigMapper
            .selectSysCommissionConfigByKey("self_rate");
        return config != null ? config.getConfigValue() : new BigDecimal("40");
    }

    /**
     * 获取推广佣金比例
     */
    @Override
    public BigDecimal getPromotionCommissionRate()
    {
        SysCommissionConfig config = commissionConfigMapper
            .selectSysCommissionConfigByKey("promotion_rate");
        return config != null ? config.getConfigValue() : new BigDecimal("5");
    }

    /**
     * 更新佣金配置
     */
    @Override
    @Transactional
    public boolean updateCommissionConfig(String configKey, BigDecimal configValue)
    {
        SysCommissionConfig config = commissionConfigMapper
            .selectSysCommissionConfigByKey(configKey);
        
        if (config == null)
        {
            // 创建新配置
            config = new SysCommissionConfig();
            config.setConfigKey(configKey);
            config.setConfigValue(configValue);
            config.setCreateTime(new Date());
            return commissionConfigMapper.insertSysCommissionConfig(config) > 0;
        }
        else
        {
            // 更新配置
            config.setConfigValue(configValue);
            config.setUpdateTime(new Date());
            return commissionConfigMapper.updateSysCommissionConfig(config) > 0;
        }
    }
}
