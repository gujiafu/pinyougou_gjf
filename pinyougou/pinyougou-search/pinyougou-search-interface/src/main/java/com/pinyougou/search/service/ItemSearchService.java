package com.pinyougou.search.service;

import java.util.Map; /**
 * @author GuJiaFu
 * @date 2018/5/10
 */
public interface ItemSearchService {
    Map<String,Object> search(Map<String, Object> searchMap);
}
