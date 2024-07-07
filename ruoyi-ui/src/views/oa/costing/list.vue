<template>
  <div class="app-container">

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="68px">
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
            icon="el-icon-plus"
            @click="handleUpdate(scope.row,'1')"
            v-hasPermi="['oa:finance:add']"
          >入账
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-minus"
            @click="handleUpdate(scope.row,'0')"
            v-hasPermi="['oa:finance:add']"
          >出账
          </el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-bank-card"
            @click="financeList(scope.row)"
            v-hasPermi="['oa:project:edit']"
          >明细
          </el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-bank-card"
            @click="handleCheck(scope.row)"
            v-hasPermi="['oa:project:edit']"
          >核算
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


    <!-- 出入账提交信息弹出层 -->
    <el-dialog :title="title" :visible.sync="open" width="76%" append-to-body>

      <el-descriptions class="margin-top" :column="3" border>
        <el-descriptions-item label-style="width: 120px">
          <template slot="label">
            <i class="el-icon-school"></i>
            项目名称
          </template>
          {{ projectName }}
        </el-descriptions-item>
        <el-descriptions-item label-style="width: 120px">
          <template slot="label">
            <i class="el-icon-price-tag"></i>
            项目标识
          </template>
          {{ projectId }}
        </el-descriptions-item>
        <el-descriptions-item label-style="width: 120px">
          <template slot="label">
            <i class="el-icon-key"></i>
            账单类型
          </template>
          {{ financeType == '0' ? "出账" : "入账" }}
        </el-descriptions-item>
      </el-descriptions>
      <el-divider>提交表单信息</el-divider>


      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="16">
            <el-form-item :label="financeType == 1 ? '入账名称' : '出账名称'" prop="financeTitle">
              <el-input v-model="form.financeTitle" placeholder="请输入账务名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="交易时间" prop="financeTime">
              <el-date-picker clearable
                              v-model="form.financeTime"
                              type="date"
                              value-format="yyyy-MM-dd HH:mm:ss"
                              placeholder="请选择交易时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="16">
            <el-form-item :label="financeType == 1 ? '付款方' : '经手人'" prop="financeParties">
              <el-input v-model="form.financeParties" placeholder="请输入经手人/付款方"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="支付类型" prop="payType">
              <el-select v-model="form.payType" placeholder="请选择支付类型">
                <el-option
                  v-for="dict in dict.type.sys_pay_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="开票比例" prop="makeRatio">
              <el-input v-model="form.makeRatio" placeholder="请输入开票比例"/>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="开票金额" prop="makePrice">
              <el-input v-model="form.makePrice" placeholder="请输入开票金额"/>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="开票时间" prop="makeTime">
              <el-input v-model="form.makeTime" placeholder="请输入开票金额"/>

            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="开票情况说明" prop="makeExplain">
              <el-input v-model="form.makeExplain" placeholder="请输入开票情况说明"/>
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
        <el-divider content-position="center">明细</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" plain @click="handleAddDetailList">添加明细
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" plain @click="handleDeleteDetailList">删除
            </el-button>
          </el-col>
          <!--          <el-col :span="1.5">总金额(¥)：<span style="color: #ff9900; font-size: 150%">{{priceSum}}</span>元</el-col>-->
        </el-row>
        <el-table :data="detailList" :row-class-name="rowDetailListIndex"
                  @selection-change="handleDetailListSelectionChange" ref="detailList">
          <el-table-column type="selection" width="50" align="center"/>
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="明细名称" prop="detailTitle">
            <template slot-scope="scope">
              <el-input v-model="scope.row.detailTitle" placeholder="请输入名称"/>
            </template>
          </el-table-column>
          <el-table-column label="金额(单位：元)" prop="price">
            <template slot-scope="scope">
              <el-input v-model="scope.row.price" placeholder="请输入金额" οnkeyup="value=value.replace(/[^\d]/g,'')"/>
            </template>
          </el-table-column>
          <el-table-column label="大写金额(零壹贰叁肆伍陆柒捌玖万仟佰拾亿元角分)" prop="bigPrice">
            <template slot-scope="scope">
              <el-input v-model="scope.row.bigPrice" placeholder="请输入大写金额"/>
            </template>
          </el-table-column>
          <!--          <el-table-column label="备注" prop="remark" >
                      <template slot-scope="scope">
                        <el-input v-model="scope.row.remark" placeholder="请输入备注" />
                      </template>
                    </el-table-column>-->
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="titleCheck" :visible.sync="openCheck" width="50%" append-to-body>
      <div class="tipTitle">项目名称：{{ projectName }}</div>
      <div class="tip">
        核算情况
      </div>
      <div class="demo-shadow">
        <el-row :gutter="5">
          <el-col :span="8">
            <div class="shadow-col">
              <h4>项目总金额</h4>
              <p class="cl3">¥ {{ projectCheckCount.funds}} 元</p>
            </div>
          </el-col>

          <el-col :span="8">
            <div class="shadow-col">
              <h4>项目已收款</h4>
              <p class="cl2">¥ {{ projectCheckCount.come}} 元</p>
            </div>
          </el-col>

          <el-col :span="8">
            <div class="shadow-col">
              <h4>项目未收款</h4>
              <p class="cl1">¥ {{ projectCheckCount.unliquidated }} 元</p>
            </div>
          </el-col>
        </el-row>
      </div>

      <div class="demo-shadow" style="margin-top: 20px">
        <el-row :gutter="5">
          <el-col :span="8">
            <div class="shadow-col">
              <h4>项目入账</h4>
              <p class="cl2">¥ {{ projectCheckCount.come }} 元</p>
            </div>
          </el-col>

          <el-col :span="8">
            <div class="shadow-col">
              <h4>项目支出</h4>
              <p class="cl1">¥ {{ projectCheckCount.out }} 元</p>
            </div>
          </el-col>

          <el-col :span="8">
            <div class="shadow-col">
              <h4>项目核算</h4>
              <p class="cl3">¥ {{ projectCheckCount.check }} 元</p>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-dialog>

    <el-dialog :title="titleFinance" :visible.sync="openFinance" width="75%" append-to-body>
      <div style="color: #1ab394; font-size: 18px">项目名称：{{ projectName }}</div>
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="入账明细" name="first">
          <el-form :model="financeParams" ref="queryForm" size="small" :inline="true" label-width="68px">
            <el-form-item label="入账名称" prop="financeTitle">
              <el-input
                v-model="financeParams.financeTitle"
                placeholder="请输入名称"
                clearable
              />
            </el-form-item>
            <el-form-item label="付款方" prop="financeParties">
              <el-input
                v-model="financeParams.financeParties"
                placeholder="请输入付款方"
                clearable
              />
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
              <el-button type="primary" icon="el-icon-search" size="mini" @click="searchFinance('1')">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetFinance('1')">重置</el-button>
            </el-form-item>
          </el-form>
          <el-table v-loading="loading" :data="financeListEnter">
            <el-table-column label="入账名称" align="left" prop="financeTitle" width="260"/>
            <el-table-column label="付款方" align="center" prop="financeParties"/>
            <el-table-column label="总金额" align="center" prop="priceSum"/>
            <el-table-column label="支付类型" align="center" prop="payType" width="100">
              <template slot-scope="scope">
                <dict-tag :options="dict.type.sys_pay_type" :value="scope.row.payType"/>
              </template>
            </el-table-column>
            <!--          <el-table-column label="进出账类型" align="center" prop="financeType" />-->
            <el-table-column label="交易日期" align="center" prop="financeTime" width="110">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.financeTime, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="开票状态" align="center" prop="makeTime" width="80">
              <template slot-scope="scope">
                <span v-if="scope.row.makeTime == null || (scope.row.makePrice == null || scope.row.makePrice == '')"
                      style="color: #cccccc">未开票</span>
                <span v-else style="color: #1ab394">已开票</span>
              </template>
            </el-table-column>
            <el-table-column label="创建者" align="center" prop="createBy" width="80"/>
            <el-table-column label="创建时间" align="center" prop="createTime" width="110">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="备注" align="center" prop="remark" width="180">
              <template slot-scope="scope">
                <span>{{ scope.row.remark == null || scope.row.remark == '' ? "暂无" : scope.row.remark }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-view"
                  @click="handleLook(scope.row)"
                  v-hasPermi="['oa:finance:query']"
                >查看
                </el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="financeUpdate(scope.row)"
                  v-hasPermi="['oa:finance:edit']"
                >修改
                </el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleDelete(scope.row)"
                  v-hasPermi="['oa:finance:remove']"
                >删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <pagination
            v-show="totalEnter > 0"
            :total="totalEnter"
            :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize"
            @pagination="getListFinance('1')"
          />

        </el-tab-pane>
        <el-tab-pane label="出账明细" name="second">
          <el-form :model="financeParams" ref="queryForm" size="small" :inline="true" label-width="68px">
            <el-form-item label="入账名称" prop="financeTitle">
              <el-input
                v-model="financeParams.financeTitle"
                placeholder="请输入名称"
                clearable
              />
            </el-form-item>
            <el-form-item label="付款方" prop="financeParties">
              <el-input
                v-model="financeParams.financeParties"
                placeholder="请输入付款方"
                clearable
              />
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
              <el-button type="primary" icon="el-icon-search" size="mini" @click="searchFinance('0')">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetFinance('0')">重置</el-button>
            </el-form-item>
          </el-form>

          <el-table v-loading="loading" :data="financeListOut">
            <el-table-column label="出账名称" align="left" prop="financeTitle" width="260"/>
            <el-table-column label="经手人" align="center" prop="financeParties"/>
            <el-table-column label="总金额" align="center" prop="priceSum"/>
            <el-table-column label="支付类型" align="center" prop="payType" width="80">
              <template slot-scope="scope">
                <dict-tag :options="dict.type.sys_pay_type" :value="scope.row.payType"/>
              </template>
            </el-table-column>
            <el-table-column label="交易日期" align="center" prop="financeTime" width="120">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.financeTime, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="创建者" align="center" prop="createBy" width="80"/>
            <el-table-column label="开票状态" align="center" prop="makeTime" width="80">
              <template slot-scope="scope">
                <span v-if="scope.row.makeTime == null || (scope.row.makePrice == null || scope.row.makePrice == '' )"
                      style="color: #cccccc">未开票</span>
                <span v-else style="color: #1ab394">已开票</span>
              </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createTime" width="120">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="备注" align="center" prop="remark">
              <template slot-scope="scope">
                <span>{{ scope.row.remark == null || scope.row.remark == '' ? "暂无" : scope.row.remark }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="180" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-view"
                  @click="handleLook(scope.row)"
                  v-hasPermi="['oa:finance:query']"
                >查看
                </el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="financeUpdate(scope.row)"
                  v-hasPermi="['oa:finance:edit']"
                >修改
                </el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleDelete(scope.row)"
                  v-hasPermi="['oa:finance:remove']"
                >删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <pagination
            v-show="totalOut > 0"
            :total="totalOut"
            :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize"
            @pagination="getListFinance('0')"
          />
        </el-tab-pane>
      </el-tabs>
    </el-dialog>

    <!--查看账目-->
    <el-dialog :title="title" :visible.sync="openLook" width="60%" append-to-body>
      <el-descriptions :column="3" :title="projectName" border>
        <el-descriptions-item :label="type == 1 ? '入账名称' : '出账名称'" :span="3" :labelStyle="lableBg">
          {{ form.financeTitle }}
        </el-descriptions-item>
        <el-descriptions-item label="交易时间" :span="1" :labelStyle="lableBg">
          {{ parseTime(form.financeTime, '{y}-{m}-{d}') }}
        </el-descriptions-item>
        <el-descriptions-item :label="type == 1 ? '付款方' : '经手人'" :span="1" :labelStyle="lableBg">
          {{ form.financeParties }}
        </el-descriptions-item>
        <el-descriptions-item label="支付类型" :span="1" :labelStyle="lableBg">
          <dict-tag :options="dict.type.sys_pay_type" :value="form.payType"/>
        </el-descriptions-item>
        <el-descriptions-item label="开票状态" :span="2" :labelStyle="lableBg">
          <span v-if="form.makeTime == null || form.makePrice == null" style="color: #cccccc">未开票</span>
          <span v-else style="color: #1ab394">
              已开票, 开票金额：¥ {{ form.makePrice }},
              开票比例：{{ form.makeRatio == null ? '未填' : form.makeRatio }},
              开票时间：{{ form.makeTime }},
              开票情况说明：{{ form.makeExplain == null ? '未填' : form.makeExplain }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="创建者" :span="1" :labelStyle="lableBg">
          {{ form.createBy }}
        </el-descriptions-item>
        <el-descriptions-item label="附件" :span="3" :labelStyle="lableBg">
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
      <el-table :data="detailList" style="margin-top: 30px;">
        <el-table-column label="明细名称" prop="detailTitle">
          <template slot-scope="scope">
            <div>{{ scope.row.detailTitle }}</div>
          </template>
        </el-table-column>
        <el-table-column label="金额(单位：元)" prop="price">
          <template slot-scope="scope">
            <div>¥{{ scope.row.price }}元</div>
          </template>
        </el-table-column>
        <el-table-column label="大写金额" prop="bigPrice">
          <template slot-scope="scope">
            <div>{{ scope.row.bigPrice }}</div>
          </template>
        </el-table-column>
        <!--        <el-table-column label="备注" prop="remark" >
                  <template slot-scope="scope">
                    <div>{{scope.row.remark}}</div>
                  </template>
                </el-table-column>-->
      </el-table>
      <h2 class="cl3">{{ type == 1 ? '入账合计' : '出账合计' }}：¥{{ priceSum }}元</h2>
    </el-dialog>
  </div>
</template>

<script>
import {listProject, getProject, delProject, addProject, updateProject} from "@/api/oa/project";
import {
  addFinance,
  delFinance,
  getFinance,
  findProjectFinance,
  listFinancePro,
  updateFinance, listFinance
} from "@/api/oa/finance";

export default {
  name: "list",
  dicts: ['sys_project_type', 'sys_project_status', 'sys_pay_type'],
  data() {
    return {
      // 项目管理表格数据
      projectList: [],
      //项目ID
      projectId: '',
      projectName: '',
      // 项目总条数
      total: 0,
      //入账弹出层标题
      title: '',
      //tab选中界面
      activeName: 'first',
      //出入账弹出层
      open: false,
      //进出账类型0：支出，1：入账
      financeType: '',
      //表单
      form: {},
      // 子表选中数据
      checkedDetailList: [],
      //子表
      detailList: {},
      priceSum: 0,
      // 按钮loading
      buttonLoading: false,
      // 遮罩层
      loading: true,
      // 进出账管理表格数据
      financeListEnter: [],
      financeListOut: [],
      // 分页总条数
      totalEnter: 0,
      totalOut: 0,

      //搜索日期范围
      searchTime: [],

      //核算弹出层
      openCheck: false,
      titleCheck: "",
      projectCheckCount: {},

      //明细弹出层
      openFinance: false,
      titleFinance: "",
      //查看弹出层
      openLook: false,
      //详情lable背景
      lableBg: "background: #f0f9eb; width:150px; text-align: center;",
      type: '',
      // 查询参数
      financeParams: {
        pageNum: 1,
        pageSize: 10,
        projectId: undefined,
        financeTitle: undefined,
        financeType: undefined,
      },


      // 表单校验
      rules: {
        financeTitle: [
          {required: true, message: "账务名称不能为空", trigger: "blur"}
        ],
        financeParties: [
          {required: true, message: "经手人/付款方不能为空", trigger: "blur"}
        ],
        financeType: [
          {required: true, message: "进出账类型不能为空", trigger: "blur"}
        ]
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

    //明细按钮操作
    financeList(row) {

      this.openFinance = true;
      this.titleFinance = "明细管理";
      this.projectId = row.projectId;
      this.financeType = row.projectType;
      this.projectName = row.projectName;
      this.activeName = "first"
      this.getListFinance('1');
    },

    //明细列表
    getListFinance(type) {
      this.loading = true;
      this.financeParams = {
        financeType: type,
        projectId: this.projectId, //项目id为0，排除项目
        financeTitle: this.financeParams.financeTitle,
        financeParties: this.financeParams.financeParties
      }
      this.financeParams.params = {};
      if (null != this.searchTime && '' != this.searchTime) {
        this.financeParams.params["beginCreateTime"] = this.getRealDate(this.searchTime[0]);
        this.financeParams.params["endCreateTime"] = this.getRealDate(this.searchTime[1]);
      }

      listFinance(this.financeParams).then(response => {
        //出账列表
        if (type == '0') {
          this.financeListPro(response.rows).then(res => {
            this.financeListOut = res;
          })
          this.totalOut = response.total;
        }
        //入账列表
        if (type == '1') {
          this.financeListPro(response.rows).then(res => {
            this.financeListEnter = res;
          })
          this.totalEnter = response.total;
        }
        this.loading = false;
      });
    },


    financeListPro(obj) {
      return new Promise((resolve, reject) => {
        let financeArr = []
        obj.forEach((vo, index) => {
          let obj = vo.detailList;
          let sum = []
          if (obj.length > 0) {
            obj.forEach((vo, key) => {
              sum.push(parseFloat(vo.price))
            })
          }
          let priceSum = sum.reduce((accumulator, currentValue) => accumulator + currentValue);
          financeArr.push({
            index: vo.index,
            // projectName: projectName,
            financeId: vo.financeId,
            financeTitle: vo.financeTitle,
            financeParties: vo.financeParties,
            financeType: vo.financeType,
            payType: vo.payType,
            makePrice: vo.makePrice,
            makeTime: vo.makeTime,
            financeTime: vo.financeTime,
            createBy: vo.createBy,
            createTime: vo.createTime,
            detailList: vo.detailList,
            priceSum: (vo.financeType == '0' ? ' - ' : ' + ') + '¥' + priceSum + '元',
            project: vo.project,
            remark: vo.remark
          })
        })
        resolve(financeArr);
      });
    },

    /** 入账按钮操作 */
    handleUpdate(row, type) {
      this.reset();
      this.open = true;
      this.title = type == '1' ? "提交入账信息" : "提交出账信息";
      this.projectId = row.projectId;
      this.financeType = type;
      this.projectName = row.projectName;
      if (this.detailList.length == 0) {
        this.handleAddDetailList()
      }

    },

    /**出入账修改*/
    financeUpdate(row) {
      this.reset();
      this.openFinance = false;
      this.open = true;
      this.title = "修改操作";
      // this.projectShow = false;
      const financeId = row.financeId;
      getFinance(financeId).then(response => {
        this.loading = false;
        this.form = response.data;
        this.detailList = response.data.detailList;
        this.projectName = response.data.project.projectName;
        this.projectId = response.data.project.projectId;

        // this.detailPriceSum(response.data.detailList);
      });

    },

    /**查看按钮操作**/
    handleLook(row) {
      this.type = row.financeType
      this.loading = true;
      this.title = "查看账目";
      this.reset();
      const financeId = row.financeId || this.ids
      getFinance(financeId).then(response => {
        this.loading = false;
        this.form = response.data;
        this.detailList = response.data.detailList;
        //核算求和
        this.detailPriceSum(response.data.detailList);
        this.openLook = true;

      });
    },


    //核算弹出层
    handleCheck(row) {
      console.log(3333, row)
      this.openCheck = true;
      this.titleCheck = "项目核算";
      this.projectName = row.projectName;

      this.projectCheck(row);
    },
    /**本项目核算**/
    projectCheck(row) {
      let map = {
        projectId: row.projectId,
      }

      findProjectFinance(map).then(response => {
        this.projectCheckCount = response.data
      })

    },

    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        //关联project属性 需要删除
        delete this.form.project;
        if (valid) {
          let arr = this.detailList;
          let boolValue = []
          arr.forEach((vo, i) => {
            let ii = i + 1
            let price = this.isDecimal(vo.price, 2)
            if (vo.detailTitle == null || vo.detailTitle == undefined || vo.detailTitle == "" ) {
              this.$modal.msgWarning("第" + ii + "条明细名称不能为空！");
            } else if (vo.price == null || vo.price == undefined || vo.price == "") {
              this.$modal.msgWarning("第" + ii + "条明细金额不能为空,且必须是两位小数点数字！");
            } else if (!price) {
              this.$modal.msgWarning("第" + ii + "条明细金额必须是两位小数点数字！");
            } else if (vo.bigPrice == null || vo.bigPrice == undefined || vo.bigPrice == "" ) {
              this.$modal.msgWarning("第" + ii + "条明细大写金额不能为空！");
            } else if(this.smalltoBIG(vo.price) != vo.bigPrice){
              this.$modal.msgWarning("第" + ii + "条明细大小写金额不一致！");
            }else {
              boolValue.push(1)
            }
          })

          if(arr.length == boolValue.length){
            this.form.detailList = arr;
            if (this.form.financeId != null) {
              updateFinance(this.form).then(response => {
                this.$modal.msgSuccess(response.msg);
                this.open = false;
                this.openFinance = true;
                this.getListFinance(this.form.financeType)
                if (this.form.financeType == '1') {
                  this.activeName = "first"
                }
                if (this.form.financeType == '0') {
                  this.activeName = "second"
                }
              }).finally(() => {
                this.buttonLoading = false;
              });
            } else {
              this.form.projectId = this.projectId;
              this.form.financeType = this.financeType;
              addFinance(this.form).then(response => {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.openFinance = true;
                this.getListFinance('1')
                this.getListFinance('0')
                //定位到入账列表
                if (this.financeType == '1') {
                  this.activeName = "first"
                }
                if (this.financeType == '0') {
                  this.activeName = "second"
                }
              }).finally(() => {
                this.buttonLoading = false;
              });
            }

            console.log("验证成功")
          }
        } else {
          console.log("验证失败")
        }
      })
    },


    isDecimal(n, len) {
      return new RegExp("^\\d+(\\.\\d{1," + len + "})?$").test(n);
    },


    // 表单重置
    reset() {
      this.form = {
        financeId: undefined,
        projectId: undefined,
        financeTitle: undefined,
        financeParties: undefined,
        payType: undefined,
        financeType: undefined,
        financeTime: undefined,
        makeTime: undefined,
        makeRatio: undefined,
        makePrice: undefined,
        makeExplain: undefined,
        accessory: undefined,
        remark: undefined,
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined
      };
      this.detailList = []
      this.resetForm("form");
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },

    /** 金额转为万元 **/
    convertToTenThousand(amount){
      const convertToTenThousand = (amount) => `${Number((amount / 10000).toFixed(6))} 万元`;
      return convertToTenThousand(amount);
    },

    /**
     * 求和
     * obj是列表数据
     * */
    detailPriceSum(obj) {
      if (obj.length > 0) {
        let sum = []
        obj.forEach((vo, key) => {
          sum.push(parseFloat(vo.price))
        })
        this.priceSum = sum.reduce((accumulator, currentValue) => accumulator + currentValue);
      }
    },
    /**
     * tab选项卡点击事件
     * */
    handleClick(tab, event) {
      //条件判断
      if (tab.index == '0') {
        this.getListFinance('1');
      }
      if (tab.index == '1') {
        this.getListFinance('0');
      }
    },

    /** 出入账明细序号 */
    rowDetailListIndex({row, rowIndex}) {
      row.index = rowIndex + 1;
    },
    /** 出入账明细添加按钮操作 */
    handleAddDetailList() {
      let obj = {};
      obj.detailTitle = "";
      obj.price = "";
      obj.remark = "";
      this.detailList.push(obj);
    },
    /** 出入账明细删除按钮操作 */
    handleDeleteDetailList() {
      if (this.checkedDetailList.length == 0) {
        this.$modal.msgError("请先选择要删除的出入账明细数据");
      } else {
        const detailList = this.detailList;
        const checkedDetailList = this.checkedDetailList;
        this.detailList = detailList.filter(function (item) {
          return checkedDetailList.indexOf(item.index) == -1
        });
      }
    },
    /** 复选框选中数据 */
    handleDetailListSelectionChange(selection) {
      this.checkedDetailList = selection.map(item => item.index)
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 进出账搜索按钮操作 */
    searchFinance(type) {
      this.financeParams.pageNum = 1;
      this.getListFinance(type);
      // console.log(33332,this.financeParams)
      // this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams = {};
      this.searchTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 重置出入账按钮操作 */
    resetFinance(type) {
      this.resetForm("queryForm");
      this.financeParams.financeTitle = undefined;
      this.financeParams.financeParties = undefined;
      this.searchTime = '';
      this.searchFinance(type);
    },
    /**日期转字符串**/
    getRealDate(startDate) {
      // 时间转换
      var datejson = new Date(startDate).toJSON();
      var date = new Date(+new Date(datejson)
        + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
      return date;
    },
    /**人民币大小写转换**/
    smalltoBIG(num) {  //转成人民币大写金额形式
      let str1 = '零壹贰叁肆伍陆柒捌玖';  //0-9所对应的汉字
      let str2 = '万仟佰拾亿仟佰拾万仟佰拾元角分'; //数字位所对应的汉字
      let str3;    //从原num值中取出的值
      let str4;    //数字的字符串形式
      let str5 = '';  //人民币大写金额形式
      let i;    //循环变量
      let j;    //num的值乘以100的字符串长度
      let ch1;    //数字的汉语读法
      let ch2;    //数字位的汉字读法
      let nzero = 0;  //用来计算连续的零值是几个
      num = Math.abs(num).toFixed(2);  //将num取绝对值并四舍五入取2位小数
      str4 = (num * 100).toFixed(0).toString();  //将num乘100并转换成字符串形式
      j = str4.length;      //找出最高位
      if (j > 15){return '溢出';}
      str2 = str2.substr(15-j);    //取出对应位数的str2的值。如：200.55,j为5所以str2=佰拾元角分
      //循环取出每一位需要转换的值
      for(i=0;i<j;i++){
        str3 = str4.substr(i,1);   //取出需转换的某一位的值
        if (i != (j-3) && i != (j-7) && i != (j-11) && i != (j-15)){    //当所取位数不为元、万、亿、万亿上的数字时
          if (str3 == '0'){
            ch1 = '';
            ch2 = '';
            nzero = nzero + 1;
          }
          else{
            if(str3 != '0' && nzero != 0){
              ch1 = '零' + str1.substr(str3*1,1);
              ch2 = str2.substr(i,1);
              nzero = 0;
            }
            else{
              ch1 = str1.substr(str3*1,1);
              ch2 = str2.substr(i,1);
              nzero = 0;
            }
          }
        }
        else{ //该位是万亿，亿，万，元位等关键位
          if (str3 != '0' && nzero != 0){
            ch1 = "零" + str1.substr(str3*1,1);
            ch2 = str2.substr(i,1);
            nzero = 0;
          }
          else{
            if (str3 != '0' && nzero == 0){
              ch1 = str1.substr(str3*1,1);
              ch2 = str2.substr(i,1);
              nzero = 0;
            }
            else{
              if (str3 == '0' && nzero >= 3){
                ch1 = '';
                ch2 = '';
                nzero = nzero + 1;
              }
              else{
                if (j >= 11){
                  ch1 = '';
                  nzero = nzero + 1;
                }
                else{
                  ch1 = '';
                  ch2 = str2.substr(i,1);
                  nzero = nzero + 1;
                }
              }
            }
          }
        }
        if (i == (j-11) || i == (j-3)){  //如果该位是亿位或元位，则必须写上
          ch2 = str2.substr(i,1);
        }
        str5 = str5 + ch1 + ch2;
        if (i == j-1 && str3 == '0' ){   //最后一位（分）为0时，加上“整”
          str5 = str5 + '整';
        }
      }
      if (num == 0){
        str5 = '零元整';
      }
      return str5;
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const financeId = row.financeId;
      this.$modal.confirm('是否确认删除编号为"' + financeId + '"的数据项？').then(() => {
        this.loading = true;
        return delFinance(financeId);
      }).then(() => {
        if (row.financeType == '1') {
          this.getListFinance('1')
        }
        if (row.financeType == '0') {
          this.getListFinance('0')
        }
        // this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      }).finally(() => {
        this.loading = false;
      });
    },

    // 多选框选中数据
    /* handleSelectionChange(selection) {
       this.ids = selection.map(item => item.projectId)
       this.single = selection.length !== 1
       this.multiple = !selection.length
     },*/
  }
}
</script>

<style scoped>
.margin-top {
  margin: 0 50px 20px 50px
}

/*核算*/
.tipTitle{padding: 15px 0;font-size: 14px}
.tip {
  padding: 0 10px;
  background-color: #ecf8ff;
  border-radius: 4px;
  border-top: 5px solid #50bfff;
  text-align: center;
  margin-bottom: 5px;
  font-size: 20px;
  height: 50px;
  line-height: 46px;
}

.demo-shadow {
  height: 140px;
  width: 100%;
  padding-top: 10px;
  text-align: center;
  line-height: 28px;
  background-color: #ecf8ff;
}

.demo-shadow h4 {
  color: #999999;
}

.demo-shadow p {
  font-size: 28px
}

.cl1 {
  color: #ff9900
}

.cl2 {
  color: #67C23A
}

.cl3 {
  color: #409EFF
}

.bottom-shadow {
  margin-top: 30px;
  text-align: center
}


/*end 核算*/
</style>
