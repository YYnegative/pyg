package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import com.pinyougou.sellergoods.service.GoodsService;
import com.pinyougou.service.impl.BaseServiceImpl;
import com.pinyougou.vo.Goods;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class GoodsServiceImpl extends BaseServiceImpl<TbGoods> implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsDescMapper goodsDescMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private SellerMapper sellerMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private ItemCatMapper itemCatMapper;

    @Override
    public PageInfo<TbGoods> search(Integer pageNum, Integer pageSize, TbGoods goods) {
        //设置分页
        PageHelper.startPage(pageNum, pageSize);
        //创建查询对象
        Example example = new Example(TbGoods.class);

        //创建查询条件对象
        Example.Criteria criteria = example.createCriteria();

        //模糊查询
        /**if (StringUtils.isNotBlank(goods.getProperty())) {
            criteria.andLike("property", "%" + goods.getProperty() + "%");
        }*/

        List<TbGoods> list = goodsMapper.selectByExample(example);
        return new PageInfo<>(list);
    }

    @Override
    public void addGoods(Goods goods) {
        //保存商品基本信息
        add(goods.getGoods());

        //保存商品描述信息
        goods.getGoodsDesc().setGoodsId(goods.getGoods().getId());

        goodsDescMapper.insertSelective(goods.getGoodsDesc());

        //保存商品sku列表
        if (goods.getItemList() != null && goods.getItemList().size() > 0) {
            for (TbItem tbItem : goods.getItemList()) {

                //标题：spu商品名称+当前这个sku的所有规格选项值拼接而成
                String title = goods.getGoods().getGoodsName();
                //将规格转换为对象，然后将其属性值拼接到标题中
                Map<String, String> specMap = JSON.parseObject(tbItem.getSpec(), Map.class);
                for (Map.Entry<String, String> entry : specMap.entrySet()) {
                    title += " " + entry.getValue();
                }
                tbItem.setTitle(title);

                //图片，获取spu的第1张图片
                if (StringUtils.isNotBlank(goods.getGoodsDesc().getItemImages())) {
                    List<Map> imageList = JSON.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);
                    if (imageList != null && imageList.size() > 0) {
                        tbItem.setImage(imageList.get(0).get("url").toString());
                    }
                }

                tbItem.setGoodsId(goods.getGoods().getId());
                //未审核
                tbItem.setStatus("0");
                //商家
                TbSeller seller = sellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId());
                tbItem.setSellerId(seller.getSellerId());
                tbItem.setSeller(seller.getName());
                //品牌
                TbBrand brand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
                tbItem.setBrand(brand.getName());

                //分类;使用商品spu中的第3级商品分类
                TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(goods.getGoods().getCategory3Id());
                tbItem.setCategoryid(itemCat.getId());
                tbItem.setCategory(itemCat.getName());

                tbItem.setCreateTime(new Date());
                tbItem.setUpdateTime(tbItem.getCreateTime());

                //保存商品sku
                itemMapper.insertSelective(tbItem);
            }
        }
    }

}
