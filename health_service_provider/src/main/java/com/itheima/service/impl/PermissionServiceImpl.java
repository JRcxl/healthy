package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.PermissionDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Permission;
import com.itheima.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 权限管理业务层实现类
 */
@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl implements PermissionService {


    @Autowired
    private PermissionDao permissionDao;

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        //1.分别获取当前页、每页显示的条数、查询条件
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //2.获取总条数
        PageHelper.startPage(currentPage,pageSize);
        //3.调用dao层的方法
        Page<Permission> page = permissionDao.findByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 添加权限
     * @param permission
     * @return
     */
    @Override
    public void add(Permission permission) {
         permissionDao.add(permission);
    }

    /**
     * 根据id查询权限
     * @param id
     * @return
     */
    @Override
    public Permission findById(Integer id) {
        return permissionDao.findById(id);
    }

    /**
     * 修改权限
     * @param permission
     */
    @Override
    public void edit(Permission permission) {
        permissionDao.edit(permission);
    }

    /**
     * 删除权限
     * @param id
     */
    @Override
    public void delete(Integer id) {
        //需要做一个判断，如果和角色有关联就不能删除
        Integer count = permissionDao.findCountByid(id);
        if (count > 0 ){
            throw  new RuntimeException("根据角色有关联不能删除");
        }
        permissionDao.delete(id);
    }

    /**
     * 查询所有的权限
     * @return
     */
    @Override
    public List<Permission> findAllPermissions() {
        List<Permission> list  = permissionDao.findAllPermissions();
        return list;
    }
}
