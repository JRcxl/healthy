package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Role;

import java.util.List;
import java.util.Set;

/**
 * 角色数据层接口
 */
public interface RoleDao {
    /**
     * 根据用户id查询多个角色
     * @param userId
     * @return
     */
    public Set<Role> findRoleByUserId(Integer userId);

    /**
     * 根据条件查询
     * @param queryString
     * @return
     */
    Page<Role> findByCondition(String queryString);

    /**
     * 添加用户
     * @param role
     */
    void addRole(Role role);

    /**
     * 添加角色和菜单之间的关系
     * @param roleId
     * @param menuId
     */
    void addRoleMenu(Integer roleId, Integer menuId);

    /**
     * 添加角色和权限的关系
     * @param roleId
     * @param permissionId
     */
    void addRolePermission(Integer roleId, Integer permissionId);

    /**
     * 根据角色id查询菜单id
     * @param id
     * @return
     */
    List<Integer> findMenuIdsByRoleId(Integer id);

    /**
     * 根据角色id查询所有的权限id
     * @param id
     * @return
     */
    List<Integer> findPermissionIdsByRoleId(Integer id);

    /**
     * 修改角色
     * @param role 角色对象
     */
    void update(Role role);

    /**
     * 根据角色id删除菜单id 中间表
     * @param roleId
     */
    void deleteMenuIdByRoleId(Integer roleId);

    /**
     * 根据角色id删除权限id 中间表
     * @param roleId
     */
    void deletePermissionIdByRoleId(Integer roleId);

    /**
     * 删除角色
     * @param id
     */
    void delete(Integer id);

    /**
     *根据角色id查询角色
     * @param id
     * @return
     */
    Role findRoleByRoleId(Integer id);

    /**
     * 查询所有的角色
     * @return
     */
    List<Role> findAll();

}
