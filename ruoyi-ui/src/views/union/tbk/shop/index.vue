<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="搜索关键词" prop="q">
        <el-input
          v-model="queryParams.q"
          placeholder="请输入店铺关键词"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="shopList">
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
      <el-table-column label="卖家昵称" align="center" prop="sellerNick" :show-overflow-tooltip="true" width="200" />
      <el-table-column label="店铺类型" align="center" prop="shopType" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.shopType === 'B'" type="primary">天猫</el-tag>
          <el-tag v-else-if="scope.row.shopType === 'C'" type="danger">淘宝</el-tag>
          <el-tag v-else type="info">未知</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="店铺名称" align="center" prop="shopTitle" :show-overflow-tooltip="true" width="200" />
      <el-table-column label="卖家ID" align="center" prop="userId" width="200" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleVisit(scope.row)"
          >访问店铺</el-button>
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
import { getShop } from "@/api/union/tbk";

export default {
  name: "TbkShop",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 店铺表格数据
      shopList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        q: "官方旗舰店",
        adzoneId: "110571800148",
        isTmall: true,
        sort: "commission_rate_desc"
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询店铺列表 */
    getList() {
      this.loading = true;
      getShop(this.queryParams).then(response => {
        const data = response.data;
        console.log(JSON.stringify(data.results));
        if (data && data.results) {
          this.shopList = data.results || [];
          this.total = data.totalResults || 0;
        } else {
          this.shopList = [];
          this.total = 0;
        }
        this.loading = false;
      }).catch(() => {
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.queryParams.q = "官方旗舰店";
      this.handleQuery();
    },
    /** 访问店铺 */
    handleVisit(row) {
      window.open(row.shopUrl, '_blank');
    }
  }
};
</script>
