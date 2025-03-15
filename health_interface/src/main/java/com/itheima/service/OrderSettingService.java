package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文件上传业务层接口
 * @author pc
 */
public interface OrderSettingService {
    /**
     * 文件上传
     * @param orderSettings
     */
    public void add(ArrayList<OrderSetting> orderSettings);

    /**
     * 根据日期查询预约设置信息
     * @param date
     * @return
     */
    public List<Map> getOrderSettingByMonth(String date);

    /**
     * 修改预约设置
     * @param orderSetting
     */
    public void editNumberByDate(OrderSetting orderSetting);
}
