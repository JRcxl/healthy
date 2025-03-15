package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Permission;

import java.util.List;
import java.util.Set;

/**
 * 权限数据层的接口
 */
public interface PermissionDao {
    /**
     * 根据角色id查询权限
     * @param roleId
     * @return
     */
    public Set<Permission> findPermissionByRoleId(Integer roleId);

    /**
     * 根据条件查询
     * @param queryString
     * @return
     */
    Page<Permission> findByCondition(String queryString);

    /**
     * 添加权限
     * @param permission
     * @return
     */
    void add(Permission permission);

    /**
     * 根据id查询权限
     * @param id
     * @return
     */
    Permission findById(Integer id);

    /**
     * 修改权限
     * @param permission
     */
    void edit(Permission permission);

    /**
     * 根据权限id查询角色，是否存在
     * @param id
     * @return
     */
    Integer findCountByid(Integer id);

    /**
     * 删除权限
     * @param id
     */
    void delete(Integer id);

    /**
     * 查询所有权限
     * @return
     */
    List<Permission> findAllPermissions();

}
