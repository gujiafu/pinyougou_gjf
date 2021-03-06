package com.pinyougou.content.service;

import com.pinyougou.pojo.TbContent;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.PageResult;

import java.util.List;

public interface ContentService extends BaseService<TbContent> {

    PageResult search(Integer page, Integer rows, TbContent content);

    /**
     * 根据内容分类id,status=1,sort_order降序查找分类
     * @return
     */
    List<TbContent> findContentListByCategoryId(Long categoryId);
}