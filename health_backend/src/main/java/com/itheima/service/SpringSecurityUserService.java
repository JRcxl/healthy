package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Set;

/**
 * 权限认证
 * @author pc
 */
@Component
public class SpringSecurityUserService implements UserDetailsService {
    /**
     * dubbo远程通过网络调用
     */
    @Reference
    private UserService userService;

    /**
     * 实现权限认证
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //1.通过用户名查询用户
       User user =  userService.findUserByUsername(username);
        if (user == null){
            //说明没有这个用户
            return  null;
        }
        System.out.println("查询中》》》");
        //2.创建一个list集合
        ArrayList<GrantedAuthority> arrayList = new ArrayList<>();
        //3.从查询出来的用户中获取角色
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            //4.给角色授权
            arrayList.add(new SimpleGrantedAuthority(role.getKeyword()));
            //5.从角色中获取权限
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                //6.给权限授权
                arrayList.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }

        //7.将密码交给 security 进行校验
        org.springframework.security.core.userdetails.User securityUser = new org.springframework.security.core.userdetails.User(username,user.getPassword(),arrayList);

        return securityUser;
    }
}
