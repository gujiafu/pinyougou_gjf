package com.pinyougou.service;

import com.pinyougou.vo.PageResult;

import java.io.Serializable;
import java.util.List;

public interface BaseService<T> {

    //查询全部
    public List<T> findAll();

    //查询一个
    public T findOne(Serializable id);

    //根据主键查询
    public List<T> findByWhere(T t);

    //根据分页查询
    public PageResult findPage(Integer page, Integer rows);

    //根据条件分页查询
    public PageResult findPage(Integer page, Integer rows, T t);

    //选择性新增
    public void add(T t);

    //选择性更新
    public void update(T t);

    //批量删除
    public void deleteByIds(Serializable[] ids);
}
