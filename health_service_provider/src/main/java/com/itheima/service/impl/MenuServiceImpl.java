package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.MenuDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Menu;
import com.itheima.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 菜单管理业务层接口
 */
@Service(interfaceClass = MenuService.class)
@Transactional
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuDao menuDao;

    /**
     * 分页查询
     *
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        //1.分别获取当前页，每页显示的条数、查询条件
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //2.获取总条数
        PageHelper.startPage(currentPage, pageSize);
        //3.调用dao层的方法
        Page<Menu> page = menuDao.findByCondition(queryString);
        long total = page.getTotal();
        List<Menu> result = page.getResult();
        //4.返回结果
        return new PageResult(total,result);
    }

    /**
     * 根据用户名查询菜单信息
     * @param username
     * @return
     */
    @Override
    public List<Menu> findSideMenus(String username) {
        try {
            //1.调用dao层的方法
            List<Menu> menus =  menuDao.findSideMenus(username);
            if (menus != null && menus.size() >  0 ){
                //2.遍历菜单，通过菜单获取到父菜单的id
                for (Menu menu : menus) {
                    Integer parentId = menu.getId();
                    //3.通过父id查询子菜单
                   List<Menu>  childrenMenus =  menuDao.findChildrenByParentId(parentId);
                   menu.setChildren(childrenMenus);
                }
            }
            return menus;
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("获取左侧菜单失败");
        }
    }

    /**
     * 添加菜单
     * @param menu
     */
    @Override
    public void add(Menu menu) {
        menuDao.add(menu);
    }

    /**
     * 查询所有的菜单
     * @return
     */
    @Override
    public List<Menu> findAllMenu() {
        List<Menu> list =  menuDao.findAllMenu();
        return list;
    }

    /**
     * 查询子菜单
     * @return
     */
    @Override
    public List<Menu> findOptions() {
        try {
            //1.调用dao层的方法,获取到父菜单
            Page<Menu> parent = menuDao.findParent(null);
            List<Menu> result = parent.getResult();
            //2.为了将子菜单变成父菜单
            Menu menu = new Menu();
            menu.setName("我是父菜单");
            result.add(menu);
            //3.返回结果
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("获取子菜单id失败");
        }
    }

    /**
     * 删除菜单
     * @param id
     */
    @Override
    public void delete(Integer id) {
        menuDao.delete(id);
    }

    /**
     * 根据id查询菜单信息
     * @param id
     * @return
     */
    @Override
    public Menu findById(Integer id) {
        return menuDao.findById(id);
    }

    /**
     * 修改菜单信息
     * @param menu
     */
    @Override
    public void edit(Menu menu) {
        menuDao.edit(menu);
    }
}
