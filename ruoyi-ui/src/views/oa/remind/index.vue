<template>
  <div class="wrap">
    <el-row :gutter="20">
      <el-col :span="16">
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="grid-content bg01">
              <div class="info-left"><i class="el-icon-alarm-clock"></i></div>
              <div class="info-right">
                <h4>今日待办</h4>
                <div class="conter"><span class="pull-right">件</span>{{todayRemind}}</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="grid-content bg02">
              <div class="info-left"><i class="el-icon-timer"></i></div>
              <div class="info-right">
                <h4>所有待办</h4>
                <div class="conter"><span class="pull-right">件</span>{{allRemind}}</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="grid-content bg03">
              <div class="info-left"><i class="el-icon-basketball"></i></div>
              <div class="info-right">
                <h4>逾期未办</h4>
                <div class="conter">{{overdueRemind}}</div>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="grid-content bg04">
              <div class="info-left"><i class="el-icon-edit-outline"></i></div>
              <div class="info-right">
                <h4>历史已办</h4>
                <div class="conter"><span class="pull-right">件</span>{{historyRemind}}</div>
              </div>
            </div>
          </el-col>

          <el-col :span="24">
            <div class="table-block">
              <template>
                <el-table :data="listRemind" stripe style="width: 100%">
                  <el-table-column prop="taskTitle" label="待办事件"></el-table-column>
                  <el-table-column prop="content" label="事件内容"></el-table-column>
                  <el-table-column label="待办日期" align="center" prop="createTime" width="120">
                    <template slot-scope="scope">
                      <span v-if="getTimesTamp(now) > getTimesTamp(scope.row.taskTime)"
                            style="color: #1ab394">{{ parseTime(scope.row.taskTime, '{y}-{m}-{d}') }}</span>
                      <span v-else style="color: #ff9900">{{ parseTime(scope.row.taskTime, '{y}-{m}-{d}') }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column prop="createBy" label="创建者" width="120"></el-table-column>
                  <el-table-column label="创建时间" align="center" prop="createTime" width="120">
                    <template slot-scope="scope">
                      <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
                    </template>
                  </el-table-column>

                  <el-table-column prop="name" label="办理操作" align="center" width="120">
                    <template slot-scope="scope">
                      <el-button
                        size="mini"
                        type="text"
                        icon="el-icon-view"
                        @click="handleLook(scope.row)"
                        v-hasPermi="['oa:remind:edit']"
                      >查看
                      </el-button>
                      <el-button
                        size="mini"
                        type="text"
                        icon="el-icon-edit"
                        @click="handleTask(scope.row)"
                        v-hasPermi="['oa:remind:edit']"
                      >办理
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </template>
            </div>

          </el-col>
        </el-row>


      </el-col>
      <el-col :span="8">
        <div class="demo-block">
          <el-calendar>
            <!-- 这里使用的是 2.5 slot 语法-->
            <template
              slot="dateCell"
              slot-scope="{date, data}">
              <!--自定义内容-->

                <div class="calendar-day" style="text-align: center">
                  <div>
                    <span class="everyDay">{{ data.day.split('-').slice(2).join('-') }}</span>
                  </div>
                  <div v-for="item in brightDate">
                    <div v-if="(parseTime(item.taskTime, '{y}-{m}-{d}')).indexOf(data.day) != -1" class="task-block">
                      待办({{ item.count }})
                    </div>
                  </div>
                </div>

            </template>
          </el-calendar>
        </div>


      </el-col>
    </el-row>

    <!--查看事件弹出层-->
    <el-dialog :title="title" :visible.sync="openLook" width="30%" append-to-body>

      <el-descriptions :column="4" border>
        <el-descriptions-item label="待办事件" :labelStyle="lableBg" span="2" width="80px">
          {{ lookRemind.taskTitle }}
        </el-descriptions-item>
        <el-descriptions-item label="待办时间" :labelStyle="lableBg" span="2">
          {{ parseTime(lookRemind.taskTime, '{y}-{m}-{d}') }}
        </el-descriptions-item>
        <el-descriptions-item label="事件内容" :labelStyle="lableBg" span="4">
          {{ lookRemind.content }}
        </el-descriptions-item>
        <el-descriptions-item label="创建者" :labelStyle="lableBg" span="2" width="80px">
          {{ lookRemind.createBy }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间" :labelStyle="lableBg" span="2">
          {{ parseTime(lookRemind.createTime, '{y}-{m}-{d}') }}
        </el-descriptions-item>
        <el-descriptions-item label="备注" :labelStyle="lableBg" span="4">
          {{ lookRemind.remark }}
        </el-descriptions-item>
        <el-descriptions-item :labelStyle="lableBg" span="4">
          <template slot="label">
            <i class="el-icon-paperclip"></i>
            附件
          </template>
          <!--附件-->
          <template v-if="lookRemind.accessory">
            <ImageOss :name="lookRemind.accessory"/>
          </template>
          <template v-else>
            <span style="color: #999999">暂无附件</span>
          </template>
          <!--附件-->
        </el-descriptions-item>

      </el-descriptions>

    </el-dialog>

    <!--办理事件弹出层-->
    <el-dialog :title="title" :visible.sync="openTask" width="30%" append-to-body>
      <el-descriptions :column="4" border>
        <el-descriptions-item label="待办事件" :labelStyle="lableBg" span="2" width="80px">
          {{ form.taskTitle }}
        </el-descriptions-item>
        <el-descriptions-item label="待办时间" :labelStyle="lableBg" span="2">
          {{ parseTime(form.taskTime, '{y}-{m}-{d}') }}
        </el-descriptions-item>
        <el-descriptions-item label="事件内容" :labelStyle="lableBg" span="4">
          {{ form.content }}
        </el-descriptions-item>
        <el-descriptions-item label="创建者" :labelStyle="lableBg" span="2" width="80px">
          {{ form.createBy }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间" :labelStyle="lableBg" span="2">
          {{ parseTime(form.createTime, '{y}-{m}-{d}') }}
        </el-descriptions-item>
        <el-descriptions-item label="备注" :labelStyle="lableBg" span="4">
          {{ form.remark }}
        </el-descriptions-item>
        <el-descriptions-item :labelStyle="lableBg" span="4">
          <template slot="label">
            <i class="el-icon-paperclip"></i>
            附件
          </template>
          <!--附件-->
          <template v-if="form.accessory">
            <ImageOss :name="form.accessory"/>
          </template>
          <template v-else>
            <span style="color: #999999">暂无附件</span>
          </template>
          <!--附件-->
        </el-descriptions-item>

      </el-descriptions>

      <div class="form-back">
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-form-item label="办理情况" prop="manage">
            <el-input v-model="form.manage" type="textarea" placeholder="请输入办理情况"/>
          </el-form-item>

        </el-form>
        <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancelTask">取 消</el-button>
       </span>
      </div>

    </el-dialog>

    <!--添加事件弹出层-->
    <el-dialog :title="title" :visible.sync="open" width="30%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="待办事件" prop="taskTitle">
          <el-input v-model="form.taskTitle" placeholder="请输入待办事件"/>
        </el-form-item>
        <el-form-item label="事件内容" prop="content">
          <el-input v-model="form.content" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="办理时间" prop="taskTime">
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
          <el-input v-model="form.remark" placeholder="请输入备注"/>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
       </span>
    </el-dialog>

    <div class="footer-bottom">
      <el-button type="primary" @click="addForm">添加事件</el-button>
    </div>

  </div>
</template>

<script>
import {addRemind, getRemind, getRemindList, getRemindCount, updateRemind} from "@/api/oa/remind";

export default {
  name: "index",
  data() {
    return {
      now: new Date(),
      brightDate: [],
      open: false,
      openLook: false,
      openTask: false,
      title: '',
      //详情lable背景
      lableBg: "background: #f0f9eb; width:120px; text-align: center;",
      // 表单参数
      form: {},
      lookRemind: {},
      //今日待办
      todayRemind: 0,
      //所有待办
      allRemind: 0,
      //逾期未办
      overdueRemind: 0,
      //历史已经办
      historyRemind: 0,
      // 表单校验
      rules: {
        taskTitle: [
          {required: true, message: "提醒名称不能为空", trigger: "blur"}
        ],
        content: [
          {required: true, message: "事件内容不能为空", trigger: "blur"}
        ],
        taskTime: [
          {required: true, message: "办理时间不能为空", trigger: "blur"}
        ],
      },
      listRemind: [],
      // 查询参数
      queryParams: {},
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
    }
  },
  created() {
    this.getList();
    this.getNowRamind();
    this.getAllRamind();
    this.getOverdueRamind()
    this.getHistoryRamind();
  },
  methods: {
    /** 查询事件提醒列表 */
    getList() {
      this.loading = true;
      let data = {
        taskStatus: '0',
      }
      //获取待办事件列表
      getRemindList(data).then(response => {
        this.listRemind = response.data;
        //日历待办：遍历去重、增加属性字段count（遇到重复+1）
        let arr = response.data;
       // 使用reduce方法遍历数组，累计相同id的次数，并构建新数组
        let newArr = arr.reduce((acc, current) => {
          let duplicateCount = acc.filter(item => this.parseTime(item.taskTime, '{y}-{m}-{d}') === this.parseTime(current.taskTime, '{y}-{m}-{d}')).length;
          if (duplicateCount === 0) {
            acc.push({...current, count: 1});
          } else {
            // 找到重复对象并加1
            let index = acc.findIndex(item => this.parseTime(item.taskTime, '{y}-{m}-{d}') === this.parseTime(current.taskTime, '{y}-{m}-{d}'));
            acc[index].count = acc[index].count + 1;
          }
          return acc;
        }, []);
        this.brightDate = newArr;

        this.loading = false;
      });
    },
    /**今日待办事件统计**/
    getNowRamind(){
      this.queryParams.params = {};
      this.queryParams.taskStatus = '0'
      this.queryParams.params["beginTime"] = this.parseTime(this.now, '{y}-{m}-{d}') + ' 00:00:00';
      this.queryParams.params["endTime"] = this.parseTime(this.now, '{y}-{m}-{d}') + ' 23:59:59';
      getRemindCount(this.queryParams).then(response => {
        this.todayRemind = response.data
      })
    },

    /**逾期未办事件统计**/
    getOverdueRamind(){
      this.queryParams.params = {};
      this.queryParams.taskStatus = '0'
      this.queryParams.params["beginTime"] = this.parseTime(new Date('2020-01-01'), '{y}-{m}-{d}') + ' 00:00:00';
      this.queryParams.params["endTime"] = this.parseTime(this.now, '{y}-{m}-{d}') + ' 23:59:59';
      getRemindCount(this.queryParams).then(response => {
        this.overdueRemind = response.data
      })
    },

    /**所有待办事件统计**/
    getAllRamind(){
      this.queryParams.params = {};
      this.queryParams.taskStatus = '0'
      getRemindCount(this.queryParams).then(response => {
        this.allRemind = response.data
      })
    },

    /**已办事件统计**/
    getHistoryRamind(){
      this.queryParams.params = {};
      this.queryParams.taskStatus = '1'
      getRemindCount(this.queryParams).then(response => {
        this.historyRemind = response.data
      })
    },

    /**添加事件按钮操作**/
    addForm() {
      this.reset();
      this.open = true;
    },
    /** 查看按钮操作 */
    handleLook(row) {
      this.loading = true;
      this.reset();
      const remindId = row.remindId || this.ids
      getRemind(remindId).then(response => {
        this.loading = false;
        this.lookRemind = response.data;
        this.openLook = true;
        this.title = "查看待办详情";
      });
    },

    /** 办理按钮操作 */
    handleTask(row) {
      this.loading = true;
      this.reset();
      const remindId = row.remindId || this.ids
      getRemind(remindId).then(response => {
        this.loading = false;
        this.form = response.data;
        this.openTask = true;
        this.title = "事件办理";

      });
    },

    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.remindId != null) {
            if (this.getTimesTamp(this.now) > this.getTimesTamp(this.form.taskTime) && this.form.manage != null) {
              console.log("执行任务办理")
              this.form.taskStatus = '1';
              updateRemind(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.openTask = false;
                this.getList();
              }).finally(() => {
                this.buttonLoading = false;
              });
            } else {
              this.$modal.msgWarning("办理情况不可为空！或任务时间未到！")
            }


          } else {
            addRemind(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          }

          //执行办理统计
          this.getNowRamind();
          this.getAllRamind();
          this.getOverdueRamind()
          this.getHistoryRamind();
        }
      });
    },

    getTimesTamp(date) {
      return new Date(date).getTime()

    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    cancelTask() {
      this.openTask = false;
      this.reset();
    },


  }
}
</script>

<style scoped>
.wrap {
  padding: 30px;
}


.grid-content {
  display: flex;
  border-radius: 8px;
  min-height: 36px;
  color: #f5f5f5;
  padding: 12px;
}

.info-left {
  height: 100px;
  width: 100px;
  font-size: 88px;
  text-align: center
}

.info-right {
  margin-left: 15px;
}

.info-right h4 {
  font-size: 18px;
  color: #f5f5f5;
  margin: 12px 0 10px 0;
}

.info-right .conter {
  font-size: 45px;
  color: #ffffff
}

.info-right .conter span {
  font-size: 16px;
  margin-top: 24px;
  color: #f5f5f5
}

.bg01 {
  background-color: #1ab394
}

.bg02 {
  background-color: #409EFF
}

.bg03 {
  background-color: #F56C6C
}

.bg04 {
  background-color: #E6A23C
}

.form-back {
  margin-top: 20px;
}

.table-block {
  border: 1px solid #ebebeb;
  border-bottom: 0;
  border-radius: 6px;
  transition: .2s;
  margin-top: 20px;
}

.demo-block {
  border: 1px solid #ebebeb;
  border-radius: 6px;
  transition: .2s;
  padding-bottom: 8px;
}

.task-block {
  color: #1ab394;
  font-size: 14px;
  margin-top: 20px
}

.footer-bottom {
  margin: 24px 0;
}
</style>
