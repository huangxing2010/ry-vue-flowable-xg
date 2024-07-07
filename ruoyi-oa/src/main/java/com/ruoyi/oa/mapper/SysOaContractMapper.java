package com.ruoyi.oa.mapper;

import com.ruoyi.oa.domain.SysOaContract;
import com.ruoyi.oa.domain.vo.SysOaContractVo;
import com.ruoyi.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

/**
 * 合同管理Mapper接口
 *
 * @author huangxing
 * @date 2024-01-10
 */
@Mapper
public interface SysOaContractMapper extends BaseMapperPlus<SysOaContractMapper, SysOaContract, SysOaContractVo> {

}
