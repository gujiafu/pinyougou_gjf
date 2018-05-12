package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.Goods;
import com.pinyougou.vo.PageResult;

import java.util.List;

public interface GoodsService extends BaseService<TbGoods> {

    PageResult search(Integer page, Integer rows, TbGoods goods);

    /**
     * 添加
     * @param goods
     */
    void addGoods(Goods goods);

    Goods findGoods(Long id);

    /**
     * 更改商品goods sku的状态
     * @param ids
     * @param status
     * @return
     */
    void updateStatus(Long[] ids, String status);

    List<TbItem> findItemListByGoodsIdsAndStatus(Long[] ids, String status);
}