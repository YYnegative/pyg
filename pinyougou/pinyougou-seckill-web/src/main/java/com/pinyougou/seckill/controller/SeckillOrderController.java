package com.pinyougou.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.seckill.service.SeckillOrderService;
import com.pinyougou.vo.Result;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/seckillOrder")
@RestController
public class SeckillOrderController {

    @Reference
    private SeckillOrderService seckillOrderService;

    /**
     * 新增
     * @param seckillOrder 实体
     * @return 操作结果
     */
    @PostMapping("/add")
    public Result add(@RequestBody TbSeckillOrder seckillOrder){
        try {
            seckillOrderService.add(seckillOrder);

            return Result.ok("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("新增失败");
    }

    /**
     * 根据主键查询
     * @param id 主键
     * @return 实体
     */
    @GetMapping("/findOne/{id}")
    public TbSeckillOrder findOne(@PathVariable String id){
        return seckillOrderService.findOne(id);
    }

    /**
     * 修改
     * @param seckillOrder 实体
     * @return 操作结果
     */
    @PostMapping("/update")
    public Result update(@RequestBody TbSeckillOrder seckillOrder){
        try {
            seckillOrderService.update(seckillOrder);
            return Result.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("修改失败");
    }

    /**
     * 根据主键数组批量删除
     * @param ids 主键数组
     * @return 实体
     */
    @GetMapping("/delete")
    public Result delete(String[] ids){
        try {
            seckillOrderService.deleteByIds(ids);
            return Result.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("删除失败");
    }

    /**
     * 根据条件搜索
     * @param pageNum 页号
     * @param pageSize 页面大小
     * @param seckillOrder 搜索条件
     * @return 分页信息
     */
    @PostMapping("/search")
    public PageInfo<TbSeckillOrder> search(@RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                           @RequestBody TbSeckillOrder seckillOrder) {
        return seckillOrderService.search(pageNum, pageSize, seckillOrder);
    }

}
