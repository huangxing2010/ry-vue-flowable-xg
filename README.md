<p align="center">
	<img alt="logo" src="https://gitee.com/xnqysabout/oa-flow-pms/raw/master/doc/logo.jpg">
</p>

<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">工程项目管理系统</h1>
<h4 align="center">基于若依框架ruoyi-flowable-plus开发，SpringBoot+Vue前后端分离</h4>


<p align="center" style="margin-top: 50px;">
   <a href="https://gitee.com/xnqysabout/oa-flow-pms">
	  <img alt="开源版" src="https://gitee.com/xnqysabout/oa-flow-pms/raw/master/doc/images/btn02.png">
    </a>
    <a href="https://gitee.com/xnqysabout/ry-vue-flowable-xg">
	  <img alt="旗舰版" src="https://gitee.com/xnqysabout/oa-flow-pms/raw/master/doc/images/btn01.png">
    </a>
</p>

# 新版发布
* 旗舰版请移步：[工程项目管理系统V2.0-NEW](https://gitee.com/xnqysabout/oa-flow-pms)

# 功能对比
<p align="center">
	  <img alt="" src="https://gitee.com/xnqysabout/oa-flow-pms/raw/master/doc/images/contrast.png">
</p>

# 项目功能介绍
* 基于若依管理系统 ruoyi-flowable-plus进行二次开发，基于Spring Boot、Mybatis-plus、Vue和ElementUI技术栈。
* 工程项目管理系统适用于工程项目的投标管理及项目进度及成本控制用户的OA办公系统；
* 流程管理：流程发起、我的流程、待办任务、已办任务；
* 流程部署：在原有基础上增加了任务监听器，根据部门 id 流转到该部门的主管审批；
* 项目管理：项目信息的维护，重点功能是项目的流程进度控制，根据项目进度类型填写任务进度信息；
* 任务管理：工程项目进度的管理，支持添加、修改、删除、查看等操作；
* 合同管理：合同在实际场景中分两部分，一是关联项目合同信息，二是除项目合同外还有日常采购合同作为单独信息管理
* 投标管理：参与的投标工程项目的信息录入，发起的工作流走完后会自动保存到数据库，另外对未开标或者筹划中的的项目做了采购意向管理；
* 财务管理：财务管理分两部分，一是关联项目财务的收入、支出情况及核算；二是非项目的采购收支情况；
* 重要提醒：对工作中的事件做的任务提醒，办理情况等内容；
* 知识库等：普通分类文章信息；
* oss云存储，请自行配置；

# 仓库地址
* GitHub [https://github.com/huangxing2010/ry-vue-flowable-xg](https://github.com/huangxing2010/ry-vue-flowable-xg)
* Gitee [https://gitee.com/xnqysabout/ry-vue-flowable-xg](https://gitee.com/xnqysabout/ry-vue-flowable-xg)

# 安装步骤
## 后端安装配置
* 1、git 源码；
* 2、导入 idea , 自动下载依赖；
* 3、ruoyi-admin/src/main/resources/application-dev.yml 修改为自己的数据库、用户名、密码、端口；
* 4、创建数据库；
* 5、导入数据库 /script/sql/mysql/ry-vue-flowable-xg.sql；
* 6、启动项目；
## 前端安装配置
```agsl
// 建议不要直接使用 cnpm 安装依赖，会有各种诡异的 bug。可以通过如下操作解决 npm 下载速度慢的问题
npm install --registry=https://registry.npmmirror.com

// 启动服务
npm run dev
```



# 解决test报错
*  测试环境
> 执行：mvn clean package -D maven.test.skip=true -P dev

* 生产环境
> 执行：mvn clean package -D maven.test.skip=true -P prod

# 在线演示
* [在线演示：item.oa.system-code.vip](http://item.oa.system-code.vip)
> 演示服务不限制CURD操作，希望大家按需使用，不要恶意添加脏数据或对服务器进行攻击等操作。（将不定期清理数据）

<table>
 <tr>
<td>用户名</td>
<td>密码</td>
<td>角色</td>
</tr>
<tr>
<td>boss</td>
<td>123456</td>
<td>总经理</td>
</tr>
<tr>
<td>zzj</td>
<td>123456</td>
<td>行政总监</td>
</tr>
<tr>
<td>lwy</td>
<td>123456</td>
<td>行政文员</td>
</tr>
<tr>
<td>hzj</td>
<td>123456</td>
<td>工程总监</td>
</tr>
<tr>
<td>hg</td>
<td>123456</td>
<td>工程师</td>
</tr>
<tr>
<td>pbwyh</td>
<td>123456</td>
<td>评标委员会</td>
</tr>
</table>

# 特别鸣谢
* RuoYi-Vue
* RuoYi-Vue-Plus
* RuoYi-flowable
* bpmn-process-designer

# 项目支出
* 如果项目对您有帮助，请给项目点个 star，如果您需要技术咨询，欢迎加QQ群：605069634！

# 演示图

<table>
    <tr>
        <td><img src="https://oscimg.oschina.net/oscnet/up-6833234c2f34ca3350f46615a1d520299c1.png"/></td>
        <td><img src="https://oscimg.oschina.net/oscnet/up-8a9abf3099b0af576969f3bfa366d5bb097.png"/></td>
    </tr>
<tr>
        <td><img src="https://oscimg.oschina.net/oscnet/up-95ce7c1fb2da53363731717d81a6bd67855.png"/></td>
        <td><img src="https://oscimg.oschina.net/oscnet/up-eb22214324b947291865d1c838e7b6884b8.png"/></td>
    </tr>
<tr>
        <td><img src="https://oscimg.oschina.net/oscnet/up-f13cd5c2018bd08aade6012780f94f194ad.png"/></td>
        <td><img src="https://oscimg.oschina.net/oscnet/up-6c4cf60a4eb72086aca662782f28464ca77.png"/></td>
    </tr>

<tr>
        <td><img src="https://oscimg.oschina.net/oscnet/up-47a21c0231ded25461d170d84f49d6f2b3d.png"/></td>
        <td><img src="https://oscimg.oschina.net/oscnet/up-00d2f1ec7ef77592b3ca0cd96aaba0cb770.png"/></td>
    </tr>
<tr>
        <td><img src="https://oscimg.oschina.net/oscnet/up-e91e58070106cdbfb999ba7af6305ae5c0a.png"/></td>
        <td><img src="https://oscimg.oschina.net/oscnet/up-cf034ac32a683b111e9eab1860674455bd9.png"/></td>
    </tr>

   
</table>

# 交流群
qq群： 605069634


# 版权声明
* 采用MIT开源协议，完全免费给个人及企业使用。
* 项目处于开发阶段，工作流流程还存在不足。因此，目前仅推荐用于学习、毕业设计等个人使用。



## 推荐
大家在使用本项目时，推荐结合贺波老师的书
[《深入Flowable流程引擎：核心原理与高阶实战》](https://item.jd.com/14804836.html)学习。这本书得到了Flowable创始人Tijs Rademakers亲笔作序推荐，对系统学习和深入掌握Flowable的用法非常有帮助。
<img src="https://foruda.gitee.com/images/1727432593738798662/46c08088_2042292.png" width="800" height="1000"/>
