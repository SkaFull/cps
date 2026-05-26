<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="账户ID" prop="accountId">
        <el-input
          v-model="queryParams.accountId"
          placeholder="请输入账户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="流水类型" prop="flowType">
        <el-select v-model="queryParams.flowType" placeholder="请选择流水类型" clearable>
          <el-option label="收入" value="income" />
          <el-option label="支出" value="expense" />
        </el-select>
      </el-form-item>
      <el-form-item label="业务类型" prop="businessType">
        <el-select v-model="queryParams.businessType" placeholder="请选择业务类型" clearable>
          <el-option label="自购佣金" value="self_commission" />
          <el-option label="推广佣金" value="promotion_commission" />
          <el-option label="提现" value="withdraw" />
          <el-option label="退款" value="refund" />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:commission:flow:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="flowList">
      <el-table-column label="流水ID" align="center" prop="flowId" width="80" />
      <el-table-column label="账户ID" align="center" prop="accountId" width="80" />
      <el-table-column label="用户ID" align="center" prop="userId" width="80" />
      <el-table-column label="用户昵称" align="center" prop="nickName" width="120" />
      <el-table-column label="流水类型" align="center" prop="flowType" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.flowType === 'income' ? 'success' : 'warning'">
            {{ scope.row.flowType === 'income' ? '收入' : '支出' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="业务类型" align="center" prop="businessType" width="120">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.businessType === 'self_commission'" type="primary">自购佣金</el-tag>
          <el-tag v-else-if="scope.row.businessType === 'promotion_commission'" type="success">推广佣金</el-tag>
          <el-tag v-else-if="scope.row.businessType === 'withdraw'" type="warning">提现</el-tag>
          <el-tag v-else-if="scope.row.businessType === 'refund'" type="danger">退款</el-tag>
          <el-tag v-else type="info">{{ scope.row.businessType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="金额" align="center" prop="amount" width="120">
        <template slot-scope="scope">
          <span :style="{color: scope.row.flowType === 'income' ? '#67C23A' : '#E6A23C', fontWeight: 'bold'}">
            {{ scope.row.flowType === 'income' ? '+' : '-' }}¥{{ scope.row.amount }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="变动前余额" align="center" prop="balanceBefore" width="120">
        <template slot-scope="scope">
          <span>¥{{ scope.row.balanceBefore }}</span>
        </template>
      </el-table-column>
      <el-table-column label="变动后余额" align="center" prop="balanceAfter" width="120">
        <template slot-scope="scope">
          <span style="color: #409EFF; font-weight: bold;">¥{{ scope.row.balanceAfter }}</span>
        </template>
      </el-table-column>
      <el-table-column label="关联订单ID" align="center" prop="relatedOrderId" width="120" />
      <el-table-column label="业务描述" align="center" prop="businessDescription" :show-overflow-tooltip="true" width="200" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
            v-hasPermi="['system:commission:flow:query']"
          >详情</el-button>
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

    <!-- 流水详情对话框 -->
    <el-dialog title="流水详情" :visible.sync="detailOpen" width="700px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="流水ID">{{ detailData.flowId }}</el-descriptions-item>
        <el-descriptions-item label="账户ID">{{ detailData.accountId }}</el-descriptions-item>
        <el-descriptions-item label="用户ID">{{ detailData.userId }}</el-descriptions-item>
        <el-descriptions-item label="用户昵称">{{ detailData.nickName }}</el-descriptions-item>
        <el-descriptions-item label="流水类型">
          <el-tag :type="detailData.flowType === 'income' ? 'success' : 'warning'">
            {{ detailData.flowType === 'income' ? '收入' : '支出' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="业务类型">
          <el-tag v-if="detailData.businessType === 'self_commission'" type="primary">自购佣金</el-tag>
          <el-tag v-else-if="detailData.businessType === 'promotion_commission'" type="success">推广佣金</el-tag>
          <el-tag v-else-if="detailData.businessType === 'withdraw'" type="warning">提现</el-tag>
          <el-tag v-else-if="detailData.businessType === 'refund'" type="danger">退款</el-tag>
          <el-tag v-else type="info">{{ detailData.businessType }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="金额">
          <span :style="{color: detailData.flowType === 'income' ? '#67C23A' : '#E6A23C', fontWeight: 'bold', fontSize: '18px'}">
            {{ detailData.flowType === 'income' ? '+' : '-' }}¥{{ detailData.amount }}
          </span>
        </el-descriptions-item>
        <el-descriptions-item label="变动前余额">
          <span style="font-size: 16px;">¥{{ detailData.balanceBefore }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="变动后余额">
          <span style="color: #409EFF; font-weight: bold; font-size: 16px;">¥{{ detailData.balanceAfter }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="关联订单ID">{{ detailData.relatedOrderId || '无' }}</el-descriptions-item>
        <el-descriptions-item label="业务描述" :span="2">{{ detailData.businessDescription }}</el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2">
          {{ parseTime(detailData.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark || '无' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listFlow, getFlow } from "@/api/system/commissionFlow";

export default {
  name: "CommissionFlow",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 佣金流水表格数据
      flowList: [],
      // 是否显示详情弹出层
      detailOpen: false,
      // 详情数据
      detailData: {},
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        accountId: null,
        flowType: null,
        businessType: null
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询佣金流水列表 */
    getList() {
      this.loading = true;
      listFlow(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.flowList = response.rows;
        this.total = response.total;
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
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 查看详情按钮操作 */
    handleDetail(row) {
      this.detailData = {};
      const flowId = row.flowId;
      getFlow(flowId).then(response => {
        this.detailData = response.data;
        this.detailOpen = true;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/commission/flow/export', {
        ...this.queryParams
      }, `佣金流水_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
