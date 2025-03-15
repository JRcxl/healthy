package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Menu;
import com.itheima.service.MenuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 菜单
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Reference
    private MenuService menuService;

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @PreAuthorize("hasAuthority('MENU_QUERY')")
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return  menuService.pageQuery(queryPageBean);
    }

    /**
     * 动态查询菜单
     * @return
     */
    @PreAuthorize("hasAuthority('MENU_QUERY')")
    @RequestMapping("/findMenus")
    public Result findMenus(){
        try {
            //1.通过框架获取用户名
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            //2.调用service层的方法，来查询前台需要的信息
            List<Menu> list = menuService.findSideMenus(username);
            return new Result(true, MessageConstant.GET_MENU_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MENU_FAIL);
        }
    }


    /**
     * 添加菜单
     * @return
     */
    @PreAuthorize("hasAuthority('MENU_ADD')")
    @RequestMapping("/add")
    public Result add(@RequestBody Menu menu){
        try {
            menuService.add(menu);
            return new Result(true,"新增菜单成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"新增菜单失败");
        }
    }

    /**
     * 查询所有
     * @return
     */
    @PreAuthorize("hasAuthority('MENU_QUERY')")
    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<Menu> menuList = menuService.findAllMenu();
            return new Result(true,"查询菜单成功",menuList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询菜单失败");
        }
    }

    /**
     * 条件查询子菜单
     * @return
     */
    @RequestMapping("/findOptions")
    public Result findOptions(){
        try {
            List<Menu> list = menuService.findOptions();
            return new Result(true,"查询子菜单成功",list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询子菜单失败");
        }
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            menuService.delete(id);
            return new Result(true,"删除菜单成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除菜单失败");
        }
    }

    /**
     * 根据菜单id查询所有的菜单
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Menu menu =  menuService.findById(id);
            return new Result(true,"查询菜单信息成功",menu);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询菜单信息失败");
        }
    }

    /**
     * 修改菜单信息
     * @param menu
     * @return
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody Menu menu){
        try {
            menuService.edit(menu);
            return new Result(true,"修改菜单成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"修改菜单失败");
        }
    }
}
