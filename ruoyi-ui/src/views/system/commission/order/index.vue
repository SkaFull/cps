<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="订单号" prop="orderId">
        <el-input
          v-model="queryParams.orderId"
          placeholder="请输入订单号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="平台类型" prop="platformType">
        <el-select v-model="queryParams.platformType" placeholder="请选择平台" clearable>
          <el-option label="淘宝客" value="tbk" />
          <el-option label="京东" value="jd" />
          <el-option label="拼多多" value="pdd" />
        </el-select>
      </el-form-item>
      <el-form-item label="订单状态" prop="orderStatus">
        <el-select v-model="queryParams.orderStatus" placeholder="请选择状态" clearable>
          <el-option label="待结算" value="pending" />
          <el-option label="已结算" value="settled" />
          <el-option label="已退款" value="refunded" />
        </el-select>
      </el-form-item>
      <el-form-item label="下单时间">
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
          v-hasPermi="['system:orderCommission:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList">
      <el-table-column label="订单号" align="center" prop="orderId" :show-overflow-tooltip="true" width="180" />
      <el-table-column label="平台" align="center" prop="platformType" width="80">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.platformType === 'tbk'" type="warning">淘宝</el-tag>
          <el-tag v-else-if="scope.row.platformType === 'jd'" type="danger">京东</el-tag>
          <el-tag v-else-if="scope.row.platformType === 'pdd'" type="success">拼多多</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="商品标题" align="center" prop="itemTitle" :show-overflow-tooltip="true" width="200" />
      <el-table-column label="订单金额" align="center" prop="orderAmount" width="100">
        <template slot-scope="scope">
          <span style="color: #E6A23C;">¥{{ scope.row.orderAmount }}</span>
        </template>
      </el-table-column>
      <el-table-column label="总佣金" align="center" prop="totalCommission" width="100">
        <template slot-scope="scope">
          <span style="color: #67C23A; font-weight: bold;">¥{{ scope.row.totalCommission }}</span>
        </template>
      </el-table-column>
      <el-table-column label="自购佣金" align="center" prop="selfCommissionAmount" width="110">
        <template slot-scope="scope">
          <span style="color: #409EFF;">¥{{ scope.row.selfCommissionAmount }} ({{ scope.row.selfCommissionRate }}%)</span>
        </template>
      </el-table-column>
      <el-table-column label="推广佣金" align="center" prop="promotionCommissionAmount" width="110">
        <template slot-scope="scope">
          <span v-if="scope.row.hasReferrer" style="color: #E6A23C;">
            ¥{{ scope.row.promotionCommissionAmount }} ({{ scope.row.promotionCommissionRate }}%)
          </span>
          <span v-else style="color: #909399;">无</span>
        </template>
      </el-table-column>
      <el-table-column label="订单状态" align="center" prop="orderStatus" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.orderStatus === 'pending'" type="info">待结算</el-tag>
          <el-tag v-else-if="scope.row.orderStatus === 'settled'" type="success">已结算</el-tag>
          <el-tag v-else-if="scope.row.orderStatus === 'refunded'" type="danger">已退款</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="下单时间" align="center" prop="orderTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.orderTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.orderStatus === 'pending'"
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleSettle(scope.row)"
            v-hasPermi="['system:orderCommission:settle']"
          >结算</el-button>
          <el-button
            v-if="scope.row.orderStatus === 'settled'"
            size="mini"
            type="text"
            icon="el-icon-refresh-left"
            @click="handleRefund(scope.row)"
            v-hasPermi="['system:orderCommission:refund']"
          >退款</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
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

    <!-- 订单结算对话框 -->
    <el-dialog title="订单结算" :visible.sync="settleOpen" width="500px" append-to-body>
      <el-form ref="settleForm" :model="settleForm" :rules="settleRules" label-width="120px">
        <el-form-item label="订单号">
          <el-input v-model="settleForm.orderId" :disabled="true" />
        </el-form-item>
        <el-form-item label="预估佣金">
          <el-input v-model="settleForm.totalCommission" :disabled="true">
            <template slot="append">元</template>
          </el-input>
        </el-form-item>
        <el-form-item label="实际佣金" prop="actualCommission">
          <el-input-number v-model="settleForm.actualCommission" :min="0" :precision="2" :step="0.01" />
          <span style="margin-left: 10px; color: #909399;">元</span>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="settleForm.settleRemark" type="textarea" placeholder="请输入结算备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitSettle">确认结算</el-button>
        <el-button @click="settleOpen = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 订单详情对话框 -->
    <el-dialog title="订单详情" :visible.sync="detailOpen" width="800px" append-to-body>
      <el-descriptions :column="2" border v-if="orderDetail">
        <el-descriptions-item label="订单号">{{ orderDetail.orderId }}</el-descriptions-item>
        <el-descriptions-item label="平台类型">
          <el-tag v-if="orderDetail.platformType === 'tbk'" type="warning">淘宝客</el-tag>
          <el-tag v-else-if="orderDetail.platformType === 'jd'" type="danger">京东</el-tag>
          <el-tag v-else-if="orderDetail.platformType === 'pdd'" type="success">拼多多</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="商品标题" :span="2">{{ orderDetail.itemTitle }}</el-descriptions-item>
        <el-descriptions-item label="订单金额">¥{{ orderDetail.orderAmount }}</el-descriptions-item>
        <el-descriptions-item label="总佣金">¥{{ orderDetail.totalCommission }}</el-descriptions-item>
        <el-descriptions-item label="自购佣金">
          ¥{{ orderDetail.selfCommissionAmount }} ({{ orderDetail.selfCommissionRate }}%)
        </el-descriptions-item>
        <el-descriptions-item label="推广佣金">
          <span v-if="orderDetail.hasReferrer">
            ¥{{ orderDetail.promotionCommissionAmount }} ({{ orderDetail.promotionCommissionRate }}%)
          </span>
          <span v-else>无</span>
        </el-descriptions-item>
        <el-descriptions-item label="平台分成">¥{{ orderDetail.platformAmount }}</el-descriptions-item>
        <el-descriptions-item label="订单状态">
          <el-tag v-if="orderDetail.orderStatus === 'pending'" type="info">待结算</el-tag>
          <el-tag v-else-if="orderDetail.orderStatus === 'settled'" type="success">已结算</el-tag>
          <el-tag v-else-if="orderDetail.orderStatus === 'refunded'" type="danger">已退款</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="下单用户ID">{{ orderDetail.buyerUserId }}</el-descriptions-item>
        <el-descriptions-item label="是否有推荐人">
          <el-tag v-if="orderDetail.hasReferrer" type="success">是</el-tag>
          <el-tag v-else type="info">否</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="推荐人用户ID" v-if="orderDetail.hasReferrer">
          {{ orderDetail.referrerUserId }}
        </el-descriptions-item>
        <el-descriptions-item label="下单时间" :span="2">{{ parseTime(orderDetail.orderTime) }}</el-descriptions-item>
        <el-descriptions-item label="结算时间" :span="2" v-if="orderDetail.settleTime">
          {{ parseTime(orderDetail.settleTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2">{{ parseTime(orderDetail.createTime) }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listOrderCommission, getOrderCommission, settleOrder, refundOrder } from "@/api/system/orderCommission";

export default {
  name: "OrderCommission",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 订单佣金表格数据
      orderList: [],
      // 日期范围
      dateRange: [],
      // 结算对话框
      settleOpen: false,
      // 详情对话框
      detailOpen: false,
      // 订单详情
      orderDetail: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderId: null,
        platformType: null,
        orderStatus: null
      },
      // 结算表单
      settleForm: {},
      // 结算表单校验
      settleRules: {
        actualCommission: [
          { required: true, message: "实际佣金不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询订单佣金列表 */
    getList() {
      this.loading = true;
      listOrderCommission(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.orderList = response.rows;
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
    /** 结算按钮操作 */
    handleSettle(row) {
      this.settleForm = {
        id: row.id,
        orderId: row.orderId,
        totalCommission: row.totalCommission,
        actualCommission: row.totalCommission,
        settleRemark: ''
      };
      this.settleOpen = true;
    },
    /** 提交结算 */
    submitSettle() {
      this.$refs["settleForm"].validate(valid => {
        if (valid) {
          settleOrder(this.settleForm.id, this.settleForm).then(response => {
            this.$modal.msgSuccess("订单结算成功");
            this.settleOpen = false;
            this.getList();
          });
        }
      });
    },
    /** 退款按钮操作 */
    handleRefund(row) {
      this.$modal.confirm('确认对订单"' + row.orderId + '"进行退款操作？').then(function() {
        return refundOrder(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("退款成功");
      }).catch(() => {});
    },
    /** 详情按钮操作 */
    handleView(row) {
      getOrderCommission(row.id).then(response => {
        this.orderDetail = response.data;
        this.detailOpen = true;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/orderCommission/export', {
        ...this.queryParams
      }, `order_commission_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
