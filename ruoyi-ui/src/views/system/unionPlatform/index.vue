<template>
  <div class="app-container">
    <!-- 搜索条件 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="联盟类型" prop="platformType">
        <el-select v-model="queryParams.platformType" placeholder="请选择联盟类型" clearable>
          <el-option label="淘宝客" value="tbk" />
          <el-option label="京东" value="jd" />
          <el-option label="拼多多" value="pdd" />
          <el-option label="大淘客" value="dtk" />
        </el-select>
      </el-form-item>
      <el-form-item label="App Key" prop="appKey">
        <el-input
          v-model="queryParams.appKey"
          placeholder="请输入App Key"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="使用状态" prop="isQuote">
        <el-select v-model="queryParams.isQuote" placeholder="请选择使用状态" clearable>
          <el-option label="未被应用" value="0" />
          <el-option label="被使用" value="1" />
          <el-option label="失效" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:unionPlatform:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:unionPlatform:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:unionPlatform:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="unionPlatformList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="联盟ID" align="center" prop="id" width="80" />
      <el-table-column label="联盟类型" align="center" prop="platformType" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.platformType === 'tbk'" type="success">淘宝客</el-tag>
          <el-tag v-else-if="scope.row.platformType === 'jd'" type="primary">京东</el-tag>
          <el-tag v-else-if="scope.row.platformType === 'pdd'" type="warning">拼多多</el-tag>
          <el-tag v-else-if="scope.row.platformType === 'dtk'" type="info">大淘客</el-tag>
          <el-tag v-else>未知</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="联盟名称" align="center" prop="platformName" width="150" :show-overflow-tooltip="true" />
      <el-table-column label="App Key" align="center" prop="appKey" width="180" :show-overflow-tooltip="true" />
      <el-table-column label="站点ID" align="center" prop="siteId" width="120" :show-overflow-tooltip="true" />
      <el-table-column label="推广位ID" align="center" prop="adzoneId" width="120" :show-overflow-tooltip="true" />
      <el-table-column label="使用状态" align="center" prop="isQuote" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isQuote === '0'">未被应用</el-tag>
          <el-tag v-else-if="scope.row.isQuote === '1'" type="success">被使用</el-tag>
          <el-tag v-else-if="scope.row.isQuote === '2'" type="danger">失效</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="160" fixed="right">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:unionPlatform:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
            v-hasPermi="['system:unionPlatform:query']"
          >详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            style="color: #F56C6C"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:unionPlatform:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 新增/修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="联盟类型" prop="platformType">
          <el-select v-model="form.platformType" placeholder="请选择联盟类型" style="width: 100%">
            <el-option label="淘宝客" value="tbk" />
            <el-option label="京东" value="jd" />
            <el-option label="拼多多" value="pdd" />
            <el-option label="大淘客" value="dtk" />
          </el-select>
        </el-form-item>
        <el-form-item label="联盟名称" prop="platformName">
          <el-input v-model="form.platformName" placeholder="请输入联盟名称（用于标识此联盟账号）" />
        </el-form-item>
        <el-form-item label="App Key" prop="appKey">
          <el-input v-model="form.appKey" placeholder="请输入App Key" />
        </el-form-item>
        <el-form-item label="App Secret" prop="appSecret">
          <el-input v-model="form.appSecret" placeholder="请输入App Secret" show-password />
        </el-form-item>
        <el-form-item label="站点ID" prop="siteId">
          <el-input v-model="form.siteId" placeholder="请输入站点ID（京东联盟必填）" />
        </el-form-item>
        <el-form-item label="推广位ID" prop="adzoneId">
          <el-input v-model="form.adzoneId" placeholder="请输入推广位ID" />
        </el-form-item>
        <el-form-item label="访问令牌" prop="accessToken">
          <el-input v-model="form.accessToken" placeholder="请输入访问令牌（如需）" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="联盟信息详情" :visible.sync="detailOpen" width="600px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="联盟ID">{{ detailData.id }}</el-descriptions-item>
        <el-descriptions-item label="联盟类型">
          <el-tag v-if="detailData.platformType === 'tbk'" type="success">淘宝客</el-tag>
          <el-tag v-else-if="detailData.platformType === 'jd'" type="primary">京东</el-tag>
          <el-tag v-else-if="detailData.platformType === 'pdd'" type="warning">拼多多</el-tag>
          <el-tag v-else-if="detailData.platformType === 'dtk'" type="info">大淘客</el-tag>
          <el-tag v-else>未知</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="联盟名称" :span="2">{{ detailData.platformName || '无' }}</el-descriptions-item>
        <el-descriptions-item label="App Key" :span="2">{{ detailData.appKey }}</el-descriptions-item>
        <el-descriptions-item label="站点ID">{{ detailData.siteId || '无' }}</el-descriptions-item>
        <el-descriptions-item label="推广位ID">{{ detailData.adzoneId || '无' }}</el-descriptions-item>
        <el-descriptions-item label="使用状态">
          <el-tag v-if="detailData.isQuote === '0'">未被应用</el-tag>
          <el-tag v-else-if="detailData.isQuote === '1'" type="success">被使用</el-tag>
          <el-tag v-else-if="detailData.isQuote === '2'" type="danger">失效</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建人">{{ detailData.createBy }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(detailData.createTime) }}</el-descriptions-item>
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
import {
  listUnionPlatform,
  getUnionPlatform,
  addUnionPlatform,
  updateUnionPlatform,
  delUnionPlatform
} from "@/api/system/unionPlatform";

export default {
  name: "UnionPlatform",
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
      // 联盟信息表格数据
      unionPlatformList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 详情对话框
      detailOpen: false,
      // 详情数据
      detailData: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        platformType: null,
        appKey: null,
        isQuote: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        platformType: [
          { required: true, message: "联盟类型不能为空", trigger: "change" }
        ],
        platformName: [
          { required: true, message: "联盟名称不能为空", trigger: "blur" }
        ],
        appKey: [
          { required: true, message: "App Key不能为空", trigger: "blur" }
        ],
        appSecret: [
          { required: true, message: "App Secret不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询联盟信息列表 */
    getList() {
      this.loading = true;
      listUnionPlatform(this.queryParams).then(response => {
        this.unionPlatformList = response.rows;
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
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 重置表单 */
    reset() {
      this.form = {
        id: null,
        platformType: null,
        platformName: null,
        appKey: null,
        appSecret: null,
        siteId: null,
        adzoneId: null,
        accessToken: null,
        remark: null
      };
      this.resetForm("form");
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.title = "新增联盟信息";
      this.open = true;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids[0];
      getUnionPlatform(id).then(response => {
        this.form = response.data;
        this.title = "修改联盟信息";
        this.open = true;
      });
    },
    /** 详情按钮操作 */
    handleDetail(row) {
      getUnionPlatform(row.id).then(response => {
        this.detailData = response.data;
        this.detailOpen = true;
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateUnionPlatform(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addUnionPlatform(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 取消按钮 */
    cancel() {
      this.open = false;
      this.reset();
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除联盟信息编号为"' + ids + '"的数据项？').then(() => {
        return delUnionPlatform(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    }
  }
};
</script>
