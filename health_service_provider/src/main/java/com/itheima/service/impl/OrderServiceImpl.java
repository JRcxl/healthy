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
import com.itheima.service.NotificationService;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author pc
 * 预约成功保存到数据库
 */
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

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

    @Autowired
    private RedisTemplate<String, Integer> redisTemplate;
    @Autowired
    private NotificationService notificationService;

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
        Order order = new Order(member.getId(),
                                        date,
                                        (String)map.get("oderType"),
                                        Order.ORDERSTATUS_NO,
                                        Integer.parseInt((String)map.get("setmealId")));
        orderDao.add(order);

        //查询本月到诊人数 未到诊人数，清除redis中数据
        jedisPool.getResource().del("findYear");

        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public Map findById(Integer id) throws Exception{
           Map map = orderDao.findById4Detail(id);
           if (map != null){
               Date orderDate = (Date) map.get("orderDate");
               map.put( "orderDate",DateUtils.parseDate2String(orderDate));
           }
        return map;
    }


    /**
     * 查询本月到诊人数 未到诊人数
     * @param format
     * @return
     */
    @Override
    public List<String> findPeopleNumberByDate(String format) {
        List<String> findYear = jedisPool.getResource().lrange("findYear", 0, -1);
        if(findYear!=null && findYear.size()>0){
            //redis中有数据,直接取并返回
            return findYear;

        }else {
            //查询
            List<Integer> orderStatus = orderDao.findPeopleNumberByDate(format+"-01");

            //存入redis中
            for (Integer data : orderStatus) {
                jedisPool.getResource().rpush("findYear",data.toString());
            }
            return jedisPool.getResource().lrange("findYear", 0, -1);
        }


    }


    /**
     * 全年/每月 体检人数
     * @return
     */
    @Override
    public List<Integer> findYear() {

        String yyyy = new SimpleDateFormat("yyyy").format(new Date());
        String mm = new SimpleDateFormat("yyyy-MM").format(new Date());

        //一年体检人数
        Integer y = orderDao.findVisitsCountAfterDate(new SimpleDateFormat("yyyy").format(new Date()) + "-01");
        //一个月体检人数
        Integer m = orderDao.findVisitsCountAfterDate(new SimpleDateFormat("yyyy-MM").format(new Date()) + "-01");
        List<Integer> list=new ArrayList<>();
        list.add(y);
        list.add(m);
        return list;
    }

    @Override
    public boolean processOrder(Order order) {
        return false;
    }

    @Override
    public void confirmOrder(String txId) {

    }

    @Override
    public void sendSuccessNotification(Integer memberId) {

    }

    @Override
    public void markAsManual(String txId) {

    }

    @Override
    public void createPendingOrder(Order order) {

    }

    @Override
    public boolean isOrderExists(String txId) {
        return false;
    }




}
