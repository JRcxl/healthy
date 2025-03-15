package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.service.ReservationService;
import com.itheima.utils.SMSUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

import org.apache.rocketmq.common.message.Message;

// Java
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/order")
public class ReservationController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Reference
    private OrderService orderService;
    @Reference
    private ReservationService reservationService;

    //注入jedispool资源
    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private DefaultMQProducer mqProducer;

    @RequestMapping("/reservation")
    public Result createReservation(@RequestBody Map map) {
        //1.从redis里面取出手机号
        String telephone = (String) map.get("telephone");
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        String validateCode = (String) map.get("validateCode");
        //2.进行比对
        if (codeInRedis != null && !codeInRedis.equals(validateCode)){
            //验证码比对失败，给出提示
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //3.比对成功，预约成功
        //调用service层资源
        Result result = null ;
        try {
            //获取提交类型
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            // 序列化为 JSON 字符串
            String jsonStr = new ObjectMapper().writeValueAsString(map); // 使用 Jackson
            //result = orderService.order(map);                           //这里先不执行本地事务

            // 1. 生成预约记录（初始状态为处理中）
            //String reservationId= (String) result.getData();
            // 2. 发送延迟消息（延迟级别3对应10秒）
            Message msg = new Message("RESERVATION_TOPIC",
                    jsonStr.getBytes(StandardCharsets.UTF_8));
            msg.setDelayTimeLevel(3); // RocketMQ预设延迟级别
            SendResult sendResult=mqProducer.send(msg);
            String msgId = sendResult.getMsgId();

            // 存储到 Redis，设置过期时间（例如 24 小时）
            String redisKey = "msg:" + msgId;
            redisTemplate.opsForValue().set(redisKey, "SENT", 5, TimeUnit.MINUTES);
            System.out.println("消息发送成功！MsgId: " + sendResult.getMsgId() + ", Topic: " + sendResult.getMessageQueue().getTopic());

        } catch (Exception e) {
            e.printStackTrace();
            //预约失败
            return result;
        }
        //4.比对失败，预约失败
        if (result.isFlag()){
            //发送短信
            String orderDate = (String) map.get("orderDate");
            try {
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,orderDate);
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
        return result ;

    }
}
