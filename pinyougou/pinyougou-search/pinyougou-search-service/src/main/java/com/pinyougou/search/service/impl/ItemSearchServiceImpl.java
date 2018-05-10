package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;

import java.util.HashMap;
import java.util.Map;

/**
 * @author GuJiaFu
 * @date 2018/5/10
 */
@Service(interfaceClass = ItemSearchService.class,timeout = 60000)
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    @Override
    public Map<String, Object> search(Map<String, Object> searchMap) {

        Map<String,Object> resultMap = new HashMap<>();
        String keywords = (String) searchMap.get("keywords");
        if(StringUtils.isNotBlank(keywords)){
            SimpleQuery query = new SimpleQuery();
            Criteria criteria = new Criteria("item_keywords");
            criteria.is(keywords);
            query.addCriteria(criteria);
            ScoredPage<TbItem> scoredPage = solrTemplate.queryForPage(query, TbItem.class);
            resultMap.put("rows",scoredPage.getContent());
        }else{
            SimpleQuery query = new SimpleQuery("*:*");
            ScoredPage<TbItem> scoredPage = solrTemplate.queryForPage(query, TbItem.class);
            resultMap.put("rows",scoredPage.getContent());
        }


        return resultMap;
    }

}
