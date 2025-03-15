package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.utils.SMSUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * 预约展示层
 * @author pc
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    //注入order service资源
    @Reference
    private OrderService orderService;
    //注入jedispool资源
    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private DefaultMQProducer mqProducer;

    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map){

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
             result = orderService.order(map);
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

    /**
     *根据id查询
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Map map = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }





}
