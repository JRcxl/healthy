package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author pc
 * 文件上传数据层接口
 */
public interface OrderSettingDao {
    /**
     * 添加
     * @param orderSetting
     */
    public void add(OrderSetting orderSetting);

    /**
     * 根据日期修改预约人数
     * @param orderSetting
     */
    public void editNumberByOrderDate(OrderSetting orderSetting);

    /**
     * 根据日期查询所有人数
     * @param orderDate
     * @return
     */
    public long findCountByOrderDate(Date orderDate);

    /**
     * 根据日期查询预约信息
     * @param map
     * @return
     */
    public List<OrderSetting> getOrderSettingByMonth(Map map);


    /**
     * 根据日期查询预约
     * @param date 日期
     * @return
     */
    public OrderSetting findOrderSettingByOrderDate(Date date);

    /**
     * 根据日期修改已预约人数
     * @param orderSetting
     */
    public void editReservationsByOrderDate(OrderSetting orderSetting);
}
