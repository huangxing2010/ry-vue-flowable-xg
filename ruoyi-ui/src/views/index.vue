<template>
  <div class="app-container home">



    <el-row :gutter="20">
      <el-col :span="16">
        <el-row :gutter="20">
          <el-col :span="6">
            <div @click="goTarget('work/create')" class="work">
              <el-badge class="item-ico" type="warning">
                <i class="el-icon-s-operation fz cl1"></i>
                <div size="small">流程发起</div>
              </el-badge>
            </div>
          </el-col>
          <el-col :span="6">
            <div @click="goTarget('work/own')" class="work">
              <el-badge :value="ownCount" class="item-ico" type="warning">
                <i class="el-icon-date fz cl2"></i>
                <div size="small">我的流程</div>
              </el-badge>
            </div>
          </el-col>
          <el-col :span="6">
            <div @click="goTarget('work/todo')" class="work">
              <el-badge :value="todoListCount" class="item-ico" type="warning">
                <i class="el-icon-chat-line-round fz cl3"></i>
                <div size="small">待办任务</div>
              </el-badge>
            </div>
          </el-col>
          <el-col :span="6">
            <div @click="goTarget('work/finished')" class="work">
              <el-badge :value="finishedCount" class="item-ico" type="warning" v-on:click="goTarget('work/finished')">
                <i class="el-icon-set-up fz cl4"></i>
                <div size="small">已办任务</div>
              </el-badge>
            </div>
          </el-col>
        </el-row>

<!--        <div class="card-info" style="margin-top: 30px">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span><i class="el-icon-monitor"></i> 系统基本信息</span>
              <el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>
            </div>

            <div class="el-table el-table&#45;&#45;enable-row-hover el-table&#45;&#45;medium">
              <table cellspacing="0" style="width: 100%">
                <tbody>
                <tr>
                  <td class="el-table__cell is-leaf"><div class="cell">Redis版本</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ cache.info.redis_version }}</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell">运行模式</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ cache.info.redis_mode == "standalone" ? "单机" : "集群" }}</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell">端口</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ cache.info.tcp_port }}</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell">客户端数</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ cache.info.connected_clients }}</div></td>
                </tr>
                <tr>
                  <td class="el-table__cell is-leaf"><div class="cell">运行时间(天)</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ cache.info.uptime_in_days }}</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell">使用内存</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ cache.info.used_memory_human }}</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell">使用CPU</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ parseFloat(cache.info.used_cpu_user_children).toFixed(2) }}</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell">内存配置</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ cache.info.maxmemory_human }}</div></td>
                </tr>
                <tr>
                  <td class="el-table__cell is-leaf"><div class="cell">AOF是否开启</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ cache.info.aof_enabled == "0" ? "否" : "是" }}</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell">RDB是否成功</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ cache.info.rdb_last_bgsave_status }}</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell">Key数量</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.dbSize">{{ cache.dbSize }} </div></td>
                  <td class="el-table__cell is-leaf"><div class="cell">网络入口/出口</div></td>
                  <td class="el-table__cell is-leaf"><div class="cell" v-if="cache.info">{{ cache.info.instantaneous_input_kbps }}kps/{{cache.info.instantaneous_output_kbps}}kps</div></td>
                </tr>
                </tbody>
              </table>
            </div>



          </el-card>

        </div>-->

      </el-col>
      <el-col :span="8">



        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span><i class="el-icon-microphone"></i> 通知公告</span>
<!--            <el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>-->
          </div>
          <div v-for="(v, k) in noticeList" :key="k" class="text item" @click="toDrawer(v.noticeId)" type="primary" style="margin-left: 3px;margin-bottom: 5px;">
            <span class="pull-right">{{ parseTime(v.createTime, '{y}-{m}-{d}') }}</span> <i class="el-icon-arrow-right"></i> {{v.noticeTitle}}
          </div>
        </el-card>

      </el-col>
      <el-col :span="16" >

      </el-col>
      <el-col :span="8"><div class="grid-content bg-purple"></div></el-col>
    </el-row>
    <h2>新的一天从这里开始，助力工作,让办公更轻松!!!</h2>

    <el-drawer
      :title="noticeTitle"
      :visible.sync="drawer"
      :with-header="true">
      <div class="drawer-con">
        <div v-html="noticeContent"></div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { getCache } from "@/api/monitor/cache";
import {getFinishedCount, getOwnCount, getTodoListCount} from "@/api/oa/homeCount";
import {getNotice, listNoticeLimit} from "@/api/system/notice";
export default {
  name: "Index",
  data() {
    return {
      // 版本号
      version: "0.8.3",
      // 统计命令信息
      commandstats: null,
      // 使用内存
      usedmemory: null,
      // cache信息
      cache: [],
      finishedCount: 0,
      todoListCount: 0,
      ownCount: 0,
      noticeList:[],
      noticeTitle: '',
      noticeContent : '',
      //抽屉
      drawer: false,
    };
  },
  created() {
    this.getList();
    this.getFinishedCount()
    this.getTodoListCount()
    this.getOwnCount()
    this.getListNotice()
  },
  methods: {
    /**我待流程统计**/
    getOwnCount(){
      getOwnCount().then(res => {

        this.ownCount = res.data
      })
    },
    /**待办任务统计**/
    getTodoListCount(){
      getTodoListCount().then(res => {
        // console.log(3333,res.data)
        this.todoListCount = res.data
      })
    },
    /**已办任务统计**/
    getFinishedCount(){
      getFinishedCount().then(res => {
        this.finishedCount = res.data
      })
    },

    /** 查缓存询信息 */
    getList() {
      getCache().then((response) => {
        this.cache = response.data;
        this.$modal.closeLoading();

      });
    },
    /** 查询公告列表 */
    getListNotice() {
      this.loading = true;
      listNoticeLimit().then(response => {
        this.noticeList = response;
        this.loading = false;
      });
    },
    toDrawer(nid){
      this.drawer = true
      getNotice(nid).then(res => {
        this.noticeTitle = res.data.noticeTitle;
        this.noticeContent = res.data.noticeContent;
      })
    },
    /** 修改按钮操作 */
   /* handleUpdate(row) {
      this.reset();
      const noticeId = row.noticeId || this.ids
      getNotice(noticeId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改公告";
      });
    },*/
    toNotice(nid){
      console.log(2222,nid)
    },
    //路由跳转
    goTarget(href) {
      this.$router.push({ path: href});
      console.log(999,href)
    },

  },
};
</script>

<style scoped lang="scss">
.home {
  background-color: #f5f5f5;
  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;



  .work {
    background: #ffffff;
    text-align: center;
  }
  .item-ico {
    width: 100%;
    height: 208px;
    margin-top: 10px;
    margin-right: 80px;
    padding: 30px;
    background-color: #ffffff;
    font-size: 18px;
    text-align: center;
    color: #999999;
  }
  .item-ico .fz{font-size: 88px;margin-bottom: 12px;}
  .item-ico .cl1{color: #67C23A}
  .item-ico .cl2{color: #E6A23C}
  .item-ico .cl3{color: #F56C6C}
  .item-ico .cl4{color: #409EFF}
  .box-card .el-card__header {
    padding: 0;
    border-bottom: 10px solid #EBEEF5;
    box-sizing: border-box;
  }
  .clearfix{font-size: 18px;font-weight: 600;}
  .text {
    font-size: 16px;
    border-bottom: #cccccc 1px dashed;
    padding-bottom: 6px;
  }
  .item {
    margin-bottom: 18px;
  }
  .item span{font-size: 12px;color: #cccccc}
  .grid-content {
    border-radius: 4px;
    min-height: 36px;
  }
  .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }

  h2 {
    margin-top: 30px;
    font-size: 18px;
    font-weight: 100;
  }

  .drawer-con{padding:0 20px}

/*  .card-info .item{
    width: 100%;
    height: 24px;
    margin-top: 10px;
    margin-right: 10px;
    padding: 10px;
    background-color: #ffffff;
    font-size: 16px;
    text-align: left;
    color: #999999;
  }*/




}
</style>

