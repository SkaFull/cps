<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="申请人姓名" prop="realName">
        <el-input
          v-model="queryParams.realName"
          placeholder="请输入申请人姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="联系电话" prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入联系电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="邀请码" prop="invitationCode">
        <el-input
          v-model="queryParams.invitationCode"
          placeholder="请输入邀请码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="审核状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择审核状态" clearable>
          <el-option label="待审核" value="0" />
          <el-option label="已生效" value="1" />
          <el-option label="已失效" value="2" />
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
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:tbkApply:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="tbkApplyList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="申请ID" align="center" prop="id" width="80" />
      <el-table-column label="申请人姓名" align="center" prop="realName" width="120" />
      <el-table-column label="用户名" align="center" width="120">
        <template slot-scope="scope">
          {{ scope.row.user ? scope.row.user.userName : '-' }}
        </template>
      </el-table-column>
      <el-table-column label="用户昵称" align="center" width="120">
        <template slot-scope="scope">
          {{ scope.row.user ? scope.row.user.nickName : '-' }}
        </template>
      </el-table-column>
      <el-table-column label="联系电话" align="center" prop="phone" width="130" />
      <el-table-column label="微信号" align="center" prop="wechat" width="130" />
      <el-table-column label="小程序AppID" align="center" prop="miniAppId" width="180" :show-overflow-tooltip="true" />
      <el-table-column label="邀请码" align="center" prop="invitationCode" width="120" />
      <el-table-column label="申请联盟类型" align="center" prop="applyPlatformTypes" width="180">
        <template slot-scope="scope">
          <span v-if="!scope.row.applyPlatformTypes">-</span>
          <el-tag
            v-else
            v-for="type in scope.row.applyPlatformTypes.split(',')"
            :key="type"
            :type="getPlatformTagType(type)"
            size="mini"
            style="margin: 2px"
          >
            {{ getPlatformTypeName(type) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="审核状态" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === '0'" type="warning">待审核</el-tag>
          <el-tag v-else-if="scope.row.status === '1'" type="success">已生效</el-tag>
          <el-tag v-else-if="scope.row.status === '2'" type="danger">已失效</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="审核人" align="center" prop="auditBy" width="100" />
      <el-table-column label="审核时间" align="center" prop="auditTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.auditTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="申请时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="250" fixed="right">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status === '0'"
            size="mini"
            type="success"
            icon="el-icon-check"
            @click="handleAudit(scope.row, '1')"
            v-hasPermi="['system:tbkApply:audit']"
          >通过</el-button>
          <el-button
            v-if="scope.row.status === '0'"
            size="mini"
            type="danger"
            icon="el-icon-close"
            @click="handleAudit(scope.row, '2')"
            v-hasPermi="['system:tbkApply:audit']"
          >拒绝</el-button>
          <el-button
            v-if="scope.row.status === '1'"
            size="mini"
            type="warning"
            icon="el-icon-lock"
            @click="handleChangeStatus(scope.row, '2')"
            v-hasPermi="['system:tbkApply:edit']"
          >禁用</el-button>
          <el-button
            v-if="scope.row.status === '2'"
            size="mini"
            type="success"
            icon="el-icon-unlock"
            @click="handleChangeStatus(scope.row, '1')"
            v-hasPermi="['system:tbkApply:edit']"
          >启用</el-button>
          <el-button
            v-if="scope.row.status === '1'"
            size="mini"
            type="primary"
            icon="el-icon-connection"
            @click="handleAssign(scope.row)"
            v-hasPermi="['system:tbkApply:assign']"
          >分配联盟</el-button>
          <el-button
            size="mini"
            type="primary"
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
            v-hasPermi="['system:tbkApply:query']"
          >详情</el-button>
          <el-button
            size="mini"
            type="danger"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:tbkApply:remove']"
          >删除</el-button>
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

    <!-- 审核对话框 -->
    <el-dialog :title="auditTitle" :visible.sync="auditOpen" width="600px" append-to-body>
      <el-form ref="auditForm" :model="auditForm" :rules="auditRules" label-width="100px">
        <el-form-item label="申请人姓名">
          <el-input v-model="auditForm.realName" disabled />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="auditForm.phone" disabled />
        </el-form-item>
        <el-form-item label="微信号">
          <el-input v-model="auditForm.wechat" disabled />
        </el-form-item>
        <el-form-item label="审核备注" prop="remark" v-if="auditForm.status === '2'">
          <el-input v-model="auditForm.remark" type="textarea" :rows="4" placeholder="请输入拒绝原因" />
        </el-form-item>
        <el-form-item label="审核备注" prop="remark" v-else>
          <el-input v-model="auditForm.remark" type="textarea" :rows="4" placeholder="请输入审核备注（可选）" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="auditOpen = false">取 消</el-button>
        <el-button type="primary" @click="submitAudit">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 分配联盟对话框 -->
    <el-dialog title="分配联盟信息" :visible.sync="assignOpen" width="1000px" append-to-body>
      <div style="margin-bottom: 16px; padding: 12px; background: #f5f7fa; border-radius: 4px">
        <span style="font-size: 14px; color: #606266">申请人：</span>
        <span style="font-size: 14px; font-weight: bold">{{ assignRow.realName }}</span>
        <span style="margin-left: 20px; font-size: 13px; color: #909399">每种联盟类型只能分配一个账号</span>
      </div>

      <!-- 使用Tab标签按联盟类型分组显示 -->
      <el-tabs v-model="activeTabType" type="border-card">
        <el-tab-pane
          v-for="type in platformTypes"
          :key="type"
          :name="type"
        >
          <span slot="label">
            <el-tag :type="getPlatformTagType(type)" size="small" effect="plain">
              {{ getPlatformTypeName(type) }}
            </el-tag>
            <el-badge
              v-if="getAssignedByType(type)"
              is-dot
              class="item"
              style="margin-left: 5px"
            />
          </span>

          <!-- 已分配的联盟（表格展示） -->
          <div v-if="getAssignedByType(type)" style="margin-bottom: 12px">
            <div style="font-size: 13px; font-weight: bold; margin-bottom: 8px; color: #303133">
              <i class="el-icon-check" style="color: #67C23A"></i> 已分配联盟
            </div>
            <el-table :data="[getAssignedByType(type)]" size="mini" border>
              <el-table-column label="联盟ID" prop="unionPlatformId" width="80" align="center" />
              <el-table-column label="联盟名称" min-width="120" align="center">
                <template slot-scope="scope">
                  {{ scope.row.unionPlatform.platformName || '-' }}
                </template>
              </el-table-column>
              <el-table-column label="App Key" min-width="150">
                <template slot-scope="scope">{{ scope.row.unionPlatform.appKey }}</template>
              </el-table-column>
              <el-table-column label="站点ID" width="120" align="center">
                <template slot-scope="scope">{{ scope.row.unionPlatform.siteId || '-' }}</template>
              </el-table-column>
              <el-table-column label="推广位ID" width="120" align="center">
                <template slot-scope="scope">{{ scope.row.unionPlatform.adzoneId }}</template>
              </el-table-column>
              <el-table-column label="备注" min-width="150" align="center">
                <template slot-scope="scope">
                  {{ scope.row.unionPlatform.remark || '-' }}
                </template>
              </el-table-column>
              <el-table-column label="分配时间" prop="createTime" width="140" align="center">
                <template slot-scope="scope">{{ parseTime(scope.row.unionPlatform.createTime) }}</template>
              </el-table-column>
              <el-table-column label="操作" width="280" align="center">
                <template slot-scope="scope">
                  <!-- PDD授权按钮 -->
                  <el-button
                    v-if="type === 'pdd'"
                    size="mini"
                    :type="scope.row.authStatus === '1' ? 'success' : 'primary'"
                    :icon="scope.row.authStatus === '1' ? 'el-icon-link' : 'el-icon-link'"
                    @click="scope.row.authStatus === '1' ? showAuthUrl(scope.row) : handlePddAuth(scope.row)"
                    v-hasPermi="['system:tbkApply:pddAuth']"
                  >{{ scope.row.authStatus === '1' ? '授权URL' : '授权备案' }}</el-button>
                  <el-button
                    size="mini"
                    type="warning"
                    icon="el-icon-edit"
                    @click="handleChangeUnion(type)"
                    v-hasPermi="['system:tbkApply:assign']"
                  >更换</el-button>
                  <el-button
                    size="mini"
                    type="danger"
                    icon="el-icon-delete"
                    @click="handleCancelTypeAssign(type)"
                    v-hasPermi="['system:tbkApply:cancel']"
                  >取消</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <!-- 可选联盟列表（未分配或正在更换时显示） -->
          <div v-if="!getAssignedByType(type) || changingType === type">
            <div style="font-size: 13px; font-weight: bold; margin-bottom: 8px; color: #303133">
              <i class="el-icon-s-grid"></i>
              {{ changingType === type ? '选择新的联盟账号' : '请选择要分配的联盟账号' }}
            </div>
            <el-table
              :data="getAvailableByType(type)"
              size="mini"
              border
              highlight-current-row
              @current-change="(row) => handleCurrentChange(type, row)"
            >
              <el-table-column width="55" align="center">
                <template slot-scope="scope">
                  <el-radio
                    :label="scope.row.id"
                    v-model="selectedUnionByType[type]"
                    @change="() => {}"
                  >{{/* 不显示label文本 */}}</el-radio>
                </template>
              </el-table-column>
              <el-table-column label="联盟ID" prop="id" width="80" align="center" />
              <el-table-column label="联盟名称" min-width="120" align="center">
                <template slot-scope="scope">
                  {{ scope.row.platformName || '-' }}
                </template>
              </el-table-column>
              <el-table-column label="App Key" prop="appKey" min-width="150" :show-overflow-tooltip="true" />
              <el-table-column label="站点ID" prop="siteId" width="120" align="center">
                <template slot-scope="scope">{{ scope.row.siteId || '-' }}</template>
              </el-table-column>
              <el-table-column label="推广位ID" prop="adzoneId" width="120" align="center" />
              <el-table-column label="备注" min-width="150" align="center">
                <template slot-scope="scope">
                  {{ scope.row.remark || '-' }}
                </template>
              </el-table-column>
              <el-table-column label="状态" prop="isQuote" width="90" align="center">
                <template slot-scope="scope">
                  <el-tag v-if="scope.row.isQuote === '0'" size="mini">未使用</el-tag>
                  <el-tag v-else-if="scope.row.isQuote === '1'" type="success" size="mini">使用中</el-tag>
                  <el-tag v-else-if="scope.row.isQuote === '2'" type="danger" size="mini">失效</el-tag>
                </template>
              </el-table-column>
            </el-table>
            <div v-if="getAvailableByType(type).length === 0" style="text-align: center; padding: 20px; color: #909399; font-size: 14px; background: #f5f7fa; border-radius: 4px">
              暂无可用的{{ getPlatformTypeName(type) }}联盟
            </div>
            <div v-if="changingType === type" style="margin-top: 8px; text-align: right">
              <el-button size="mini" @click="cancelChange">取消</el-button>
              <el-button
                size="mini"
                type="primary"
                @click="confirmChange(type)"
                :disabled="!selectedUnionByType[type]"
              >确认更换</el-button>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>

      <div slot="footer" class="dialog-footer">
        <el-button @click="assignOpen = false">关 闭</el-button>
        <el-button
          type="primary"
          @click="submitAssign"
          v-if="hasUnassignedTypes"
        >确定分配</el-button>
        <el-button
          type="danger"
          icon="el-icon-delete"
          @click="handleCancelAllAssign"
          v-if="assignedList.length > 0"
          v-hasPermi="['system:tbkApply:cancel']"
        >取消全部分配</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="申请详情" :visible.sync="detailOpen" width="800px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="申请ID">{{ detailData.id }}</el-descriptions-item>
        <el-descriptions-item label="用户ID">{{ detailData.userId }}</el-descriptions-item>
        <el-descriptions-item label="申请人姓名">{{ detailData.realName }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detailData.phone }}</el-descriptions-item>
        <el-descriptions-item label="微信号">{{ detailData.wechat }}</el-descriptions-item>
        <el-descriptions-item label="邀请码">{{ detailData.invitationCode }}</el-descriptions-item>
        <el-descriptions-item label="代理等级">
          <el-tag type="primary" size="small">Lv.{{ detailData.agentLevel || 1 }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="已邀请">
          <span style="color: #409EFF; font-weight: bold">{{ detailData.invitedCount || 0 }}</span> 人
        </el-descriptions-item>
        <el-descriptions-item label="邀请上限">
          <span style="color: #67C23A; font-weight: bold">{{ detailData.inviteLimit || 0 }}</span> 人
        </el-descriptions-item>
        <el-descriptions-item label="剩余邀请">
          <span style="color: #E6A23C; font-weight: bold">{{ detailData.remainingInvites || 0 }}</span> 人
        </el-descriptions-item>
        <el-descriptions-item label="申请联盟类型" :span="2">
          <span v-if="!detailData.applyPlatformTypes">-</span>
          <el-tag
            v-else
            v-for="type in detailData.applyPlatformTypes.split(',')"
            :key="type"
            :type="getPlatformTagType(type)"
            size="small"
            style="margin-right: 8px"
          >
            {{ getPlatformTypeName(type) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="小程序AppID" :span="2">{{ detailData.miniAppId }}</el-descriptions-item>
        <el-descriptions-item label="审核状态">
          <el-tag v-if="detailData.status === '0'" type="warning">待审核</el-tag>
          <el-tag v-else-if="detailData.status === '1'" type="success">已生效</el-tag>
          <el-tag v-else-if="detailData.status === '2'" type="danger">已失效</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="审核人">{{ detailData.auditBy }}</el-descriptions-item>
        <el-descriptions-item label="审核时间">{{ parseTime(detailData.auditTime) }}</el-descriptions-item>
        <el-descriptions-item label="审核备注" :span="2">{{ detailData.auditRemark || '无' }}</el-descriptions-item>
        <el-descriptions-item label="申请时间">{{ parseTime(detailData.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ parseTime(detailData.updateTime) }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ detailData.remark || '无' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listTbkApply, getTbkApply, delTbkApply, auditTbkApply, changeStatus } from "@/api/system/tbkApply";
import { assignUnionPlatforms, cancelAssignment, getAssignedUnions, getAvailableUnions, pddAuthRecord } from "@/api/system/unionPlatform";

export default {
  name: "TbkApply",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 淘宝客代理申请表格数据
      tbkApplyList: [],
      // 审核对话框
      auditOpen: false,
      // 审核标题
      auditTitle: "",
      // 审核表单
      auditForm: {},
      // 审核表单校验
      auditRules: {
        remark: [
          { required: true, message: "拒绝原因不能为空", trigger: "blur" }
        ]
      },
      // 详情对话框
      detailOpen: false,
      // 详情数据
      detailData: {},
      // 分配联盟对话框
      assignOpen: false,
      // 当前分配的申请行
      assignRow: {},
      // 可用联盟列表
      availableUnionList: [],
      // 已分配联盟列表
      assignedList: [],
      // 联盟类型字典数据
      union_platform_typeOptions: [],
      // 每种类型选中的联盟ID
      selectedUnionByType: {},
      // 正在更换的联盟类型
      changingType: null,
      // 当前激活的Tab标签
      activeTabType: '',
      // 存储每个relation的授权URL
      authUrlMap: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        realName: null,
        phone: null,
        invitationCode: null,
        status: null
      }
    };
  },
  computed: {
    // 从字典动态生成联盟类型列表
    platformTypes() {
      return this.union_platform_typeOptions.map(d => d.dictValue);
    },
    // 是否有未分配的类型
    hasUnassignedTypes() {
      return this.platformTypes.some(type => {
        const hasAssigned = this.getAssignedByType(type);
        const hasSelected = this.selectedUnionByType[type];
        const hasAvailable = this.getAvailableByType(type).length > 0;
        return !hasAssigned && hasSelected && hasAvailable;
      });
    }
  },
  created() {
    this.getDicts("union_platform_type").then(response => {
      this.union_platform_typeOptions = response.data;
    });
    this.getList();
  },
  methods: {
    /** 查询淘宝客代理申请列表 */
    getList() {
      this.loading = true;
      listTbkApply(this.queryParams).then(response => {
        this.tbkApplyList = response.rows;
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
      this.ids = selection.map(item => item.id)
      this.multiple = !selection.length
    },
    /** 审核按钮操作 */
    handleAudit(row, status) {
      this.auditForm = {
        id: row.id,
        realName: row.realName,
        phone: row.phone,
        wechat: row.wechat,
        status: status,
        remark: ""
      };
      this.auditTitle = status === '1' ? "审核通过" : "审核拒绝";
      this.auditOpen = true;
    },
    /** 提交审核 */
    submitAudit() {
      this.$refs["auditForm"].validate(valid => {
        if (valid) {
          // 如果是通过审核，备注可以为空
          if (this.auditForm.status === '1' || this.auditForm.remark) {
            auditTbkApply(this.auditForm.id, this.auditForm.status, this.auditForm.remark).then(response => {
              this.$modal.msgSuccess("审核成功");
              this.auditOpen = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 修改状态 */
    handleChangeStatus(row, status) {
      const text = status === "1" ? "启用" : "禁用";
      this.$modal.confirm('确认要"' + text + '""' + row.realName + '"的代理申请吗？').then(() => {
        return changeStatus(row.id, status);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess(text + "成功");
      }).catch(() => {});
    },
    /** 分配联盟按钮操作 */
    handleAssign(row) {
      this.assignRow = row;
      this.selectedUnionByType = {};
      this.availableUnionList = [];
      this.assignedList = [];
      this.changingType = null;
      // 初始化激活的Tab为第一个联盟类型
      this.activeTabType = this.platformTypes.length > 0 ? this.platformTypes[0] : '';

      // 根据申请ID加载可用联盟列表
      getAvailableUnions(row.id).then(response => {
        this.availableUnionList = response.data || [];
      });

      // 加载已分配联盟
      getAssignedUnions(row.id).then(response => {
        this.assignedList = response.data || [];
      });

      this.assignOpen = true;
    },
    /** 根据类型获取已分配的联盟 */
    getAssignedByType(type) {
      return this.assignedList.find(item => item.platformType === type);
    },
    /** 根据类型获取可用联盟列表 */
    getAvailableByType(type) {
      return this.availableUnionList.filter(item => item.platformType === type);
    },
    /** 更换联盟 */
    handleChangeUnion(type) {
      this.changingType = type;
      // 清除之前的选择
      this.$set(this.selectedUnionByType, type, null);
    },
    /** 取消更换 */
    cancelChange() {
      this.changingType = null;
      this.$set(this.selectedUnionByType, this.changingType, null);
    },
    /** 表格行点击事件 */
    handleCurrentChange(type, row) {
      if (row) {
        this.$set(this.selectedUnionByType, type, row.id);
      }
    },
    /** 确认更换 */
    confirmChange(type) {
      const newUnionId = this.selectedUnionByType[type];
      if (!newUnionId) {
        this.$modal.msgWarning("请选择要更换的联盟");
        return;
      }

      const typeName = this.getPlatformTypeName(type);
      this.$modal.confirm(`确认要更换${typeName}联盟吗？`).then(() => {
        // 调用分配接口（后端会自动处理替换逻辑）
        return assignUnionPlatforms(this.assignRow.id, [newUnionId]);
      }).then(() => {
        this.$modal.msgSuccess("更换成功");
        this.changingType = null;
        // 刷新列表
        this.handleAssign(this.assignRow);
      }).catch(() => {});
    },
    /** 取消某类型的分配 */
    handleCancelTypeAssign(type) {
      const assigned = this.getAssignedByType(type);
      if (!assigned) return;

      const typeName = this.getPlatformTypeName(type);
      this.$modal.confirm(`确认要取消${typeName}联盟的分配吗？`).then(() => {
        // 需要后端支持按联盟ID取消，暂时使用取消全部然后重新分配其他类型的方式
        // 这里先收集其他类型的联盟ID
        const otherUnionIds = this.assignedList
          .filter(item => item.platformType !== type)
          .map(item => item.unionPlatformId);

        // 先取消全部
        return cancelAssignment(this.assignRow.id).then(() => {
          // 如果还有其他类型，重新分配
          if (otherUnionIds.length > 0) {
            return assignUnionPlatforms(this.assignRow.id, otherUnionIds);
          }
        });
      }).then(() => {
        this.$modal.msgSuccess("取消分配成功");
        // 刷新列表
        this.handleAssign(this.assignRow);
      }).catch(() => {});
    },
    /** 提交分配 */
    submitAssign() {
      // 收集所有已选择的联盟ID（排除已分配的）
      const newUnionIds = [];
      this.platformTypes.forEach(type => {
        if (!this.getAssignedByType(type) && this.selectedUnionByType[type]) {
          newUnionIds.push(this.selectedUnionByType[type]);
        }
      });

      if (newUnionIds.length === 0) {
        this.$modal.msgWarning("请至少选择一个联盟进行分配");
        return;
      }

      // 合并已分配的联盟ID和新选择的联盟ID
      const existingIds = this.assignedList.map(item => item.unionPlatformId);
      const allUnionIds = [...existingIds, ...newUnionIds];

      this.$modal.confirm(`确认要分配 ${newUnionIds.length} 个新的联盟信息吗？`).then(() => {
        return assignUnionPlatforms(this.assignRow.id, allUnionIds);
      }).then(() => {
        this.$modal.msgSuccess("分配成功");
        this.assignOpen = false;
        this.getList();
      }).catch(() => {});
    },
    /** 取消全部分配 */
    handleCancelAllAssign() {
      this.$modal.confirm(`确认要取消"${this.assignRow.realName}"的全部联盟分配吗？`).then(() => {
        return cancelAssignment(this.assignRow.id);
      }).then(() => {
        this.$modal.msgSuccess("取消全部分配成功");
        // 刷新列表
        this.handleAssign(this.assignRow);
      }).catch(() => {});
    },
    /** 详情按钮操作 */
    handleDetail(row) {
      const id = row.id;
      getTbkApply(id).then(response => {
        // 合并响应数据，包含额外的统计字段
        this.detailData = {
          ...response.data,
          agentLevel: response.agentLevel,
          inviteLimit: response.inviteLimit,
          invitedCount: response.invitedCount,
          remainingInvites: response.remainingInvites
        };
        this.detailOpen = true;
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除淘宝客代理申请编号为"' + ids + '"的数据项？').then(() => {
        return delTbkApply(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 获取联盟类型标签类型（从字典的 listClass 字段获取） */
    getPlatformTagType(type) {
      const dict = this.union_platform_typeOptions.find(d => d.dictValue === type);
      return dict ? dict.listClass : '';
    },
    /** 获取联盟类型名称（从字典的 dictLabel 字段获取） */
    getPlatformTypeName(type) {
      const dict = this.union_platform_typeOptions.find(d => d.dictValue === type);
      return dict ? dict.dictLabel : type;
    },
    /** PDD授权备案 */
    handlePddAuth(relation) {
      // 检查是否已授权
      if (relation.authStatus === '1') {
        this.$modal.msgWarning("该联盟已完成授权备案，无需重复操作");
        return;
      }

      this.$modal.confirm('确认要对该拼多多联盟进行授权备案吗？授权后将生成授权链接。').then(() => {
        const loading = this.$loading({
          lock: true,
          text: '正在进行授权备案，请稍候...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });

        return pddAuthRecord(relation.id).then(response => {
          loading.close();

          if (response.code === 200) {
            const authUrl = response.data.authUrl;

            // 保存授权URL到authUrlMap
            this.$set(this.authUrlMap, relation.id, authUrl);

            // 显示授权链接对话框
            this.$alert(
              `<div style="margin-bottom: 10px;">授权备案成功！请将以下授权链接发送给用户完成授权：</div>
               <div style="padding: 10px; background: #f5f7fa; border-radius: 4px; word-break: break-all;">
                 <a href="${authUrl}" target="_blank" style="color: #409EFF;">${authUrl}</a>
               </div>
               <div style="margin-top: 10px; color: #909399; font-size: 12px;">
                 提示：用户需要点击该链接完成授权操作
               </div>`,
              '授权备案成功',
              {
                dangerouslyUseHTMLString: true,
                confirmButtonText: '我知道了',
                type: 'success'
              }
            );

            // 刷新已分配联盟列表
            this.handleAssign(this.assignRow);
          } else {
            this.$modal.msgError(response.msg || '授权备案失败');
          }
        }).catch(error => {
          loading.close();
          this.$modal.msgError(error.message || '授权备案异常，请稍后重试');
        });
      }).catch(() => {});
    },
    /** 显示授权URL */
    showAuthUrl(relation) {
      const authUrl = relation.authUrl;

      if (!authUrl) {
        this.$modal.msgWarning("未找到授权地址，请重新进行授权备案");
        return;
      }

      // 显示授权链接对话框
      this.$alert(
        `<div style="margin-bottom: 15px; font-size: 14px; color: #303133;">授权地址：</div>
         <div style="padding: 12px; background: #f5f7fa; border-radius: 4px; word-break: break-all; margin-bottom: 15px; position: relative;">
           <a href="${authUrl}" target="_blank" style="color: #409EFF; text-decoration: none;">${authUrl}</a>
         </div>
         <div style="text-align: center; margin-bottom: 15px;">
           <button
             onclick="
               const url = '${authUrl}';
               if (navigator.clipboard && navigator.clipboard.writeText) {
                 navigator.clipboard.writeText(url).then(() => {
                   this.$message.success('授权URL已复制到剪贴板');
                 }).catch(() => {
                   const textarea = document.createElement('textarea');
                   textarea.value = url;
                   textarea.style.position = 'fixed';
                   textarea.style.opacity = '0';
                   document.body.appendChild(textarea);
                   textarea.select();
                   document.execCommand('copy');
                   document.body.removeChild(textarea);
                   this.$message.success('授权URL已复制到剪贴板');
                 });
               } else {
                 const textarea = document.createElement('textarea');
                 textarea.value = url;
                 textarea.style.position = 'fixed';
                 textarea.style.opacity = '0';
                 document.body.appendChild(textarea);
                 textarea.select();
                 document.execCommand('copy');
                 document.body.removeChild(textarea);
                 alert('授权URL已复制到剪贴板');
               }
             "
             style="
               padding: 10px 20px;
               background: #409EFF;
               color: white;
               border: none;
               border-radius: 4px;
               cursor: pointer;
               font-size: 14px;
               transition: background 0.3s;
             "
             onmouseover="this.style.background='#66b1ff'"
             onmouseout="this.style.background='#409EFF'"
           >
             <i class="el-icon-document-copy" style="margin-right: 5px;"></i>
             复制授权URL
           </button>
         </div>
         <div style="padding: 10px; background: #fff4e6; border-left: 4px solid #ff9800; color: #ff6f00; font-size: 13px;">
           <i class="el-icon-warning" style="margin-right: 5px;"></i>
           <strong>重要提示：</strong>请拷贝到手机上授权打开才能授权成功
         </div>`,
        '授权URL',
        {
          dangerouslyUseHTMLString: true,
          confirmButtonText: '我知道了',
          type: 'info',
          customClass: 'auth-url-dialog'
        }
      );
    }
  }
};
</script>
