<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="120px">
      <el-form-item label="投标项目名称" prop="bidTitle">
        <el-input
          v-model="queryParams.bidTitle"
          placeholder="请输入投标项目名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="投标项目编号" prop="bidNumber">
        <el-input
          v-model="queryParams.bidNumber"
          placeholder="请输入投标项目编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="投标结果" prop="isWin">
        <el-select v-model="queryParams.isWin" placeholder="请选择采购方式">
          <el-option
            v-for="dict in dict.type.sys_is_win"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          ></el-option>
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
          v-hasPermi="['oa:oaBid:add']"
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
          v-hasPermi="['oa:oaBid:edit']"
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
          v-hasPermi="['oa:oaBid:remove']"
        >删除
        </el-button>
      </el-col>
<!--      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['oa:oaBid:export']"
        >导出
        </el-button>
      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="oaBidList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
<!--      <el-table-column label="文章标识" align="left" prop="bidId" v-if="true"/>-->
      <el-table-column label="投标项目名称" align="left" prop="bidTitle"/>
      <el-table-column label="投标项目编号" align="left" prop="bidNumber"/>
<!--      <el-table-column label="采购人" align="left" prop="clientName"/>-->
<!--      <el-table-column label="代理机构" align="left" prop="agency"/>-->
      <el-table-column label="采购方式" align="center" width="80"  prop="bidType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_bid_type" :value="scope.row.bidType"/>
        </template>
      </el-table-column>
      <el-table-column label="预算金额" align="center" width="120" prop="budget"/>
      <!--      <el-table-column label="项目分包情况" align="center" prop="package" />-->


      <el-table-column label="公告发布时间" align="center" width="130" prop="noticeTime" />


<!--      <el-table-column label="获取文件期限" align="center" prop="deadline"/>-->
      <el-table-column label="报名方式" align="center" width="150" prop="signType"/>
      <!-- <el-table-column label="获取招标文件方式" align="center" prop="getFile" />
       <el-table-column label="投标截止时间" align="center" prop="expirationTime" width="180">
         <template slot-scope="scope">
           <span>{{ parseTime(scope.row.expirationTime, '{y}-{m}-{d}') }}</span>
         </template>
       </el-table-column>
     <el-table-column label="开标地点" align="center" prop="bidAddress" />

       <el-table-column label="是否缴纳保证金" align="center" prop="document" width="180">
         <template slot-scope="scope">
           <span>{{ scope.row.document == null ? "否" : "是" }}</span>
         </template>
       </el-table-column>

      <el-table-column label="缴纳保证金情况" align="center" prop="document" />
      -->
      <el-table-column label="投标结果" align="center" width="100" prop="isWin">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_is_win" :value="scope.row.isWin"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="120">
         <template slot-scope="scope">
           <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
         </template>
       </el-table-column>
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetails(scope.row)"
            v-hasPermi="['oa:oaBid:edit']"
          >查看
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-female"
            @click="handleDocument(scope.row)"
            v-hasPermi="['oa:oaBid:edit']"
          >保证金
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['oa:oaBid:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['oa:oaBid:remove']"
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

    <el-dialog :title="title" :visible.sync="openDocument" width="40%" append-to-body>

      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="缴纳信息" name="first">
          <el-form :model="documents" label-width="100px">
            <el-form-item label="缴纳金额" prop="bidTitle">
              {{ documents.outMoney == '' ? '未填写' : documents.outMoney }}
            </el-form-item>
            <el-form-item label="缴纳时间" prop="bidTitle">
              {{ documents.outTime == '' ? '未填写' : parseTime(documents.outTime, '{y}-{m}-{d}') }}
            </el-form-item>
          </el-form>
          <div>
            <el-link type="warning" @click="editDocument">点击编辑</el-link>
          </div>
          <!--编辑-->
          <el-form :model="documents" label-width="100px" v-show="editStart">
            <el-form-item label="缴纳金额" prop="bidTitle">
              <el-input v-model="documents.outMoney" placeholder="请输入缴纳金额"/>
            </el-form-item>
            <el-form-item label="缴纳时间" prop="bidTitle">
              <div class="block">
                <el-date-picker
                  v-model="documents.outTime"
                  type="date"
                  @change="changeDateOut"
                  placeholder="选择日期">
                </el-date-picker>
              </div>
            </el-form-item>

            <el-form-item label="退回金额" prop="bidTitle" hidden="true">
              <el-input v-model="documents.comeMoney" placeholder="请输入缴纳金额"/>
            </el-form-item>
            <el-form-item label="退回时间" prop="bidTitle" hidden="true">
              <div class="block">
                <el-date-picker
                  v-model="documents.comeTime"
                  type="date"
                  @change="changeDateCome"
                  placeholder="选择日期">
                </el-date-picker>
              </div>
            </el-form-item>
            <div style="margin-left: 100px">
              <el-button type="primary" @click="submitDocumentForm('1')">保存</el-button>
              <el-button @click="closeDocumentForm">关闭</el-button>
            </div>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="退回信息" name="second">
          <div v-if="documentShow == 0">
            保证金未缴纳或者免缴，无退回信息！
          </div>
          <div v-else>
            <el-form :model="documents" label-width="100px">
              <el-form-item label="退回金额" prop="bidTitle">
                {{ documents.comeMoney == '' ? '未填写' : documents.comeMoney }}
              </el-form-item>
              <el-form-item label="退回时间" prop="bidTitle">
                {{ documents.comeTime == '' ? '未填写' : parseTime(documents.comeTime , '{y}-{m}-{d}') }}
              </el-form-item>
            </el-form>
            <div>
              <el-link type="warning" @click="editDocument">点击编辑</el-link>
            </div>
            <!--编辑-->
            <el-form :model="documents" label-width="100px" v-show="editStart">
              <el-form-item label="缴纳金额" prop="bidTitle" hidden="true">
                <el-input v-model="documents.outMoney" placeholder="请输入缴纳金额"/>
              </el-form-item>
              <el-form-item label="缴纳时间" prop="bidTitle" hidden="true">
                <div class="block">
                  <el-date-picker
                    v-model="documents.outTime"
                    type="date"
                    fotmat = "YYYY-MM-dd"
                    value-format="YYYY-MM-dd"
                    @change="changeDateOut"
                    placeholder="选择日期">
                  </el-date-picker>
                </div>
              </el-form-item>

              <el-form-item label="退回金额" prop="bidTitle">
                <el-input v-model="documents.comeMoney" placeholder="请输入缴纳金额"/>
              </el-form-item>
              <el-form-item label="退回时间" prop="bidTitle">
                <div class="block">
                  <el-date-picker
                    v-model="documents.comeTime"
                    type="date"
                    @change="changeDateCome"
                    placeholder="选择日期">
                  </el-date-picker>
                </div>
              </el-form-item>
              <div style="margin-left: 100px">
                <el-button type="primary" @click="submitDocumentForm('2')">保存</el-button>
                <el-button @click="closeDocumentForm">关闭</el-button>
              </div>
            </el-form>
          </div>
        </el-tab-pane>
      </el-tabs>


    </el-dialog>
    <!--详情弹出层-->
    <el-dialog :title="title" :visible.sync="openDetails" width="60%" append-to-body>
      <el-descriptions class="margin-top" :column="4" border>
        <el-descriptions-item span="4" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-user"></i>
            项目名称
          </template>
          {{ form.bidTitle }}
        </el-descriptions-item>
        <el-descriptions-item span="2" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-mobile-phone"></i>
            项目编号
          </template>
          {{ form.bidNumber }}
        </el-descriptions-item>
        <el-descriptions-item span="2" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-location-outline"></i>
            预算金额
          </template>
          {{ form.budget }}
        </el-descriptions-item>
        <el-descriptions-item span="2" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-tickets"></i>
            报名方式
          </template>
          <dict-tag :options="dict.type.sys_sign_type" :value="form.signType"/>
        </el-descriptions-item>
        <el-descriptions-item span="2" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-office-building"></i>
            采购方式
          </template>
          <dict-tag :options="dict.type.sys_bid_type" :value="form.bidType"/>
        </el-descriptions-item>
        <el-descriptions-item span="2" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-office-building"></i>
            发布时间
          </template>
          {{form.noticeTime}}
        </el-descriptions-item>
        <el-descriptions-item span="2" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-office-building"></i>
            投标截止时间
          </template>
          {{form.expirationTime}}
        </el-descriptions-item>
        <el-descriptions-item span="4" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-office-building"></i>
            分包情况
          </template>
          {{ form.packageInfo }}
        </el-descriptions-item>
        <el-descriptions-item span="4" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-office-building"></i>
            获取文件期限
          </template>
          {{ form.deadline }}
        </el-descriptions-item>
        <el-descriptions-item span="4" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-office-building"></i>
            获取文件方式
          </template>
          {{ form.patternFile }}
        </el-descriptions-item>
        <el-descriptions-item span="4" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-office-building"></i>
            采购人信息
          </template>
          {{ form.clientName }}
        </el-descriptions-item>
        <el-descriptions-item span="4" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-office-building"></i>
            开标地点
          </template>
          {{ form.bidAddress }}
        </el-descriptions-item>
        <el-descriptions-item span="4" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-office-building"></i>
            代理机构
          </template>
          {{ form.agency }}
        </el-descriptions-item>
        <el-descriptions-item span="4" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-office-building"></i>
            保证金缴纳情况
          </template>
          {{ form.document }}
        </el-descriptions-item>
        <el-descriptions-item span="4" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-office-building"></i>
            投标结果
          </template>
          <dict-tag :options="dict.type.sys_is_win" :value="form.isWin"/>
        </el-descriptions-item>
        <el-descriptions-item span="4" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-office-building"></i>
            备注
          </template>
          {{ form.remark }}
        </el-descriptions-item>
        <el-descriptions-item span="4" :labelStyle="lableBg">

          <template slot="label">
            <i class="el-icon-office-building"></i>
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
    </el-dialog>

    <!-- 添加或修改投标管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="70%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="16">
            <el-form-item label="项目名称" prop="bidTitle">
              <el-input v-model="form.bidTitle" placeholder="请输入投标项目名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="项目编号" prop="bidNumber">
              <el-input v-model="form.bidNumber" placeholder="请输入项目编号"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="预算金额" prop="budget">
              <el-input v-model="form.budget" placeholder="请输入采购预算金额"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="报名方式" prop="budget">
              <el-input v-model="form.signType" placeholder="请选择报名方式"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="采购方式" prop="bidType">
              <el-select v-model="form.bidType" placeholder="请选择采购方式">
                <el-option
                  v-for="dict in dict.type.sys_bid_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="发布时间" prop="budget">
              <el-input v-model="form.noticeTime" placeholder="请填写发布时间"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="投标截止时间" prop="expirationTime">
              <el-input v-model="form.expirationTime" placeholder="请填写投标截止时间"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="分包情况" prop="package">
              <el-input v-model="form.packageInfo" placeholder="请输入项目分包情况"/>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="获取文件期限" prop="deadline">
              <el-input v-model="form.deadline" placeholder="请输入获取文件期限"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="获取文件方式" prop="getFile">
              <el-input v-model="form.patternFile" placeholder="请输入获取招标文件方式"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="采购人信息" prop="clientName">
              <el-input v-model="form.clientName" placeholder="请输入采购人联系人"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="开标地点" prop="bidAddress">
              <el-input v-model="form.bidAddress" placeholder="请输入开标地点"/>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="代理机构" prop="agency">
              <el-input v-model="form.agency" placeholder="请输入采购代理机构"/>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="保证金缴纳情况" prop="document">
              <el-input v-model="form.document" placeholder="请输入保证金缴纳情况"/>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="投标结果" prop="isWin">
              <el-radio-group v-model="form.isWin">
                <el-radio
                  v-for="dict in dict.type.sys_is_win"
                  :key="dict.value"
                  :label="dict.value"
                >{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" placeholder="请输入备注"/>
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
import {listOaBid, getOaBid, delOaBid, addOaBid, updateOaBid} from "@/api/oa/oaBid";
import {listByIds} from "@/api/system/oss";

export default {
  name: "OaBid",
  dicts: ['sys_sign_type', 'sys_bid_type', 'sys_is_win'],
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
      // 投标管理表格数据
      oaBidList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      //详情弹出层
      openDetails: false,
      //保证金弹出层
      openDocument: false,
      //保证金tabs标签
      activeName: 'first',
      editStart: false,
      // fileList: [],
      //日期范围搜索
      searchTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        bidTitle: undefined,
        bidNumber: undefined,
        noticeTime: undefined,
        createTime: undefined,
      },
      // 表单参数
      form: {},
      //保证金缴纳情况
      documents: '',
      documentShow: '',
      //详情lable背景
      lableBg: "background:rgb(253 246 236); width:150px; text-align: center;",
      // 表单校验
      rules: {
        bidId: [
          {required: true, message: "文章id不能为空", trigger: "blur"}
        ],
        bidTitle: [
          {required: true, message: "投标项目名称不能为空", trigger: "blur"}
        ],
        bidNumber: [
          {required: true, message: "采购项目编号不能为空", trigger: "blur"}
        ],
        bidType: [
          {required: true, message: "采购方式不能为空", trigger: "change"}
        ],
        budget: [
          {required: true, message: "采购预算金额不能为空", trigger: "blur"}
        ],
      }

    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询投标管理列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.searchTime && '' != this.searchTime) {
        this.queryParams.params["beginCreateTime"] = this.getRealDateBet(this.searchTime[0]);
        this.queryParams.params["endCreateTime"] = this.getRealDateBet(this.searchTime[1]);
      }
      listOaBid(this.queryParams).then(response => {
        this.oaBidList = response.rows;
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
        bidId: undefined,
        bidTitle: undefined,
        bidNumber: undefined,
        bidType: undefined,
        budget: undefined,
        package: undefined,
        noticeTime: undefined,
        deadline: undefined,
        signType: undefined,
        getFile: undefined,
        expirationTime: undefined,
        bidAddress: undefined,
        clientName: undefined,
        agency: undefined,
        document: undefined,
        remark: undefined,
        accessory: undefined,
        createBy: undefined,
        createTime: undefined,
        updateBy: undefined,
        updateTime: undefined
      };
      this.resetForm("form");
      // this.fileList = [];
      this.documents = {
        outMoney: undefined,
        outTime: undefined,
        comeMoney: undefined,
        comeTime: undefined
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
      this.queryParams.bidTitle = undefined;
      this.queryParams.bidNumber = undefined;
      this.queryParams.isWin = undefined;
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.bidId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加投标管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const bidId = row.bidId || this.ids
      getOaBid(bidId).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改投标管理";
      });
    },
    /**详情按钮操作**/
    handleDetails(row) {
      this.loading = true;
      this.reset();
      const bidId = row.bidId || this.ids
      getOaBid(bidId).then(response => {
        this.loading = false;
        this.form = response.data;
        // this.getFile(response.data.accessory);
        this.openDetails = true;
        this.title = "投标信息详情";
      });
    },
    //openDocument
    /**保证金按钮操作**/
    handleDocument(row) {
      this.loading = true;
      this.reset();
      const bidId = row.bidId || this.ids
      getOaBid(bidId).then(response => {
        this.loading = false;
        this.form = response.data;
        this.activeName = 'first';
        let res = JSON.parse(response.data.documentInfo);
        if (res == null) {
          this.documentShow = 0;
          this.documents = {
            outMoney: undefined,
            outTime: undefined,
            comeMoney: undefined,
            comeTime: undefined
          }
        } else {
          this.documentShow = 1;
          this.documents = {
            outMoney: res.outMoney,
            outTime: res.outTime,
            comeMoney: res.comeMoney,
            comeTime: res.comeTime
          }
        }
        this.openDocument = true;
        this.title = "保证金管理";
      });
    },

    /**获取附件**/
   /* getFile(val) {
      if (val) {
        listByIds(val).then(res => {
          this.fileList = res.data;
        })
      } else {
        this.fileList = [];
      }
    },*/


    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.bidId != null) {
            updateOaBid(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addOaBid(this.form).then(response => {
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

    /**缴纳保证金**/
    submitDocumentForm(type) {
      let document = {
        outTime: this.getRealDateBet(this.documents.outTime) == 'NaN-NaN-NaN' ? '' : this.getRealDateBet(this.documents.outTime),
        outMoney: this.documents.outMoney,
        comeTime: this.getRealDateBet(this.documents.comeTime) == 'NaN-NaN-NaN' ? '' : this.getRealDateBet(this.documents.comeTime),
        comeMoney: this.documents.comeMoney
      }
      if (type == '1') {
        if (document.outMoney != '' && document.outTime != '') {
          // if (isFinite(this.documents.outMoney)) {
            this.form.documentInfo = JSON.stringify(document)
            updateOaBid(this.form).then(response => {
              this.$modal.msgSuccess("保存成功");
              this.handleDocument(this.form)
              this.editStart = false
            }).finally(() => {
              this.buttonLoading = false;
            });
          /*} else {
            this.$modal.msgError("您输入信息有误，请重新输入！")
          }*/
        } else {
          this.$modal.msgError("内容不可为空！")
        }
      }

      if (type == '2') {
        if (document.comeMoney != '' && document.comeTime != '') {
          // if (isFinite(this.documents.comeMoney)) {
            this.form.documentInfo = JSON.stringify(document)
            updateOaBid(this.form).then(response => {
              this.$modal.msgSuccess("保存成功");
              this.handleDocument(this.form)
              this.editStart = false
            }).finally(() => {
              this.buttonLoading = false;
            });
          /*} else {
            this.$modal.msgError("您输入信息有误，请重新输入！")
          }*/
        } else {
          this.$modal.msgError("内容不可为空！")
        }
      }


    },
    /**编辑保证金**/
    editDocument() {
      this.editStart = true
    },
    /**关闭保证金表单**/
    closeDocumentForm() {
      this.editStart = false
      this.handleDocument(this.form)
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const bidIds = row.bidId || this.ids;
      this.$modal.confirm('是否确认删除投标管理编号为"' + bidIds + '"的数据项？').then(() => {
        this.loading = true;
        return delOaBid(bidIds);
      }).then(() => {
        this.loading = false;
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      }).finally(() => {
        this.loading = false;
      });
    },
    //tabs标签
    handleClick(tab, event) {
      // console.log(tab, event);
      this.editStart = false;
    },
    changeDateOut(e) {
      this.documents.outTime = this.getRealDateBet(this.documents.outTime);
    },
    changeDateCome(e) {
      this.documents.comeTime = this.getRealDateBet(this.documents.comeTime);
    },
    /**日期转字符串**/
   /* getRealDateBet(startDate) {
      let newDate = new Date(startDate).getTime() - 1000 * 60 * 60 * 24
      let date = new Date(newDate);
      let y = date.getFullYear()
      let m = (date.getMonth() + 1 + "").padStart(2, "0")
      let d = (date.getDate() + 1 + "").padStart(2, "0")
      var h = (date.getHours() + "").padStart(2, "0");
      var mm = (date.getMinutes() + "").padStart(2, "0");
      var s = (date.getSeconds() + "").padStart(2, "0");
      return [y, m, d].join("-")
      // return [y,m,d].join("-")+ " " + ([h,mm,s].join(":"))
    },*/

    /**日期转字符串**/
    getRealDateBet(startDate) {
      // 时间转换
      var datejson = new Date(startDate).toJSON();
      var date = new Date( + new Date(datejson)
        + 8*3600*1000).toISOString().
      replace(/T/g,' ').
      replace(/\.[\d]{3}Z/,'')
      return date;
    },

    /** 导出按钮操作 */
    handleExport() {
      this.download('oa/oaBid/export', {
        ...this.queryParams
      }, `oaBid_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

<style>
.custom-bg {
  background-color: #ff9900;
}

.el-descriptions-item__label.is-bordered-label.labelColorOne {
  color: #141415;
  font-size: 16px;
  background: #8c8c94;
}

.my-label {
  background: #ff9900;
}

.my-content {
  background: #FDE2E2;
}
</style>
