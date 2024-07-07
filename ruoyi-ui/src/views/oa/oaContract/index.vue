<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="合同编号" prop="contractNum">
        <el-input
          v-model="queryParams.contractNum"
          placeholder="请输入合同编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="合同名称" prop="contractName">
        <el-input
          v-model="queryParams.contractName"
          placeholder="请输入合同名称"
          clearable
          @keyup.enter.native="handleQuery"
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
          v-hasPermi="['oa:oaContract:add']"
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
          v-hasPermi="['oa:oaContract:edit']"
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
          v-hasPermi="['oa:oaContract:remove']"
        >删除</el-button>
      </el-col>
<!--      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['oa:oaContract:export']"
        >导出</el-button>
      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="oaContractList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="合同ID" align="center" prop="contractId" v-if="false"/>
<!--      <el-table-column label="合同编号" align="center" prop="contractId" />-->
      <el-table-column label="合同名称" align="left" width="260" prop="contractName" />
      <el-table-column label="甲方名称" align="center" prop="firstName" />
      <el-table-column label="乙方名称" align="center" prop="secondName" />
      <el-table-column label="乙方负责人" align="center" prop="secondPerson" width="100"/>
      <el-table-column label="乙方电话" align="center" prop="secondPhone" width="120"/>
      <el-table-column label="合同金额" align="center" prop="contractPrice" width="120" />
      <el-table-column label="签订日期" align="center" prop="signTime" width="120">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.signTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="contractStatus" width="120">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_show_hide" :value="scope.row.contractStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="创建者" align="center" prop="createBy" width="120"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handlePreview(scope.row)"
            v-hasPermi="['oa:oaContract:edit']"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['oa:oaContract:edit']"
          >修改</el-button>
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

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 查看对话框-->
    <el-dialog :title="title" :visible.sync="openPreview" width="65%" append-to-body>
      <div id="main">
      <el-descriptions class="margin-top" :column="3" border>
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
          {{ form.signTime }}
        </el-descriptions-item>
        <el-descriptions-item span="1" :labelStyle="lableBg">
          <template slot="label">
            <i class="el-icon-date"></i>
            有效期
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
      <div class="print">
         <span class="pull-right " v-print="printObj">
          <i class="el-icon-printer"></i>
        </span>
      </div>
    </el-dialog>

    <!-- 添加或修改合同管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="65%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="16">
            <el-form-item label="合同名称" prop="contractName">
              <el-input v-model="form.contractName" placeholder="请输入合同名称" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="合同编号" prop="contractNum">
              <el-input v-model="form.contractNum" placeholder="请输入合同编号" />
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="乙方名称" prop="secondName">
              <el-input v-model="form.secondName" placeholder="请输入乙方名称" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="乙方负责人" prop="secondPerson">
              <el-input v-model="form.secondPerson" placeholder="请输入乙方负责人" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="乙方电话" prop="secondPhone">
              <el-input v-model="form.secondPhone" placeholder="请输入乙方电话" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="甲方名称" prop="firstName">
              <el-input v-model="form.firstName" placeholder="请输入甲方名称" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="甲方负责人" prop="firstPerson">
              <el-input v-model="form.firstPerson" placeholder="请输入甲方负责人" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="甲方电话" prop="firstPhone">
              <el-input v-model="form.firstPhone" placeholder="请输入甲方电话" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="合同金额" prop="contractPrice">
              <el-input v-model="form.contractPrice" placeholder="请输入合同金额" />
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
              <el-input v-model="form.validity" placeholder="请输入质保期" />
            </el-form-item>
          </el-col>
         <el-col :span="24">
           <el-form-item label="备注" prop="remark">
             <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
           </el-form-item>
         </el-col>
          <!--   <el-col :span="8"><div class="grid-content bg-purple"></div></el-col>
            <el-col :span="8"><div class="grid-content bg-purple"></div></el-col>-->
        </el-row>

<!--        <el-form-item label="状态" prop="contractStatus">
          <el-radio-group v-model="form.contractStatus">
            <el-radio
              v-for="dict in dict.type.sys_show_hide"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>-->
        <el-form-item label="附件" prop="accessory">
          <file-upload v-model="form.accessory"/>
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
import { listOaContract, getOaContract, delOaContract, addOaContract, updateOaContract } from "@/api/oa/oaContract";
import {listByIds} from "@/api/system/oss";

export default {
  name: "OaContract",
  dicts: ['sys_show_hide'],
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
      // 合同管理表格数据
      oaContractList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      openPreview: false,
      //日期范围搜索
      searchTime: [],
      //详情lable背景
      lableBg: "background: #f0f9eb; width:150px; text-align: center;",
      printObj: {
        id: "main",    // 这里是要打印元素的ID
        popTitle: '',  // 打印的标题
        extraCss: '',  // 打印可引入外部的一个 css 文件
        extraHead: ''  // 打印头部文字
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        contractNum: undefined,
        contractName: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        contractName: [
          { required: true, message: "合同名称不能为空", trigger: "blur" }
        ],
        firstPerson: [
          { required: true, message: "甲方负责人不能为空", trigger: "blur" }
        ],
        secondName: [
          { required: true, message: "乙方名称不能为空", trigger: "blur" }
        ],
        secondPerson: [
          { required: true, message: "乙方负责人不能为空", trigger: "blur" }
        ],
        secondPhone: [
          { required: true, message: "乙方电话不能为空", trigger: "blur" }
        ],
        contractPrice: [
          { required: true, message: "项目金额不能为空", trigger: "blur" },
          // { validator: this.validateAmount, trigger: 'blur' }
        ],

      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询合同管理列表 **/
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.searchTime && '' != this.searchTime) {
        this.queryParams.params["beginCreateTime"] = this.getRealDate(this.searchTime[0]);
        this.queryParams.params["endCreateTime"] = this.getRealDate(this.searchTime[1]);
      }
      listOaContract(this.queryParams).then(response => {
        this.oaContractList = response.rows;
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
        contractId: undefined,
        processId: undefined,
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
      this.ids = selection.map(item => item.contractId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加合同管理";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const contractId = row.contractId || this.ids
      getOaContract(contractId).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改合同管理";
      });
    },
    /** 查看按钮操作 */
    handlePreview(row){
      this.loading = true;
      this.reset();
      const contractId = row.contractId
      getOaContract(contractId).then(response => {
        this.loading = false;
        this.form = response.data;
        this.openPreview = true;
        this.title = "修改合同管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.contractId != null) {
            updateOaContract(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addOaContract(this.form).then(response => {
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
    /** 删除按钮操作 */
    handleDelete(row) {
      const contractIds = row.contractId || this.ids;
      this.$modal.confirm('是否确认删除合同管理编号为"' + contractIds + '"的数据项？').then(() => {
        this.loading = true;
        return delOaContract(contractIds);
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
      this.download('oa/oaContract/export', {
        ...this.queryParams
      }, `oaContract_${new Date().getTime()}.xlsx`)
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
};
</script>

<style scoped>
.print{font-size: 18px; color: #1c84c6;border: 0; margin: 24px 0}
</style>
