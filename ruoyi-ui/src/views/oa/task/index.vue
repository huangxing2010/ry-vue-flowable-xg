<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryFormProject" size="small" :inline="true" label-width="68px">
      <el-form-item label="项目名称" prop="projectName">
        <el-input
          v-model="queryParams.projectName"
          placeholder="请输入项目名称"
          clearable
        />
      </el-form-item>
      <el-form-item label="项目编号" prop="projectNum">
        <el-input
          v-model="queryParams.projectNum"
          placeholder="请输入项目编号"
          clearable
        />
      </el-form-item>
      <el-form-item label="项目类型" prop="projectType">
        <el-select v-model="queryParams.projectType" placeholder="请选择项目类型" clearable>
          <el-option
            v-for="dict in dict.type.sys_project_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="projectType">
        <el-select v-model="queryParams.projectStatus" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_project_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="日期范围">
        <el-date-picker
          v-model="searchTime"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="['00:00:00', '23:59:59']">
        </el-date-picker>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQueryProject">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQueryProject">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="projectList">
      <el-table-column label="项目名称" align="left" prop="projectName"/>
      <el-table-column label="项目编号" align="left" prop="projectNum"/>
      <el-table-column label="项目总金额" align="center" width="120" prop="funds"  />
      <el-table-column label="项目类型" align="center" prop="projectType" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_project_type" :value="scope.row.projectType"/>
        </template>
      </el-table-column>
      <el-table-column label="履约保证金" align="center" prop="bail" width="150">
        <template slot-scope="scope">
          <div v-if="scope.row.bail == '{}' ">
            未缴纳
          </div>
          <div v-else style="color: red">
            <div v-if="JSON.parse(scope.row.bail).money != '' " style="color: #1c84c6">
              已缴纳
              -
              <span v-if="JSON.parse(scope.row.bail).backMoney != '' " style="color: #1ab394">已退回</span>
              <span  v-else style="color: #ff9900">未退回</span>
            </div>
            <div v-else>未缴纳</div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="负责人" align="center" prop="functionary" width="100"/>
      <el-table-column label="创建者" align="center" prop="createBy" width="120"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="projectStatus" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_project_status" :value="scope.row.projectStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="250" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-copy-document"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['oa:project:edit']"
          >任务管理
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

    <el-dialog :title="titleDialog" :visible.sync="taskDialog" width="75%">
      <el-form :model="taskParams" ref="queryForm" size="small" :inline="true" label-width="68px">
        <el-form-item label="任务主题" prop="taskTitle">
          <el-input
            v-model="taskParams.taskTitle"
            placeholder="请输入任务主题"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="工作类型" prop="taskType">
          <el-select v-model="taskParams.taskType" placeholder="请选择工作类型" clearable>
            <el-option
              v-for="dict in dict.type.sys_work_type"
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


      <div style="color: #1ab394; font-size: 16px; margin-bottom: 20px">项目名称：{{projectName}}</div>
      <el-table v-loading="loading" :data="taskList">
<!--        <el-table-column type="selection" width="55" align="center" />-->
        <!--      <el-table-column label="ID" align="center" prop="taskId" v-if="true"/>-->

        <el-table-column label="任务主题" align="left" prop="taskTitle" />
        <el-table-column label="工作类型" align="center" prop="taskType" width="120">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.sys_work_type" :value="scope.row.taskType"/>
          </template>
        </el-table-column>
<!--        <el-table-column label="项目标识" align="left" width="180"  prop="projectId" />-->
        <el-table-column label="协作人员" align="center" prop="collaborator" width="120" />
        <el-table-column label="任务开始时间" align="center" prop="beginTime" width="120">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.beginTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="任务结束时间" align="center" prop="finishTime" width="120">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.finishTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="优先级" align="center" prop="taskGrade" width="80">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.sys_sort_grade" :value="scope.row.taskGrade"/>
          </template>
        </el-table-column>
<!--        <el-table-column label="创建者" align="center" prop="createBy" />-->
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
              @click="handleLookTask(scope.row)"
              v-hasPermi="['oa:task:query']"
            >查看</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdateTask(scope.row)"
              v-hasPermi="['oa:task:edit']"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['oa:task:remove']"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-show="taskTotal>0"
        :total="taskTotal"
        :page.sync="taskParams.pageNum"
        :limit.sync="taskParams.pageSize"
        @pagination="getTaskList(this.projectId)"
      />

    </el-dialog>

    <!-- 添加或修改任务管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="50%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">

        <el-row>
          <el-col :span="16">
            <el-form-item label="任务主题" prop="taskTitle">
              <el-input v-model="form.taskTitle" placeholder="请输入任务主题" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="项目标识" prop="projectId" hidden>
              <el-input v-model="form.projectId" placeholder="请输入项目ID" disabled="disabled"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="工作类型" prop="taskType">
              <el-select v-model="form.taskType" placeholder="请选择工作类型" disabled="disabled">
                <el-option
                  v-for="dict in dict.type.sys_work_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="16">
            <el-form-item label="任务时间">
              <el-col :span="11">
                <el-form-item prop="beginTime">
                  <el-date-picker clearable
                                  v-model="form.beginTime"
                                  type="datetime"
                                  value-format="yyyy-MM-dd HH:mm:ss"
                                  placeholder="请选择开始时间">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col class="line" :span="2">至</el-col>
              <el-col :span="11">
                <el-form-item prop="finishTime">
                  <el-date-picker clearable
                                  v-model="form.finishTime"
                                  type="datetime"
                                  value-format="yyyy-MM-dd HH:mm:ss"
                                  placeholder="请选择结束时间">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="优先级" prop="taskGrade">
              <el-radio-group v-model="form.taskGrade">
                <el-radio
                  v-for="dict in dict.type.sys_sort_grade"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="16">
            <el-form-item label="协作人员" prop="collaborator">
              <el-input v-model="form.collaborator" placeholder="请输入协作人员" />
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="详细描述">
              <editor v-model="form.content" :min-height="192"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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

    <!--查看弹出层-->
    <el-dialog title="提示" :visible.sync="openLook" width="50%">

      <el-descriptions class="margin-top" :column="4" border>
        <el-descriptions-item span="2" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-user"></i>
            任务主题
          </template>
          {{ form.taskTitle }}
        </el-descriptions-item>

        <el-descriptions-item span="2" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-s-unfold"></i>
            工作类型
          </template>
          <dict-tag :options="dict.type.sys_work_type" :value="form.taskType"/>
        </el-descriptions-item>

        <el-descriptions-item span="2" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-s-unfold"></i>
            协同人员
          </template>
          {{ form.collaborator }}
        </el-descriptions-item>

        <el-descriptions-item span="2" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-s-unfold"></i>
            优先级
          </template>
          <dict-tag :options="dict.type.sys_sort_grade" :value="form.taskGrade"/>
        </el-descriptions-item>
        <el-descriptions-item span="2" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-s-unfold"></i>
            任务开始时间
          </template>
          {{ parseTime(form.beginTime, '{y}-{m}-{d}') }}
        </el-descriptions-item>
        <el-descriptions-item span="2" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-s-unfold"></i>
            任务结束时间
          </template>
          {{ parseTime(form.finishTime, '{y}-{m}-{d}') }}
        </el-descriptions-item>
        <el-descriptions-item span="4" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-s-unfold"></i>
            工作内容
          </template>
          <div v-html="form.content "></div>
        </el-descriptions-item>
        <el-descriptions-item span="4" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-s-unfold"></i>
            备注
          </template>
          <div v-html="form.remark"></div>
        </el-descriptions-item>
        <el-descriptions-item span="4" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-s-unfold"></i>
            附件
          </template>

          <div v-if="fileList.length > 0">
            <div :key="file.url" v-for="(file, index) in fileList">
              <el-link :href="`${file.url}`" :underline="false" target="_blank">
                <span class="el-icon-document"> {{ file.originalName }} </span>
              </el-link>
            </div>
          </div>
          <div v-else style="color: #cccccc">暂无附件...</div>
        </el-descriptions-item>







      </el-descriptions>


    </el-dialog>



  </div>
</template>

<script>
import { listTask, getTask, delTask, addTask, updateTask } from "@/api/oa/task";
import {listProject} from "@/api/oa/project";
import {listByIds} from "@/api/system/oss";

export default {
  name: "Task",
  dicts: ['sys_project_type','sys_project_status', 'sys_work_type', 'sys_sort_grade'],
  data() {
    return {

      // 项目管理表格数据
      projectList: [],
      //项目id
      projectId: '',
      projectName: '',
      // 项目总条数
      total: 0,
      taskTotal: 0,
      // 遮罩层
      loading: true,
      //搜索日期范围
      searchTime: [],
      //任务弹出层标题
      titleDialog: '',
      //任务管理弹出层
      taskDialog:false,
      // 任务管理表格数据
      taskList: [],
      //任务表单
      form: {},
      // 按钮loading
      buttonLoading: false,
      // 任务修改弹出层
      open: false,
      //查看弹出层
      openLook: false,
      //附件
      fileList: [],
      //详情lable背景
      lableBg: "background: #f0f9eb; width:150px; text-align: center;",
      //任务弹出层标题
      title: '',
      // 表单校验
      rules: {
        taskId: [
          { required: true, message: "ID不能为空", trigger: "blur" }
        ],
        projectId: [
          { required: true, message: "项目ID不能为空", trigger: "blur" }
        ],
        taskTitle: [
          { required: true, message: "任务主题不能为空", trigger: "blur" }
        ],
        taskType: [
          { required: true, message: "工作类型不能为空", trigger: "change" }
        ],
        beginTime: [
          { required: true, message: "开始时间不能为空", trigger: "blur" }
        ],
        finishTime: [
          { required: true, message: "结束时间不能为空", trigger: "blur" }
        ],
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        projectName: undefined,
        projectNum: undefined,
      },
      taskParams: {
        pageNum: 1,
        pageSize: 10,
        projectName: undefined,
        projectNum: undefined,
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询项目管理列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.searchTime && '' != this.searchTime) {
        this.queryParams.params["beginCreateTime"] = this.getRealDate(this.searchTime[0]);
        this.queryParams.params["endCreateTime"] = this.getRealDate(this.searchTime[1]);
      }

      listProject(this.queryParams).then(response => {
        this.projectList = response.rows;
        //项目周期
        this.total = response.total;
        this.loading = false;
      });
    },

    /**项目搜索按钮操作**/
    handleQueryProject(){
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQueryProject(){
      this.queryParams = {}; //queryFormProject
      this.searchTime = [];
      this.resetForm("queryFormProject");
      this.handleQueryProject();
    },
    /** 任务搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getTaskList(this.projectId);
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.taskParams = {};
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /**任务管理按钮操作**/
    handleUpdate(rows){
      this.titleDialog = "任务管理";
      this.taskDialog = true;
      console.log(4545, rows)
      this.projectId = rows.projectId;
      this.projectName = rows.projectName;
      this.getTaskList(rows.projectId);

    },
    /**获取任务列表**/
    getTaskList(pid){
      this.taskParams.projectId = pid
      listTask(this.taskParams).then(response => {
        this.taskList = response.rows;
        this.taskTotal = response.total;
        this.loading = false;

      })
    },
    /**查看任务**/
    handleLookTask(row){
      this.loading = true;
      this.reset();
      const taskId = row.taskId || this.ids
      getTask(taskId).then(response => {
        this.loading = false;
        this.form = response.data;
        this.getFile(response.data.accessory)
        this.openLook = true;
        this.title = "查看任务";
      });
    },
    /** 修改按钮操作 */
    handleUpdateTask(row) {
      this.loading = true;
      this.reset();
      const taskId = row.taskId || this.ids
      getTask(taskId).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改任务管理";
      });
    },
    /** 任务提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;

          if (this.form.taskId != null) {
            updateTask(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              // this.getList();
              this.getTaskList(this.projectId);
            }).finally(() => {
              this.buttonLoading = false;
            });
          }
          /*else {
            addTask(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          }*/
        }
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {};
      this.resetForm("form");
      this.fileList = [];
    },
    /**获取附件**/
    getFile(val) {
      if (val) {
        listByIds(val).then(res => {
          this.fileList = res.data;
        })
      } else {
        this.fileList = [];
      }
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const taskIds = row.taskId || this.ids;
      this.$modal.confirm('是否确认删除任务管理编号为"' + taskIds + '"的数据项？').then(() => {
        this.loading = true;
        return delTask(taskIds);
      }).then(() => {
        this.loading = false;
        // this.getList();
        this.getTaskList(this.projectId)
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      }).finally(() => {
        this.loading = false;
      });
    },
    /**日期转字符串**/
    getRealDate(startDate) {
      // 时间转换
      var datejson = new Date(startDate).toJSON();
      var date = new Date( + new Date(datejson)
        + 8*3600*1000).toISOString().
      replace(/T/g,' ').
      replace(/\.[\d]{3}Z/,'')
      return date;
    },
  }
};
</script>
