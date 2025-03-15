package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;

import java.util.List;
import java.util.Map;

/**
 * 权限校验数据层接口
 */
public interface UserDao {
    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    public User findUserByUsername(String username);

    /**
     * 根据条件查询
     * @param queryString
     * @return
     */
    Page<User> findByCondition(String queryString);

    /**
     * 添加用户
     * @param user
     */
    void add(User user);

    /**
     * 用户和角色之间的关联关系
     * @param map
     */
    void setUserAndRole(Map map);

    /**
     * 根据用户id查询所对应的角色
     * @param userId
     * @return
     */
    List<Role> findRoleByUserId(Integer userId);

    /**
     * 根据用户id查询角色id
     * @param userId
     * @return
     */
    List<Integer> findByIdUserAndRole(Integer userId);

    /**
     * 删除用户和角色的中间表
     * @param userId
     */
    void deleteUserAndRole(Integer userId);

    /**
     * 修改用户
     * @param user
     */
    void edit(User user);

    /**
     * 删除用户
     * @param userId
     */
    void deleteUser(Integer userId);

    /**
     * 查询用户基本信息
     * @param userId
     * @return
     */
    User findById(Integer userId);
}
