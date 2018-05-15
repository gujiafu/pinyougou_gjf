package com.pinyougou.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.GoodsService;
import com.pinyougou.sellergoods.service.ItemCatService;
import com.pinyougou.vo.Goods;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author GuJiaFu
 * @date 2018/5/14
 */
@Controller
public class ItemController {

    @Reference
    private GoodsService goodsService;
    @Reference
    private ItemCatService itemCatService;

    @GetMapping("/{goodsId}")
    public ModelAndView toItemPage(@PathVariable Long goodsId){
        ModelAndView mv = new ModelAndView("item");
//        mv.setViewName("item");
        // 根据id查找spu
        Goods goods = goodsService.findGoodsByIdAndStatus(goodsId,"1");
        mv.addObject("goods",goods.getGoods());
        mv.addObject("goodsDesc",goods.getGoodsDesc());
        mv.addObject("itemList",goods.getItemList());

        // 查找多级分类
        Long category1Id = goods.getGoods().getCategory1Id();
        if(category1Id!=null){
            mv.addObject("category1Name",itemCatService.findOne(category1Id).getName());
        }
        Long category2Id = goods.getGoods().getCategory2Id();
        if(category1Id!=null){
            mv.addObject("category2Name",itemCatService.findOne(category2Id).getName());
        }
        Long category3Id = goods.getGoods().getCategory3Id();
        if(category1Id!=null){
            mv.addObject("category3Name",itemCatService.findOne(category3Id).getName());
        }

        return mv;
    }




}
