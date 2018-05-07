package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.PageResult;

import java.util.List;

public interface SpecificationOptionService extends BaseService<TbSpecificationOption> {

    PageResult search(Integer page, Integer rows, TbSpecificationOption specificationOption);

    /**
     * 根据规格id查找规格选项列表
     * @param specId
     * @return
     */
    List<TbSpecificationOption> findBySepcId(Long specId);
}