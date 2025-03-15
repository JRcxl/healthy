package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.MenuDao;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Role;
import com.itheima.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色业务层实现类
 */
@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private PermissionDao permissionDao;
    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        //1.获取当前页、每页显示的条数、查询条件
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //2.获取总条数
        PageHelper.startPage(currentPage,pageSize);
        //3.调用dao层的方法
        Page<Role> roles = roleDao.findByCondition(queryString);
        //4.返回结果
        return new PageResult(roles.getTotal(),roles.getResult());
    }

    /**
     * 添加角色
     * @param menuIds 菜单ids
     * @param permissionIds 权限ids
     * @param role
     */
    @Override
    public void add(Role role, Integer[] menuIds, Integer[] permissionIds ) {
        //1.调用dao层的方法
        roleDao.addRole(role);
        Integer roleId = role.getId();
        //2.根据角色id和菜单id添加角色和菜单
        if (menuIds != null && menuIds.length > 0 ){
            for (Integer menuId : menuIds) {
                roleDao.addRoleMenu(roleId,menuId);
            }
        }
        //3.3.根据角色id和权限id添加角色和权限
        if (permissionIds != null && permissionIds.length > 0 ){
            for (Integer permissionId : permissionIds) {
                roleDao.addRolePermission(roleId,permissionId);
            }
        }
    }

    /**
     * 根据角色id查询
     * @param id
     * @return
     */
    @Override
    public Role findById(Integer id) {
       Role role =  roleDao.findRoleByRoleId(id);
        return role;
    }
    //    @Override
//    public Map findById(Integer id) {
//        //1.根据角色id查询所有的菜单id
//       List<Integer> MenuIds =  roleDao.findMenuIdsByRoleId(id);
//        //2.根据角色id查询所有的权限id
//        List<Integer>  PermissionIds = roleDao.findPermissionIdsByRoleId(id);
//        //3.创建一个map，存储菜单id和权限id
//        Map map = new HashMap();
//        map.put("PermissionIds",PermissionIds);
//        map.put("MenuIds",MenuIds);
//        return map;
    //}

    /**
     * 修改角色
     * @param role 角色对象
     * @param menuIds 菜单id
     * @param permissionIds 权限id
     */
    @Override
    public void update(Role role, Integer[] menuIds, Integer[] permissionIds) {
        //1.修改角色
        Integer roleId = role.getId();
        roleDao.update(role);
        //2.根据角色id修改菜单和角色表id
                //2.1先删除掉原有的
        roleDao.deleteMenuIdByRoleId(roleId);
                //2.2然后在添加
        if(menuIds != null && menuIds.length > 0 ){
            for (Integer menuId : menuIds) {
                roleDao.addRoleMenu(roleId,menuId);
            }
        }
        //3.根据角色id修改权限和角色表id
            //3.1先删除原有的
            roleDao.deletePermissionIdByRoleId(roleId);
        if (permissionIds != null && permissionIds.length > 0 ){
            for (Integer permissionId : permissionIds) {
                //3.2然后在添加
                roleDao.addRolePermission(roleId,permissionId);
            }
        }
    }

    /**
     * 删除角色
     * @param id
     */
    @Override
    public void delete(Integer id) {
        //1.先删除菜单之间的管理
        roleDao.deleteMenuIdByRoleId(id);
        //2.然后删除权限之间的管理
        roleDao.deletePermissionIdByRoleId(id);
        //3.最好删除角色
        roleDao.delete(id);
    }

    /**
     * 根据角色id查询菜单id
     * @param id
     * @return
     */
    @Override
    public List<Integer> findMenuIdsByRoleId(Integer id) {
        List<Integer> menuIds = roleDao.findMenuIdsByRoleId(id);
        return menuIds;
    }

    /**
     * 根据角色id查询所有的权限ids
     * @param id
     * @return
     */
    @Override
    public List<Integer> findPermissionIdsByRoleId(Integer id) {
        List<Integer> permissionIds = roleDao.findPermissionIdsByRoleId(id);
        return permissionIds;
    }
}
