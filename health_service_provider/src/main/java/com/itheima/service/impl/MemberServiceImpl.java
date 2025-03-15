package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.MemberDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import com.itheima.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author pc
 * 会员业务层实现类
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    /**
     * 根据电话查询会员
     * @param telephone
     * @return
     */
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findMemberByTelephone(telephone);
    }

    /**
     * 添加会员
     * @param member
     */
    @Override
    public void add(Member member) {
        //1.对密码进行加密
        String password = member.getPassword();
        if (password != null){
            member.setPassword(MD5Utils.md5(password));
        }
        //2.调用dao层的添加方法
        memberDao.add(member);
    }

    /**
     * 根据月份查询会员数
     * @param months
     * @return
     */
    @Override
    public List<Integer> findMemberByMonths(ArrayList<String> months) {
        //因为返回的是list集合
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (String month : months) {
             month = month + ".31";
        Integer count =  memberDao.findMemberByregTime(month);
        arrayList.add(count);
        }
        return arrayList;
    }

    /**
     * 根据会员名查询会员信息
     * @param username
     * @return
     */
    @Override
    public Member findByUsername(String username) {
        return memberDao.findMemberByUsername(username);
    }

    /**
     * 根据密码登录
     * @param name
     * @param password
     * @return
     */
    @Override
    public Member findByNameAndPassword(String name, String password) {
        return memberDao.findMemeberByNameAndPassword(name,password);
    }

    /**
     * 修改用户信息
     * @param member
     */
    @Override
    public void edit(Member member) {
        //1.应该对密码进行加密
//        String password = member.getPassword();
//        if (password != null && password.length() > 0 ){
//            member.setPassword(MD5Utils.md5(password));
//        }
        memberDao.edit(member);
    }

    /**
     * 根据邮箱查询会员信息
     * @param email
     * @return
     */
    @Override
    public Member findByEmail(String email) {
        return memberDao.findByEmail(email);
    }

    /**
     * 性别占比 1---男    2---女
     * @return
     */
    @Override
    public List<Integer> findSex() {

        return memberDao.findSex();
    }

    /**
     * 统计年龄段占比 1-18  18-45  45-60  60以上
     * @return
     */
    @Override
    public Map<String, Integer> findAge() {
        return memberDao.findAge();
    }



    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);
        Page<Member> page = memberDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void deleteById(Integer id) {
        memberDao.deleteById(id);
    }



    @Override
    public Member findById(Integer id) {
        return memberDao.findById(id);
    }

    @Override
    public List<Integer> findMemberCountByMonths(List<String> months) {
        List<Integer> memberCount = new ArrayList<>();
        for (String month : months) {
            //String date = month + ".31";
            String date = getLastDayOfMonth(month);
            System.out.println("data:"+date);
            Integer count = memberDao.findMemberCountBeforeDate(date);
            memberCount.add(count);
        }
        return memberCount;
    }

    private String getLastDayOfMonth(String month) {
        String[] parts = month.split("\\.");
        int year = Integer.parseInt(parts[0]);
        int monthNum = Integer.parseInt(parts[1]);

        // 判断每个月的最后一天
        int lastDay;
        switch (monthNum) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                lastDay = 31;  // 31天的月份
                break;
            case 4: case 6: case 9: case 11:
                lastDay = 30;  // 30天的月份
                break;
            case 2:
                // 判断闰年2月
                if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                    lastDay = 29;  // 闰年2月有29天
                } else {
                    lastDay = 28;  // 非闰年2月有28天
                }
                break;
            default:
                throw new IllegalArgumentException("无效的月份: " + monthNum);
        }

        // 返回最后一天的日期，格式为 "YYYY.MM.DD"
        return String.format("%04d.%02d.%02d", year, monthNum, lastDay);
    }

}
