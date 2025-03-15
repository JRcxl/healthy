package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import com.itheima.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 根据用户名查询用户
 */
@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    /**
     * 查询用户
     * @param username
     * @return
     */
    @Override
    public User findUserByUsername(String username) {

        //1.调用dao层的方法，查询用户
       User user =  userDao.findUserByUsername(username);
       if (user == null ){
           return  null;
       }
           Integer userId = user.getId();
           //2.根据用户id查询查询角色
           Set<Role> roles = roleDao.findRoleByUserId(userId);
          if (roles != null && roles.size() > 0 ){
              for (Role role : roles) {
                  //3.跟角色id查询权限
                  Integer roleId = role.getId();
                Set<Permission> permissions =  permissionDao.findPermissionByRoleId(roleId);
                if (permissions != null && permissions.size() > 0){
                    role.setPermissions(permissions);
                 }
              }
              user.setRoles(roles);
          }
        return user;
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        //1.分别获取当前页、每页显示的条数、查询条件
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //2.获取总条数
        PageHelper.startPage(currentPage,pageSize);
        //3.调用dao层的查询方法
       Page<User> page =  userDao.findByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 查询所有的角色
     * @return
     */
    @Override
    public List<Role> findAll() {
       List<Role> list =  roleDao.findAll();
        return list;
    }

    /**
     * 添加用户
     * @param user 用户对象
     * @param roleIds 角色对象
     */
    @Override
    public void add(User user, Integer[] roleIds) {
        //1.需要对用户密码进行加密
        if (user != null){
            String password = user.getPassword();
            if (password != null && password.length() > 0){
                user.setPassword(MD5Utils.md5(password));
            }
        }
        //2.添加用户
        userDao.add(user);
        Integer userId = user.getId();
        //3.对传过来的角色id进行判断
        if (roleIds != null && roleIds.length > 0 ){
            //添加用户和角色之间的关联
            this.setUserAndRole(userId,roleIds);
        }
    }

    /**
     * 用户和角色之间的关联
     * @param userId
     * @param roleIds
     */
    private void setUserAndRole(Integer userId, Integer[] roleIds) {
        //1.先做非空判断
        if (roleIds != null && roleIds.length > 0 ){
            //2.遍历 角色id
            for (Integer roleId : roleIds) {
                //3.创建一个map集合
                Map map = new HashMap();
                map.put("userId",userId);
                map.put("roleId",roleId);
                //4.调用dao层的方法
                userDao.setUserAndRole(map);
            }
        }
    }

    /**
     *给用户添加角色
     * @param roleIds
     * @param userId
     */
    @Override
    public void addUserAndRole(Integer[] roleIds, Integer userId) {
        if (roleIds != null && roleIds.length > 0 ){
            this.setUserAndRole(userId,roleIds);
        }
    }

    /**
     * 根据用户id查询对应的角色
     * @param userId
     * @return
     */
    @Override
    public User findById(Integer userId) {
       return userDao.findById(userId);
    }

    /**
     * 根据用户id查询角色id
     * @param userId
     * @return
     */
    @Override
    public List<Integer> findByIdUserAndRole(Integer userId) {
        List<Integer> list = userDao.findByIdUserAndRole(userId);
        if (list != null){
            return list;
        }
        return null;
    }

    /**
     * 修改用户
     * @param user 用户
     * @param roleIds 角色ids
     */
    @Override
    public void edit(User user, Integer[] roleIds) {
        //1.先删除中间表
        Integer userId = user.getId();
        userDao.deleteUserAndRole(userId);
        //2.然后添加到中间表
        if (roleIds != null && roleIds.length > 0 ){
            this.setUserAndRole(userId,roleIds);
        }
        //3.进行修改
        userDao.edit(user);
    }

    /**
     * 删除用户
     * @param userId
     */
    @Override
    public void deleteUser(Integer userId) {
        //1.先去根据用户id查询对应的角色，如果有就不能删除，没有就删除
        List<Role> list = userDao.findRoleByUserId(userId);
        if (list != null && list.size() > 0 ){
            //说明有数据，不能删除
            throw new RuntimeException("跟角色有关联，如需删除，请先删除对应的角色");
        }else {
        //2.然后在删除基本信息
        userDao.deleteUser(userId);
        }
    }

    /**
     * 根据用户id查询对应的角色信息
     * @param userId
     * @return
     */
    @Override
    public List<Role> findRoleByUserId(Integer userId) {
        return userDao.findRoleByUserId(userId);
    }
}
