package com.pinyougou.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;

import java.io.Serializable;
import java.util.List;

public abstract class BaseServiceImpl<T> implements BaseService<T> {

    //在spring 4.x版本之后的泛型依赖注入
    @Autowired
    private Mapper<T> mapper;

    @Override
    public List<T> findAll() {
        return mapper.selectAll();
    }

    @Override
    public T findOne(T t) {
        return mapper.selectOne(t);
    }

    @Override
    public T findById(Serializable id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<T> findByPage(Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        return mapper.selectAll();
    }

    @Override
    public PageResult findByPage(Integer page, Integer rows, T t) {
        PageHelper.startPage(page, rows);
        List<T> list = mapper.select(t);
        PageInfo<T> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public void add(T t) {
        mapper.insertSelective(t);
    }

    @Override
    public void update(T t) {
        mapper.updateByPrimaryKeySelective(t);

    }

    @Override
    public void deleteByIds(Serializable[] ids) {
        if (ids != null && ids.length > 0) {
            for (Serializable id : ids) {
                mapper.deleteByPrimaryKey(id);
            }
        }
    }
}
