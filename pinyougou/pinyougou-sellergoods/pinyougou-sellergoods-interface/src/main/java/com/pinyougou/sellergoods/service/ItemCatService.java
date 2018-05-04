package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.PageResult;

import java.util.List;

public interface ItemCatService extends BaseService<TbItemCat> {

    PageResult search(Integer page, Integer rows, TbItemCat itemCat);

    /**
     * 根据父id查找
     * @param pid
     * @return
     */
    List<TbItemCat> findByParentId(Long pid);
}