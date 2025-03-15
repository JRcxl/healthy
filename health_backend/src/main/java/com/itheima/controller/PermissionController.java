package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Permission;
import com.itheima.service.PermissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 权限管理表现层
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Reference
    private PermissionService permissionService;

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @PreAuthorize("hasAuthority('MENU_QUERY')")
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return permissionService.pageQuery(queryPageBean);
    }

    /**
     * 添加权限
     * @param permission
     * @return
     */
    @PreAuthorize("hasAuthority('MENU_ADD')")
    @RequestMapping("/add")
    public Result add(@RequestBody Permission permission){
        try {
            permissionService.add(permission);
            return new Result(true, "添加权限成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加权限失败！");
        }
    }

    /**
     *根据id查询权限
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('MENU_QUERY')")
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Permission permission = permissionService.findById(id);
            return new Result(true, "查询权限成功！",permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询权限失败！");
        }
    }

    /**
     * 修改权限
     * @return
     */
    @PreAuthorize("hasAuthority('MENU_EDIT')")
    @RequestMapping("/edit")
    public Result edit(@RequestBody Permission permission){
        try {
             permissionService.edit(permission);
            return new Result(true, "修改权限成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"修改权限失败！");
        }
    }

    /**
     * 删除权限
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('MENU_DELETE')")
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            permissionService.delete(id);
            return new Result(true, "删除权限成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除权限失败！");
        }
    }

    /**
     * 查询所有
     * @return
     */
    @RequestMapping("/findAll")
    public Result findAll(){
        try {
           List<Permission> list =  permissionService.findAllPermissions();
            return new Result(true,"查询权限成功",list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询权限失败");
        }
    }
}
