<template>
  <div class="app-container">

    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="入账管理" name="first">

        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
                 label-width="68px">
          <el-form-item label="入账名称" prop="financeTitle">
            <el-input
              v-model="queryParams.financeTitle"
              placeholder="请输入名称"
              clearable
            />
          </el-form-item>
          <el-form-item label="付款方" prop="financeParties">
            <el-input
              v-model="queryParams.financeParties"
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
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery('1')">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery('1')">重置</el-button>
          </el-form-item>
        </el-form>


        <el-table v-loading="loading" :data="financeListEnter">
          <!--          <el-table-column label="入账标识" align="left" width="190" prop="financeId" v-if="true"/>-->
          <!--          <el-table-column label="项目标识" align="left" prop="projectId" />-->
          <el-table-column label="入账名称" align="left" prop="financeTitle"/>
          <el-table-column label="付款方" align="center" prop="financeParties"/>
          <el-table-column label="总金额" align="center" prop="priceSum"/>
          <el-table-column label="支付类型" align="center" prop="payType">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.sys_pay_type" :value="scope.row.payType"/>
            </template>
          </el-table-column>
          <!-- <el-table-column label="进出账类型" align="center" prop="financeType" />-->
          <el-table-column label="交易日期" align="center" prop="financeTime" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.financeTime, '{y}-{m}-{d}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="创建者" align="center" prop="createBy"/>
          <el-table-column label="开票状态" align="center" prop="makeTime" width="80">
            <template slot-scope="scope">
              <span v-if="scope.row.makeTime == null || (scope.row.makePrice == null || scope.row.makePrice == '')"
                    style="color: #cccccc">未开票</span>
              <span v-else style="color: #1ab394">已开票</span>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" align="center" prop="createTime" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
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
                @click="handleUpdate(scope.row)"
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
          @pagination="getListFinance('0')"
        />

        <el-row :gutter="10" class="mb8" style="margin-top: 30px">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="handleAddEnter"
              v-hasPermi="['oa:finance:add']"
            >入账
            </el-button>
          </el-col>
        </el-row>

      </el-tab-pane>
      <el-tab-pane label="出账管理" name="second">

        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
                 label-width="68px">
          <el-form-item label="出账名称" prop="financeTitle">
            <el-input
              v-model="queryParams.financeTitle"
              placeholder="请输入出账名称"
              clearable
            />
          </el-form-item>
          <el-form-item label="经手人" prop="financeParties">
            <el-input
              v-model="queryParams.financeParties"
              placeholder="请输入经手人"
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
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery('0')">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery('0')">重置</el-button>
          </el-form-item>
        </el-form>

        <el-table v-loading="loading" :data="financeListOut">
          <!--          <el-table-column label="入账标识" align="left" width="190" prop="financeId" v-if="true"/>-->
          <!--          <el-table-column label="项目标识" align="left" prop="projectId" />-->
          <el-table-column label="出账名称" align="left" prop="financeTitle"/>
          <el-table-column label="经手人" align="center" prop="financeParties"/>
          <el-table-column label="总金额" align="center" prop="priceSum"/>
          <el-table-column label="支付类型" align="center" prop="payType">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.sys_pay_type" :value="scope.row.payType"/>
            </template>
          </el-table-column>
          <!--          <el-table-column label="进出账类型" align="center" prop="financeType" />-->
          <el-table-column label="交易日期" align="center" prop="financeTime" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.financeTime, '{y}-{m}-{d}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="创建者" align="center" prop="createBy"/>
          <el-table-column label="开票状态" align="center" prop="makeTime" width="80">
            <template slot-scope="scope">
              <span v-if="scope.row.makeTime == null || (scope.row.makePrice == null || scope.row.makePrice == '')"
                    style="color: #cccccc">未开票</span>
              <span v-else style="color: #1ab394">已开票</span>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" align="center" prop="createTime" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
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
                @click="handleUpdate(scope.row)"
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

        <el-row :gutter="10" class="mb8" style="margin-top: 30px">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="handleAddOut"
              v-hasPermi="['oa:finance:add']"
            >出账
            </el-button>
          </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="财务核算" name="third">
        <el-row :gutter="20">
          <el-col :span="24">
            <div class="tip-top">

              按日期检索：
              <el-date-picker
                v-model="value1"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                @blur="getTimeBlur">
              </el-date-picker>
              <span class="sp1">收入：</span>
              <span class="sp2 cl2">¥ {{ checkByTime.income }} 元</span>
              <span class="sp1">支出：</span>
              <span class="sp2 cl1"> ¥ {{ checkByTime.out }} 元</span>
              <span class="sp1">核算：</span>
              <span class="sp2 cl3">¥{{ checkByTime.check }}元</span>
            </div>
          </el-col>

          <el-col :span="12">
            <div class="tip">
              本月核算情况
            </div>
            <div class="demo-shadow">
              <el-row :gutter="5">
                <el-col :span="8">
                  <div class="shadow-col">
                    <h4>本月收入</h4>
                    <p class="cl2">¥ {{ monthCheck.income }} 元</p>
                  </div>
                </el-col>

                <el-col :span="8">
                  <div class="shadow-col">
                    <h4>本月支出</h4>
                    <p class="cl1">¥ {{ monthCheck.out }} 元</p>
                  </div>
                </el-col>


                <el-col :span="8">
                  <div class="shadow-col">
                    <h4>本月核算</h4>
                    <p class="cl3">¥ {{ monthCheck.check }} 元</p>
                  </div>
                </el-col>


              </el-row>


            </div>


          </el-col>
          <el-col :span="12">
            <div class="tip">
              本年核算情况
            </div>
            <div class="demo-shadow">
              <el-row :gutter="5">
                <el-col :span="8">
                  <div class="shadow-col">
                    <h4>本月收入</h4>
                    <p class="cl2">¥ {{ yearCheck.income }} 元</p>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="shadow-col">
                    <h4>本月支出</h4>
                    <p class="cl1">¥ {{ yearCheck.out }} 元</p>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="shadow-col">
                    <h4>本月核算</h4>
                    <p class="cl3">¥ {{ yearCheck.check }} 元</p>
                  </div>
                </el-col>
              </el-row>
            </div>
          </el-col>


          <el-col :span="24">
            <div class="date-col">
              <el-table
                :data="currentList"
                style="width: 100%"
                :header-cell-style="{background:'#50bfff',color:'#ffffff',fontSize:'18px', borderTopRightRadius: '5px', borderTopLeftRadius: '5px'}"
                :row-class-name="tableRowClassName">
                <el-table-column
                  prop="onMonth"
                  label="月份(近6个月)"
                >
                </el-table-column>
                <el-table-column
                  prop="financeCome"
                  label="收入">
                </el-table-column>
                <el-table-column
                  prop="financeOut"
                  label="支出"
                >
                </el-table-column>
                <el-table-column
                  prop="count"
                  label="核算">
                </el-table-column>
              </el-table>
            </div>
          </el-col>

        </el-row>


      </el-tab-pane>
    </el-tabs>

    <!--查看账目-->
    <el-dialog :title="title" :visible.sync="openLook" width="60%" append-to-body>
      <el-descriptions :column="4" border>
        <el-descriptions-item :label="type == 1 ? '入账名称' : '出账名称'" :span="2" :labelStyle="lableBg">
          {{ form.financeTitle }}
        </el-descriptions-item>
        <el-descriptions-item label="交易时间" :span="2" :labelStyle="lableBg">
          {{ parseTime(form.financeTime, '{y}-{m}-{d}') }}
        </el-descriptions-item>
        <el-descriptions-item :label="type == 1 ? '付款方' : '经手人'" :span="2" :labelStyle="lableBg">
          {{ form.financeParties }}
        </el-descriptions-item>
        <el-descriptions-item label="支付类型" :span="2" :labelStyle="lableBg">
          <dict-tag :options="dict.type.sys_pay_type" :value="form.payType"/>
        </el-descriptions-item>

        <el-descriptions-item label="创建者" :span="2" :labelStyle="lableBg">
          {{ form.createBy }}
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="2" :labelStyle="lableBg">
          {{ form.remark }}
        </el-descriptions-item>
        <el-descriptions-item label="开票状态2" :span="4" :labelStyle="lableBg">
          <span v-if="form.makeTime == null || form.makePrice == null" style="color: #cccccc">未开票</span>
          <span v-else style="color: #1ab394">
              已开票, 开票金额：¥ {{ form.makePrice }},
              开票比例：{{ form.makeRatio == null ? '未填' : form.makeRatio }},
              开票时间：{{ form.makeTime }},
              开票情况说明：{{ form.makeExplain == null ? '未填' : form.makeExplain }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="附件" :span="4" :labelStyle="lableBg">
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
        <el-table-column label="名称" prop="detailTitle">
          <template slot-scope="scope">
            <div>{{ scope.row.detailTitle }}</div>
          </template>
        </el-table-column>
        <el-table-column label="金额" prop="price">
          <template slot-scope="scope">
            <div>¥{{ scope.row.price }}元</div>
          </template>
        </el-table-column>
        <el-table-column label="大写金额" prop="bigPrice">
          <template slot-scope="scope">
            <div>{{ scope.row.bigPrice }}</div>
          </template>
        </el-table-column>

      </el-table>
      <h2 class="cl3">{{ type == 1 ? '入账合计' : '出账合计' }}：¥{{ priceSum }}元</h2>
    </el-dialog>


    <!-- 添加或修改进出账管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="70%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="16">
            <el-form-item :label="type == 1 ? '入账名称' : '出账名称'" prop="financeTitle">
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
            <el-form-item :label="type == 1 ? '付款方' : '经手人'" prop="financeParties">
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
              <el-input v-model="form.makeTime" placeholder="请输入开票时间"/>
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
          <!--<el-col :span="1.5">总金额(¥)：<span style="color: #ff9900; font-size: 150%">{{priceSum}}</span>元</el-col>-->
        </el-row>

        <el-table :data="detailList" :row-class-name="rowDetailListIndex"
                  @selection-change="handleDetailListSelectionChange" ref="detailList">
          <el-table-column type="selection" width="50" align="center"/>
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="名称" prop="detailTitle">
            <template slot-scope="scope">
              <el-input v-model="scope.row.detailTitle" placeholder="请输入名称"/>
            </template>
          </el-table-column>
          <el-table-column label="金额" prop="price">
            <template slot-scope="scope">
              <el-input v-model="scope.row.price" placeholder="请输入金额"/>
            </template>
          </el-table-column>
          <el-table-column label="大写金额(零壹贰叁肆伍陆柒捌玖万仟佰拾亿元角分)" prop="bigPrice">
            <template slot-scope="scope">
              <el-input v-model="scope.row.bigPrice" placeholder="请输入大写金额"/>
            </template>
          </el-table-column>

        </el-table>


      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script>
import {
  listFinance,
  getFinance,
  delFinance,
  addFinance,
  updateFinance,
  findFinance,
  findFinanceList
} from "@/api/oa/finance";
import Vue from "vue";

export default {
  name: "Finance",
  dicts: ['sys_pay_type'],
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
      totalEnter: 0,
      totalOut: 0,
      // 进出账管理表格数据
      financeListEnter: [],
      financeListOut: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      openLook: false,
      //详情lable背景
      lableBg: "background: #f0f9eb; width:150px; text-align: center;",
      //弹出层出入账类型
      type: '',
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        projectId: undefined,
        financeTitle: undefined,
        financeType: undefined,
      },
      // 表单参数
      form: {},
      detailList: [],
      // 子表选中数据
      checkedDetailList: [],
      //搜索日期范围
      searchTime: '',
      //明细总价
      priceSum: 0,
      //选项卡标签
      activeName: 'first',

      like: true,
      value1: [],
      value2: 1314,
      titleLink: "增长人数",
      checkByTime: {},
      monthCheck: {},
      // monthOutByTime: 0,
      // monthComeByTime: 0,
      yearCheck: {},
      // yearComeByTime: 0,

      currentList: [{
        date: '2016-05-02',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }, {
        date: '2016-05-04',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1517 弄'
      }, {
        date: '2016-05-01',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1519 弄'
      }, {
        date: '2016-05-03',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1516 弄'
      }],
      // 表单校验
      rules: {
        financeTitle: [
          {required: true, message: "账务名称不能为空", trigger: "blur"}
        ],
        financeParties: [
          {required: true, message: "经手人/付款方不能为空", trigger: "blur"}
        ],
        payType: [
          {required: true, message: "支付类型不能为空", trigger: "change"}
        ],
        financeType: [
          {required: true, message: "进出账类型不能为空", trigger: "blur"}
        ],
        financeTime: [
          {required: true, message: "交易时间不能为空", trigger: "blur"}
        ],
      }
    };
  },
  created() {
    /*this.getListOut();*/
    // this.getListEnter();
    this.getListFinance('1');

  },
  onShow() {

  },
  methods: {
    getListFinance(type) {
      this.loading = true;
      /*let data = {
        financeType: type,
        projectId: 0, //项目id为0，排除项目
      }*/
      this.queryParams = {
        financeType: type,
        projectId: 0, //项目id为0，排除项目
        financeTitle: this.queryParams.financeTitle,
        financeParties: this.queryParams.financeParties

      }
      //使用实体里的参数属性,日期转为字符串格式
      this.queryParams.params = {};
      if (null != this.searchTime && '' != this.searchTime) {
        this.queryParams.params["beginCreateTime"] = this.getRealDate(this.searchTime[0]);
        this.queryParams.params["endCreateTime"] = this.getRealDate(this.searchTime[1]);
      }

      listFinance(this.queryParams).then(response => {
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

    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    //tabs选项卡
    handleClick(tab, event) {
      if (tab.index == '0') {
        this.getListFinance('1');
      }
      if (tab.index == '1') {
        this.getListFinance('0');
      }
      if (tab.index == '2') {
        this.currentBlur()
        this.currentBlurList()
      }
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
        makeRatio: undefined,
        makeTime: undefined,
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
    /** 搜索按钮操作 */
    handleQuery(type) {
      this.queryParams.pageNum = 1;
      this.getListFinance(type);
      // this.getList();
    },
    /** 重置按钮操作 */
    resetQuery(type) {
      this.resetForm("queryForm");
      this.queryParams.financeTitle = undefined;
      this.queryParams.financeParties = undefined;
      this.searchTime = '';
      this.handleQuery(type);
    },
    // 多选框选中数据
    /*handleSelectionChange(selection) {
      this.ids = selection.map(item => item.financeId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },*/
    /** 新增入账按钮操作 */
    handleAddEnter() {
      this.reset();
      this.open = true;
      this.type = 1;
      this.form.financeType = '1'
      this.title = "新增入账信息";
      this.handleAddDetailList();
    },
    /** 新增出账按钮操作 */
    handleAddOut() {
      this.reset();
      this.open = true;
      this.type = 0;
      this.form.financeType = '0'
      this.title = "新增出账信息";
      this.handleAddDetailList();
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.type = row.financeType
      this.loading = true;
      this.reset();
      const financeId = row.financeId || this.ids
      getFinance(financeId).then(response => {
        this.loading = false;
        this.form = response.data;
        this.detailList = response.data.detailList;
        this.detailPriceSum(response.data.detailList);
        this.open = true;
        this.title = "修改进出账管理";
      });
    },
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
        this.detailPriceSum(response.data.detailList);
        this.openLook = true;

      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        //删除project关联表属性
        delete this.form.project;
        if (valid) {
          let arr = this.detailList;
          let boolValue = []
          arr.forEach((vo, i) => {
            let ii = i + 1
            let price = this.isDecimal(vo.price, 2)
            if (vo.detailTitle == null || vo.detailTitle == undefined || vo.detailTitle == "") {
              this.$modal.msgWarning("第" + ii + "条明细名称不能为空！");
            } else if (vo.price == null || vo.price == undefined || vo.price == "") {
              this.$modal.msgWarning("第" + ii + "条明细金额不能为空,且必须是两位小数点数字！");
            } else if (!price) {
              this.$modal.msgWarning("第" + ii + "条明细金额必须是两位小数点数字！");
            } else if (vo.bigPrice == null || vo.bigPrice == undefined || vo.bigPrice == "") {
              this.$modal.msgWarning("第" + ii + "条明细大写金额不能为空！");
            } else if(this.smalltoBIG(vo.price) != vo.bigPrice){
              this.$modal.msgWarning("第" + ii + "条明细大小写金额不一致！");
            } else {
              boolValue.push(1)
            }
          })
          if(arr.length == boolValue.length) {
            this.form.detailList = arr;
            if (this.form.financeId != null) {
              updateFinance(this.form).then(response => {
                this.$modal.msgSuccess(response.msg);
                this.open = false;
                this.getListFinance(this.form.financeType);
              }).finally(() => {
                this.buttonLoading = false;
              });
            } else {
              addFinance(this.form).then(response => {
                this.$modal.msgSuccess(response.msg);
                this.open = false;
                //定位到入账列表
                this.getListFinance('0');
                this.getListFinance('1');
              }).finally(() => {
                this.buttonLoading = false;
              });
            }

          }


          // this.form.detailList = this.detailList;

 /*         if (this.detailList.length == 0) {
            this.$modal.msgWarning("费用名称金额不能为空！");
          } else {
            // this.form.makeTime = this.getRealDate(this.form.makeTime)
            if (this.form.financeId != null) {
              updateFinance(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                // this.getListEnter();
                // this.getListOut();
                this.getListFinance('0');
                this.getListFinance('1');
              }).finally(() => {
                this.buttonLoading = false;
              });
            } else {
              addFinance(this.form).then(response => {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                //定位到入账列表
                /!*this.getListEnter();
                this.getListOut();*!/
                this.getListFinance('0');
                this.getListFinance('1');
              }).finally(() => {
                this.buttonLoading = false;
              });
            }
          }*/
        }
      });
    },

    isDecimal(n, len) {
      return new RegExp("^\\d+(\\.\\d{1," + len + "})?$").test(n);
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      // const financeIds = row.financeId || this.ids;
      const financeId = row.financeId;
      this.$modal.confirm('是否确认删除编号为"' + financeId + '"的数据项？').then(() => {
        this.loading = true;
        return delFinance(financeId);
      }).then(() => {
        if (row.financeType == '1') {
          // this.getListEnter()
          this.getListFinance('1');

        }
        if (row.financeType == '0') {
          // this.getListOut()
          this.getListFinance('0');
        }
        // this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      }).finally(() => {
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
            financeId: vo.financeId,
            financeTitle: vo.financeTitle,
            financeParties: vo.financeParties,
            financeType: vo.financeType,
            payType: vo.payType,
            financeTime: vo.financeTime,
            createBy: vo.createBy,
            makeTime: vo.makeTime,
            makePrice: vo.makePrice,
            createTime: vo.createTime,
            detailList: vo.detailList,
            priceSum: (vo.financeType == '0' ? ' - ' : ' + ') + '¥' + parseFloat(priceSum.toFixed(10)) + '元'
          })
        })
        resolve(financeArr);
      });
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
     * 核算table列表
     * @param row
     * @param rowIndex
     * @returns {string}
     */
    tableRowClassName({row, rowIndex}) {
      if (rowIndex === 1) {
        return 'warning-row';
      } else if (rowIndex === 3) {
        return 'success-row';
      }
      return 'brand-row';
    },

    /**日期转字符串**/
    getRealDate(startDate) {
      // 时间转换
      var datejson = new Date(startDate).toJSON();
      var date = new Date(+new Date(datejson)
        + 8 * 3600 * 1000).toISOString().replace(/T/g, ' ').replace(/\.[\d]{3}Z/, '')
      return date;
    },

    /**
     * 按时间检索核算情况
     * @param e
     */
    getTimeBlur(e) {
      let beginTime = e.displayValue[0] + ' 00:00:00'
      let endTime = e.displayValue[1] + ' 23:59:59'
      let dataCome = {
        beginTime: beginTime,
        endTime: endTime
      }
      findFinance(dataCome).then(res => {
        this.checkByTime = res.data;
      })
    },

    /**
     * 本年本月核算情况
     */
    currentBlur() {
      let now = new Date()
      //本月开始和结束时间
      var nowMonth = new Date(now.getFullYear(), now.getMonth(), 1); // 获取本月第一天的日期时间（时间为0:0:0）
      var monthEnd = new Date(now.getFullYear(), now.getMonth() + 1, 0, 23, 59, 59); // 获取本月最后一天的日期时间（时间为23:59:59）
      //本月收入参数
      let dataCome = {
        beginTime: Vue.prototype.parseTime(nowMonth, '{y}-{m}-{d} {h}:{i}:{s}'),
        endTime: Vue.prototype.parseTime(monthEnd, '{y}-{m}-{d} {h}:{i}:{s}'),
      }
      //本月核算
      findFinance(dataCome).then(res => {
        // this.monthComeByTime = res.data.rwo;
        this.monthCheck = res.data;
      })


      //本年开始和结束时间
      const startDate = new Date(now.getFullYear(), 0);
      startDate.setHours(0, 0, 0, 0);
      // 设定结束时间为当年12月31日23:59:59
      const endDate = new Date(now.getFullYear() + 1, 0); // 注意这里的参数索引从0开始表示1月
      endDate.setSeconds(-1);

      //本年参数
      let yearDataCome = {
        beginTime: Vue.prototype.parseTime(startDate, '{y}-{m}-{d} {h}:{i}:{s}'),
        endTime: Vue.prototype.parseTime(endDate, '{y}-{m}-{d} {h}:{i}:{s}'),
      }
      //本年核算
      findFinance(yearDataCome).then(res => {
        this.yearCheck = res.data;
      })


    },
    /**
     * 最近六个月核算情况，如果显示更多月份，请修改控制器：int[] integers = {0,1, 2, 3, 4, 5};
     */
    currentBlurList() {
      findFinanceList().then(res => {
        this.currentList = res.data;
      })
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
    /** 导出按钮操作 */
    handleExport() {
      this.download('oa/finance/export', {
        ...this.queryParams
      }, `finance_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

<style>
.hidden-input {
  display: none; /* 或 visibility: hidden; */
}

.demo-shadow {
  height: 140px;
  width: 100%;
  padding-top: 10px;
  text-align: center;

  line-height: 28px;
  background-color: #ecf8ff;
  /*box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)*/
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

.tip {
  padding: 0 10px;
  background-color: #ecf8ff;
  border-radius: 4px;
  border-left: 5px solid #50bfff;
  border-right: 5px solid #50bfff;
  text-align: center;
  margin-bottom: 5px;
  margin-top: 18px;
  font-size: 20px;
  height: 50px;
  line-height: 46px;
}

.tip-top {
  padding: 0 10px;
  background-color: #ecf8ff;
  border-radius: 4px;
  border-top: 5px solid #50bfff;
  text-align: center;
  margin-bottom: 5px;
  font-size: 18px;
  height: 88px;
  line-height: 72px;
}

.tip-top .sp1 {
  margin-left: 100px;
  font-size: 18px;
  color: #999999;
}

.tip-top .sp2 {
  margin-right: 100px;
  font-size: 32px;
}

.date-col {
  margin-top: 20px;
}

.el-table .warning-row {
  background: oldlace;
}

.el-table .success-row {
  background: #f0f9eb;
}

.el-table .brand-row {
  background: #ecf8ff;
}
</style>
