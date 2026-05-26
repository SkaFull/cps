<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="代理用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入代理用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="代理级别" prop="agentLevel">
        <el-select v-model="queryParams.agentLevel" placeholder="请选择代理级别" clearable>
          <el-option
            v-for="dict in dict.type.agent_level"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="账户状态" prop="accountStatus">
        <el-select v-model="queryParams.accountStatus" placeholder="请选择账户状态" clearable>
          <el-option label="正常" value="0" />
          <el-option label="冻结" value="1" />
        </el-select>
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
          v-hasPermi="['system:commission:account:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="accountList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="账户ID" align="center" prop="accountId" width="80" />
      <el-table-column label="用户ID" align="center" prop="userId" width="80" />
      <el-table-column label="用户昵称" align="center" prop="nickName" width="120" />
      <el-table-column label="代理级别" align="center" prop="agentLevel" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.agent_level" :value="scope.row.agentLevel"/>
        </template>
      </el-table-column>
      <el-table-column label="总收益" align="center" prop="totalEarnings" width="120">
        <template slot-scope="scope">
          <span style="color: #67C23A; font-weight: bold;">¥{{ scope.row.totalEarnings }}</span>
        </template>
      </el-table-column>
      <el-table-column label="可用余额" align="center" prop="availableBalance" width="120">
        <template slot-scope="scope">
          <span style="color: #409EFF; font-weight: bold;">¥{{ scope.row.availableBalance }}</span>
        </template>
      </el-table-column>
      <el-table-column label="冻结金额" align="center" prop="frozenAmount" width="120">
        <template slot-scope="scope">
          <span style="color: #E6A23C;">¥{{ scope.row.frozenAmount }}</span>
        </template>
      </el-table-column>
      <el-table-column label="已提现金额" align="center" prop="withdrawnAmount" width="120">
        <template slot-scope="scope">
          <span style="color: #909399;">¥{{ scope.row.withdrawnAmount }}</span>
        </template>
      </el-table-column>
      <el-table-column label="自购订单数" align="center" prop="selfOrderCount" width="100" />
      <el-table-column label="推广订单数" align="center" prop="promotionOrderCount" width="100" />
      <el-table-column label="账户状态" align="center" prop="accountStatus" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.accountStatus === '0' ? 'success' : 'danger'">
            {{ scope.row.accountStatus === '0' ? '正常' : '冻结' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
            v-hasPermi="['system:commission:account:query']"
          >详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-money"
            @click="handleFreeze(scope.row)"
            v-hasPermi="['system:commission:account:edit']"
          >{{ scope.row.accountStatus === '0' ? '冻结' : '解冻' }}</el-button>
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

    <!-- 账户详情对话框 -->
    <el-dialog title="账户详情" :visible.sync="detailOpen" width="800px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="账户ID">{{ detailData.accountId }}</el-descriptions-item>
        <el-descriptions-item label="用户ID">{{ detailData.userId }}</el-descriptions-item>
        <el-descriptions-item label="用户昵称">{{ detailData.nickName }}</el-descriptions-item>
        <el-descriptions-item label="代理级别">
          <dict-tag :options="dict.type.agent_level" :value="detailData.agentLevel"/>
        </el-descriptions-item>
        <el-descriptions-item label="总收益">
          <span style="color: #67C23A; font-weight: bold; font-size: 16px;">¥{{ detailData.totalEarnings }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="可用余额">
          <span style="color: #409EFF; font-weight: bold; font-size: 16px;">¥{{ detailData.availableBalance }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="冻结金额">
          <span style="color: #E6A23C; font-size: 16px;">¥{{ detailData.frozenAmount }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="已提现金额">
          <span style="color: #909399; font-size: 16px;">¥{{ detailData.withdrawnAmount }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="自购订单数">{{ detailData.selfOrderCount }}</el-descriptions-item>
        <el-descriptions-item label="推广订单数">{{ detailData.promotionOrderCount }}</el-descriptions-item>
        <el-descriptions-item label="账户状态">
          <el-tag :type="detailData.accountStatus === '0' ? 'success' : 'danger'">
            {{ detailData.accountStatus === '0' ? '正常' : '冻结' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="上次结算时间">
          {{ parseTime(detailData.lastSettlementTime, '{y}-{m}-{d} {h}:{i}:{s}') || '暂无' }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2">
          {{ parseTime(detailData.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}
        </el-descriptions-item>
        <el-descriptions-item label="更新时间" :span="2">
          {{ parseTime(detailData.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}
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
import { listAccount, getAccount, freezeAccount, unfreezeAccount } from "@/api/system/commissionAccount";

export default {
  name: "CommissionAccount",
  dicts: ['agent_level'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 佣金账户表格数据
      accountList: [],
      // 弹出层标题
      title: "",
      // 是否显示详情弹出层
      detailOpen: false,
      // 详情数据
      detailData: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        agentLevel: null,
        accountStatus: null
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询佣金账户列表 */
    getList() {
      this.loading = true;
      listAccount(this.queryParams).then(response => {
        this.accountList = response.rows;
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
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.accountId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 查看详情按钮操作 */
    handleDetail(row) {
      this.detailData = {};
      const accountId = row.accountId || this.ids[0];
      getAccount(accountId).then(response => {
        this.detailData = response.data;
        this.detailOpen = true;
      });
    },
    /** 冻结/解冻账户 */
    handleFreeze(row) {
      const accountId = row.accountId;
      const isFreeze = row.accountStatus === '0';
      const text = isFreeze ? '冻结' : '解冻';

      this.$modal.confirm('是否确认' + text + '账户ID为"' + accountId + '"的佣金账户？').then(() => {
        const apiCall = isFreeze ? freezeAccount(accountId) : unfreezeAccount(accountId);
        return apiCall;
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(text + "成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/commission/account/export', {
        ...this.queryParams
      }, `佣金账户_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
