package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.ReservationService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = ReservationService.class)
@Transactional
public class ReservationServiceImpl implements ReservationService {
    /**
     * 注入dao层预约资源
     */
    @Autowired
    private OrderDao orderDao;
    /**
     * 注入dao层预约设置资源
     */
    @Autowired
    private OrderSettingDao orderSettingDao;
    /**
     * 注入dao层会员资源
     */
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private JedisPool jedisPool;

    /**
     * 预约
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public Result order(Map map) throws Exception {
        //1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
        //根据日期查询是否有这个用户

        String orderDate = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingDao.findOrderSettingByOrderDate(date);
        //对其进行判断，如果为空说明没有预约过
        if (orderSetting == null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        //调用dao层方法，根据日期查看预约数
        int number = orderSetting.getNumber();//可预约人数
        int reservations = orderSetting.getReservations();//已预约人数
        if (reservations >= number){
            //说明预约满了
            return new Result(false,MessageConstant.ORDER_FULL);
        }
        //3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
        //调用dao层方法，根据电话查看是否为会员
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findMemberByTelephone(telephone);
        if (member != null){
            //说明数据库已经有了这个用户，就说明是会员
            Integer memberId = member.getId();
            int setmealId = Integer.parseInt((String) map.get("setmealId"));
            Order order = new Order(memberId,date,null,null,setmealId);
            List<Order> list = orderDao.findByCondition(order);
            if (list != null && list.size() > 0 ){
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }else {
            //4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
            orderSetting.setReservations(reservations + 1);
            orderSettingDao.editReservationsByOrderDate(orderSetting);

            //不是会员
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }
        //5、预约成功，向预约表中保存预约数据，并更新当日的已预约人数
        Order order = new Order(
                member.getId(),
                date,
                (String)map.get("oderType"),
                Order.ORDERSTATUS_NO,
                Integer.parseInt((String)map.get("setmealId")),
                "SUCCESS");
        orderDao.add(order);

        //查询本月到诊人数 未到诊人数，清除redis中数据
        jedisPool.getResource().del("findYear");

        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }



       @Override
        public void markAsFailed(int reservationId,String mark) {

            orderDao.updatemark(reservationId,mark);
        }



}
