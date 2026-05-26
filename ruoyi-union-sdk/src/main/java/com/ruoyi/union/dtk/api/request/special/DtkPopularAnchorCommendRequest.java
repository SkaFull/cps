package com.ruoyi.union.dtk.api.request.special;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ruoyi.union.dtk.api.client.DtkApiRequest;
import com.ruoyi.union.dtk.api.request.base.DtkPageParamRequest;
import com.ruoyi.union.dtk.api.response.base.DtkApiResponse;
import com.ruoyi.union.dtk.api.response.base.DtkPageResponse;
import com.ruoyi.union.dtk.api.response.special.DtkPopularAnchorCommendResponse;
import com.ruoyi.union.dtk.api.utils.ObjectUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 热门主播力荐商品请求参数实体
 *
 * @author 1
 * @date 2020/11/10 18:22
 */
@Getter
@Setter
public class DtkPopularAnchorCommendRequest extends DtkPageParamRequest implements DtkApiRequest<DtkApiResponse<DtkPageResponse<DtkPopularAnchorCommendResponse>>> {
    @ApiModelProperty(value = "版本号", example = "v1.0.0")
    private String version = "v1.0.0";
    @ApiModelProperty("热门主播力荐商品请求path")
    private final String requestPath = "/live/goods-list";

    @Override
    public Map<String, String> getTextParams() throws IllegalAccessException {
        return ObjectUtil.objToMap(this);
    }

    @Override
    public String apiVersion() {
        return this.version;
    }

    @Override
    public TypeReference<DtkApiResponse<DtkPageResponse<DtkPopularAnchorCommendResponse>>> responseType() {
        return new TypeReference<DtkApiResponse<DtkPageResponse<DtkPopularAnchorCommendResponse>>>() {
        };
    }

    @Override
    public String requestUrl() {
        return this.requestPath;
    }
}
