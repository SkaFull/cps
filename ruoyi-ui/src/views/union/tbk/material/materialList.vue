<template>
  <div>
    <el-table v-loading="loading" :data="materialList">
      <el-table-column label="物料集合名称" align="center" prop="materialName" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="物料ID" align="center" prop="materialId" width="120" />
      <el-table-column label="物料类型" align="center" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.materialType === 1" type="primary">商品</el-tag>
          <el-tag v-else-if="scope.row.materialType === 2" type="danger">权益</el-tag>
          <el-tag v-else type="info">未知</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="物料主题类型" align="center" width="120">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.subject === 1" type="primary">促销活动</el-tag>
          <el-tag v-else-if="scope.row.subject === 2" type="success">热门主题</el-tag>
          <el-tag v-else-if="scope.row.subject === 3" type="warning">精选榜单</el-tag>
          <el-tag v-else-if="scope.row.subject === 4" type="danger">行业频道</el-tag>
          <el-tag v-else-if="scope.row.subject === 5" type="info">其他</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="时间" align="center" width="240">
        <template slot-scope="scope">
          <span style="color: #409EFF">{{ formatTime(scope.row.startTime) }} - {{ formatTime(scope.row.endTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleViewItems(scope.row)"
          >查看商品</el-button>
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

    <!-- 商品列表对话框 -->
    <el-dialog title="物料商品列表" :visible.sync="itemDialogVisible" width="90%" append-to-body>
      <material-item-list v-if="itemDialogVisible" :materialId="currentMaterialId" />
    </el-dialog>
  </div>
</template>

<script>
import { optimusTouMaterialIdsGet } from "@/api/union/tbk";
import MaterialItemList from './materialItemList.vue';

export default {
  name: "MaterialList",
  components: {
    MaterialItemList
  },
  props: {
    subject: {
      type: Number,
      required: true
    },
    materialType: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 物料列表
      materialList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        adzoneId: "110571800148",
        materialQuery: null
      },
      // 商品对话框
      itemDialogVisible: false,
      currentMaterialId: ""
    };
  },
  watch: {
    subject: {
      handler() {
        this.getList();
      },
      immediate: true
    }
  },
  methods: {
    /** 查询物料列表 */
    getList() {
      this.loading = true;
      // 构建materialQuery对象
      const materialQuery = {
        subject: this.subject,
        material_type: this.materialType
      };
      this.queryParams.materialQuery = JSON.stringify(materialQuery);

      optimusTouMaterialIdsGet(this.queryParams).then(response => {
        const data = response.data;
        if (data && data.tbkDgOptimusTouMaterialIdsGetResponse && data.tbkDgOptimusTouMaterialIdsGetResponse.resultList) {
          this.materialList = data.tbkDgOptimusTouMaterialIdsGetResponse.resultList.mapData || [];
          this.total = data.tbkDgOptimusTouMaterialIdsGetResponse.totalResults || 0;
        } else {
          this.materialList = [];
          this.total = 0;
        }
        this.loading = false;
      }).catch(() => {
        this.loading = false;
      });
    },
    /** 格式化时间戳 */
    formatTime(timestamp) {
      if (!timestamp) return '';
      const date = new Date(timestamp * 1000);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    },
    /** 查看商品 */
    handleViewItems(row) {
      this.currentMaterialId = row.materialId;
      this.itemDialogVisible = true;
    }
  }
};
</script>
