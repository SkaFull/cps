<template>
  <div>
    <el-table v-loading="loading" :data="promotionList">
      <el-table-column label="图片" align="center" width="80">
        <template slot-scope="scope">
          <el-image
            style="width: 60px; height: 60px"
            :src="scope.row.shopPictureUrl"
            :preview-src-list="[scope.row.shopPictureUrl]"
            fit="cover"
          />
        </template>
      </el-table-column>
      <el-table-column label="卖家昵称" align="center" prop="nick" :show-overflow-tooltip="true" width="180" />
      <el-table-column label="店铺名称" align="center" prop="shopTitle" :show-overflow-tooltip="true" width="180" />
      <el-table-column label="权益类型" align="center" prop="promotionType" width="100" />
      <el-table-column label="权益信息" align="center" width="180">
        <template slot-scope="scope">
          <div v-if="scope.row.promotionList && scope.row.promotionList.length > 0">
            <el-tag
              v-for="(promo, index) in scope.row.promotionList"
              :key="index"
              type="danger"
              size="small"
              style="margin: 2px"
            >
              满{{ promo.entryCondition }}减{{ promo.entryDiscount }}
            </el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="权益时间" align="center" width="200">
        <template slot-scope="scope">
          <div v-if="scope.row.promotionList && scope.row.promotionList.length > 0">
            <div
              v-for="(promo, index) in scope.row.promotionList"
              :key="index"
              style="color: #409EFF; font-size: 12px; margin: 2px 0"
            >
              始:{{ formatTime(promo.entryUsedStartTime) }} 止:{{ formatTime(promo.entryUsedEndTime) }}
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="剩余库存" align="center" prop="remainCount" width="100" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleVisit(scope.row)"
          >访问</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { dgOptimusPromotion } from "@/api/union/tbk";

export default {
  name: "PromotionList",
  props: {
    promotionId: {
      type: String,
      default: ""
    }
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 权益列表
      promotionList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        adzoneId: "110571800148",
        promotionId: this.promotionId
      }
    };
  },
  watch: {
    promotionId: {
      handler() {
        this.queryParams.promotionId = this.promotionId;
        this.getList();
      },
      immediate: true
    }
  },
  methods: {
    /** 查询权益列表 */
    getList() {
      this.loading = true;
      dgOptimusPromotion(this.queryParams).then(response => {
        const data = response.data;
        if (data && data.tbkDgOptimusPromotionResponse && data.tbkDgOptimusPromotionResponse.resultList) {
          this.promotionList = data.tbkDgOptimusPromotionResponse.resultList.mapData || [];
          this.total = data.tbkDgOptimusPromotionResponse.totalResults || 0;
        } else {
          this.promotionList = [];
          this.total = 0;
        }
        this.loading = false;
      }).catch(() => {
        this.loading = false;
      });
    },
    /** 格式化时间 */
    formatTime(timeStr) {
      if (!timeStr) return '';
      // timeStr格式示例: "2024-12-29 00:00:00"
      return timeStr.substring(0, 10);
    },
    /** 访问店铺 */
    handleVisit(row) {
      if (row.promotionExtend && row.promotionExtend.promotionUrl) {
        window.open(row.promotionExtend.promotionUrl, '_blank');
      }
    }
  }
};
</script>
