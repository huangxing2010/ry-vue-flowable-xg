<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="事件名称" prop="taskTitle">
        <el-input
          v-model="queryParams.taskTitle"
          placeholder="请输入提醒名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
<!--      <el-form-item label="办理时间" prop="taskTime">
        <el-date-picker clearable
                        v-model="queryParams.taskTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择办理时间">
        </el-date-picker>
      </el-form-item>-->
      <el-form-item label="待办类型" prop="taskStatus">
        <el-select v-model="queryParams.status" placeholder="请选择办理状态" clearable>
          <el-option
            v-for="dict in taskStatus"
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
<!--      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['oa:remind:add']"
        >新增</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['oa:remind:edit']"
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
          v-hasPermi="['oa:remind:remove']"
        >删除</el-button>
      </el-col>
<!--      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['oa:remind:export']"
        >导出</el-button>
      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="remindList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
<!--      <el-table-column label="提醒ID" align="center" prop="remindId" v-if="true"/>-->
      <el-table-column label="事件名称" align="left" prop="taskTitle" />
      <el-table-column label="事件内容" align="left" prop="content" />
      <el-table-column label="办理时间" align="center" prop="taskTime" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.taskTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="办理状态" align="center" prop="taskStatus" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_task_status" :value="scope.row.taskStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" width="180" />
      <el-table-column label="创建者" align="center" prop="createBy" width="100"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleLook(scope.row)"
            v-hasPermi="['oa:remind:edit']"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['oa:remind:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['oa:remind:remove']"
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

    <!-- 查看事件提醒对话框 -->
    <el-dialog :title="title" :visible.sync="openLook" width="800px" append-to-body>
      <el-descriptions :column="4" border>
        <el-descriptions-item label="事件名称" :labelStyle="lableBg" span="4">
          {{formLook.taskTitle}}
        </el-descriptions-item>
        <el-descriptions-item label="事件内容" :labelStyle="lableBg" span="4">
          {{formLook.content}}
        </el-descriptions-item>
        <el-descriptions-item label="待办时间" :labelStyle="lableBg" span="4">
          {{parseTime(formLook.taskTime, '{y}-{m}-{d}')}}
        </el-descriptions-item>
        <el-descriptions-item label="附件" :labelStyle="lableBg" span="4">
          <!--附件-->
          <template v-if="formLook.accessory">
            <ImageOss :name="formLook.accessory"/>
          </template>
          <template v-else>
            <span style="color: #999999">暂无附件</span>
          </template>
          <!--附件-->
        </el-descriptions-item>

      </el-descriptions>

    </el-dialog>

    <!-- 添加或修改事件提醒对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="事件名称" prop="taskTitle">
          <el-input v-model="form.taskTitle" placeholder="请输入提醒名称" />
        </el-form-item>
        <el-form-item label="事件内容" prop="content">
          <el-input v-model="form.content" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="待办时间" prop="taskTime">
          <el-date-picker clearable
                          v-model="form.taskTime"
                          type="date"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="请选择办理时间">
          </el-date-picker>
        </el-form-item>

        <el-form-item label="附件" prop="accessory">
          <file-upload v-model="form.accessory"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRemind, getRemind, delRemind, addRemind, updateRemind } from "@/api/oa/remind";

export default {
  name: "Remind",
  dicts: ['sys_task_status'],
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
      // 总条数
      total: 0,
      // 事件提醒表格数据
      remindList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      openLook: false,
      //搜索栏办理状态
      taskStatus:[
        {value: '0', label: '今日待办'},
        {value: '1', label: '所有待办'},
        {value: '2', label: '逾期未办'},
        {value: '3', label: '历史已办'},
      ],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        taskTitle: undefined,
        taskStatus: undefined,
      },
      formLook: {},
      //详情lable背景
      lableBg: "background: #f0f9eb; width:120px; text-align: center;",
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        taskTitle: [
          { required: true, message: "提醒名称不能为空", trigger: "blur" }
        ],
        content: [
          { required: true, message: "事件内容不能为空", trigger: "blur" }
        ],
        taskTime: [
          { required: true, message: "办理时间不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询事件提醒列表 */
    getList() {
      this.loading = true;
      let now = new Date();
      //今日待办
      if(this.queryParams.status == '0'){
        this.queryParams.params = {};
        this.queryParams.taskStatus = '0'
        this.queryParams.params["beginTime"] = this.parseTime(now, '{y}-{m}-{d}') + ' 00:00:00';
        this.queryParams.params["endTime"] = this.parseTime(now, '{y}-{m}-{d}') + ' 23:59:59';
      }
      //所有待办
      if(this.queryParams.status == '1'){
        this.queryParams.params = {};
        this.queryParams.taskStatus = '0'
      }
      //逾期未办
      if(this.queryParams.status == '2'){
        this.queryParams.params = {};
        this.queryParams.taskStatus = '0'
        this.queryParams.params["beginTime"] = this.parseTime(new Date('2020-01-01'), '{y}-{m}-{d}') + ' 00:00:00';
        this.queryParams.params["endTime"] = this.parseTime(now, '{y}-{m}-{d}') + ' 23:59:59';
      }
      //历史已办
      if(this.queryParams.status == '3'){
        this.queryParams.params = {};
        this.queryParams.taskStatus = '1'
      }
      listRemind(this.queryParams).then(response => {
        this.remindList = response.rows;
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
        remindId: undefined,
        taskTitle: undefined,
        content: undefined,
        taskTime: undefined,
        taskStatus: undefined,
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
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        taskTitle: undefined,
        taskStatus: undefined
      }
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.remindId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加事件提醒";
    },
    /** 查看按钮操作 */
    handleLook(row){
      this.loading = true;
      this.reset();
      const remindId = row.remindId || this.ids
      getRemind(remindId).then(response => {
        this.loading = false;
        this.formLook = response.data;
        this.openLook = true;
        this.title = "查看历史事件";
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const remindId = row.remindId || this.ids
      getRemind(remindId).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改事件提醒";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.remindId != null) {
            updateRemind(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addRemind(this.form).then(response => {
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
      const remindIds = row.remindId || this.ids;
      this.$modal.confirm('是否确认删除事件提醒编号为"' + remindIds + '"的数据项？').then(() => {
        this.loading = true;
        return delRemind(remindIds);
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
      this.download('oa/remind/export', {
        ...this.queryParams
      }, `remind_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
