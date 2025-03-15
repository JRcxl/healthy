package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Member;

import java.util.List;
import java.util.Map;

/**
 * @author pc
 * 根据日期查看是否为会员
 */
public interface MemberDao {
    /**
     * 根据日期查看是否为会员
     * @param telephone
     * @return
     */
    public Member findMemberByTelephone(String telephone);

    /**
     * 添加不是会员完成自动注册
     * @param member
     */
    public void add(Member member);

    /**
     * 根据日期查询会员数
     * @param month
     * @return
     */
    public Integer findMemberByregTime(String month);

    /**
     * 今日新增会员数
     * @param today
     * @return
     */
    public Integer findMemberByDate(String today);

    /**
     * 总的会员数
     * @return
     */
    public Integer findMemner();

    /**
     * 本周新增会员数
     * @param weekMonday
     * @return
     */
    Integer findMemberByAfterDate(String weekMonday);

    /**
     * 根据会员名查询会员名
     * @param username
     * @return
     */
    Member findMemberByUsername(String username);

    Member findMemeberByNameAndPassword(String name, String password);

    /**
     * 修改会员信息
     * @param member
     */
    void edit(Member member);

    /**
     * 根据邮箱查询会员信息
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

    Page<Member> selectByCondition(String queryString);

    void deleteById(Integer id);

    Member findById(Integer id);

    Integer findMemberCountBeforeDate(String date);
}
