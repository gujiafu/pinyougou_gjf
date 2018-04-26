package com.pinyougou.mapper;

import com.pinyougou.pojo.TbSeckillOrder;

public interface TbSeckillOrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TbSeckillOrder record);

    int insertSelective(TbSeckillOrder record);

    TbSeckillOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TbSeckillOrder record);

    int updateByPrimaryKey(TbSeckillOrder record);
}