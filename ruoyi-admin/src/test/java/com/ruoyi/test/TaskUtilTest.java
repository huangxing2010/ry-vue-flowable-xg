package com.ruoyi.test;

import com.ruoyi.oa.domain.SysOaContract;
import com.ruoyi.oa.domain.bo.SysOaContractBo;
import com.ruoyi.oa.mapper.SysOaContractMapper;
import com.ruoyi.oa.service.ISysOaContractService;
import com.ruoyi.oa.service.impl.SysOaContractServiceImpl;
import org.flowable.engine.RuntimeService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
@DisplayName("测试")
public class TaskUtilTest {
    @Autowired
    RuntimeService runtimeService;


    @DisplayName("测试")
    @Test
    public void getform(){
    }
}
