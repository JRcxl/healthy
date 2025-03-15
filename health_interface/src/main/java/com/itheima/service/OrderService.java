package com.itheima.service;

import com.itheima.entity.Result;
import com.itheima.pojo.Order;

import java.util.List;
import java.util.Map;

/**
 * @author pc
 * 提交预约
 */
public interface OrderService {

    /**
     * 预约成功
     * @param map
     */
    public Result order(Map map) throws Exception;

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public Map findById(Integer id)throws Exception;


    /**
     * 查询本月到诊人数 未到诊人数
     * @param format
     * @return
     */
    List<String> findPeopleNumberByDate(String format);

    /**
     * 全年/每月 体检人数
     * @return
     */
    List<Integer> findYear();

    boolean processOrder(Order order);

    void confirmOrder(String txId);

    void sendSuccessNotification(Integer memberId);

    void markAsManual(String txId);

    void createPendingOrder(Order order);

    boolean isOrderExists(String txId);
}
