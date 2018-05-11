package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.TypeTemplateMapper;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.pojo.TbTypeTemplate;
import com.pinyougou.sellergoods.service.TypeTemplateService;
import com.pinyougou.service.impl.BaseServiceImpl;
import com.pinyougou.vo.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service(interfaceClass = TypeTemplateService.class)
public class TypeTemplateServiceImpl extends BaseServiceImpl<TbTypeTemplate> implements TypeTemplateService {

    @Autowired
    private TypeTemplateMapper typeTemplateMapper;
    @Autowired
    private SpecificationOptionServiceImpl specificationOptionService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public PageResult search(Integer page, Integer rows, TbTypeTemplate typeTemplate) {
        PageHelper.startPage(page, rows);

        Example example = new Example(TbTypeTemplate.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(typeTemplate.getName())){
            criteria.andLike("name", "%" + typeTemplate.getName() + "%");
        }

        List<TbTypeTemplate> list = typeTemplateMapper.selectByExample(example);
        PageInfo<TbTypeTemplate> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 根据模板id查找,规格和规格选项
     * @param id
     * @return
     */
    public List<Map> findSpecList(Long id) {
        TbTypeTemplate tbTypeTemplate = typeTemplateMapper.selectByPrimaryKey(id);
        String specIds = tbTypeTemplate.getSpecIds();
        List<Map> maps = JSONArray.parseArray(specIds, Map.class);
        for (Map map : maps) {
            Object specId = map.get("id");
            List<TbSpecificationOption> options = specificationOptionService.findBySepcId(Long.parseLong(specId.toString()));
            map.put("options",options);
        }
        return maps;
    }

    /**
     * 查询所有类型模板的品牌brandIds和规格specIds到缓存Redis中
     */
    @Override
    public void updateTypeTemplateToRedis() {
        List<TbTypeTemplate> typeTemplateList = findAll();
        if(typeTemplateList!=null && typeTemplateList.size()>0){

            for (TbTypeTemplate typeTemplate : typeTemplateList) {
                // 遍历储存brandIds
                List<Map> brandList = JSONArray.parseArray(typeTemplate.getBrandIds(), Map.class);
                redisTemplate.boundHashOps("brandList").put(typeTemplate.getId(),brandList);
                // 遍历储存specIds
                List<Map> specList = findSpecList(typeTemplate.getId());
                redisTemplate.boundHashOps("specList").put(typeTemplate.getId(),specList);
            }
        }
    }
}
