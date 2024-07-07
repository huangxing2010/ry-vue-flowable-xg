<template>
  <div class="app-container">

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="项目名称" prop="projectName">
        <el-input
          v-model="queryParams.projectName"
          placeholder="请输入项目名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="项目编号" prop="projectNum">
        <el-input
          v-model="queryParams.projectNum"
          placeholder="请输入项目编号"
          clearable
          @keyup.enter.native="handleQuery"
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
          v-hasPermi="['oa:project:add']"
        >新增项目
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
          v-hasPermi="['oa:project:edit']"
        >项目实施
        </el-button>
      </el-col>
      <!--
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['oa:project:remove']"
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
                v-hasPermi="['oa:project:export']"
              >导出</el-button>
            </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="projectList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <!--      <el-table-column label="ID" align="center" prop="projectId" v-if="true"/>-->
      <el-table-column label="项目名称" align="left" prop="projectName"/>
      <el-table-column label="项目编号" align="left" prop="projectNum" width="230"/>
      <el-table-column label="项目总金额" align="center" prop="funds"  width="120">
        <template slot-scope="scope">
          {{convertToTenThousand(scope.row.funds)}}
        </template>
      </el-table-column>
      <el-table-column label="项目类型" align="center" prop="projectType" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_project_type" :value="scope.row.projectType"/>
        </template>
      </el-table-column>
      <el-table-column label="履约保证金" align="center" prop="bail" width="150">
        <template slot-scope="scope">
          <div v-if="bailStatusTo(scope.row.bail) === 0">
            <span style="color: #999999">未缴纳</span>
           </div>
          <div v-else style="color: red">
            <div style="color: #1c84c6">
              已缴纳
              -
              <span v-if="bailStatusIn(scope.row.bail) === 1" style="color: #1ab394">已退回</span>
              <span  v-else style="color: #ff9900">未退回</span>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="负责人" align="center" prop="functionary" width="80"/>
      <el-table-column label="创建者" align="center" prop="createBy" width="90"/>
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
      <el-table-column label="备注" align="center" prop="remark" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.remark == null || scope.row.remark == '' ? '暂无' : scope.row.remark }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['oa:project:edit']"
          >实施
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['oa:project:remove']"
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

    <!-- 添加或修改项目管理对话框 -->
    <el-dialog :title="title" :visible.sync="editShow" @close="closeDialog" width="76%" append-to-body>
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="项目信息" name="first">
          <el-tabs :tab-position="tabPosition" v-model="activeChild" >
            <el-tab-pane label="项目查看" name="one">
              <el-descriptions class="margin-top" title="基本信息" :column="4" border>
                <el-descriptions-item :labelStyle="lableBg" span="2">
                  <template slot="label">
                    <i class="el-icon-eleme"></i>
                    项目名称
                  </template>
                  {{ form.projectName }}

                </el-descriptions-item>
                <el-descriptions-item :labelStyle="lableBg" span="2">
                  <template slot="label">
                    <i class="el-icon-s-unfold"></i>
                    项目编号
                  </template>
                  {{ form.projectNum }}
                </el-descriptions-item>
                <el-descriptions-item :labelStyle="lableBg" span="2">
                  <template slot="label">
                    <i class="el-icon-location-information"></i>
                    项目地址
                  </template>
                  {{ form.address == undefined ? '未添加' : form.address }}
                </el-descriptions-item>
                <el-descriptions-item :labelStyle="lableBg" span="2">
                  <template slot="label">
                    <i class="el-icon-goods"></i>
                    项目总金额
                  </template>
                    {{convertToTenThousand(form.funds)}}
                </el-descriptions-item>
                <el-descriptions-item :labelStyle="lableBg" span="2">
                  <template slot="label">
                    <i class="el-icon-time"></i>
                    项目周期
                  </template>
                  {{ parseTime(form.beginTime, '{y}-{m}-{d}') }}至{{ parseTime(form.finishTime, '{y}-{m}-{d}') }}
                </el-descriptions-item>
                <el-descriptions-item :labelStyle="lableBg" span="2">
                  <template slot="label">
                    <i class="el-icon-key"></i>
                    项目类型
                  </template>
                  <span v-for="dict in dict.type.sys_project_type" :key="dict.value"
                        v-if="form.projectType == dict.value">{{ dict.label }}</span>
                </el-descriptions-item>
                <el-descriptions-item :labelStyle="lableBg" span="2">
                  <template slot="label">
                    <i class="el-icon-s-custom"></i>
                    负责人
                  </template>
                  {{ form.functionary }}
                </el-descriptions-item>
                <el-descriptions-item :labelStyle="lableBg" span="2">
                  <template slot="label">
                    <i class="el-icon-star-off"></i>
                    交货期
                  </template>
                  {{ form.delivery }}
                </el-descriptions-item>
                <el-descriptions-item :labelStyle="lableBg" span="2">
                  <template slot="label">
                    <i class="el-icon-goods"></i>
                    质保期
                  </template>
                  {{ form.guarantee }}
                </el-descriptions-item>

                <el-descriptions-item :labelStyle="lableBg" span="2">
                  <template slot="label">
                    <i class="el-icon-medal"></i>
                    优先级别
                  </template>
                  <span v-for="dict in dict.type.sys_sort_grade" :key="dict.value"
                        v-if="form.projectGrade == dict.value">{{ dict.label }}</span>
                </el-descriptions-item>
                <el-descriptions-item :labelStyle="lableBg" span="2">
                  <template slot="label">
                    <i class="el-icon-aim"></i>
                    状态
                  </template>
                  <span v-for="dict in dict.type.sys_project_status" :key="dict.value"
                        v-if="form.projectStatus == dict.value">{{ dict.label }}</span>
                </el-descriptions-item>

                <el-descriptions-item :labelStyle="lableBg" span=2>
                  <template slot="label">
                    <i class="el-icon-notebook-1"></i>
                    备注
                  </template>
                  {{ form.remark }}
                </el-descriptions-item>
                <el-descriptions-item :labelStyle="lableBg" span=4>
                  <template slot="label">
                    <i class="el-icon-collection"></i>
                    项目介绍
                  </template>
                  {{ form.introduction }}
                </el-descriptions-item>
                <el-descriptions-item :labelStyle="lableBg" span=4>
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
              <div style="margin-top: 24px"></div>
              <el-descriptions class="margin-top" title="开票信息" :column="4" border>
                <el-descriptions-item label="开票名称" :span="2" :labelStyle="lableBg">
                  {{ form.invoiceName == null ? '未填写' : form.invoiceName }}
                </el-descriptions-item>
                <el-descriptions-item label="纳税人识别号" :span="2" :labelStyle="lableBg">
                  {{ form.invoiceNumber == null ? '未填写' : form.invoiceNumber }}
                </el-descriptions-item>
                <el-descriptions-item label="地址电话" :span="2" :labelStyle="lableBg">
                  {{ form.invoiceAddress == null ? '未填写' : form.invoiceAddress }}
                </el-descriptions-item>
                <el-descriptions-item label="开户行及账号" :span="2" :labelStyle="lableBg">
                  {{ form.invoiceBank == null ? '未填写' : form.invoiceBank }}
                </el-descriptions-item>
              </el-descriptions>
              <div style="margin-top: 24px"></div>
              <el-descriptions class="margin-top" title="履约保证金缴纳情况" :column="3" border>
                <el-descriptions-item label="缴纳金额" :span="1" :labelStyle="lableBg">
                  {{ bail.money == null || bail.money == undefined ? '未填写' : bail.money }}
                </el-descriptions-item>
                <el-descriptions-item label="缴纳时间" :span="1" :labelStyle="lableBg">
                  {{ bail.theTime == null || bail.theTime == undefined ? '未填写' : bail.theTime }}
                </el-descriptions-item>
                <el-descriptions-item label="缴纳比例" :span="1" :labelStyle="lableBg">
                  {{ bail.ratio == null || bail.ratio == undefined ? '未填写' : bail.ratio }}
                </el-descriptions-item>
                <el-descriptions-item label="退回金额" :span="1" :labelStyle="lableBg">
                  {{ bail.backMoney == null || bail.backMoney == undefined  ? '未填写' : bail.backMoney }}
                </el-descriptions-item>
                <el-descriptions-item label="退回时间" :span="2" :labelStyle="lableBg">
                  {{ bail.backTime == null || bail.backTime == undefined ? '未填写' : bail.backTime }}
                </el-descriptions-item>
                <el-descriptions-item label="备注" :span="2" :labelStyle="lableBg">
                  {{ bail.remark == null || bail.remark == undefined ? '未填写' : bail.remark }}
                </el-descriptions-item>

              </el-descriptions>
            </el-tab-pane>

            <el-tab-pane label="项目修改" name="two">

              <el-form ref="form" :model="form" :rules="rules" label-width="120px">
                <el-divider content-position="center">基本信息</el-divider>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="项目名称" prop="projectName">
                      <el-input v-model="form.projectName" placeholder="请输入项目名称"/>
                    </el-form-item>
                  </el-col>
                  <el-col :span="6">
                    <el-form-item label="项目编号" prop="projectNum">
                      <el-input v-model="form.projectNum" placeholder="请输入项目编号"/>
                    </el-form-item>
                  </el-col>
                  <el-col :span="6">
                    <el-form-item label="负责人" prop="functionary">
                      <el-input v-model="form.functionary" placeholder="请输入项目负责人"/>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="项目地址" prop="address">
                      <el-input v-model="form.address" placeholder="请输入项目地址"/>
                    </el-form-item>
                  </el-col>
                  <el-col :span="6">
                    <el-form-item label="项目类型" prop="projectType">
                      <el-select v-model="form.projectType" placeholder="请选择项目类型" style="width: 100%">
                        <el-option
                          v-for="dict in dict.type.sys_project_type"
                          :key="dict.value"
                          :label="dict.label"
                          :value="dict.value"

                        ></el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="6">
                    <el-form-item label="优先级" prop="projectGrade">
                      <el-radio-group v-model="form.projectGrade">
                        <el-radio
                          v-for="dict in dict.type.sys_sort_grade"
                          :key="dict.value"
                          :label="dict.value"
                        >{{ dict.label }}
                        </el-radio>
                      </el-radio-group>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="项目总金额" prop="funds">
                      <el-input v-model="form.funds" placeholder="请输入项目地址"/>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="交货期" prop="delivery">
                      <el-input v-model="form.delivery" placeholder="请输入交货期"/>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="质保期" prop="guarantee">
                      <el-input v-model="form.guarantee" placeholder="请输入质保期"/>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="项目周期">
                      <el-date-picker
                        v-model="periodItem"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        type="daterange"
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        @blur="getTimeBlur">
                      </el-date-picker>
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="项目介绍" prop="introduction">
                      <el-input v-model="form.introduction" type="textarea" placeholder="请输入内容"/>
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="备注" prop="remark">
                      <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="附件" prop="accessory">
                      <file-upload v-model="form.accessory"/>
                    </el-form-item>
                  </el-col>
                </el-row>

                <el-row>
                  <el-divider content-position="center">甲方开票信息</el-divider>
                  <el-col :span="6">
                    <el-form-item label="开票名称" prop="remark">
                      <el-input v-model="form.invoiceName" placeholder="请输入开票名称"/>
                    </el-form-item>
                  </el-col>
                  <el-col :span="6">
                    <el-form-item label="纳税人识别号" prop="remark">
                      <el-input v-model="form.invoiceNumber" placeholder="请输入纳税人识别号"/>
                    </el-form-item>
                  </el-col>
                  <el-col :span="6">
                    <el-form-item label="地址电话" prop="remark">
                      <el-input v-model="form.invoiceAddress" placeholder="请输入地址电话"/>
                    </el-form-item>
                  </el-col>
                  <el-col :span="6">
                    <el-form-item label="开户行及账号" prop="remark">
                      <el-input v-model="form.invoiceBank" placeholder="请输入开户行及账号"/>
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-row>
                  <el-divider content-position="center">履约保证金缴纳情况</el-divider>
                  <el-col :span="8">
                    <el-form-item label="缴纳金额" prop="bail">
                      <el-input v-model="bail.money" placeholder="请输入缴纳金额"/>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="缴纳时间" prop="bail">
                      <el-input v-model="bail.theTime" placeholder="请选输入时间"/>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="缴纳比例" prop="bail">
                      <el-input v-model="bail.ratio" placeholder="请输入缴纳比例"/>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="退回金额" prop="bail">
                      <el-input v-model="bail.backMoney" placeholder="请输入退回金额"/>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="退回时间" prop="bail">
                      <el-input v-model="bail.backTime" placeholder="请输入退回时间"/>
                    </el-form-item>
                  </el-col>

                  <el-col :span="8">
                    <el-form-item label="备注" prop="bail">
                      <el-input v-model="bail.remark" placeholder="请输入备注"/>
                    </el-form-item>
                  </el-col>
                  <div style="color: #ff9900;padding-left: 50px;margin-bottom: 15px">备注：缴纳金额为空状态即为未缴纳，退回金额为空即为状态为未退回</div>
                </el-row>
              </el-form>
              <div class="dialog-footer">
                <el-button type="primary" @click="submitForm">保存1</el-button>
                <!--                <el-button @click="cancel">取 消</el-button>-->
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-tab-pane>
        <el-tab-pane label="任务管理" name="second">

          <div style="margin: 24px">
            <el-steps :space="180" :active="active" finish-status="success">
              <el-step v-for="item in stepsListTop" :title="item.dictValue +':'+ item.dictLabel"></el-step>
            </el-steps>
          </div>

          <el-tabs :tab-position="tabPositionTask" v-model="activeTitle" @tab-click="activeClick">
            <el-tab-pane label="进度控制" name="first">
              <div class="block">

                <el-timeline :reverse="reverse" ref="divHeight">
                  <el-timeline-item
                    v-for="(activity, index) in stepsList"
                    :key="index"
                    :icon="activity.icon"
                    :type="activity.type"
                    :color="activity.color"
                    :size="activity.size"
                  >
                    {{ activity.dictValue }} {{ activity.dictLabel }}
                    <div :id="'print' + index"  v-if="activity.taskList.length > 0">
                      <el-card class="card-col">
                        <el-descriptions v-for="(item, key) in activity.taskList" :key="key"  :title="'创建日期：'+ item.createTime"
                                         border class="card-col-center" :column="4">
                          <template slot="extra">
                            <div></div>
                          </template>
                          <el-descriptions-item label-style="width:120px;text-align: center;" span="2">
                            <template slot="label">
                              <i class="el-icon-receiving"></i>
                              任务主题{{item.length}}
                            </template>
                            {{ item.taskTitle }}
                          </el-descriptions-item>
                          <el-descriptions-item label-style="width:120px;text-align: center;" span="2">
                            <template slot="label">
                              <i class="el-icon-user"></i>
                              协同人员
                            </template>
                            <div style="width: 280px">{{ item.collaborator }}</div>

                          </el-descriptions-item>
                          <el-descriptions-item label-style="width:120px;text-align: center;" span="2">
                            <template slot="label">
                              <i class="el-icon-mobile-phone"></i>
                              工作时间
                            </template>
                      {{item.beginTime}} 至 {{item.finishTime}}
                          </el-descriptions-item>

                          <el-descriptions-item label-style="width:120px;text-align: center;" span="2">
                            <template slot="label">
                              <i class="el-icon-tickets"></i>
                              优先级
                            </template>
                            <el-tag size="small">
                              {{ item.taskGrade === '1' ? '中' : item.taskGrade === '2' ? '高' : '低' }}
                            </el-tag>
                          </el-descriptions-item>

                          <el-descriptions-item span="4">
                            <template slot="label">
                              <i class="el-icon-office-building"></i>
                              工作描述
                            </template>
                            <div v-html="item.content"></div>
                          </el-descriptions-item>
                          <el-descriptions-item span="2">
                            <template slot="label">
                              <i class="el-icon-paperclip"></i>
                              附件
                            </template>
                            <!--附件-->
                            <template v-if="item.accessory">
                              <ImageOss :name="item.accessory"/>
                            </template>
                            <template v-else>
                              <span style="color: #999999">暂无附件</span>
                            </template>
                            <!--附件-->
                          </el-descriptions-item>
                          <el-descriptions-item label-style="width:120px;text-align: center;" span="2">
                            <template slot="label">
                              <i class="el-icon-location-outline"></i>
                              添加者
                            </template>
                            {{ item.createBy }}
                          </el-descriptions-item>

                        </el-descriptions>
                      </el-card>
                    </div>
                    <div v-else style="padding: 30px;font-size: 12px;font-weight: 500;color: #ff9900">
                      任务待完成...
                    </div>
                    <div v-print="printObj" @click="printValue(index)"
                         style="font-size: 18px; color: #1c84c6;border: 0;float: right;margin-top: 10px;"><i
                      class="el-icon-printer"></i></div>
                  </el-timeline-item>
                </el-timeline>


              </div>

            </el-tab-pane>
            <el-tab-pane label="任务添加" name="second">
              <el-form ref="taskForm" :model="taskForm" :rules="taskRules" label-width="110px">
                <el-row>
                  <el-col :span="16">
                    <el-form-item label="任务主题" prop="taskTitle">
                      <el-input v-model="taskForm.taskTitle" placeholder="请输入任务主题"/>
                    </el-form-item>
                  </el-col>


                  <el-col :span="8">
                    <el-form-item label="项目标识" prop="projectId" hidden>
                      <el-input v-model="form.projectId" disabled="disabled"/>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <el-form-item label="工作类型" prop="taskType">
                      <el-select v-model="taskForm.taskType" placeholder="请选择工作类型" style="width: 100%;">
                        <el-option
                          v-for="dict in dict.type.sys_work_type"
                          :key="dict.value"
                          :label="dict.label"
                          :value="dict.value"
                        ></el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="协作人员" prop="collaborator">
                      <el-input v-model="taskForm.collaborator" placeholder="请输入协作人员"/>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="任务时间">
                      <el-col :span="11">
                        <el-form-item prop="beginTime">
                          <el-date-picker clearable
                                          v-model="taskForm.beginTime"
                                          type="datetime"
                                          value-format="yyyy-MM-dd HH:mm:ss"
                                          placeholder="请选择开始时间">
                          </el-date-picker>
                        </el-form-item>
                      </el-col>
                      <el-col class="line" :span="2">-</el-col>
                      <el-col :span="11">
                        <el-form-item prop="finishTime">
                          <el-date-picker clearable
                                          v-model="taskForm.finishTime"
                                          type="datetime"
                                          value-format="yyyy-MM-dd HH:mm:ss"
                                          placeholder="请选择结束时间">
                          </el-date-picker>
                        </el-form-item>
                      </el-col>
                    </el-form-item>
                  </el-col>

                  <el-col :span="12">
                    <el-form-item label="优先级" prop="taskGrade">
                      <el-radio-group v-model="taskForm.taskGrade">
                        <el-radio
                          v-for="dict in dict.type.sys_sort_grade"
                          :key="dict.value"
                          :label="dict.value"
                        >{{ dict.label }}
                        </el-radio>
                      </el-radio-group>
                    </el-form-item>
                  </el-col>


                  <el-col :span="24">
                    <el-form-item label="详细描述">
                      <editor v-model="taskForm.content" :min-height="192"/>
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">
                    <el-form-item label="备注" prop="remark">
                      <el-input v-model="taskForm.remark" type="textarea" placeholder="请输入内容"/>
                    </el-form-item>
                  </el-col>
                  <el-col :span="24">

                    <el-form-item label="附件" prop="accessory">
                      <file-upload v-model="taskForm.accessory"/>
                    </el-form-item>
                  </el-col>
                  <el-col :span="8">
                    <div class="grid-content bg-purple"></div>
                  </el-col>
                </el-row>
              </el-form>
              <div class="dialog-footer">
                <el-button :loading="buttonLoading" type="primary" @click="submitTaskForm">保存</el-button>
                <!--<el-button @click="cancel">取 消</el-button>-->
              </div>
            </el-tab-pane>


          </el-tabs>
        </el-tab-pane>
        <el-tab-pane label="合同管理" name="third">

          <el-tabs tab-position="left" v-model="tabContract" @tab-click="handleTabContract" style="height: 400px;">
            <el-tab-pane label="项目合同" name="tab01">
              <div v-if="contractForm.contractName != undefined">
                <el-descriptions :title="desTitle" class="margin-top" :column="3" border>
                  <template slot="extra">
                    项目编号：{{ projectNum }}
                  </template>
                  <el-descriptions-item span="2" :labelStyle="lableBg">
                    <template slot="label">
                      <i class="el-icon-user"></i>
                      项目名称
                    </template>
                    {{ contractForm.contractName }}
                  </el-descriptions-item>
                  <el-descriptions-item span="1" :labelStyle="lableBg">
                    <template slot="label">
                      <i class="el-icon-s-unfold"></i>
                      合同编号
                    </template>
                    {{ contractForm.contractNum }}
                  </el-descriptions-item>
                  <el-descriptions-item span="1" :labelStyle="lableBg">
                    <template slot="label">
                      <i class="el-icon-eleme"></i>
                      甲方名称
                    </template>
                    {{ contractForm.firstName }}
                  </el-descriptions-item>
                  <el-descriptions-item span="1" :labelStyle="lableBg">
                    <template slot="label">
                      <i class="el-icon-user"></i>
                      甲方负责人
                    </template>
                    {{ contractForm.firstPerson }}
                  </el-descriptions-item>
                  <el-descriptions-item span="1" :labelStyle="lableBg">
                    <template slot="label">
                      <i class="el-icon-mobile-phone"></i>
                      甲方电话
                    </template>
                    {{ contractForm.firstPhone }}
                  </el-descriptions-item>
                  <el-descriptions-item span="1" :labelStyle="lableBg">
                    <template slot="label">
                      <i class="el-icon-eleme"></i>
                      乙方名称
                    </template>
                    {{ contractForm.secondName }}
                  </el-descriptions-item>
                  <el-descriptions-item span="1" :labelStyle="lableBg">
                    <template slot="label">
                      <i class="el-icon-user"></i>
                      乙方负责人
                    </template>
                    {{ contractForm.secondPerson }}
                  </el-descriptions-item>
                  <el-descriptions-item span="1" :labelStyle="lableBg">
                    <template slot="label">
                      <i class="el-icon-mobile-phone"></i>
                      乙方电话
                    </template>
                    {{ contractForm.secondPhone }}
                  </el-descriptions-item>
                  <el-descriptions-item span="1" :labelStyle="lableBg">
                    <template slot="label">
                      <i class="el-icon-trophy"></i>
                      合同金额
                    </template>
                    {{ contractForm.contractPrice }}
                  </el-descriptions-item>
                  <el-descriptions-item span="1" :labelStyle="lableBg">
                    <template slot="label">
                      <i class="el-icon-c-scale-to-original"></i>
                      签订日期
                    </template>
                    {{ contractForm.signTime }}
                  </el-descriptions-item>
                  <el-descriptions-item span="1" :labelStyle="lableBg">
                    <template slot="label">
                      <i class="el-icon-date"></i>
                      有效期
                    </template>
                    {{ contractForm.validity }}
                  </el-descriptions-item>
                  <el-descriptions-item span="3" :labelStyle="lableBg">
                    <template slot="label">
                      <i class="el-icon-pie-chart"></i>
                      备注
                    </template>
                    {{ contractForm.remark }}
                  </el-descriptions-item>
                  <el-descriptions-item span="3" :labelStyle="lableBg">
                    <template slot="label">
                      <i class="el-icon-paperclip"></i>
                      附件
                    </template>
                    <!--附件-->
                    <template v-if="contractForm.accessory">
                      <ImageOss :name="contractForm.accessory"/>
                    </template>
                    <template v-else>
                      <span style="color: #999999">暂无附件</span>
                    </template>
                    <!--附件-->
                  </el-descriptions-item>
                </el-descriptions>
              </div>
              <div v-else>
                <div class="message">暂无项目合同信息，请在合同管理里添加项目合同信息！</div>
              </div>
            </el-tab-pane>

            <el-tab-pane label="采购合同" name="tab02">
              <div v-if="oaContractList.length == 0" class="message">暂无采购合同信息，请在合同管理里添加采购合同信息！
              </div>
              <div v-else>
                <el-table :data="oaContractList" style="margin-bottom: 30px">
                  <el-table-column label="合同名称" align="left" prop="contractName"/>
                  <el-table-column label="甲方名称" align="center" prop="firstName" width="180"/>
                  <el-table-column label="乙方名称" align="center" prop="secondName" width="220"/>
                  <el-table-column label="乙方负责人" align="center" prop="secondPerson" width="100"/>
                  <el-table-column label="乙方电话" align="center" prop="secondPhone" width="120"/>
                  <el-table-column label="合同金额" align="center" prop="contractPrice" width="150"/>
                  <el-table-column label="签订日期" align="center" prop="signTime" width="100">
                    <template slot-scope="scope">
                      <span>{{ parseTime(scope.row.signTime, '{y}-{m}-{d}') }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column label="创建者" align="center" prop="createBy" width="80"/>
                  <el-table-column label="创建时间" align="center" prop="createTime" width="100">
                    <template slot-scope="scope">
                      <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
                    <template slot-scope="scope">
                      <el-button
                        size="mini"
                        type="text"
                        icon="el-icon-view"
                        @click="handlePreview(scope.row)"
                        v-hasPermi="['oa:oaContract:edit']"
                      >查看
                      </el-button>
                    </template>
                  </el-table-column>


                </el-table>
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-tab-pane>
        <!--<el-tab-pane label="定时任务补偿" name="fourth">定时任务补偿</el-tab-pane>-->
      </el-tabs>
    </el-dialog>

    <el-dialog :title="title" :visible.sync="addShow" width="76%" append-to-body>

      <el-form ref="form" :model="form" :rules="rules" label-width="110px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="项目名称" prop="projectName">
              <el-input v-model="form.projectName" placeholder="请输入项目名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="项目编号" prop="projectNum">
              <el-input v-model="form.projectNum" placeholder="请输入项目编号"/>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="负责人" prop="functionary">
              <el-input v-model="form.functionary" placeholder="请输入项目负责人"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="项目地址" prop="address">
              <el-input v-model="form.address" placeholder="请输入项目地址"/>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="项目类型" prop="projectType">
              <el-select v-model="form.projectType" placeholder="请选择项目类型" style="width: 100%">
                <el-option
                  v-for="dict in dict.type.sys_project_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="优先级" prop="projectGrade">
              <el-radio-group v-model="form.projectGrade">
                <el-radio
                  v-for="dict in dict.type.sys_sort_grade"
                  :key="dict.value"
                  :label="dict.value"
                >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="项目总金额" prop="funds">
              <el-input v-model="form.funds" placeholder="请输入项目总金额"/>
            </el-form-item>
          </el-col>


          <el-col :span="12">
            <el-form-item label="交货期" prop="address">
              <el-input v-model="form.delivery" placeholder="请输入交货期"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="项目周期">
              <el-date-picker
                v-model="periodItem"
                value-format="yyyy-MM-dd HH:mm:ss"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                @blur="getTimeBlur">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="质保期" prop="address">
              <el-input v-model="form.guarantee" placeholder="请输入质保期"/>
            </el-form-item>
          </el-col>


          <el-col :span="24">
            <el-form-item label="项目介绍" prop="introduction">
              <el-input v-model="form.introduction" type="textarea" placeholder="请输入内容"/>
            </el-form-item>
          </el-col>
          <!--                  <el-col :span="12">
                              <el-form-item label="状态" prop="projectStatus">
                                <el-radio-group v-model="form.projectStatus">
                                  <el-radio
                                    v-for="dict in dict.type.sys_project_status"
                                    :key="dict.value"
                                    :label="dict.value"
                                  >{{ dict.label }}
                                  </el-radio>
                                </el-radio-group>
                              </el-form-item>
                            </el-col>-->
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="附件" prop="accessory">
              <file-upload v-model="form.accessory"/>
            </el-form-item>
          </el-col>
        </el-row>

      </el-form>
      <div class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">保存</el-button>
        <!--<el-button @click="cancel">取 消</el-button>-->
      </div>

    </el-dialog>

    <el-dialog :title="titleContract" :visible.sync="dialogContract" width="55%" append-to-body>
      <el-descriptions :title="desTitle" class="margin-top" :column="3" border>
        <template slot="extra">
          项目编号：{{ projectNum }}
        </template>

        <el-descriptions-item span="2" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-user"></i>
            合同名称
          </template>
          {{ contractForm.contractName }}
        </el-descriptions-item>
        <el-descriptions-item span="1" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-s-unfold"></i>
            合同编号
          </template>
          {{ contractForm.contractNum }}
        </el-descriptions-item>
        <el-descriptions-item span="1" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-eleme"></i>
            甲方名称
          </template>
          {{ contractForm.firstName }}
        </el-descriptions-item>
        <el-descriptions-item span="1" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-user"></i>
            甲方负责人
          </template>
          {{ contractForm.firstPerson }}
        </el-descriptions-item>
        <el-descriptions-item span="1" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-mobile-phone"></i>
            甲方电话
          </template>
          {{ contractForm.firstPhone }}
        </el-descriptions-item>
        <el-descriptions-item span="1" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-eleme"></i>
            乙方名称
          </template>
          {{ contractForm.secondName }}
        </el-descriptions-item>
        <el-descriptions-item span="1" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-user"></i>
            乙方负责人
          </template>
          {{ contractForm.secondPerson }}
        </el-descriptions-item>
        <el-descriptions-item span="1" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-mobile-phone"></i>
            乙方电话
          </template>
          {{ contractForm.secondPhone }}
        </el-descriptions-item>
        <el-descriptions-item span="1" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-trophy"></i>
            合同金额
          </template>
          {{ contractForm.contractPrice }}
        </el-descriptions-item>
        <el-descriptions-item span="1" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-c-scale-to-original"></i>
            签订日期
          </template>
          {{ contractForm.signTime }}
        </el-descriptions-item>
        <el-descriptions-item span="1" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-date"></i>
            有效期
          </template>
          {{ contractForm.validity }}
        </el-descriptions-item>
        <el-descriptions-item span="3" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-pie-chart"></i>
            备注
          </template>
          {{ contractForm.remark }}
        </el-descriptions-item>

        <el-descriptions-item span="3" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-paperclip"></i>
            附件
          </template>
          <!--附件-->
          <template v-if="contractForm.accessory">
            <ImageOss :name="contractForm.accessory"/>
          </template>
          <template v-else>
            <span style="color: #999999">暂无附件</span>
          </template>
          <!--附件-->
        </el-descriptions-item>


      </el-descriptions>

    </el-dialog>


  </div>
</template>

<script>
import {listProject, getProject, delProject, addProject, updateProject} from "@/api/oa/project";
import {addTask, getTaskByDictType, listTaskByType} from "@/api/oa/task";
import {getDicts} from "@/api/system/dict/data";
import {findContractByProjectId, getOaContract, selectContractByProjectId} from "@/api/oa/oaContract";
import {listByIds} from "@/api/system/oss";

export default {
  name: "Project",
  dicts: ['sys_project_status', 'sys_project_type', 'sys_sort_grade', 'sys_work_type', 'sys_sort_grade'],
  data() {
    return {
      // 按钮loading
      buttonLoading: false,
      // 遮罩层
      loading: true,
      reverse: true,
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
      // 项目管理表格数据
      projectList: [],
      //tabs标签
      activeName: 'first',
      activeTitle: 'first',
      activeChild: 'one',
      tabContract: 'tab01',
      //tabs标签-left
      tabPosition: 'left',
      tabPositionTask: 'left',
      //步骤列表
      stepsList: [],
      stepsListTop: [],
      showCard: false,
      active: 0,
      // 弹出层标题
      title: "",
      titleContract: "",
      //添加弹出层
      addShow: false,
      //编辑弹出层
      editShow: false,
      // 是否显示弹出层
      open: false,
      //合同项目信息
      projectId: '',
      desTitle: '8888',
      projectNum: '',
      contractForm: {},
      //附件
      fileList: [],
      //详情lable背景
      lableBg: "background: #f0f9eb; width:150px; text-align: center;",

      //采购合同列表
      oaContractList: [],
      dialogContract: false,

      searchTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        projectName: undefined,
        projectNum: undefined,
        beginTime: undefined,
        finishTime: undefined
      },

      printObj: {
        id: '',    // 这里是要打印元素的ID
        popTitle: '',  // 打印的标题
        extraCss: '',  // 打印可引入外部的一个 css 文件
        extraHead: ''  // 打印头部文字
      },
      //履约保证金
      bail: {
        money: undefined,
        theTime: undefined,
        ratio: undefined,
        backMoney: undefined,
        backTime: undefined
      },
      // 表单参数
      form: {},
      periodItem: '',
      taskForm: {},
      activities: [{
        content: '支持使用图标',
        timestamp: '2018-04-12 20:46',
        size: 'large',
        type: 'primary',
        icon: 'el-icon-more'
      }, {
        content: '支持自定义颜色',
        timestamp: '2018-04-03 20:46',
        color: '#0bbd87'
      }, {
        content: '支持自定义尺寸',
        timestamp: '2018-04-03 20:46',
        size: 'large'
      }, {
        content: '默认样式的节点',
        timestamp: '2018-04-03 20:46'
      }],
      // 表单校验
      rules: {
        projectId: [
          {required: true, message: "ID不能为空", trigger: "blur"}
        ],
        projectName: [
          {required: true, message: "项目名称不能为空", trigger: "blur"}
        ],
        projectNum: [
          {required: true, message: "项目编号不能为空", trigger: "blur"}
        ],
        projectType: [
          {required: true, message: "项目类型不能为空", trigger: "change"}
        ],
        functionary: [
          {required: true, message: "项目负责人不能为空", trigger: "blur"}
        ],
        address:[
          {required: true, message: "项目地址不能为空", trigger: "blur"}
        ],
        funds: [
          { required: true, message: '项目总金额不能为空', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              const regex = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9]))/;
              if (!regex.test(value)) {
                callback(new Error('请输入正确的金额格式'));
              } else {
                callback();
              }
            },
            trigger: 'blur'
          }
        ],
        beginTime: [
          {required: true, message: "开始日期不能为空", trigger: "blur"}
        ],
        finishTime: [
          {required: true, message: "结束日期不能为空", trigger: "blur"}
        ],
        contractId: [
          {required: true, message: "关联合同ID不能为空", trigger: "blur"}
        ],
      },
      taskRules: {
        projectId: [
          {required: true, message: "项目标识不能为空", trigger: "blur"}
        ],
        taskTitle: [
          {required: true, message: "任务主题不能为空", trigger: "blur"}
        ],
        taskType: [
          {required: true, message: "工作类型不能为空", trigger: "change"}
        ],
        beginTime: [
          {required: true, message: "开始时间不能为空", trigger: "blur"}
        ],
        finishTime: [
          {required: true, message: "结束时间不能为空", trigger: "blur"}
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getDicts()
  },

  methods: {
    /**new-工作类型进度**/
    getTaskByDictType(pid){
      getTaskByDictType(pid).then(res => {
        //排序
        this.stepsList = res.data.data.taskList.sort((a, b) => parseInt(b.dictValue) - parseInt(a.dictValue));
        this.active = res.data.data.active;
      })
    },

    /**获取字典类型为工作类型的数据**/
    getDicts(){
      getDicts('sys_work_type').then(res => {
        this.stepsListTop = res.data;
      })
    },

    getFileTask(acc) {
      return new Promise((resolve, reject) => {
        listByIds(acc).then(res => {
          resolve(res.data);
        })
      });
    },

    /**根据类型查询任务的promise*/
    getListByType(type, pid) {
      return new Promise(resolve => {
        let data = {
          taskType: type,
          projectId: pid
        }
        listTaskByType(data).then(res => {
          resolve(res)
        })
      })
    },
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
        this.total = response.total;
        this.loading = false;
      });
    },

    /**履约保证金是否缴纳**/
    bailStatusTo(data){
        if(JSON.parse(data).money === '' || JSON.parse(data).money === undefined){
          return 0; //未缴纳
        }else {
          return 1 //已缴纳
        }
    },
    /**履约保证金是否退回**/
    bailStatusIn(data){
      if(JSON.parse(data).backMoney === '' || JSON.parse(data).backMoney === undefined){
        return 0; //未退回
      }else {
        return 1 //已退回
      }
    },

    /**任务打印按钮操作**/
    printValue(index) {
      this.printObj.id = 'print' + index;
    },

    /**日期转字符串**/
    getRealDate(startDate) {
      // 时间转换
      var datejson = new Date(startDate).toJSON();
      var date = new Date(+new Date(datejson)
        + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
      return date;
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        projectId: undefined,
        projectName: undefined,
        projectNum: undefined,
        projectType: undefined,
        address: undefined,
        functionary: undefined,
        beginTime: undefined,
        finishTime: undefined,
        delivery: undefined,
        guarantee: undefined,
        introduction: undefined,
        projectGrade: undefined,
        projectStatus: undefined,
        contractId: undefined,
        invoiceName: undefined,
        invoiceNumber: undefined,
        invoiceAddress: undefined,
        invoiceBank: undefined,
        accessory: undefined,
        remark: undefined,
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined,
        bail: undefined
      };
      this.resetForm("form");
      this.contractForm = {};
      this.fileList = [];
      this.bail = {};
    },
    /**标签*/
    handleClick(tab, event) {
      if (tab.index == '1' && this.projectId != '') {
        this.getTaskByDictType(this.projectId);
        // this.taskWorkType(this.projectId);
        // this.getStepsActive(this.projectId);
      }
      if (tab.index == '2' && this.projectId != '') {
        this.activeTitle = 'first'
      }
    },

    activeClick(tab, event) {
      if (tab.index == '1') {
        // console.log(33356656, tab.index);
      }
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.searchTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.projectId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.periodItem = ''
      this.addShow = true;
      // this.open = true;
      this.title = "添加项目";
    },
    /**
     * 时间范围失去焦点时绑定日期数据
     * @param e
     */
    getTimeBlur(e) {
      this.form.beginTime = e.value[0]
      this.form.finishTime = e.value[1]
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.editShow = true;
      this.activeName = 'first';
      this.activeChild = 'one';
      this.taskForm = {};
      this.reset();
      const projectId = row.projectId
      this.projectId = row.projectId;
      this.handleContract(row);
      getProject(projectId).then(response => {
        this.loading = false;
        this.form = response.data;
        this.bail = response.data.bail == null ? this.bail : JSON.parse(response.data.bail);
        let period = [];
        period.push(response.data.beginTime);
        period.push(response.data.finishTime);
        this.periodItem = period;
        this.taskForm.projectId = response.data.projectId;

        this.open = true;
        this.title = "项目名称:" + response.data.projectName;
      });
    },

    /**项目合同管理**/
    handleContract(row) {
      this.reset();
      this.loading = true;
      this.projectId = row.projectId;
      this.desTitle = row.projectName;
      this.projectNum = row.projectNum
      this.findContract(row.projectId)
    },
    findContract(pId) {
      let data = {
        projectId: pId,
        contractType: '2'
      }
      findContractByProjectId(data).then(res => {
        if (res.data == null) {
          this.contractForm = {
            contractId: undefined,
            projectId: undefined,
            contractNum: undefined,
            contractName: undefined,
            firstName: undefined,
            firstPerson: undefined,
            firstPhone: undefined,
            secondName: undefined,
            secondPerson: undefined,
            secondPhone: undefined,
            contractPrice: undefined,
            signTime: undefined,
            validity: undefined,
            contractStatus: undefined,
            accessory: undefined,
            remark: undefined,
            createBy: undefined,
            createTime: undefined,
            updateBy: undefined,
            updateTime: undefined
          }
        } else {
          this.contractForm = res.data;
          this.getFile(res.data.accessory);
        }


      })
    },

    /**采购合同列表**/
    getContractList(pId) {
      this.loading = true;
      let data = {
        projectId: pId,
        contractType: '1'
      }
      selectContractByProjectId(data).then(response => {
        this.oaContractList = response.data;
      })
    },
    /**查看采购合同**/
    handlePreview(row) {
      this.loading = true;
      this.dialogContract = true;
      this.contractForm = {};
      // this.reset();
      // this.buyOntractShow = false;
      this.titleContract = "查看合同";
      this.getContractById(row.contractId);

    },
    /**获取合同信息**/
    getContractById(cId) {
      getOaContract(cId).then(response => {
        this.loading = false;
        this.contractForm = response.data;
        this.getFile(response.data.accessory);
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          this.form.beginTime = this.periodItem[0]
          this.form.finishTime = this.periodItem[1]
          //履约保证金转json
          this.form.bail = JSON.stringify(this.bail);
          if (this.form.projectId != null) {
            updateProject(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              // this.open = false;
              this.activeChild = 'one';
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addProject(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              // this.open = false;
              this.addShow = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          }
        }
      });
    },
    /**添加任务**/
    submitTaskForm() {
      this.$refs["taskForm"].validate(valid => {
        if (valid) {
          let selectValue = this.active + 1;
          let value = this.taskForm.taskType;
          if (parseInt(value) > selectValue) {
            this.$modal.msgWarning("工作类型只能按照流程进度选择，不可跨越，请重新选择!")
          } else {
            addTask(this.taskForm).then(response => {
              this.$modal.msgSuccess("添加成功");
              this.getTaskByDictType(this.projectId);
              // this.taskWorkType(this.projectId)
              // this.getStepsActive(this.projectId)
              if (value == '9' && this.form.projectStatus == '0') {
                this.changeProjectStatus()
              }
              this.activeName = 'second'
              this.activeTitle = 'first'
            }).finally(() => {
              this.buttonLoading = false;
            });
          }
          // console.log("验证通过")
        } else {
          // console.log("验证失败")
        }
      })
    },

    /**项目完成后修改项目状态**/
    changeProjectStatus() {
      this.form.projectStatus = '1';
      updateProject(this.form).then(response => {
        this.$modal.msgSuccess("任务进度完成");
        this.getList()
      }).finally(() => {
        this.buttonLoading = false;
      });
    },

    /** 金额转为万元 **/
    convertToTenThousand(amount){
      const convertToTenThousand = (amount) => `${Number((amount / 10000).toFixed(6))} 万元`;
      return convertToTenThousand(amount);
    },

    /**合同选项卡**/
    handleTabContract(tab, event) {
      if (tab.index == '1') {
        this.getContractList(this.projectId);
      }
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

    /**关闭弹窗**/
    closeDialog() {
      this.editShow = false;
      this.getList();
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const projectIds = row.projectId || this.ids;
      this.$modal.confirm('是否确认删除项目管理编号为"' + projectIds + '"的数据项？').then(() => {
        this.loading = true;
        return delProject(projectIds);
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
      this.download('oa/project/export', {
        ...this.queryParams
      }, `project_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
<style>
.line {
  padding-left: 13px
}

.dialog-footer {
  padding-left: 70px
}

.block {
}

.el-timeline {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  list-style: none;
  padding-left: 20px;
}

.el-card__body {
  padding: 0
}

.card-col {
  margin-top: 10px;
  padding: 10px 15px
}

.el-descriptions__header {
  margin-bottom: 5px;
  margin-top: 5px;
}

.el-descriptions__extra {
  font-size: 12px;
  font-weight: 500;
  color: #cccccc
}

h4, p {
  line-height: 24px;
  padding: 0;
  margin: 0;
  font-weight: 500
}

.message {
  color: #cccccc;
  text-align: center
}

/*分割线的样式*/
.el-divider {
  background-color: #409eff;
}

.el-divider--horizontal {
  display: block;
  height: 1px;
  width: 100%;
  margin: 10px 20px 20px 20px;
}

.el-divider__text.is-left {
  color: #409eff;
  left: 0;
  font-weight: bold;
  margin: 0 10px;
  padding: 0 5px;
}

/*分割线的样式 end*/
</style>
