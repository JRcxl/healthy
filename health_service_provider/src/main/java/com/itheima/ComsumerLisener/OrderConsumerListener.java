package com.itheima.ComsumerLisener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.itheima.entity.Result;
import com.itheima.service.ReservationService;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.MessageExt;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderConsumerListener implements MessageListenerConcurrently {
    private static final Logger log = LoggerFactory.getLogger(OrderConsumerListener.class);

    @Autowired
    private DefaultMQProducer mqProducer;


    private ReservationService reservationService;

    public void setMqProducer(DefaultMQProducer mqProducer) {
        this.mqProducer = mqProducer;
    }

    public void setReservationService(ReservationService reservationService) {  //注入的xml采用实现类注入，这里set方法又赋给接口
        this.reservationService = reservationService;
    }

    private static final int MAX_RETRY = 3;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(
            List<MessageExt> messages,
            ConsumeConcurrentlyContext context
    ) {
        for (MessageExt message : messages) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
                String jsonStr = new String(message.getBody(), StandardCharsets.UTF_8);
                Map<String, Object> msgMap = mapper.readValue(jsonStr, new TypeReference<Map<String, Object>>() {});

                Result result = reservationService.order(msgMap);
                boolean success = result.isFlag();
                if (!success) {
                    handleFailedReservation((Integer) result.getData(), message);
                }
            } catch (JsonProcessingException e) {
                // 不可重试异常：JSON解析失败（数据格式错误）
                log.error("消息反序列化失败，数据格式错误，无需重试。MsgId: {}, Body: {}", message.getMsgId(), new String(message.getBody()), e);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            } catch (SQLException e) {
                // 数据库错误分类处理
                if (isRetryableSqlError(e)) {
                    // 可重试错误（如连接超时、死锁）
                    log.warn("数据库临时错误，触发重试。MsgId: {}", message.getMsgId(), e);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                } else {
                    // 不可重试错误（如字段不匹配、唯一键冲突）
                    log.error("数据库持久性错误，无需重试。MsgId: {}, SQLState: {}", message.getMsgId(), e.getSQLState(), e);
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            } catch (RemoteAccessException e) {
                // 网络或远程服务错误（可重试）
                log.warn("远程服务调用失败，稍后重试。MsgId: {}", message.getMsgId(), e);
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            } catch (Exception e) {
                // 其他未知异常默认重试
                log.error("未知异常，按默认策略不重试。MsgId: {}", message.getMsgId(), e);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        }
        System.out.println("成功处理消息");
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    private void handleFailedReservation(int id,MessageExt message) throws JsonProcessingException {

        String jsonStr = new String(message.getBody(), StandardCharsets.UTF_8);
        Map<String, Object> msgMap = new ObjectMapper().readValue(jsonStr, new TypeReference<Map<String, Object>>() {});

        int retryCount = message.getReconsumeTimes();
        if (retryCount >= MAX_RETRY) {
            reservationService.markAsFailed(id,"'FAILED");
        } else {
            // 动态计算下一次延迟时间（指数退避）
            int delayLevel = 3 + retryCount; // 延迟级别对应时间可自定义
            sendRetryMessage(msgMap, delayLevel);
        }
    }

    private void sendRetryMessage(Map map, int delayLevel) {
        try {
            String jsonStr = new ObjectMapper().writeValueAsString(map);
            Message retryMsg = new Message("RESERVATION_TOPIC",
                    jsonStr.getBytes(StandardCharsets.UTF_8));
            retryMsg.setDelayTimeLevel(delayLevel);
           mqProducer.send(retryMsg);
        } catch (Exception e) {
           System.out.println("预约失败");
        }
    }

    private boolean isRetryableSqlError(SQLException e) {
        // 可重试的 SQL 状态码（需根据数据库类型调整）
        String[] retryableSqlStates = {
                "08S01",  // 通信链路失败（如网络中断）
                "40001",   // 死锁（MySQL、DB2、SQL Server）
                "40P01",   // 死锁（PostgreSQL）
                "HYT00",   // 超时（如执行超时）
                "HY000"    // 通用错误（需根据实际场景判断）
        };
        String sqlState = e.getSQLState();
        // 空值检查，避免 NullPointerException
        if (sqlState == null) {
            return false; // 无法识别状态码，默认不重试
        }
        return Arrays.asList(retryableSqlStates).contains(sqlState);
    }




}