package com.ruoyi.union.dtk.api.request.mastertool;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ruoyi.union.dtk.api.client.DtkApiRequest;
import com.ruoyi.union.dtk.api.response.base.DtkApiResponse;
import com.ruoyi.union.dtk.api.response.mastertool.DtkCouponQueryResponse;
import com.ruoyi.union.dtk.api.utils.ObjectUtil;
import com.ruoyi.union.dtk.api.utils.RequiredCheck;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 优惠券查询请求参数实体
 * @author 1
 * @date 2021/07/07 14:27
 */
@Getter
@Setter
public class DtkCouponQueryRequest implements DtkApiRequest<DtkApiResponse<DtkCouponQueryResponse>> {
    @ApiModelProperty(value = "版本号", example = "v1.0.0")
    private String version = "1.0.0";

    @RequiredCheck
    @ApiModelProperty(value = "二合一链接，淘口令，或同时输入商品+优惠券链接")

    private String content;
    @ApiModelProperty("优惠券查询请求path")
    private final String requestPath = "/dels/taobao/kit/coupon/get-coupon-info";

    @Override
    public Map<String, String> getTextParams() throws IllegalAccessException {
        return ObjectUtil.objToMap(this);
    }

    @Override
    public String apiVersion() {
        return this.version;
    }

    @Override
    public TypeReference<DtkApiResponse<DtkCouponQueryResponse>> responseType() {
        return new TypeReference<DtkApiResponse<DtkCouponQueryResponse>>() {
        };
    }

    @Override
    public String requestUrl() {
        return this.requestPath;
    }
}
