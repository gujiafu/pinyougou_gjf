package com.pinyougou.search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author GuJiaFu
 * @date 2018/5/10
 */
@RequestMapping("/itemSearch")
@RestController
public class ItemSearchController {

    @Reference
    private ItemSearchService itemSearchService;

    @PostMapping("/search")
    public Map<String,Object> search(@RequestBody Map<String,Object> searchMap){
        return itemSearchService.search(searchMap);
    }

}
