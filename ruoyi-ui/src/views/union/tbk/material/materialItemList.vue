<template>
  <div>
    <el-table v-loading="loading" :data="itemList">
      <el-table-column label="图片" align="center" width="80">
        <template slot-scope="scope">
          <el-image
            style="width: 60px; height: 60px"
            :src="scope.row.pictUrl"
            :preview-src-list="[scope.row.pictUrl]"
            fit="cover"
          />
        </template>
      </el-table-column>
      <el-table-column label="标题" align="center" prop="title" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="商家" align="center" prop="shopTitle" :show-overflow-tooltip="true" width="120" />
      <el-table-column label="分类" align="center" prop="categoryName" width="100" />
      <el-table-column label="品牌" align="center" prop="brandName" width="100" />
      <el-table-column label="优惠信息" align="center" width="180">
        <template slot-scope="scope">
          <div v-if="scope.row.finalPromotionPathList && scope.row.finalPromotionPathList.length > 0">
            <el-tag
              v-for="(promo, index) in scope.row.finalPromotionPathList"
              :key="index"
              type="danger"
              size="small"
              style="margin: 2px"
            >
              {{ promo.promotionDesc }}
            </el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="现价" align="center" prop="zkFinalPrice" width="80" />
      <el-table-column label="到手价" align="center" width="80">
        <template slot-scope="scope">
          <span style="color: #FF5722; font-weight: bold">{{ scope.row.finalPromotionPrice }}</span>
        </template>
      </el-table-column>
      <el-table-column label="佣金" align="center" width="80">
        <template slot-scope="scope">
          <span style="color: #67C23A; font-weight: bold">{{ scope.row.commissionAmount }}</span>
        </template>
      </el-table-column>
      <el-table-column label="销量" align="center" prop="annualVol" width="80" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleBuy(scope.row)"
          >购买</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-document-copy"
            @click="handleGetTpwd(scope.row)"
          >淘口令</el-button>
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
import { dgMaterialRecommend, getTpwd } from "@/api/union/tbk";

export default {
  name: "MaterialItemList",
  props: {
    materialId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 商品列表
      itemList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        adzoneId: "110571800148",
        materialId: this.materialId
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询商品列表 */
    getList() {
      this.loading = true;
      dgMaterialRecommend(this.queryParams).then(response => {
        const data = response.data;
        if (data && data.tbkDgMaterialRecommendResponse && data.tbkDgMaterialRecommendResponse.resultList) {
          this.itemList = data.tbkDgMaterialRecommendResponse.resultList.mapData || [];
          this.total = data.tbkDgMaterialRecommendResponse.totalResults || 0;
        } else {
          this.itemList = [];
          this.total = 0;
        }
        this.loading = false;
      }).catch(() => {
        this.loading = false;
      });
    },
    /** 购买商品 */
    handleBuy(row) {
      const url = row.couponShareUrl || row.clickUrl;
      window.open(url, '_blank');
    },
    /** 获取淘口令 */
    handleGetTpwd(row) {
      let itemUrl = row.clickUrl;
      if (row.couponShareUrl) {
        itemUrl = row.couponShareUrl;
      }
      const params = {
        text: row.title,
        url: "https:" + itemUrl,
        logo: row.pictUrl
      };
      getTpwd(params).then(response => {
        if (response.data && response.data.model) {
          this.$modal.msgSuccess("淘口令：" + response.data.model);
          // 复制到剪贴板
          this.copyToClipboard(response.data.model);
        } else {
          this.$modal.msgError("获取淘口令失败");
        }
      });
    },
    /** 复制到剪贴板 */
    copyToClipboard(text) {
      const textarea = document.createElement('textarea');
      textarea.value = text;
      document.body.appendChild(textarea);
      textarea.select();
      try {
        document.execCommand('copy');
        this.$modal.msgSuccess('已复制到剪贴板');
      } catch (err) {
        console.error('复制失败:', err);
      }
      document.body.removeChild(textarea);
    }
  }
};
</script>
