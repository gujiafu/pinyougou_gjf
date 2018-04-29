package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.SpecificationMapper;
import com.pinyougou.mapper.SpecificationOptionMapper;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.sellergoods.service.SpecificationService;
import com.pinyougou.service.impl.BaseServiceImpl;
import com.pinyougou.vo.PageResult;
import com.pinyougou.vo.Specification;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Service(interfaceClass = SpecificationService.class,timeout = 60000)
public class SpecificationServiceImpl extends BaseServiceImpl<TbSpecification> implements SpecificationService {

    @Autowired
    private SpecificationMapper specificationMapper;
    @Autowired
    private SpecificationOptionMapper specificationOptionMapper;

    @Override
    public PageResult search(Integer page, Integer rows, TbSpecification specification) {
        PageHelper.startPage(page, rows);

        Example example = new Example(TbSpecification.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(specification.getSpecName())){
            criteria.andLike("specName", "%" + specification.getSpecName() + "%");
        }

        List<TbSpecification> list = specificationMapper.selectByExample(example);
        PageInfo<TbSpecification> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public void add(Specification spec) {
        if(spec.getSpecification()!=null){
            specificationMapper.insert(spec.getSpecification());
        }
        List<TbSpecificationOption> specificationOptionList = spec.getSpecificationOptionList();
        if(specificationOptionList!=null && specificationOptionList.size()>0){
            for (TbSpecificationOption specificationOption : specificationOptionList) {
                specificationOption.setSpecId(spec.getSpecification().getId());
                specificationOptionMapper.insert(specificationOption);
            }
        }
    }

    /**
     * 根据id查找
     * @param id
     * @return
     */
    @Override
    public Specification findOne(Long id) {
        TbSpecification tbSpecification = specificationMapper.selectByPrimaryKey(id);
        TbSpecificationOption tbSpecificationOption = new TbSpecificationOption();
        tbSpecificationOption.setSpecId(id);
        List<TbSpecificationOption> tbSpecificationOptionList = specificationOptionMapper.select(tbSpecificationOption);

        Specification specification = new Specification();
        specification.setSpecification(tbSpecification);
        specification.setSpecificationOptionList(tbSpecificationOptionList);
        return specification;
    }

    /**
     * 跟新规格
     * @param specification
     */
    @Override
    public void update(Specification specification) {
        TbSpecification tbSpecification = specification.getSpecification();
        List<TbSpecificationOption> tbSpecificationOptionList = specification.getSpecificationOptionList();
        if(StringUtils.isNotBlank(tbSpecification.getSpecName())){
            specificationMapper.updateByPrimaryKeySelective(tbSpecification);
        }

        // 先删除之前规格选项
        TbSpecificationOption tbSpecificationOption = new TbSpecificationOption();
        tbSpecificationOption.setSpecId(tbSpecification.getId());
        specificationOptionMapper.delete(tbSpecificationOption);

        if(tbSpecificationOptionList!=null && tbSpecificationOptionList.size()>0){
            // 添加新的规格选项
            for (TbSpecificationOption specificationOption : tbSpecificationOptionList) {
                specificationOption.setSpecId(tbSpecification.getId());
                specificationOptionMapper.insert(specificationOption);
            }
        }
    }

    /**
     * 根据id删除规格和规格选项
     * @param ids
     */
    public void delByIds(Serializable[] ids) {
        if (ids != null && ids.length > 0) {
            // 删除规格
            deleteByIds(ids);
            // 删除规格选项
            Example example = new Example(TbSpecificationOption.class);
            example.createCriteria().andIn("specId", Arrays.asList(ids));
            specificationOptionMapper.deleteByExample(example);
        }
    }
}
