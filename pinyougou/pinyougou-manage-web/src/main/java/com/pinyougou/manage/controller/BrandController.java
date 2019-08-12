package com.pinyougou.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/brand")
@RestController
public class BrandController {

    @Reference(timeout = 3000)
    private BrandService brandService;

    /**
     * 根据分页参数分页查询品牌列表
     * @param pageNum 页号
     * @param pageSize 页大小
     * @return 品牌列表
     */
    @GetMapping("/testPage")
    public List<TbBrand> testPage(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        return brandService.testPage(pageNum, pageSize);
    }

    /**
     * 查询品牌列表
     *
     * @return 品牌列表
     */
    @GetMapping("/findAll")
    public List<TbBrand> findAll() {
        return brandService.queryAll();
    }
}
