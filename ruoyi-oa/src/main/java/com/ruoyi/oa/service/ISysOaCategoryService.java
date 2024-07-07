package com.ruoyi.oa.service;

import com.ruoyi.oa.domain.SysOaCategory;
import com.ruoyi.oa.domain.vo.SysOaCategoryVo;
import com.ruoyi.oa.domain.bo.SysOaCategoryBo;

import java.util.Collection;
import java.util.List;

/**
 * 知识分类Service接口
 *
 * @author huangxing
 * @date 2024-01-17
 */
public interface ISysOaCategoryService {

    /**
     * 查询知识分类
     */
    SysOaCategoryVo queryById(Long categoryId);


    /**
     * 查询知识分类列表
     */
    List<SysOaCategoryVo> queryList(SysOaCategoryBo bo);

    /**
     * 新增知识分类
     */
    Boolean insertByBo(SysOaCategoryBo bo);

    /**
     * 修改知识分类
     */
    Boolean updateByBo(SysOaCategoryBo bo);

    /**
     * 校验并批量删除知识分类信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
