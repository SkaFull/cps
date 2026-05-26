package com.ruoyi.union.dtk.api.request.special;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ruoyi.union.dtk.api.client.DtkApiRequest;
import com.ruoyi.union.dtk.api.request.base.DtkPageParamRequest;
import com.ruoyi.union.dtk.api.response.base.DtkApiResponse;
import com.ruoyi.union.dtk.api.response.special.DtkGetBrandListResponse;
import com.ruoyi.union.dtk.api.utils.ObjectUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 品牌库请求参数实体
 *
 * @author 1
 * @date 2020/11/10 18:20
 */
@Getter
@Setter
public class DtkGetBrandListRequest extends DtkPageParamRequest implements DtkApiRequest<DtkApiResponse<List<DtkGetBrandListResponse>>> {
    @ApiModelProperty(value = "版本号", example = "v1.1.2")
    private String version = "v1.1.2";
    @ApiModelProperty("品牌库请求path")
    private final String requestPath = "/tb-service/get-brand-list";

    @Override
    public Map<String, String> getTextParams() throws IllegalAccessException {
        return ObjectUtil.objToMap(this);
    }

    @Override
    public String apiVersion() {
        return this.version;
    }

    @Override
    public TypeReference<DtkApiResponse<List<DtkGetBrandListResponse>>> responseType() {
        return new TypeReference<DtkApiResponse<List<DtkGetBrandListResponse>>>() {
        };
    }

    @Override
    public String requestUrl() {
        return this.requestPath;
    }
}
