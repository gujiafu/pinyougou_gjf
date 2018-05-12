package com.pinyougou.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import com.pinyougou.sellergoods.service.GoodsService;
import com.pinyougou.vo.PageResult;
import com.pinyougou.vo.Result;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RequestMapping("/goods")
@RestController
public class GoodsController {

    @Reference
    private GoodsService goodsService;
    @Reference
    private ItemSearchService itemSearchService;



    @RequestMapping("/findAll")
    public List<TbGoods> findAll() {
        return goodsService.findAll();
    }

    @GetMapping("/findPage")
    public PageResult findPage(@RequestParam(value = "page", defaultValue = "1")Integer page,
                               @RequestParam(value = "rows", defaultValue = "10")Integer rows) {
        return goodsService.findPage(page, rows);
    }

    @PostMapping("/add")
    public Result add(@RequestBody TbGoods goods) {
        try {
            goodsService.add(goods);
            return Result.ok("增加成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("增加失败");
    }

    @GetMapping("/findOne")
    public TbGoods findOne(Long id) {
        return goodsService.findOne(id);
    }

    @PostMapping("/update")
    public Result update(@RequestBody TbGoods goods) {
        try {
            goodsService.update(goods);
            return Result.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("修改失败");
    }

    @GetMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            goodsService.deleteByIds(ids);
            // 从solr中删除itemList数据
            itemSearchService.deleteItemListFromSolr(Arrays.asList(ids));
            return Result.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("删除失败");
    }

    /**
     * 分页查询列表
     * @param goods 查询条件
     * @param page 页号
     * @param rows 每页大小
     * @return
     */
    @PostMapping("/search")
    public PageResult search(@RequestBody  TbGoods goods, @RequestParam(value = "page", defaultValue = "1")Integer page,
                               @RequestParam(value = "rows", defaultValue = "10")Integer rows) {
        return goodsService.search(page, rows, goods);
    }

    /**
     * 更改商品goods sku的状态
     * @param ids
     * @param status
     * @return
     */
    @PostMapping("/updateStatus")
    public Result updateStatus(Long[] ids,String status){
        try {
            goodsService.updateStatus(ids,status);

            // 修改solr中的数据
            if(status.equals("2")){
                List<TbItem> itemList = goodsService.findItemListByGoodsIdsAndStatus(ids,"1");
                if(itemList!=null && itemList.size()>0){
                    itemSearchService.addItemListToSolr(itemList);
                }
            }

            return Result.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("修改失败!");
        }
    }



}
