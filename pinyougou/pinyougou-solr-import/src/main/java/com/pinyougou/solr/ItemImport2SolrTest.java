package com.pinyougou.solr;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.ItemMapper;
import com.pinyougou.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author GuJiaFu
 * @date 2018/5/10
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:spring/applicationContext*.xml")
public class ItemImport2SolrTest {

    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired
    private ItemMapper itemMapper;

    @Test
    public void addAllTest(){
        TbItem item = new TbItem();
        item.setStatus("1");
        List<TbItem> itemList = itemMapper.select(item);
        if(itemList!=null && itemList.size()>0){
            for (TbItem tbItem : itemList) {
                Map specMap = JSON.parseObject(tbItem.getSpec(), Map.class);
                tbItem.setSpecMap(specMap);
            }
        }
        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();
    }

    @Test
    public void deleteAllTest(){
        TbItem item = new TbItem();
        item.setStatus("1");
        List<TbItem> itemList = itemMapper.select(item);
        List<String> ids = new ArrayList<>();
        if(itemList!=null && itemList.size()>0){
            for (TbItem tbItem : itemList) {
                ids.add(tbItem.getId().toString());
            }
        }
        solrTemplate.deleteById(ids);
        solrTemplate.commit();
    }


}
