package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.PageResult;

import java.util.List;

public interface BrandService extends BaseService<TbBrand> {

    @Deprecated
    List<TbBrand> queryAll();

    /**
     * 根据分页信息查询品牌列表
     * @param page 页号
     * @param rows 页大小
     * @return 品牌列表
     */
    List<TbBrand> testPage(Integer page, Integer rows);

    /**
     * 根据条件分页查询
     *
     * @param brand 品牌信息
     * @param page 页号
     * @param rows 页大小
     * @return 分页对象
     */
    PageResult search(TbBrand brand, Integer page, Integer rows);
}
