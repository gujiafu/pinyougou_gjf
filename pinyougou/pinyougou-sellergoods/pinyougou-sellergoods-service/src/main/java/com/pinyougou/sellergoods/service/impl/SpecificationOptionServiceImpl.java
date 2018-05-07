package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.SpecificationOptionMapper;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.sellergoods.service.SpecificationOptionService;
import com.pinyougou.service.impl.BaseServiceImpl;
import com.pinyougou.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service(interfaceClass = SpecificationOptionService.class)
public class SpecificationOptionServiceImpl extends BaseServiceImpl<TbSpecificationOption> implements SpecificationOptionService {

    @Autowired
    private SpecificationOptionMapper specificationOptionMapper;

    @Override
    public PageResult search(Integer page, Integer rows, TbSpecificationOption specificationOption) {
        PageHelper.startPage(page, rows);

        Example example = new Example(TbSpecificationOption.class);
        Example.Criteria criteria = example.createCriteria();
        /*if(!StringUtils.isEmpty(specificationOption.get***())){
            criteria.andLike("***", "%" + specificationOption.get***() + "%");
        }*/

        List<TbSpecificationOption> list = specificationOptionMapper.selectByExample(example);
        PageInfo<TbSpecificationOption> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 根据规格id查找规格选项列表
     * @param specId
     * @return
     */
    public List<TbSpecificationOption> findBySepcId(Long specId) {
        TbSpecificationOption option = new TbSpecificationOption();
        option.setSpecId(specId);
        return specificationOptionMapper.select(option);
    }
}
