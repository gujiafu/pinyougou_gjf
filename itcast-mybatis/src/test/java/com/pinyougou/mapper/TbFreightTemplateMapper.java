package com.pinyougou.mapper;

import com.pinyougou.pojo.TbFreightTemplate;

public interface TbFreightTemplateMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TbFreightTemplate record);

    int insertSelective(TbFreightTemplate record);

    TbFreightTemplate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TbFreightTemplate record);

    int updateByPrimaryKey(TbFreightTemplate record);
}