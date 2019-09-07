package com.pinyougou.seckill.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.common.util.IdWorker;
import com.pinyougou.common.util.RedisLock;
import com.pinyougou.mapper.SeckillGoodsMapper;
import com.pinyougou.mapper.SeckillOrderMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.seckill.service.SeckillOrderService;
import com.pinyougou.service.impl.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class SeckillOrderServiceImpl extends BaseServiceImpl<TbSeckillOrder> implements SeckillOrderService {
    //秒杀订单存放在redis中的键名key
    public static final String SECKILL_ORDERS = "SECKILL_ORDERS";
    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    @Override
    public PageInfo<TbSeckillOrder> search(Integer pageNum, Integer pageSize, TbSeckillOrder seckillOrder) {
        //设置分页
        PageHelper.startPage(pageNum, pageSize);
        //创建查询对象
        Example example = new Example(TbSeckillOrder.class);

        //创建查询条件对象
        Example.Criteria criteria = example.createCriteria();

        //模糊查询
        /**if (StringUtils.isNotBlank(seckillOrder.getProperty())) {
            criteria.andLike("property", "%" + seckillOrder.getProperty() + "%");
        }*/

        List<TbSeckillOrder> list = seckillOrderMapper.selectByExample(example);
        return new PageInfo<>(list);
    }

    @Override
    public String submitOrder(Long seckillGoodsId, String userId) throws InterruptedException {
        String outTradeNo = "";
        //添加分布式锁
        RedisLock redisLock = new RedisLock(redisTemplate);
        if(redisLock.lock(seckillGoodsId.toString())) {
            //1. 根据秒杀商品ID查询秒杀商品并判断合法性和库存
            TbSeckillGoods seckillGoods = (TbSeckillGoods) redisTemplate.boundHashOps(SeckillGoodsServiceImpl.SECKILL_GOODS).get(seckillGoodsId);
            //2. 库存减1
            seckillGoods.setStockCount(seckillGoods.getStockCount()-1);

            if(seckillGoods.getStockCount() > 0) {
                //3. 库存大于0的话；那么要更新redis中的秒杀商品库存
                redisTemplate.boundHashOps(SeckillGoodsServiceImpl.SECKILL_GOODS).put(seckillGoodsId, seckillGoods);
            } else {
                //4. 库存等于0的话；那么要将redis中的秒杀商品同步更新到mysql，将redis中的秒杀商品删除
                seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);
                redisTemplate.boundHashOps(SeckillGoodsServiceImpl.SECKILL_GOODS).delete(seckillGoodsId);
            }
            //   释放分布式锁
            redisLock.unlock(seckillGoodsId.toString());
            //5. 生成订单保存到redis
            TbSeckillOrder tbSeckillOrder = new TbSeckillOrder();
            outTradeNo = idWorker.nextId() + "";
            tbSeckillOrder.setId(outTradeNo);
            tbSeckillOrder.setCreateTime(new Date());
            tbSeckillOrder.setMoney(seckillGoods.getCostPrice().doubleValue());
            tbSeckillOrder.setSeckillId(seckillGoodsId);
            tbSeckillOrder.setSellerId(seckillGoods.getSellerId());
            //未支付
            tbSeckillOrder.setStatus("0");
            tbSeckillOrder.setUserId(userId);
            redisTemplate.boundHashOps(SECKILL_ORDERS).put(tbSeckillOrder.getId(), tbSeckillOrder);
        }
        //6. 返回秒~杀订单id
        return outTradeNo;
    }

}
