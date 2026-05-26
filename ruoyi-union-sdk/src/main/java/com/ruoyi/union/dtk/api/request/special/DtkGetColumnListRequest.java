package com.ruoyi.union.dtk.api.request.special;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ruoyi.union.dtk.api.client.DtkApiRequest;
import com.ruoyi.union.dtk.api.request.base.DtkPageParamRequest;
import com.ruoyi.union.dtk.api.response.base.DtkApiResponse;
import com.ruoyi.union.dtk.api.response.base.DtkDiffPageResponse;
import com.ruoyi.union.dtk.api.response.special.DtkGetColumnListResponse;
import com.ruoyi.union.dtk.api.utils.ObjectUtil;
import com.ruoyi.union.dtk.api.utils.RequiredCheck;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 品牌栏目请求参数实体
 *
 * @author 1
 * @date 2020/11/30 17:27
 */
@Getter
@Setter
public class DtkGetColumnListRequest extends DtkPageParamRequest implements DtkApiRequest<DtkApiResponse<DtkDiffPageResponse<DtkGetColumnListResponse>>> {
    @ApiModelProperty(value = "版本号", example = "v1.0.0")
    private String version = "v1.0.0";
    @RequiredCheck
    @ApiModelProperty(value = "大淘客分类id")
    private Integer cid;
    @ApiModelProperty("品牌栏目请求path")
    private final String requestPath = "/delanys/brand/get-column-list";

    @Override
    public Map<String, String> getTextParams() throws IllegalAccessException {
        return ObjectUtil.objToMap(this);
    }

    @Override
    public String apiVersion() {
        return this.version;
    }

    @Override
    public TypeReference<DtkApiResponse<DtkDiffPageResponse<DtkGetColumnListResponse>>> responseType() {
        return new TypeReference<DtkApiResponse<DtkDiffPageResponse<DtkGetColumnListResponse>>>() {
        };
    }

    @Override
    public String requestUrl() {
        return this.requestPath;
    }
}
