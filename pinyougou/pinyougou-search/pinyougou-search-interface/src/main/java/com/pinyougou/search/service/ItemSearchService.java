package com.pinyougou.search.service;

import com.pinyougou.pojo.TbItem;

import java.util.List;
import java.util.Map;

/**
 * @author GuJiaFu
 * @date 2018/5/10
 */
public interface ItemSearchService {
    Map<String,Object> search(Map<String, Object> searchMap);


    void addItemListToSolr(List<TbItem> itemList);


    void deleteItemListFromSolr(List<Long> goodIds);
}
