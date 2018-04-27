package com.pinyougou.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import com.pinyougou.vo.PageResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/brand")
//@Controller
@RestController //组合注解@Controller @ResponseBody
public class BrandController {

    @Reference
    private BrandService brandService;

    @GetMapping("/findAll")
    //@ResponseBody
    public List<TbBrand> findAll(){
        return brandService.findAll();
    }

    /**
     * 根据分页信息查询品牌列表
     * @param page 页号
     * @param rows 页大小
     * @return 品牌列表
     */
    @GetMapping("/testPage")
    //@ResponseBody
    public List<TbBrand> testPage(Integer page, Integer rows){
        return brandService.findByPage(page, rows);
    }

    @GetMapping("/findPage")
    //@ResponseBody
    public PageResult findPage(Integer page, Integer rows){
        return brandService.findByPage(page, rows, null);
    }

}
