package com.itheima.dao;
import org.apache.ibatis.annotations.Param;
import com.itheima.pojo.Order;

import java.util.List;
import java.util.Map;

/**
 * @author pc
 * 预约成功
 */
public interface OrderDao {
    /**
     * 条件查询
     * @param order
     * @return
     */
    public List<Order> findByCondition(Order order);

    /**
     * 添加会员
     * @param order
     */
    public void add(Order order);

    /**
     * 根据id查询体检人信息和体检套餐
     * @param id
     * @return
     */
    public Map findById4Detail(Integer id);

    /**
     * 今日预约数
     * @param today
     * @return
     */
    public Integer findOrderCountByDate(String today);

    /**
     * 本周和本月预约数
     * @param weekMonday
     * @return
     */
    public Integer findOrderAfterByDate(String weekMonday);

    /**
     * 今日到诊数
     * @param today
     * @return
     */
    public Integer findVisitesByDate(String today);

    /**
     * 本周和本月到诊数
     * @return
     */
    public Integer findVisitsByAfterDate(String weekMonday);

    /**
     * 热门套餐
     * @return
     */
    public List<Map<String, Object>> findHotSetmeal();


    /**
     * 查询本月到诊人数 未到诊人数
     * @param date
     * @return
     */
    List<Integer> findPeopleNumberByDate(String date);

    /**
     * 统计指定时间之后 到诊数
     * @param thisWeekMonday
     * @return
     */
    Integer findVisitsCountAfterDate(String thisWeekMonday);

    void updatemark(@Param("id") int id, @Param("mark") String mark);
}
