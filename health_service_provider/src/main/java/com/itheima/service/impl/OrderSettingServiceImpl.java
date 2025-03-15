package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 文件上传
 * @author pc
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    //注入资源
    @Autowired
    private OrderSettingDao orderSettingDao;

    /**
     * 文件上传
     * @param orderSettings
     */
    @Override
    public void add(ArrayList<OrderSetting> orderSettings) {
        //1.对集合做非空判断
        if (orderSettings != null && orderSettings.size() > 0 ){
            //2.遍历集合
            for (OrderSetting orderSetting : orderSettings) {
                //3.调用dao层的根据日期是否存在的个数方法
                long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                //4.然后对象count进行判断
                if (count > 0){
                    //5.大于0 ，说明有数据，那就修改
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                }else {
                 //6.小于0，就说明没有数据，调用add方法
                 orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    /**
     * 根据日期查询预约设置信息
     * @param date
     * @return
     */
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        date = date.replaceAll("-(\\d)$", "-0$1");
        //1.获取年,月
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate firstOfMonth = LocalDate.parse(date + "-01", formatter);
        LocalDate lastOfMonth = firstOfMonth.withDayOfMonth(firstOfMonth.lengthOfMonth());

        // 打印 begin 和 end 来调试
        String begin = firstOfMonth.toString();
        String end = lastOfMonth.toString();
        System.out.println("begin: " + begin);
        System.out.println("end: " + end);

        //2. 创建一个 map 集合
        Map<String, String> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
         //3.调用dao层的方法
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
        List<Map> arrayList = new ArrayList<>();
        //4.对list集合做非空判断
        if (list != null && list.size() > 0 ){
            //5.对list集合进行遍历
            for (OrderSetting orderSetting : list) {
                //6.创建一个map集合
                HashMap hashMap = new HashMap();
                hashMap.put("date",orderSetting.getOrderDate().getDate());
                hashMap.put("number",orderSetting.getNumber());
                hashMap.put("reservations",orderSetting.getReservations());
                arrayList.add(hashMap);
            }
        }
        return arrayList;
    }

    /**
     * 修改预约设置
     * @param orderSetting
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {

        //1.调用根据日期查询预约数
        long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        //2.对数量进行判断
        if (count > 0 ){
            //3.大于0，删除
            orderSettingDao.editNumberByOrderDate(orderSetting);
            //orderSettingDao.editNumberByOrderDate(orderSetting);
        }else {
            //4.小于0，就重新添加
            orderSettingDao.add(orderSetting);
            System.out.println(orderSetting.getOrderDate());
        }
    }
}
