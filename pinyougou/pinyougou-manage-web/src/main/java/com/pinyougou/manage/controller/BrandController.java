package com.pinyougou.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
        return brandService.queryAll();
    }
}
