package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author GuJiaFu
 * @date 2018/5/10
 */
@Service(interfaceClass = ItemSearchService.class, timeout = 60000)
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map<String, Object> search(Map<String, Object> searchMap) {

        Map<String, Object> resultMap = new HashMap<>();
        // 根据关键字搜索商品列表
        resultMap.putAll(searchItem(searchMap));
        // 根据关键字搜索商品分类列表
        List<String> categoryList = searchCategoryList(searchMap);
        resultMap.put("categoryList",categoryList);
        // 根据商品分类名字查找品牌列和规格列数据
        if(categoryList!=null && categoryList.size()>0){
            Map<String, Object> map =null;
            String category = (String) searchMap.get("category");
            // 如果页面点击非第一个商品分类获取品牌和规格数据
            if(StringUtils.isNotBlank(category)){
                map = getBrandAndSpecList(category);
            }else{
                map = getBrandAndSpecList(categoryList.get(0));
            }
            resultMap.putAll(map);
        }

        return resultMap;
    }

    /**
     * 根据goods的ids查找itemList,并添加到solr中
     * @param itemList 商品id的集合
     */
    public void addItemListToSolr(List<TbItem> itemList) {
        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();
    }

    /**
     * 根据goods的ids查找itemList,从solr中删除
     * @param goodIds 商品spu的id的集合
     */
    public void deleteItemListFromSolr(List<Long> goodIds) {
        Criteria criteria = new Criteria("item_goodsid").in(goodIds);
        SimpleQuery query = new SimpleQuery();
        query.addCriteria(criteria);
        solrTemplate.delete(query);
        solrTemplate.commit();
    }

    /**
     * 搜索关键字高亮显示并搜索商品列表
     * @param searchMap
     * @return
     */
    private Map<String, Object> searchItem(Map<String, Object> searchMap) {

        String keywords = (String) searchMap.get("keywords");
        // 处理关键字空格问题
        keywords = keywords.replace(" ", "");

        // 设置高亮查询
        SimpleHighlightQuery query = new SimpleHighlightQuery();
        Criteria criteria = new Criteria("item_keywords").is(keywords);
        query.addCriteria(criteria);

        // 设置分类条件
        String category = (String) searchMap.get("category");
        if(StringUtils.isNotBlank(category)){
            Criteria categoryCriteria = new Criteria("item_category").is(category);
            SimpleFilterQuery filterQuery = new SimpleFilterQuery(categoryCriteria);
            query.addFilterQuery(filterQuery);
        }

        // 设置品牌条件
        String brand = (String) searchMap.get("brand");
        if(StringUtils.isNotBlank(brand)){
            Criteria brandCriteria = new Criteria("item_brand").is(brand);
            SimpleFilterQuery filterQuery = new SimpleFilterQuery(brandCriteria);
            query.addFilterQuery(filterQuery);
        }

        // 设置规格选项条件
        Map<String,String> spec = (Map<String, String>) searchMap.get("spec");
        if(spec!=null && spec.size()>0){
            for (Map.Entry<String, String> entry : spec.entrySet()) {
                Criteria criteriaSpec = new Criteria("item_spec_"+entry.getKey()).is(entry.getValue());
                SimpleFilterQuery filterQuery = new SimpleFilterQuery(criteriaSpec);
                query.addFilterQuery(filterQuery);
            }
        }

        // 设置价格范围
        String price = (String) searchMap.get("price");
        if(StringUtils.isNotBlank(price)){
            String[] strArray = price.split("-");
            if(strArray!=null && strArray.length>0){
                Criteria priceCriteria1 = new Criteria("item_price").greaterThanEqual(strArray[0]);
                SimpleFilterQuery filterQuery1 = new SimpleFilterQuery(priceCriteria1);
                query.addFilterQuery(filterQuery1);
                if(!strArray[1].equals("*")){
                    Criteria priceCriteria2 = new Criteria("item_price").lessThanEqual(strArray[1]);
                    SimpleFilterQuery filterQuery2 = new SimpleFilterQuery(priceCriteria2);
                    query.addFilterQuery(filterQuery2);
                }
            }
        }

        // 设置排序查询
        String sortField = (String) searchMap.get("sortField");
        String sort = (String) searchMap.get("sort");
        if(StringUtils.isNotBlank(sortField) && StringUtils.isNotBlank(sort)){
            Sort sort1 = new Sort(sort.equals("ASC")? Sort.Direction.ASC: Sort.Direction.DESC,"item_"+sortField);
            query.addSort(sort1);
        }


        // 设置高亮
        HighlightOptions highlightOptions = new HighlightOptions();
        highlightOptions.addField("item_title");// 高亮域
        highlightOptions.setSimplePrefix("<em style='color:red'>");// 高亮起始标签
        highlightOptions.setSimplePostfix("</em>");// 高亮结束标签
        query.setHighlightOptions(highlightOptions);
        // 查询
        HighlightPage<TbItem> itemHighlightPage = solrTemplate.queryForHighlightPage(query, TbItem.class);
        // 处理高亮标题
        List<HighlightEntry<TbItem>> highlighted = itemHighlightPage.getHighlighted();
        if (highlighted != null && highlighted.size() > 0) {
            for (HighlightEntry<TbItem> entry : highlighted) {
                List<HighlightEntry.Highlight> highlights =
                        entry.getHighlights();
                if (highlights != null && highlights.size() > 0 &&
                        highlights.get(0).getSnipplets() != null) {
                    // 设置高亮标题
                    entry.getEntity().setTitle(highlights.get(0).getSnipplets().get(0));
                }
            }
        }
        HashMap<String, Object> map = new HashMap<>();
        // 设置返回列表
        map.put("rows", itemHighlightPage.getContent());
        return map;
    }

    /**
     * 根据关键字搜索商品分类列表
     * @param searchMap
     * @return
     */
    public List<String> searchCategoryList(Map<String, Object> searchMap){

        List<String> categoryList = new ArrayList<>();
        SimpleQuery query = new SimpleQuery();
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);
        GroupOptions options = new GroupOptions();
        options.addGroupByField("item_category");
        query.setGroupOptions(options);
        GroupPage<TbItem> groupPage = solrTemplate.queryForGroupPage(query, TbItem.class);
        GroupResult<TbItem> groupResult = groupPage.getGroupResult("item_category");
        Page<GroupEntry<TbItem>> entries = groupResult.getGroupEntries();
        for (GroupEntry<TbItem> entry : entries) {
            categoryList.add(entry.getGroupValue());
        }
        return categoryList;
    }

    /**
     * 根据商品分类名字查找品牌列和规格列
     * @param categoryName
     * @return
     */
    public Map<String,Object> getBrandAndSpecList(String categoryName){
        HashMap map = new HashMap();
        if(StringUtils.isNotBlank(categoryName)){
            Long categoryId = (Long) redisTemplate.boundHashOps("itemCat").get(categoryName);
            if(categoryId!=null){
                List brandList = (List) redisTemplate.boundHashOps("brandList").get(categoryId);
                map.put("brandList",brandList);
            }
            if(categoryId!=null){
                List specList = (List) redisTemplate.boundHashOps("specList").get(categoryId);
                map.put("specList",specList);
            }
        }
        return map;
    }


}
