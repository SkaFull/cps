package com.ruoyi.union.jd.module;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.request.JdRequest;
import com.jd.open.api.sdk.response.AbstractResponse;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.union.jd.config.JdConfig;
import com.ruoyi.union.jd.constants.JdApiConstants;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 京东联盟模块
 * 脚本中使用
 */
@Component
@Slf4j
public class JdModule {

    @Resource
    private JdConfig jdConfig;

    // 注入 JdClient 实例，用于与京东联盟 API 进行交互
    @Resource
    private JdClient jdClient;

    /**
     * 京东联盟通用接口
     * 该方法用于通过传入的参数调用京东联盟的通用 API
     * 支持系统参数和业务参数的分离处理
     * @param <T>    泛型类型，继承自 AbstractResponse，用于指定返回的响应类型
     * @param params 参数，包含 API 方法名和调用该 API 所需的其他参数
     *               - 系统参数：直接设置到请求对象的属性（如positionId等）
     *               - 业务参数：以"业务对象名."为前缀的参数（如rankGoodsReq.rankId）
     * @return 京东联盟 API 的响应对象，类型为 T
     */
    public <T extends AbstractResponse> T execute(Map<String, String> params) {
        // 记录请求参数，方便调试和监控
        if (jdConfig.isDebug()) log.info("请求参数：{}", params);
        
        // 从参数中获取 API 方法名
        String apiMethodName = params.get("apiMethodName");
        if (StrUtil.isBlank(apiMethodName)) {
            throw new ServiceException("参数【apiMethodName】方法名称为空！");
        }
        
        // 获取请求类
        @SuppressWarnings("unchecked")
        Class<? extends JdRequest<T>> requestClass = (Class<? extends JdRequest<T>>) JdApiConstants.REQUEST_MAP.get(apiMethodName);
        if (requestClass == null) {
            throw new ServiceException(apiMethodName + "方法未集成！");
        }
        
        // 构建请求对象
        JdRequest<T> request = ReflectUtil.newInstance(requestClass);
        
        // 分离系统参数和业务参数
        Map<String, Map<String, String>> businessParamsMap = new HashMap<>();
        Map<String, String> systemParams = new HashMap<>();
        
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            
            // 跳过apiMethodName
            if ("apiMethodName".equals(key)) {
                continue;
            }
            
            // 检查是否为业务参数（包含点号）
            if (key.contains(".")) {
                String[] parts = key.split("\\.", 2);
                String businessObjName = parts[0]; // 如：rankGoodsReq
                String fieldName = parts[1];       // 如：rankId
                
                businessParamsMap.computeIfAbsent(businessObjName, k -> new HashMap<>())
                    .put(fieldName, value);
            } else {
                // 系统参数
                systemParams.put(key, value);
            }
        }
        
        // 设置系统参数到请求对象
        if (!systemParams.isEmpty()) {
            BeanUtil.fillBeanWithMap(systemParams, request, false);
        }
        
        // 处理业务参数对象
        for (Map.Entry<String, Map<String, String>> entry : businessParamsMap.entrySet()) {
            String businessObjName = entry.getKey();
            Map<String, String> businessParams = entry.getValue();
            
            try {
                // 构造setter方法名（如：setRankGoodsReq）
                String setterMethodName = "set" + StrUtil.upperFirst(businessObjName);
                
                // 查找setter方法
                Method setterMethod = null;
                Class<?> businessObjClass = null;
                for (Method method : requestClass.getMethods()) {
                    if (method.getName().equals(setterMethodName) && method.getParameterCount() == 1) {
                        setterMethod = method;
                        businessObjClass = method.getParameterTypes()[0];
                        break;
                    }
                }
                
                if (setterMethod != null && businessObjClass != null) {
                    // 创建业务参数对象
                    Object businessObj = ReflectUtil.newInstance(businessObjClass);
                    
                    // 填充业务参数
                    BeanUtil.fillBeanWithMap(businessParams, businessObj, false);
                    
                    // 设置业务参数对象到请求对象
                    setterMethod.invoke(request, businessObj);
                    
                    if (jdConfig.isDebug()) {
                        log.info("业务参数对象[{}]：{}", businessObjName, JSONUtil.toJsonStr(businessObj));
                    }
                } else {
                    log.warn("未找到业务参数对象[{}]的setter方法", businessObjName);
                }
            } catch (Exception e) {
                throw new ServiceException("设置业务参数对象[" + businessObjName + "]失败：" + e.getMessage());
            }
        }
        
        // 记录请求报文
        if (jdConfig.isDebug()) {
            log.info("请求报文：{}", JSONUtil.formatJsonStr(JSONUtil.toJsonStr(request)));
        }
        
        // 执行API调用
        T rsp;
        try {
            rsp = jdClient.execute(request);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        
        // 记录返回报文
        if (jdConfig.isDebug()) {
            log.info("返回报文：{}", JSONUtil.formatJsonStr(JSONUtil.toJsonStr(rsp)));
        }
        
        return rsp;
    }

}
