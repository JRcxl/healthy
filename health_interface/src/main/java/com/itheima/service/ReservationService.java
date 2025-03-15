package com.itheima.service;

import com.itheima.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Map;

public interface ReservationService {


    Result order(Map map) throws Exception;


    void markAsFailed(int reservationId, String mark);
}
