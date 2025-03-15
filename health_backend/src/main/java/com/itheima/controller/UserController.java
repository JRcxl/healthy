package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Role;
import com.itheima.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 获取用户名
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private UserService userService;
    /**
     * 获取用户名
     * @return
     */
    @RequestMapping("/getUsername")
    public Result getUsername(){
        //1.不用查询数据库，可以通过框架的user来获取用户名
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return userService.pageQuery(queryPageBean);
    }

    /**
     * 查询所有的角色
      * @return
     */
    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<Role> list =  userService.findAll();
            return new Result(true,"查询所有用户成功",list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询所有用户失败");
        }
    }

    /**
     * 添加用户
     * @param user 用户对象
     * @param roleIds 角色ids
     * @return
     */
    @RequestMapping("/addUser")
    public Result addUser(@RequestBody com.itheima.pojo.User user, Integer[] roleIds){
        try {
            userService.add(user,roleIds);
            return new Result(true,"添加用户成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加用户失败");
        }
    }

    /**
     * 给用户添加角色
     * @param roleIds 角色ids
     * @param userId 用户id
     * @return
     */
    @RequestMapping("/addUserAndRole")
    public Result addUserAndRole(Integer userId , Integer[] roleIds ){
        try {
            userService.addUserAndRole(roleIds,userId);
            return new Result(true,"给用户添加角色成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"给用户添加角色失败");
        }
    }

    /**
     * 根据用户id查询角色id
     * @return
     */
    @RequestMapping("/findByIdUserAndRole")
    public Result findByIdUserAndRole(Integer userId){
        try {
           List<Integer> list =  userService.findByIdUserAndRole(userId);
            return new Result(true,"给用户添加角色成功",list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"给用户添加角色失败");
        }
    }

    /**
     * 回显用户基本信息
     * @param userId
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer userId ){
        try {
           com.itheima.pojo.User user =  userService.findById(userId);
            return new Result(true,"回显用户基本信息成功",user);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"回显用户基本信息失败");
        }
    }

    /**
     * 修改用户
     * @return
     */
    @RequestMapping("/editUser")
    public Result editUser(@RequestBody com.itheima.pojo.User user, Integer[] roleIds){
        try {
            userService.edit(user,roleIds);
            return new Result(true,"修改用户成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"修改用户失败");
        }
    }

    /**
     * 删除用户
     * @param userId
     * @return
     */
    @RequestMapping("/deleteUser")
    public Result deleteUser(Integer userId){
        try {
            userService.deleteUser(userId);
            return new Result(true,"删除用户成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"该用户有角色关联，需要先删除对应的角色");
        }
    }

    /**
     * 根据用户id查询对应的角色信息
     * @param userId
     * @return
     */
    @RequestMapping("/findRoleByUserId")
    public Result findRoleByUserId(Integer userId){
        try {
            List<Role> list =  userService.findRoleByUserId(userId);
            return new Result(true,"根据用户id查询对应的角色信息成功",list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"根据用户id查询对应的角色信息失败");
        }
    }
}
