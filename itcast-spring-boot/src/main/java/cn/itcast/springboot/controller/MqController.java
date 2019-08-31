package cn.itcast.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/mq")
@RestController
public class MqController {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @GetMapping("/sendMsg")
    public String sendMQMsg(){
        try {
            Map<String, Object> map = new HashMap<String,Object>();
            map.put("id", 123L);
            map.put("name", "传智播客");

            jmsMessagingTemplate.convertAndSend("spring.boot.queue", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "发送MQ消息成功！发送的队列为spring.boot.queue";
    }
}
