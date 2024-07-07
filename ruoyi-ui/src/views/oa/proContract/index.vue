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


    <el-table v-loading="loading" :data="projectList">
      <el-table-column label="项目名称" align="left" prop="projectName"/>
      <el-table-column label="项目编号" align="left" prop="projectNum"/>

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
            icon="el-icon-office-building"
            @click="handleContract(scope.row)"
            v-hasPermi="['oa:project:edit']"
          >合同管理
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
    <el-dialog :title="title" :visible.sync="tabsProShow" @close="closeDialog" width="76%" append-to-body>
      <div style="font-size: 16px;color: #1ab394">项目名称：{{desTitle}}</div>
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="项目合同" name="first">
          <div v-if="addBottomStatus" style="margin-bottom: 30px;">
            <div id="main02">
            <el-descriptions :title="projectName" class="margin-top" :column="3" border>
              <template slot="extra">
                项目编号：{{ projectNum }}
              </template>
              <el-descriptions-item span="2" :labelStyle="lableBg">
                <template slot="label">
                  <i class="el-icon-user"></i>
                  合同名称
                </template>
                {{ form.contractName }}
              </el-descriptions-item>
              <el-descriptions-item span="1" :labelStyle="lableBg">
                <template slot="label">
                  <i class="el-icon-s-unfold"></i>
                  合同编号
                </template>
                {{ form.contractNum }}
              </el-descriptions-item>
              <el-descriptions-item span="1" :labelStyle="lableBg">
                <template slot="label">
                  <i class="el-icon-eleme"></i>
                  甲方名称
                </template>
                {{ form.firstName }}
              </el-descriptions-item>
              <el-descriptions-item span="1" :labelStyle="lableBg">
                <template slot="label">
                  <i class="el-icon-user"></i>
                  甲方负责人
                </template>
                {{ form.firstPerson }}
              </el-descriptions-item>
              <el-descriptions-item span="1" :labelStyle="lableBg">
                <template slot="label">
                  <i class="el-icon-mobile-phone"></i>
                  甲方电话
                </template>
                {{ form.firstPhone }}
              </el-descriptions-item>
              <el-descriptions-item span="1" :labelStyle="lableBg">
                <template slot="label">
                  <i class="el-icon-eleme"></i>
                  乙方名称
                </template>
                {{ form.secondName }}
              </el-descriptions-item>
              <el-descriptions-item span="1" :labelStyle="lableBg">
                <template slot="label">
                  <i class="el-icon-user"></i>
                  乙方负责人
                </template>
                {{ form.secondPerson }}
              </el-descriptions-item>
              <el-descriptions-item span="1" :labelStyle="lableBg">
                <template slot="label">
                  <i class="el-icon-mobile-phone"></i>
                  乙方电话
                </template>
                {{ form.secondPhone }}
              </el-descriptions-item>
              <el-descriptions-item span="1" :labelStyle="lableBg">
                <template slot="label">
                  <i class="el-icon-trophy"></i>
                  合同金额
                </template>
                {{ form.contractPrice }}
              </el-descriptions-item>
              <el-descriptions-item span="1" :labelStyle="lableBg">
                <template slot="label">
                  <i class="el-icon-c-scale-to-original"></i>
                  签订日期
                </template>
                {{ parseTime(form.signTime, '{y}-{m}-{d}') }}
              </el-descriptions-item>
              <el-descriptions-item span="1" :labelStyle="lableBg">
                <template slot="label">
                  <i class="el-icon-date"></i>
                  质保期
                </template>
                {{ form.validity }}
              </el-descriptions-item>
              <el-descriptions-item span="3" :labelStyle="lableBg">
                <template slot="label">
                  <i class="el-icon-pie-chart"></i>
                  备注
                </template>
                {{ form.remark }}
              </el-descriptions-item>

              <el-descriptions-item span="3" :labelStyle="lableBg">
                <template slot="label">
                  <i class="el-icon-paperclip"></i>
                  附件1
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
            </div>
          </div>
          <div v-else>
            <div>项目名称：{{desTitle}}</div>
            <div class="message">{{ message }}</div>
          </div>
          <div>
        <span class="pull-right print" v-print="printObj" @click="printId('02')">
          <i class="el-icon-printer"></i>
        </span>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-school"
              @click="editOntract"
              v-if="addBottomStatus"
            >编辑项目合同
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-school"
              @click="addOntract"
              v-else
            >添加项目合同
            </el-button>
          </div>
          <!--添加编辑项目合同-->
          <div v-show="openForm">
            <el-form ref="form" :model="form" :rules="rules" label-width="110px">
              <el-row>
                <el-col :span="16">
                  <el-form-item label="合同名称" prop="contractName">
                    <el-input v-model="form.contractName" placeholder="请输入项目名称"/>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="合同编号" prop="contractNum">
                    <el-input v-model="form.contractNum" placeholder="请输入合同编号"/>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="甲方名称" prop="firstName">
                    <el-input v-model="form.firstName" placeholder="请输入甲方名称"/>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="甲方负责人" prop="firstPerson">
                    <el-input v-model="form.firstPerson" placeholder="请输入甲方负责人"/>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="甲方电话" prop="firstPhone">
                    <el-input v-model="form.firstPhone" placeholder="请输入甲方电话"/>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8">
                  <el-form-item label="乙方名称" prop="secondName">
                    <el-input v-model="form.secondName" placeholder="请输入乙方名称"/>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="乙方负责人" prop="secondPerson">
                    <el-input v-model="form.secondPerson" placeholder="请输入乙方负责人"/>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="乙方电话" prop="secondPhone">
                    <el-input v-model="form.secondPhone" placeholder="请输入乙方电话"/>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="8">
                  <el-form-item label="合同金额" prop="contractPrice">
                    <el-input v-model="form.contractPrice" placeholder="请输入合同金额"/>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="签订日期" prop="signTime">
                    <el-date-picker clearable
                                    v-model="form.signTime"
                                    type="date"
                                    value-format="yyyy-MM-dd HH:mm:ss"
                                    placeholder="请选择签订日期">
                    </el-date-picker>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="质保期" prop="validity">
                    <el-input v-model="form.validity" placeholder="请输入质保期"/>
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
            </el-form>
            <div class="dialog-footer">
              <el-button type="primary" @click="submitForm('2')">保存</el-button>
              <el-button @click="cancel">关闭</el-button>
            </div>
          </div>


        </el-tab-pane>
        <el-tab-pane label="采购合同" name="second">

          <el-table :data="oaContractList" style="margin-bottom: 30px">
            <el-table-column label="合同名称" align="left" prop="contractName" />
            <el-table-column label="甲方名称" align="center" prop="firstName" width="190" />
            <el-table-column label="乙方名称" align="left" prop="secondName" width="230" />
            <el-table-column label="乙方负责人" align="center" prop="secondPerson" width="120" />
            <el-table-column label="乙方电话" align="center" prop="secondPhone" width="120" />
            <el-table-column label="合同金额(元)" align="center" prop="contractPrice" width="100" />
            <el-table-column label="签订日期" align="center" prop="signTime" width="120">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.signTime, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="创建者" align="center" prop="createBy" width="80" />
            <el-table-column label="创建时间" align="center" prop="createTime" width="120">
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
                  v-hasPermi="['oa:oaContract:query']"
                >查看</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleDelete(scope.row)"
                  v-hasPermi="['oa:oaContract:remove']"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-school"
              @click="addBuyOntract(1)"
            >添加采购合同
            </el-button>
          <!--添加采购合同-->
          <div v-show="buyOntractShow">
            <div style="padding:20px 30px">项目名称：{{desTitle}}</div>
            <el-form ref="form" :model="form" :rules="rules" label-width="120px">
              <el-row>
                <el-col :span="16">
                  <el-form-item label="合同名称" prop="contractName">
                    <el-input v-model="form.contractName" placeholder="请输入项目名称"/>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="合同编号" prop="contractNum">
                    <el-input v-model="form.contractNum" placeholder="请输入合同编号"/>
                  </el-form-item>
                </el-col>

                <el-col :span="8">
                  <el-form-item label="乙方名称" prop="secondName">
                    <el-input v-model="form.secondName" placeholder="请输入乙方名称"/>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="乙方负责人" prop="secondPerson">
                    <el-input v-model="form.secondPerson" placeholder="请输入乙方负责人"/>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="乙方电话" prop="secondPhone">
                    <el-input v-model="form.secondPhone" placeholder="请输入乙方电话"/>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="甲方名称" prop="firstName">
                    <el-input v-model="form.firstName" placeholder="请输入甲方名称"/>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="甲方负责人" prop="firstPerson">
                    <el-input v-model="form.firstPerson" placeholder="请输入甲方负责人"/>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="甲方电话" prop="firstPhone">
                    <el-input v-model="form.firstPhone" placeholder="请输入甲方电话"/>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="合同金额" prop="contractPrice">
                    <el-input v-model="form.contractPrice" placeholder="请输入合同金额"/>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="签订日期" prop="signTime">
                    <el-date-picker clearable
                                    v-model="form.signTime"
                                    type="datetime"
                                    value-format="yyyy-MM-dd HH:mm:ss"
                                    placeholder="请选择签订日期">
                    </el-date-picker>
                  </el-form-item>
                </el-col>
                <el-col :span="8">
                  <el-form-item label="质保期" prop="validity">
                    <el-input v-model="form.validity" placeholder="请输入质保期"/>
                  </el-form-item>
                </el-col>
                <el-col :span="24">
                  <el-form-item label="备注" prop="remark">
                    <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item label="附件" prop="accessory">
                <file-upload v-model="form.accessory"/>
              </el-form-item>

            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button type="primary" @click="submitForm('1')">保存</el-button>
              <el-button @click="cancel">关闭</el-button>
            </div>
          </div>


        </el-tab-pane>
      </el-tabs>
    </el-dialog>



    <el-dialog :title="title" :visible.sync="dialogContract" width="55%" append-to-body>

      <div id="main01">
      <el-descriptions :title="desTitle" class="margin-top" :column="4" border>
        <template slot="extra">
         项目编号：{{ projectNum }}
        </template>
        <el-descriptions-item span="2" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-user"></i>
            合同名称
          </template>
          {{ form.contractName }}
        </el-descriptions-item>
        <el-descriptions-item span="2" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-s-unfold"></i>
            合同编号
          </template>
          {{ form.contractNum }}
        </el-descriptions-item>
        <el-descriptions-item span="2" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-eleme"></i>
            甲方名称
          </template>
          {{ form.firstName }}
        </el-descriptions-item>
        <el-descriptions-item span="2" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-user"></i>
            甲方负责人
          </template>
          {{ form.firstPerson }}
        </el-descriptions-item>
        <el-descriptions-item span="2" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-mobile-phone"></i>
            甲方电话
          </template>
          {{ form.firstPhone }}
        </el-descriptions-item>
        <el-descriptions-item span="2" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-trophy"></i>
            合同金额
          </template>
          {{ form.contractPrice }}
        </el-descriptions-item>
        <el-descriptions-item span="2" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-eleme"></i>
            乙方名称
          </template>
          {{ form.secondName }}
        </el-descriptions-item>
        <el-descriptions-item span="2" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-user"></i>
            乙方负责人
          </template>
          {{ form.secondPerson }}
        </el-descriptions-item>
        <el-descriptions-item span="2" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-mobile-phone"></i>
            乙方电话
          </template>
          {{ form.secondPhone }}
        </el-descriptions-item>

        <el-descriptions-item span="2" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-c-scale-to-original"></i>
            签订日期
          </template>
          {{ form.signTime }}
        </el-descriptions-item>
        <el-descriptions-item span="2" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-date"></i>
            质保期
          </template>
          {{ form.validity }}
        </el-descriptions-item>
        <el-descriptions-item span="2" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-pie-chart"></i>
            备注
          </template>
          {{ form.remark }}
        </el-descriptions-item>
        <el-descriptions-item span="4" :labelStyle="lableBg">
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
      </div>

      <div>
        <span class="pull-right print" v-print="printObj" @click="printId('01')">
          <i class="el-icon-printer"></i>
        </span>

        <el-button
          size="mini"
          type="text"
          icon="el-icon-school"
          @click="editOntract"
          v-hasPermi="['oa:oaContract:edit']"
        >点击编辑
        </el-button>
      </div>



      <div v-show="openForm">
        <el-form ref="form" :model="form" :rules="rules" label-width="120px">
          <el-row>
            <el-col :span="16">
              <el-form-item label="合同名称" prop="contractName">
                <el-input v-model="form.contractName" placeholder="请输入项目名称"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="合同编号" prop="contractNum">
                <el-input v-model="form.contractNum" placeholder="请输入合同编号"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="甲方名称" prop="firstName">
                <el-input v-model="form.firstName" placeholder="请输入甲方名称"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="甲方负责人" prop="firstPerson">
                <el-input v-model="form.firstPerson" placeholder="请输入甲方负责人"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="甲方电话" prop="firstPhone">
                <el-input v-model="form.firstPhone" placeholder="请输入甲方电话"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="乙方名称" prop="secondName">
                <el-input v-model="form.secondName" placeholder="请输入乙方名称"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="乙方负责人" prop="secondPerson">
                <el-input v-model="form.secondPerson" placeholder="请输入乙方负责人"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="乙方电话" prop="secondPhone">
                <el-input v-model="form.secondPhone" placeholder="请输入乙方电话"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="合同金额" prop="contractPrice">
                <el-input v-model="form.contractPrice" placeholder="请输入合同金额"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="签订日期" prop="signTime">
                <el-date-picker clearable
                                v-model="form.signTime"
                                type="datetime"
                                value-format="yyyy-MM-dd HH:mm:ss"
                                placeholder="请选择签订日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="质保期" prop="validity">
                <el-input v-model="form.validity" placeholder="请输入质保期"/>
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



        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm('1')">保存</el-button>
          <el-button @click="cancel">关闭</el-button>
        </div>
      </div>




    </el-dialog>
  </div>


</template>

<script>
import {listProject} from "@/api/oa/project";
import {
  addOaContract,
  findContractByProjectId,
  updateOaContract,
  selectContractByProjectId,
  getOaContract, delOaContract
} from "@/api/oa/oaContract";
import {listByIds} from "@/api/system/oss";

export default {
  name: "index.vue",
  dicts: ['sys_project_type', 'sys_project_status'],
  data() {
    return {
      // 项目管理表格数据
      projectList: [],
      projectId: '',
      // 项目总条数
      total: 0,
      // 显示搜索条件
      showSearch: true,
      searchTime: [],

      title: '',
      tabsProShow: false,
      //合同tabs标签页
      activeName: 'first',
      //项目合同提示信息
      message: "",
      //显示添加合同状态
      addContractShow: false,
      //点击添加按钮状态
      addBottomStatus: false,
      openForm: false,
      // 表单参数
      form: {},
      // 按钮loading
      buttonLoading: false,
      //详情lable背景
      lableBg: "background: #f0f9eb; width:150px; text-align: center;",
      //合同项目信息
      desTitle: '',
      projectNum: '',
      projectName: '',
      //展开采购合同表单
      buyOntractShow: false,
      //采购合同列表
      oaContractList: [],
      dialogContract: false,
      //打印
      printObj: {
        id: "main",    // 这里是要打印元素的ID
        popTitle: '',  // 打印的标题
        extraCss: '',  // 打印可引入外部的一个 css 文件
        extraHead: ''  // 打印头部文字
      },
      // 表单校验
      rules: {
        contractName: [
          {required: true, message: "项目名称不能为空", trigger: "blur"}
        ],
        firstName: [
          {required: true, message: "甲方名称不能为空", trigger: "blur"}
        ],
        firstPerson: [
          {required: true, message: "甲方负责人不能为空", trigger: "blur"}
        ],
        firstPhone: [
          {required: true, message: "甲方电话不能为空", trigger: "blur"}
        ],
        secondName: [
          {required: true, message: "乙方名称不能为空", trigger: "blur"}
        ],
        secondPerson: [
          {required: true, message: "乙方负责人不能为空", trigger: "blur"}
        ],
        secondPhone: [
          {required: true, message: "乙方电话不能为空", trigger: "blur"}
        ],
        contractPrice: [
          { required: true, message: "项目金额不能为空", trigger: "blur" },
          // { validator: this.validateAmount, trigger: 'blur' }
        ],

      },

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        projectName: undefined,
        projectNum: undefined,
      },
    }
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

    /**项目合同管理**/
    handleContract(row) {
      this.reset();
      this.loading = true;
      this.tabsProShow = true;
      this.activeName = "first";
      this.title = "项目合同管理";
      this.projectId = row.projectId;
      this.desTitle = row.projectName;
      this.projectNum = row.projectNum;
      this.projectName = row.projectName;
      this.findContract(row.projectId)
    },
    findContract(pId){
      let data = {
        projectId: pId,
        contractType: '2'
      }
      findContractByProjectId(data).then(res => {
        if (res.data == null) {
          this.message = "暂无项目合同信息，请添加项目合同";
        } else {
          this.addBottomStatus = true;
          this.form = res.data;
        }
      })
    },



    /**添加合同**/
    addOntract() {
      this.reset();
      this.openForm = true;
    },
    /**编辑合同**/
    editOntract() {
      this.openForm = true;
    },

    /** 提交按钮 */
    submitForm(type) {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.contractId != null) {
            updateOaContract(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.openForm = false;
               if(type == '1'){
                this.getContractList(this.projectId)
                 this.getContractById(this.form.contractId);
                this.buyOntractShow = false;
              }
              if(type == '2'){
                this.findContract(this.projectId)
                this.buyOntractShow = false;
              }
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
          this.form.projectId = this.projectId;
          this.form.contractType = type;
            addOaContract(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.openForm = false;
              if(type == '1'){
                this.getContractList(this.projectId)
                this.buyOntractShow = false;
              }
              if(type == '2'){
                this.findContract(this.projectId)
                this.buyOntractShow = false;
              }
            }).finally(() => {
              this.buttonLoading = false;
            });
          }
        }
      });
    },




    /**日期转字符串**/
    getRealDate(startDate) {
      // 时间转换
      var datejson = new Date(startDate).toJSON();
      var date = new Date(+new Date(datejson)
        + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
      return date;
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams = {};
      this.searchTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /**关闭弹窗**/
    closeDialog() {
      this.addBottomStatus = false;
      this.openForm = false;
      this.getList();
    },

    /**取消按钮**/
    cancel() {
      this.openForm = false;
      this.buyOntractShow = false;
    },

    /**表单重置**/
    reset() {
      this.form = {
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
      };
      this.resetForm("form");
    },
    /**tabs标签切换页**/
    handleClick(tab, event) {
      if(tab.index == '1'){
        this.getContractList(this.projectId)
        this.buyOntractShow = false;
      }
      if(tab.index == '0'){
        this.openForm = false;
        this.findContract(this.projectId);
      }
    },

    /**采购合同列表**/
    getContractList(pId){
      this.loading = true;
      let data = {
        projectId: pId,
        contractType: '1'
      }
      selectContractByProjectId(data).then(response => {
        this.oaContractList = response.data;
      })
    },

    /**添加采购合同操作**/
    addBuyOntract(){
      this.buyOntractShow = true;
      this.reset();
    },
    /**查看采购合同**/
    handlePreview(row){
      this.loading = true;
      this.dialogContract = true;
      this.reset();
      this.buyOntractShow = false;
      this.title = "查看合同";
      this.getContractById(row.contractId);
    },
    /**获取合同信息**/
    getContractById(cId){
      getOaContract(cId).then(response => {
        this.loading = false;
        this.form = response.data;
      });
    },

    /** 金额转为万元 **/
    convertToTenThousand(amount){
      const convertToTenThousand = (amount) => `${Number((amount / 10000).toFixed(6))} 万元`;
      return convertToTenThousand(amount);
    },

    printId(num){
      this.printObj.id = "main" + num;
    },


    /** 删除按钮操作 */
    handleDelete(row) {
      const contractIds = row.contractId || this.ids;
      this.$modal.confirm('是否确认删除合同管理编号为"' + contractIds + '"的数据项？').then(() => {
        this.loading = true;
        return delOaContract(contractIds);
      }).then(() => {
        this.loading = false;
        // this.getList();
        this.getContractList(this.projectId);
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      }).finally(() => {
        this.loading = false;
      });
    },

    /**金额验证**/
    validateAmount(rule, value, callback) {
      // 自定义校验函数的实现
      if (isNaN(value) || value <= 0) {
        callback(new Error('金额必须是大于0的数字'))
      } else {
        callback()
      }
    }
  }
}
</script>

<style scoped>
.message {
  color: #cccccc;
  text-align: center
}
.print{font-size: 18px; color: #1c84c6;border: 0;margin-top: 10px;}
</style>
