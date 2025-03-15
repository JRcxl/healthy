package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Permission;

import java.util.List;

/**
 * 权限管理业务层接口
 */
public interface PermissionService {
    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    PageResult pageQuery(QueryPageBean queryPageBean);

    /**
     * 添加权限
     * @param permission
     * @return
     */
    void add(Permission permission);

    /**
     * 查询权限
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
     * 删除权限
     * @param id
     */
    void delete(Integer id);

    /**
     * 查询所有的权限
     * @return
     */
    List<Permission> findAllPermissions();

}
