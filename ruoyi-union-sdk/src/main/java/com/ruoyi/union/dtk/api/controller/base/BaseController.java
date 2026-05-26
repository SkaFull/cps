package com.ruoyi.union.dtk.api.controller.base;

import com.ruoyi.union.dtk.api.client.DtkApiClient;
import com.ruoyi.union.dtk.api.client.DtkClient;
import com.ruoyi.union.dtk.api.constant.DtkApiConstant;
import com.ruoyi.union.dtk.api.exception.DtkResultEnum;
import com.ruoyi.union.dtk.api.request.putstorage.DtkGoodsListRequest;
import com.ruoyi.union.dtk.api.response.base.DtkApiResponse;
import com.ruoyi.union.dtk.api.response.base.DtkPageResponse;
import com.ruoyi.union.dtk.api.response.putstorage.DtkGoodsListItemResponse;
import com.ruoyi.union.dtk.api.utils.Assert;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;


/**
 * dtk api: base controller
 *
 * @author baige
 * @date 2020/11/13 14:21
 */
@Component
public class BaseController {
    @Resource
    private HttpServletRequest request;

    private DtkApiClient instance;

    protected DtkClient getDtkClient() {
        String appKey = getClientHeader(DtkApiConstant.RequestCommonParam.APP_KEY);
        Assert.notBank(appKey, DtkResultEnum.APP_KEY_EMPTY);
        String appSecret = getClientHeader(DtkApiConstant.RequestCommonParam.APP_SECRET);
        Assert.notBank(appSecret, DtkResultEnum.APP_SECRET_EMPTY);
        if (instance == null) {
            synchronized (BaseController.class) {
                if (instance == null) {
                    this.instance = DtkApiClient.getInstance(appKey, appSecret);
                }
            }
        }

        return this.instance;
    }

    /**
     * 从header获取appKey和appSecret
     */
    private String getClientHeader(String headerKey) {
        return request.getHeader(headerKey);
    }

    protected String getRequestUrl() {
        String requestUrl = request.getRequestURI();
        return String.format("%s%s", DtkApiConstant.Domain.PROD, requestUrl);
    }

    public static void main(String[] args) {
        String appKey = "xxx";
        String appSecret = "xxx";
        DtkApiClient client = DtkApiClient.getInstance(appKey,appSecret);
        DtkGoodsListRequest request = new DtkGoodsListRequest();
        request.setVersion("v1.2.4");
        DtkApiResponse<DtkPageResponse<DtkGoodsListItemResponse>> execute = client.execute(request);
        System.out.println(execute);
    }
}
