package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Menu;

import java.util.List;

/**
 * 菜单管理业务层接口
 */
public interface MenuService {
    /**
     * 分页查询
     *
     * @param queryPageBean
     * @return
     */
    public PageResult pageQuery(QueryPageBean queryPageBean);

    /**
     * 根据用户名查询菜单信息
     *
     * @param username
     * @return
     */
    public List<Menu> findSideMenus(String username);

    /**
     * 新增菜单
     *
     * @param menu
     */
    void add(Menu menu);

    /**
     * 查询所有的菜单
     *
     * @return
     */
    List<Menu> findAllMenu();

    /**
     * 查询子菜单
     *
     * @return
     */
    List<Menu> findOptions();

    /**
     * 删除菜单
     *
     * @param id
     */
    void delete(Integer id);

    /**
     * 根据菜单id查询菜单信息
     *
     * @param id
     * @return
     */
    Menu findById(Integer id);

    /**
     * 修改菜单信息
     * @param menu
     */
    void edit(Menu menu);
}
