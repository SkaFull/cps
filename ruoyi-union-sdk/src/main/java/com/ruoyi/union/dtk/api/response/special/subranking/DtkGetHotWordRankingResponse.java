package com.ruoyi.union.dtk.api.response.special.subranking;

import lombok.Data;

/**
 * 各大榜单响应结果热词飙升榜实体
 *
 * @author baige
 * @date 2020/11/12 17:55
 */
@Data
public class DtkGetHotWordRankingResponse extends DtkRankingBaseResponse {
    private Integer fresh;
    private Integer lowest;
    private Integer activityType;
    private Integer score;
}
