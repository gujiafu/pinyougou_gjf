import com.pinyougou.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author GuJiaFu
 * @date 2018/5/9
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-solr.xml")
public class OneSolrTest {

    @Autowired
    private SolrTemplate solrTemplate;

    @Test
    public void addTest(){
        TbItem tbItem = new TbItem();
        tbItem.setId(1L);
        tbItem.setTitle("中兴天机axon m 折叠双屏智能手机");
        tbItem.setBrand("中兴");
        tbItem.setPrice(new BigDecimal(3999));
        tbItem.setGoodsId(123L);
        tbItem.setSeller("中兴旗靓店");
        tbItem.setCategory("手机");
        solrTemplate.saveBean(tbItem);
        solrTemplate.commit();
    }

    // 根据id删除
    @Test
    public void deleteById(){
        solrTemplate.deleteById("1");
        solrTemplate.commit();
    }

    // 根据条件删除
    @Test
    public void deleteByQuery(){
        SimpleQuery query = new SimpleQuery();
        Criteria criteria1 = new Criteria("item_title");
        criteria1.contains("axon");
        query.addCriteria(criteria1);

        solrTemplate.delete(query);
        solrTemplate.commit();
    }

    // 查询所有
    @Test
    public void queryForPageTest(){
        SimpleQuery query = new SimpleQuery("item_title:axon");
        query.setOffset(0);
        query.setRows(20);
        ScoredPage<TbItem> items = solrTemplate.queryForPage(query, TbItem.class);
        showPage(items);
    }


    public void showPage(ScoredPage<TbItem> items){
        System.out.println("总条数:"+items.getTotalElements());
        System.out.println("总页数:"+items.getTotalPages());
        List<TbItem> content = items.getContent();
        for (TbItem tbItem : content) {
            System.out.println(tbItem);
        }
    }


}
