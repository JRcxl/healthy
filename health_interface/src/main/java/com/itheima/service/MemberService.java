package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 会员业务层接口
 * @author pc
 */
public interface MemberService {
    /**
     * 根据电话查询会员
     * @param telephone
     * @return
     */
    public Member findByTelephone(String telephone);

    /**
     * 添加会员
     * @param member
     */
    public void add(Member member);

    /**
     * 根据月份查询会员数
     * @param list
     * @return
     */
    public List<Integer> findMemberByMonths(ArrayList<String> list);

    /**
     * 根据会员名获取会员信息
     * @param username
     * @return
     */
    Member findByUsername(String username);

    /**
     * 根据密码登录
     * @param name
     * @param password
     * @return
     */
    Member findByNameAndPassword(String name, String password);

    /**
     * 修改用户信息
     * @param member
     */
    void edit(Member member);

    /**
     * 根据email查询会员信息
     * @param email
     * @return
     */
    Member findByEmail(String email);


    /**
     * 性别占比
     * @return
     */
    List<Integer> findSex();


    /**
     * 统计年龄段占比 1-18  18-45  45-60  60以上
     * @return
     */
    Map<String,Integer> findAge();



    public PageResult pageQuery(QueryPageBean queryPageBean);

    public void deleteById(Integer id);



    public Member findById(Integer id);

    public List<Integer> findMemberCountByMonths(List<String> months);
}
