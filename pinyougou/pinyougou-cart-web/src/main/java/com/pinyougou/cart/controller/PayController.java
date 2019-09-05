package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pay.service.PayService;
import com.pinyougou.pojo.TbPayLog;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/pay")
@RestController
public class PayController {

    @Reference
    private OrderService orderService;

    @Reference
    private PayService payService;

    /**
     * 根据支付日志id 统一下单获取支付链接二维码地址
     * @param outTradeNo 支付日志id
     * @return 统一下单的结果（交易编号，操作结果，总金额，支付二维码链接地址）
     */
    @GetMapping("/createNative")
    public Map<String, String> createNative(String outTradeNo){
        try {
            //根据支付日志id 查询支付日志
            TbPayLog payLog = orderService.findPayLogByOutTradeNo(outTradeNo);

            if (payLog != null) {
                //调用支付系统 统一下单 方法
                return payService.createNative(outTradeNo, payLog.getTotalFee()+"");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }
}
