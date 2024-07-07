package com.ruoyi.oa.listener;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.oa.config.ContextUtil;
import com.ruoyi.oa.domain.bo.SysOaBidBo;
import com.ruoyi.oa.service.impl.SysOaBidServiceImpl;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class saveBidFormListener implements TaskListener {
    public void notify(DelegateTask delegateTask) {
        //TODO 实现你的任务监听器逻辑
        System.out.println("执行任务监听器..." + delegateTask.getVariables());

        if (EVENTNAME_COMPLETE.equals(delegateTask.getEventName())) {
            Map<String, Object> variables = delegateTask.getVariables();
            SysOaBidBo sysOaBidBo = new SysOaBidBo();
            sysOaBidBo.setBidTitle(variables.get("project_name").toString());
            sysOaBidBo.setBidNumber(variables.get("project_num").toString());
            sysOaBidBo.setBidType(variables.get("project_mode").toString());
//            sysOaBidBo.setBudget(new BigDecimal(variables.get("budget").toString()));
            sysOaBidBo.setBudget(variables.get("budget").toString());
            sysOaBidBo.setPackageInfo(variables.get("package_info").toString());
            //字符串转日期
           /* LocalDate releaseTime = LocalDate.parse(variables.get("release_time").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            ZonedDateTime zonedDateTime = releaseTime.atStartOfDay(ZoneId.systemDefault());
            sysOaBidBo.setNoticeTime(Date.from(zonedDateTime.toInstant()));*/
            sysOaBidBo.setNoticeTime(variables.get("release_time").toString());
            sysOaBidBo.setDeadline(variables.get("range_time").toString());
            sysOaBidBo.setSignType(variables.get("sign_type").toString());
            sysOaBidBo.setPatternFile(variables.get("pattern_file").toString());
            //字符串转日期
            /*LocalDate release = LocalDate.parse(variables.get("start_time").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            ZonedDateTime zonedDate = release.atStartOfDay(ZoneId.systemDefault());*/
            sysOaBidBo.setExpirationTime(variables.get("start_time").toString());
            sysOaBidBo.setBidAddress(variables.get("address").toString());
            sysOaBidBo.setClientName(variables.get("client_name").toString());
            sysOaBidBo.setAgency(variables.get("agency").toString());
            sysOaBidBo.setDocument(variables.get("document_price").toString());

            // 递归获取Object的value值
            Object attachment1 = variables.get("attachment");
            //借鉴：https://blog.csdn.net/weixin_30608131/article/details/95453992
            List<String> list = new ArrayList<>();
            if(attachment1 instanceof List){
                for(int i=0; i<((List) attachment1).size(); i++){
                    //得到对象数组里的对象值
                    Object set = ((List) attachment1).set(i, ((List) attachment1).get(i));
                    //对象转json
                    String s = JSONObject.toJSONString(set);
                    //json转对象
                    JSONObject jsonObjects = JSON.parseObject(s);
                    //对象添加到list数组
                    list.add(jsonObjects.get("ossId").toString());
                }
            }
            String collect = list.stream().collect(Collectors.joining(","));
            sysOaBidBo.setAccessory(collect);
             if(variables.get("remark") != null){
                sysOaBidBo.setRemark(variables.get("remark").toString());
            }
            SysOaBidServiceImpl sysOaBidService = ContextUtil.getBean(SysOaBidServiceImpl.class);
            sysOaBidService.insertByBo(sysOaBidBo);
        }
    }


}

