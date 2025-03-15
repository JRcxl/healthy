package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.service.ReportService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * excel报表实现类
 *
 * @author pc
 */
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    /**
     * 获取综合性数据
     *
     * @return
     */
    @Override
    public Map<String, Object> exportBusinessReport() throws Exception {
        //1.创建一个map集合
        Map<String, Object> map = new HashMap<>();
        //2.准备今天 、 本周第一天 、 本月第一天
        String today = DateUtils.parseDate2String(new Date());
        String weekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        String firstDayOfMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
        //3.分别查询所有的数据

        //今日新增会员数
        Integer todayNewMember = memberDao.findMemberByDate(today);
        //总的会员数
        Integer totalMember = memberDao.findMemner();
        //本周新增会员数
        Integer thisWeekNewMember = memberDao.findMemberByAfterDate(weekMonday);
        //本月新增会员数
        Integer thisMonthNewMember = memberDao.findMemberByAfterDate(firstDayOfMonth);

        //今日预约数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(today);
        //本周预约数
        Integer thisWeekOrderNumber = orderDao.findOrderAfterByDate(weekMonday);
        //本月预约数
        Integer thisMonthOrderNumber = orderDao.findOrderAfterByDate(firstDayOfMonth);

        //今日到诊数
        Integer todayVisitsNumber = orderDao.findVisitesByDate(today);
        //本周到诊数
        Integer thisWeekVisitsNumber = orderDao.findVisitsByAfterDate(weekMonday);
        //本月到诊数
        Integer thisMonthVisitsNumber = orderDao.findVisitsByAfterDate(firstDayOfMonth);

        //热门套餐
        List<Map<String,Object>> hotSetmeal = orderDao.findHotSetmeal();

        //4.将所有的数据添加到map中
        map.put("reportDate",today);
        map.put("todayNewMember",todayNewMember);
        map.put("totalMember",totalMember);
        map.put("thisWeekNewMember",thisWeekNewMember);
        map.put("thisMonthNewMember",thisMonthNewMember);
        map.put("todayOrderNumber",todayOrderNumber);
        map.put("thisWeekOrderNumber",thisWeekOrderNumber);
        map.put("thisMonthOrderNumber",thisMonthOrderNumber);
        map.put("todayVisitsNumber",todayVisitsNumber);
        map.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        map.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        map.put("hotSetmeal",hotSetmeal);

        //5.返回结果
        return map;
    }
}
