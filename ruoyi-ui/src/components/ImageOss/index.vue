<template>
  <!-- 附件列表 -->
  <transition-group class="upload-file-list el-upload-list el-upload-list--text" :name="`${realSrc}`"
                    tag="ul">
    <li :key="file.url" class="el-upload-list__item ele-upload-list__item-content" v-for="(file, index) in fileList">
      <el-link :underline="false" >
        <span class="el-icon-document">
          {{ getFileName(file.originalName) }}
          <a :href="`${file.url}`" target="_blank" style="margin: 0 10px">[查看]</a>
          <el-button type="text" @click="handleDownload(file.oosId)" v-hasPermi="['system:oss:download']">[下载]</el-button>
          </span>
      </el-link>
    </li>
  </transition-group>
</template>

<script>
import {download, listByIds} from "@/api/system/oss";
export default {
  name: "ImageOss",
  props: {
    name: {
      type: String,
      default: ""
    },
  },
  data() {
    return {
      fileList: [],
    }
  },
  computed: {
    realSrc() {
      if (!this.name) {
        return;
      }
      let real_src = this.name.split(",");
      listByIds(real_src).then(res => {
        let arr = res.data;
        let fileList = [];
        arr.forEach(item => {
          fileList.push({
            oosId: item.ossId,
            originalName: item.originalName,
            url: item.url
          })
        })
        this.fileList = fileList;
      })
    },
  },
  methods: {
    // 获取文件名称
    getFileName(name) {
      // 如果是url那么取最后的名字 如果不是直接返回
      if (name.lastIndexOf("/") > -1) {
        return name.slice(name.lastIndexOf("/") + 1);
      } else {
        return name;
      }
    },

    /** 下载按钮操作 */
    handleDownload(ossId) {
      this.$download.oss(ossId)
    },

  }
};
</script>

<style lang="scss" scoped>
.el-icon-document{}
.el-icon-document a{color: #1ab394}
.el-icon-document a:hover{color: #ff9900}
</style>
