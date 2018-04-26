package com.pinyougou.mapper;

import com.pinyougou.pojo.TbPayLog;

public interface TbPayLogMapper {
    int deleteByPrimaryKey(String outTradeNo);

    int insert(TbPayLog record);

    int insertSelective(TbPayLog record);

    TbPayLog selectByPrimaryKey(String outTradeNo);

    int updateByPrimaryKeySelective(TbPayLog record);

    int updateByPrimaryKey(TbPayLog record);
}