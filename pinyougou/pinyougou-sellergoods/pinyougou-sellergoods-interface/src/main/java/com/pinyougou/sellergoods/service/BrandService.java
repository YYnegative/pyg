package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;

import java.util.List;

public interface BrandService {
    List<TbBrand> queryAll();

    /**
     * 根据分页参数分页查询品牌列表
     * @param pageNum 页号
     * @param pageSize 页大小
     * @return 品牌列表
     */
    List<TbBrand> testPage(Integer pageNum, Integer pageSize);
}
