package com.ruoyi.union.dtk.api.request.mastertool;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ruoyi.union.dtk.api.client.DtkApiRequest;
import com.ruoyi.union.dtk.api.response.base.DtkApiResponse;
import com.ruoyi.union.dtk.api.response.mastertool.DtkCommodityMaterialsResponse;
import com.ruoyi.union.dtk.api.utils.ObjectUtil;
import com.ruoyi.union.dtk.api.utils.RequiredCheck;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 商品精推素材请求参数实体
 *
 * @author 1
 * @date 2021/07/07 14:27
 */
@Getter
@Setter
public class DtkCommodityMaterialsRequest implements DtkApiRequest<DtkApiResponse<List<DtkCommodityMaterialsResponse>>> {
    @ApiModelProperty(value = "版本号", example = "v1.0.0")
    private String version = "1.0.0";

    @RequiredCheck
    @ApiModelProperty(value = "大淘客商品id或联盟商品id")
    private String id;

    @ApiModelProperty("商品精推素材转链请求path")
    private final String requestPath = "/goods/material/list";

    @Override
    public Map<String, String> getTextParams() throws IllegalAccessException {
        return ObjectUtil.objToMap(this);
    }

    @Override
    public String apiVersion() {
        return this.version;
    }

    @Override
    public TypeReference<DtkApiResponse<List<DtkCommodityMaterialsResponse>>> responseType() {
        return new TypeReference<DtkApiResponse<List<DtkCommodityMaterialsResponse>>>() {
        };
    }

    @Override
    public String requestUrl() {
        return this.requestPath;
    }
}
