package com.pinyougou.pay.service;

import java.util.Map;

public interface PayService {
    /**
     * 根据支付日志id 统一下单获取支付链接二维码地址
     * @param outTradeNo 支付日志id
     * @param totalFee 支付总金额
     * @return 统一下单的结果（交易编号，操作结果，总金额，支付二维码链接地址）
     */
    Map<String, String> createNative(String outTradeNo, Long totalFee);
}
