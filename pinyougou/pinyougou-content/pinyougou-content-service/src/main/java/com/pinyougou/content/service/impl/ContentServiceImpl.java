package com.pinyougou.content.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.content.service.ContentService;
import com.pinyougou.mapper.ContentMapper;
import com.pinyougou.pojo.TbContent;
import com.pinyougou.service.impl.BaseServiceImpl;
import com.pinyougou.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

//@Transactional
@Service(interfaceClass = ContentService.class,timeout = 60000)
public class ContentServiceImpl extends BaseServiceImpl<TbContent> implements ContentService {

    //内容数据在redis中对应的key的名称
    private static final String REDIS_CONTENT ="content";

    @Autowired
    private ContentMapper contentMapper;

    @Autowired
    private RedisTemplate redisTemplate;



    @Override
    public PageResult search(Integer page, Integer rows, TbContent content) {
        PageHelper.startPage(page, rows);

        Example example = new Example(TbContent.class);
        Example.Criteria criteria = example.createCriteria();
        /*if(!StringUtils.isEmpty(content.get***())){
            criteria.andLike("***", "%" + content.get***() + "%");
        }*/

        List<TbContent> list = contentMapper.selectByExample(example);
        PageInfo<TbContent> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 根据内容分类id,status=1,sort_order降序查找分类
     * @return
     */
    public List<TbContent> findContentListByCategoryId(Long categoryId) {
        List<TbContent> contentList =null;
        // 先从Redis查找,有就直接返回
        try {
            contentList = (List<TbContent>) redisTemplate.boundHashOps(REDIS_CONTENT).get(categoryId);
            if(contentList !=null){
                return contentList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Example example = new Example(TbContent.class);
        example.orderBy("sortOrder").desc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("categoryId",categoryId);
        criteria.andEqualTo("status","1");
        contentList = contentMapper.selectByExample(example);

        // 没有就先从数据库查找,再放到Redis中
        try {
            redisTemplate.boundHashOps(REDIS_CONTENT).put(categoryId,contentList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contentList;
    }

    @Override
    public void add(TbContent tbContent) {
        super.add(tbContent);
        Long categoryId = tbContent.getCategoryId();
        updateContentInRedisByCategoryId(categoryId);
    }

    @Override
    public void update(TbContent tbContent) {
        Long id = tbContent.getId();
        TbContent oldContent = contentMapper.selectByPrimaryKey(id);
        if(!oldContent.getCategoryId().equals(tbContent.getCategoryId())){
            updateContentInRedisByCategoryId(oldContent.getCategoryId());
        }
        updateContentInRedisByCategoryId(tbContent.getCategoryId());
        super.update(tbContent);
    }

    @Override
    public void deleteByIds(Serializable[] ids) {
        Example example = new Example(TbContent.class);
        example.createCriteria().andIn("id", Arrays.asList(ids));
        List<TbContent> contentList = contentMapper.selectByExample(example);
        for (TbContent content : contentList) {
            updateContentInRedisByCategoryId(content.getCategoryId());
        }
        super.deleteByIds(ids);
    }


    public void updateContentInRedisByCategoryId(Long id){
        try {
            redisTemplate.boundHashOps(REDIS_CONTENT).delete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
