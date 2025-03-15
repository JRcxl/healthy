package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Menu;

import java.util.List;

/**
 * 菜单管理数据层接口
 */
public interface MenuDao {
    /**
     * 条件查询、分页查询
     * @param queryString
     * @return
     */
    Page<Menu> findByCondition(String queryString);

    /**
     * 通过用户名查询该用户父菜单
     * @param username
     * @return
     */
    public List<Menu> findSideMenus(String username);

    /**
     *通过父id查询子菜单
     * @param parentId
     * @return
     */
    public List<Menu> findChildrenByParentId(Integer parentId);

    /**
     * 添加菜单
     * @param menu
     */
    void add(Menu menu);

    /**
     * 查询所有的菜单
     * @return
     */
    List<Menu> findAllMenu();

    /**
     * 条件查询
     * @param queryString
     * @return
     */
    Page<Menu> findParent(String queryString);

    /**
     * 删除菜单
     * @param id
     */
    void delete(Integer id);

    /**
     * 根据菜单id查询菜单信息
     * @param id
     * @return
     */
    Menu findById(Integer id);

    /**
     * 修改菜单数据
     * @param menu
     */
    void edit(Menu menu);
}
