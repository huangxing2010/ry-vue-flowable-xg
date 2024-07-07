package com.ruoyi.oa.mapper;

import com.ruoyi.oa.domain.SysOaDetail;
import com.ruoyi.oa.domain.vo.SysOaDetailVo;
import com.ruoyi.common.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 进出账明细Mapper接口
 *
 * @author huangxing
 * @date 2024-01-17
 */
public interface SysOaDetailMapper extends BaseMapperPlus<SysOaDetailMapper, SysOaDetail, SysOaDetailVo> {



    /**
     * 批量删除明细表
     *
     * @param financeId 需要删除的数据主键集合
     * @return 结果
     */
   int deleteSysOaDetailByfinanceId(Long financeId);

    /**
     * 批量插入明细数据
     * @param sysOaDetails 明细列表
     * @return
     */
   int batchSysOaDetail(List<SysOaDetail> sysOaDetails);
}
