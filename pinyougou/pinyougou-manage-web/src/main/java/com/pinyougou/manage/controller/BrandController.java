package com.pinyougou.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import com.pinyougou.vo.PageResult;
import com.pinyougou.vo.Result;
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
    public List<TbBrand> findAll() {
        return brandService.findAll();
    }

    /**
     * 根据分页信息查询品牌列表
     *
     * @param page 页号
     * @param rows 页大小
     * @return 品牌列表
     */
    @GetMapping("/testPage")
    //@ResponseBody
    public List<TbBrand> testPage(Integer page, Integer rows) {
        return (List<TbBrand>) brandService.findPage(page, rows);
    }

    /**
     * 根据分页信息查询品牌列表
     *
     * @param page 页号
     * @param rows 页大小
     * @return 分页对象
     */
    @GetMapping("/findPage")
    //@ResponseBody
    public PageResult findPage(Integer page, Integer rows) {
        return brandService.findPage(page, rows, null);
    }

    /**
     * 新增品牌
     *
     * @param brand 品牌信息
     * @return 操作结果
     */
    @PostMapping("/add")
    public Result add(@RequestBody TbBrand brand) {
        try {
            brandService.add(brand);
            return Result.ok("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("新增失败！");
    }

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 实体类
     */
    @GetMapping("/findOne")
    public TbBrand findOne(Long id) {
        return brandService.findOne(id);
    }

    /**
     * 修改品牌
     *
     * @param brand 品牌信息
     * @return 操作结果
     */
    @PostMapping("/update")
    public Result update(@RequestBody TbBrand brand) {
        try {
            brandService.update(brand);
            return Result.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("修改失败！");
    }

    /**
     * 批量删除记录
     * @param ids id集合
     * @return 操作结果
     */
    @GetMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            brandService.deleteByIds(ids);
            return Result.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("删除失败");
    }

    /**
     * 根据条件分页查询
     *
     * @param brand 品牌信息
     * @param page 页号
     * @param rows 页大小
     * @return 分页对象
     */
    @PostMapping("/search")
    public PageResult search(@RequestBody TbBrand brand, Integer page, Integer rows) {
        return brandService.search(brand, page, rows);
    }

}
