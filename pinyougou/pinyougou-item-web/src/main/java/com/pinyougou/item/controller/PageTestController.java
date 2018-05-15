package com.pinyougou.item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.sellergoods.service.GoodsService;
import com.pinyougou.sellergoods.service.ItemCatService;
import com.pinyougou.vo.Goods;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author GuJiaFu
 * @date 2018/5/14
 */
@RequestMapping("/test")
@Controller
public class PageTestController {

    @Reference
    private GoodsService goodsService;
    @Reference
    private ItemCatService itemCatService;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${ITEM_HTML_PATH}")
    private String ITEM_HTML_PATH;

    @GetMapping("/audit")
    @ResponseBody
    public String audit(Long[] goodsIds){
        try {
            if (goodsIds!=null && goodsIds.length>0){
                for (Long id : goodsIds) {
                    getHtml(id);
                }
            }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "/failure.html";
        }

    }

    private void getHtml(Long goodsId){

        try {
            Map<String,Object> dateModel = new HashMap<>();
//        dateModel.setViewName("item");
            // 根据id查找spu
            Goods goods = goodsService.findGoodsByIdAndStatus(goodsId,"1");
            dateModel.put("goods",goods.getGoods());
            dateModel.put("goodsDesc",goods.getGoodsDesc());
            dateModel.put("itemList",goods.getItemList());

            // 查找多级分类
            Long category1Id = goods.getGoods().getCategory1Id();
            if(category1Id!=null){
                dateModel.put("category1Name",itemCatService.findOne(category1Id).getName());
            }
            Long category2Id = goods.getGoods().getCategory2Id();
            if(category1Id!=null){
                dateModel.put("category2Name",itemCatService.findOne(category2Id).getName());
            }
            Long category3Id = goods.getGoods().getCategory3Id();
            if(category1Id!=null){
                dateModel.put("category3Name",itemCatService.findOne(category3Id).getName());
            }
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");
            FileWriter fileWriter = new FileWriter(ITEM_HTML_PATH+goodsId+".html");
            template.process(dateModel,fileWriter);
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/delete")
    @ResponseBody
    public String delete(Long[] goodsIds){
        try {
            if (goodsIds!=null && goodsIds.length>0){
                for (Long goodsId : goodsIds) {
                    File file = new File(ITEM_HTML_PATH+goodsId+".html");
                    if(file.exists()){
                        file.delete();
                    }
                }
            }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "/failure.html";
        }
    }




}
