package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.PageResult;
import com.pinyougou.vo.Specification;

import java.io.Serializable;

public interface SpecificationService extends BaseService<TbSpecification> {

    PageResult search(Integer page, Integer rows, TbSpecification specification);

    // 添加规格
    void add(Specification spec);

    //根据id查找
    Specification findOne(Long id);

    // 规格数据跟新
    void update(Specification specification);

    void delByIds(Serializable[] ids);

}