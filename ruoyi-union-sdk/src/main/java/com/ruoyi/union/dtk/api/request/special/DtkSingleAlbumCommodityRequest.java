package com.ruoyi.union.dtk.api.request.special;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ruoyi.union.dtk.api.client.DtkApiRequest;
import com.ruoyi.union.dtk.api.response.DtkSingleAlbumCommodityResponse;
import com.ruoyi.union.dtk.api.response.base.DtkApiResponse;
import com.ruoyi.union.dtk.api.utils.ObjectUtil;
import com.ruoyi.union.dtk.api.utils.RequiredCheck;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 单个专辑商品列表请求参数实体
 *
 * @author 1
 * @date 2020/11/10 18:22
 */
@Getter
@Setter
public class DtkSingleAlbumCommodityRequest implements DtkApiRequest<DtkApiResponse<DtkSingleAlbumCommodityResponse>> {
    @ApiModelProperty(value = "版本号", example = "v1.0.0")
    private String version = "v1.0.0";
    @RequiredCheck
    @ApiModelProperty("专辑id。618活动期间（5.24-6.20），可获取活动商品专辑")
    private Integer albumId;
    @ApiModelProperty("单个专辑商品列表path")
    private final String requestPath = "/album/goods-list";

    @Override
    public Map<String, String> getTextParams() throws IllegalAccessException {
        return ObjectUtil.objToMap(this);
    }

    @Override
    public String apiVersion() {
        return this.version;
    }

    @Override
    public TypeReference<DtkApiResponse<DtkSingleAlbumCommodityResponse>> responseType() {
        return new TypeReference<DtkApiResponse<DtkSingleAlbumCommodityResponse>>() {
        };
    }

    @Override
    public String requestUrl() {
        return this.requestPath;
    }
}
