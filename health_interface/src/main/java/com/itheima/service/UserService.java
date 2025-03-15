package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;

import java.util.List;

/**
 * 权限校验业务层接口
 */
public interface UserService {
    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    public User findUserByUsername(String username);

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult pageQuery(QueryPageBean queryPageBean);

    /**
     * 查询所有的角色
     * @return
     */
    List<Role> findAll();

    /**
     * 添加用户
     * @param user 用户对象
     * @param roleIds 角色对象
     */
    void add(User user, Integer[] roleIds);

    /**
     * 给用户添加角色
     * @param roleIds
     * @param userId
     */
    void addUserAndRole(Integer[] roleIds, Integer userId);

    /**
     * 根据用户id查询对应的角色
     * @param userId
     * @return
     */
    User findById(Integer userId);

    /**
     * 根据用户id查询角色id
     * @param userId
     * @return
     */
    List<Integer> findByIdUserAndRole(Integer userId);

    /**
     * 修改用户
     * @param user 用户
     * @param roleIds 角色ids
     */
    void edit(User user, Integer[] roleIds);

    /**
     * 删除用户
     * @param userId
     */
    void deleteUser(Integer userId);

    /**
     * 根据用户id查询对应的角色信息
     * @param userId
     * @return
     */
    List<Role> findRoleByUserId(Integer userId);
}

