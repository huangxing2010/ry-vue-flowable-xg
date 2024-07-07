package com.ruoyi.oa.service;

import com.ruoyi.oa.domain.SysOaDetail;
import com.ruoyi.oa.domain.vo.SysOaDetailVo;
import com.ruoyi.oa.domain.bo.SysOaDetailBo;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 进出账明细Service接口
 *
 * @author huangxing
 * @date 2024-01-17
 */
public interface ISysOaDetailService {

    /**
     * 查询进出账明细
     */
    SysOaDetailVo queryById(Long detailId);

    /**
     * 查询进出账明细列表
     */
    TableDataInfo<SysOaDetailVo> queryPageList(SysOaDetailBo bo, PageQuery pageQuery);

    /**
     * 查询进出账明细列表
     */
    List<SysOaDetailVo> queryList(SysOaDetailBo bo);

    /**
     * 新增进出账明细
     */
    Boolean insertByBo(SysOaDetailBo bo);

    /**
     * 修改进出账明细
     */
    Boolean updateByBo(SysOaDetailBo bo);

    /**
     * 校验并批量删除进出账明细信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
