package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Role;

import java.util.List;

/**
 * 角色业务层
 */
public interface RoleService {
    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult pageQuery(QueryPageBean queryPageBean);

    /**
     * 添加角色
     * @param menuIds 菜单ids
     * @param permissionIds 权限ids
     * @param role
     */
    void add(Role role, Integer[] menuIds, Integer[] permissionIds);

    /**
     * 根据角色id查询
     * @param id
     * @return
     */
    Role findById(Integer id);

    /**
     * 修改角色
     * @param role 角色对象
     * @param menuIds 菜单id
     * @param permissionIds 权限id
     */
    void update(Role role, Integer[] menuIds, Integer[] permissionIds);

    /**
     * 删除角色成功
     * @param id
     */
    void delete(Integer id);

    /**
     * 根据角色id查询所有的菜单ids
     * @param id
     * @return
     */
    List<Integer> findMenuIdsByRoleId(Integer id);

    /**
     * 根据角色id查询所有的权限ids
     * @param id
     * @return
     */
    List<Integer> findPermissionIdsByRoleId(Integer id);
}
