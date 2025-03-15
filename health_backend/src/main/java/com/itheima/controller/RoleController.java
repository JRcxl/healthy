package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Menu;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.service.MenuService;
import com.itheima.service.PermissionService;
import com.itheima.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色表现层
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Reference
    private RoleService roleService;
    @Reference
    private PermissionService permissionService;
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
        return roleService.pageQuery(queryPageBean);
    }

    /**
     * 查询所有的菜单和权限信息
     * @return
     */
    @PreAuthorize("hasAuthority('ROLE_QUERY')")
    @RequestMapping("/addShow")
    public Result addShow(){
        try {
            //1.调用业务层的查询所有权限和菜单的方法
            List<Permission> permissionList =  permissionService.findAllPermissions();
            List<Menu> menuList = menuService.findAllMenu();
            //2.创建一个map
            Map map = new HashMap();
            map.put("permissionList",permissionList);
            map.put("menuList",menuList);
            return new Result(true,"获取菜单和权限信息成功",map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"获取菜单和权限信息失败");
        }
    }

    /**
     * 添加角色
     * @return
     */
    @PreAuthorize("hasAuthority('ROLE_ADD')")
    @RequestMapping("/add")
    public Result add(@RequestBody Role role, Integer[] menuIds, Integer[] permissionIds ){
        try {
            roleService.add(role,menuIds,permissionIds);
            return new Result(true,"添加角色成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加角色失败");
        }
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('ROLE_QUERY')")
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            //1.角色用一个map来装
              Role role = roleService.findById(id);
          /*    //2.查询所有的菜单
            List<Menu> menuList = menuService.findAllMenu();
            //3.查询所有的权限
            List<Permission> permissionList = permissionService.findAllPermissions();
            //集合做非空判断
            if (menuList != null ){

            }
            //4.创建一个map集合
            Map hashMap= new HashMap();
            hashMap.put("menuList",menuList);
            hashMap.put("permissionList",permissionList);
            hashMap.put("permissionIds",map.get("permissionIds"));
            hashMap.put("menuIds",map.get("menuIds"));*/
            return new Result(true,"查询角色成功",role);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询角色失败");
        }
    }

    /**
     * 修改角色
     * @param role 角色对象
     * @param menuIds 菜单id
     * @param permissionIds 权限id
     * @return
     */
    @PreAuthorize("hasAuthority('ROLE_EDIT')")
    @RequestMapping("/update")
    public Result update(@RequestBody Role role, Integer[] menuIds, Integer[] permissionIds ){
        try {
            roleService.update(role,menuIds,permissionIds);
            return new Result(true,"修改角色成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"修改角色失败");
        }
    }

    /**
     * 删除角色
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('ROLE_DELETE')")
    @RequestMapping("delete")
    public Result delete(Integer id){
        try {
            roleService.delete(id);
            return new Result(true,"删除角色成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除角色失败");
        }
    }

    /**
     * 根据角色id查询菜单ids
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('ROLE_QUERY')")
    @RequestMapping("/findMenuIdsByRoleId")
    public Result findMenuIdsByRoleId(Integer id){
        try {
            List<Integer> menuIds = roleService.findMenuIdsByRoleId(id);
            return new Result(true,"查询所有的菜单id成功",menuIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询所有的菜单失败");
        }
    }


    /**
     * 根据角色id查询菜单ids
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('ROLE_QUERY')")
    @RequestMapping("/findPermissionIdsByRoleId")
    public Result findPermissionIdsByRoleId(Integer id){
        try {
            List<Integer> permissionIds = roleService.findPermissionIdsByRoleId(id);
            return new Result(true,"查询所有的菜单id成功",permissionIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询所有的菜单失败");
        }
    }
}
