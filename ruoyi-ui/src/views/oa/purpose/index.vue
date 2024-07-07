<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="项目名称" prop="purposeTitle">
        <el-input
          v-model="queryParams.purposeTitle"
          placeholder="请输入项目名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="采购单位" prop="subhead">
        <el-input
          v-model="queryParams.subhead"
          placeholder="请输入采购单位"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="投标状态" prop="isBid">
        <el-select v-model="queryParams.isBid" placeholder="请选择投标状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_purpose_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['oa:purpose:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['oa:purpose:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['oa:purpose:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['oa:purpose:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="purposeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <!--      <el-table-column label="项目id" align="center" prop="purposeId" v-if="true"/>-->
      <el-table-column label="项目名称" align="left" prop="purposeTitle"/>
      <el-table-column label="采购单位" align="left" prop="subhead" width="280"/>
      <el-table-column label="预算金额" align="center" prop="budget" width="150"/>
      <el-table-column label="预计采购时间" align="center" prop="purposeTime" width="120" />
      <el-table-column label="创建者" align="center" prop="createBy" width="100"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="投标状态" align="center" prop="isBid" width="80">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_purpose_status" :value="scope.row.isBid"/>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" width="180"/>
      <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleQueryInfo(scope.row)"
            v-hasPermi="['oa:purpose:query']"
          >查看
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['oa:purpose:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['oa:purpose:remove']"
          >删除
          </el-button>
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

    <!--查看详情对话框-->
    <el-dialog :visible.sync="openQuery" width="50%" append-to-body>
      <el-descriptions :title="formQuery.purposeTitle" :column="4" border>
        <el-descriptions-item label="项目名称" span="2" :labelStyle="lableBg">{{formQuery.purposeTitle}}</el-descriptions-item>
        <el-descriptions-item label="采购单位" span="2" :labelStyle="lableBg">{{formQuery.subhead}}</el-descriptions-item>
        <el-descriptions-item label="预算金额" span="2" :labelStyle="lableBg">{{formQuery.budget}}</el-descriptions-item>
        <el-descriptions-item label="预计采购时间" span="2" :labelStyle="lableBg">{{formQuery.purposeTime}} </el-descriptions-item>
        <el-descriptions-item label="采购需求概况" span="4" :labelStyle="lableBg">
          <div v-html = "formQuery.content"></div>
        </el-descriptions-item>
        <el-descriptions-item label="备注" span="4" :labelStyle="lableBg">
          {{formQuery.remark}}
        </el-descriptions-item>
        <el-descriptions-item label="附件" span="4" :labelStyle="lableBg">
          <!--附件-->
          <template v-if="formQuery.accessory">
            <ImageOss :name="formQuery.accessory"/>
          </template>
          <template v-else>
            <span style="color: #999999">暂无附件</span>
          </template>
          <!--附件-->
        </el-descriptions-item>
      </el-descriptions>

    </el-dialog>

    <!-- 添加或修改采购意向对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="60%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="项目名称" prop="purposeTitle">
              <el-input v-model="form.purposeTitle" placeholder="请输入项目名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="采购单位" prop="subhead">
              <el-input v-model="form.subhead" placeholder="请输入采购单位"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预算金额" prop="budget">
              <el-input v-model="form.budget" placeholder="请输入预算金额"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预计采购时间" prop="purposeTime">
              <el-input v-model="form.purposeTime" placeholder="请输入预计采购时间"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="采购需求情况">
              <editor v-model="form.content" :min-height="192"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="投标状态" prop="isBid">
              <el-radio-group v-model="form.isBid">
                <el-radio
                  v-for="dict in dict.type.sys_purpose_status"
                  :key="dict.value"
                  :label="dict.value"
                >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea"  placeholder="请输入备注"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="附件" prop="accessory">
              <file-upload v-model="form.accessory"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listPurpose, getPurpose, delPurpose, addPurpose, updatePurpose} from "@/api/oa/purpose";

export default {
  name: "Purpose",
  dicts: ['sys_purpose_status'],
  data() {
    return {
      // 按钮loading
      buttonLoading: false,
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
      //详情lable背景
      lableBg: "background: #f0f9eb; width:150px; text-align: center;",
      // 总条数
      total: 0,
      // 采购意向表格数据
      purposeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      openQuery: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        purposeTitle: undefined,
        subhead: undefined,
        isBid: undefined,
      },
      //查看详情
      formQuery: {},
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        purposeId: [
          {required: true, message: "项目id不能为空", trigger: "blur"}
        ],
        purposeTitle: [
          {required: true, message: "项目名称不能为空", trigger: "blur"}
        ],
        subhead: [
          {required: true, message: "采购单位不能为空", trigger: "blur"}
        ],
        budget: [
          {required: true, message: "预算金额不能为空", trigger: "blur"}
        ],
        content: [
          {required: true, message: "采购需求情况不能为空", trigger: "blur"}
        ],
        purposeTime: [
          {required: true, message: "预计采购时间不能为空", trigger: "blur"}
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询采购意向列表 */
    getList() {
      this.loading = true;
      listPurpose(this.queryParams).then(response => {
        this.purposeList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        purposeId: undefined,
        purposeTitle: undefined,
        subhead: undefined,
        budget: undefined,
        content: undefined,
        purposeTime: undefined,
        isBid: undefined,
        accessory: undefined,
        remark: undefined,
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined
      };
      this.resetForm("form");
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
      this.ids = selection.map(item => item.purposeId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加采购意向";
    },
    /**查看按钮操作**/
    handleQueryInfo(row) {
      this.loading = true;
      this.reset();
      const purposeId = row.purposeId
      getPurpose(purposeId).then(response => {
        this.loading = false;
        this.formQuery = response.data;
        this.openQuery = true;
        this.title = "查看详情";
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const purposeId = row.purposeId || this.ids
      getPurpose(purposeId).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改采购意向";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.purposeId != null) {
            updatePurpose(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addPurpose(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const purposeIds = row.purposeId || this.ids;
      this.$modal.confirm('是否确认删除采购意向编号为"' + purposeIds + '"的数据项？').then(() => {
        this.loading = true;
        return delPurpose(purposeIds);
      }).then(() => {
        this.loading = false;
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('oa/purpose/export', {
        ...this.queryParams
      }, `purpose_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
