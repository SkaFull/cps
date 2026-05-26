package com.ruoyi.union.dtk.api.request.special;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ruoyi.union.dtk.api.client.DtkApiRequest;
import com.ruoyi.union.dtk.api.response.base.DtkApiResponse;
import com.ruoyi.union.dtk.api.response.special.DtkGetHalfPriceDayResponse;
import com.ruoyi.union.dtk.api.utils.ObjectUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 每日半价请求参数实体
 *
 * @author 1
 * @date 2020/11/10 18:13
 */
@Getter
@Setter
public class DtkGetHalfPriceDayRequest implements DtkApiRequest<DtkApiResponse<DtkGetHalfPriceDayResponse>> {
    @ApiModelProperty(value = "版本号", example = "v1.0.0")
    private String version = "v1.0.0";
    @ApiModelProperty(value = "默认为当前场次，场次输入格式，例如02、08、12、13", required = true)
    private String sessions;
    @ApiModelProperty("每日半价请求path")
    private final String requestPath = "/goods/get-half-price-day";

    @Override
    public Map<String, String> getTextParams() throws IllegalAccessException {
        return ObjectUtil.objToMap(this);
    }

    @Override
    public String apiVersion() {
        return this.version;
    }

    @Override
    public TypeReference<DtkApiResponse<DtkGetHalfPriceDayResponse>> responseType() {
        return new TypeReference<DtkApiResponse<DtkGetHalfPriceDayResponse>>() {
        };
    }

    @Override
    public String requestUrl() {
        return this.requestPath;
    }
}
