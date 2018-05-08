package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import com.pinyougou.sellergoods.service.GoodsService;
import com.pinyougou.service.impl.BaseServiceImpl;
import com.pinyougou.vo.Goods;
import com.pinyougou.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service(interfaceClass = GoodsService.class,timeout = 600000)
@Transactional
public class GoodsServiceImpl extends BaseServiceImpl<TbGoods> implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsDescMapper goodsDescMapper;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ItemCatMapper itemCatMapper;
    @Autowired
    private SellerMapper sellerMapper;
    @Autowired
    private BrandMapper brandMapper;

    @Override
    public PageResult search(Integer page, Integer rows, TbGoods goods) {
        PageHelper.startPage(page, rows);

        Example example = new Example(TbGoods.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(goods.getSellerId())){
            criteria.andLike("sellerId", "%" + goods.getSellerId() + "%");
        }
        if(!StringUtils.isEmpty(goods.getAuditStatus())){
            criteria.andLike("auditStatus", "%" + goods.getAuditStatus() + "%");
        }
        if(!StringUtils.isEmpty(goods.getGoodsName())){
            criteria.andLike("goodsName", "%" + goods.getGoodsName() + "%");
        }

        List<TbGoods> list = goodsMapper.selectByExample(example);
        PageInfo<TbGoods> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 添加
     *
     * @param goods
     */
    @Override
    public void addGoods(Goods goods) {
        //1、保存基本信息
        goods.getGoods().setAuditStatus("0");
        add(goods.getGoods());

        //2、保存描述信息
        goods.getGoodsDesc().setGoodsId(goods.getGoods().getId());
        goodsDescMapper.insertSelective(goods.getGoodsDesc());

        //3、保存商品sku信息
        //判断是否启用了规格
        if ("1".equals(goods.getGoods().getIsEnableSpec())) {
            //启用规格需要处理一个个的sku
            saveItemList(goods);
        } else {
            //如果没有选择启用规格则将商品spu的基本信息生成一条sku商品信息保存到tb_item中
            TbItem item = new TbItem();
            item.setTitle(goods.getGoods().getGoodsName());
            setItem(item, goods);
            item.setPrice(goods.getGoods().getPrice());
            item.setNum(9999);
            item.setStatus("0");//审核通过以后再启用
            item.setIsDefault("1");//默认
            item.setNum(10);
            itemMapper.insertSelective(item);
        }

    }

    @Override
    public Goods findGoods(Long id) {
        Goods goods = new Goods();
        // 设置goods
        TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
        goods.setGoods(tbGoods);

        // 设置goodsDesc
        TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(id);
        goods.setGoodsDesc(tbGoodsDesc);

        // 设置itemCarList
        Example example = new Example(TbItem.class);
        example.createCriteria().andEqualTo("goodsId",id);
        List<TbItem> itemList = itemMapper.selectByExample(example);
        goods.setItemList(itemList);

        return goods;
    }

    /**
     * 保存商品sku列表
     * @param goods 商品
     */
    private void saveItemList(Goods goods) {
        if (goods.getItemList() != null && goods.getItemList().size() > 0) {
            for (TbItem item : goods.getItemList()) {

                //组装sku商品的标题；商品spu的名称+所有规格对应的值
                String title = goods.getGoods().getGoodsName();
                Map specMap = JSON.parseObject(item.getSpec(), Map.class);
                Set<Map.Entry<String, String>> set = specMap.entrySet();
                for (Map.Entry<String, String> entry : set) {
                    title += " " + entry.getValue();
                }
                item.setTitle(title);
                item.setNum(item.getStockCount());
                setItem(item, goods);

                itemMapper.insertSelective(item);
            }
        }
    }

    /**
     * 设置sku商品信息
     * @param item sku商品对象
     * @param goods 商品
     */
    private void setItem(TbItem item, Goods goods) {
        item.setCreateTime(new Date());
        item.setUpdateTime(item.getCreateTime());

        //商家
        TbSeller seller = sellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId());
        item.setSellerId(goods.getGoods().getSellerId());
        item.setSeller(seller.getName());

        item.setGoodsId(goods.getGoods().getId());

        //设置品牌
        TbBrand brand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
        item.setBrand(brand.getName());

        //设置商品分类
        item.setCategoryid(goods.getGoods().getCategory3Id());
        TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(item.getCategoryid());
        item.setCategory(itemCat.getName());

        //从商品描述的图片列表中选择第一个图片
        if(!StringUtils.isEmpty(goods.getGoodsDesc().getItemImages())) {
            List<Map> imageList = JSONArray.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);
            item.setImage(imageList.get(0).get("url").toString());
        }
    }

}