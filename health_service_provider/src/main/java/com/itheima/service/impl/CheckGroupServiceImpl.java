package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pc
 * 检查组实现类
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;
    /**
     * 添加
     * @param checkGroup 检查组对象
     * @param checkitemIds 检查项ids
     */
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //调用dao层的方法
        checkGroupDao.add(checkGroup);
        Integer checkGroupId = checkGroup.getId();
        //建立表关系
        if (checkitemIds != null && checkitemIds.length > 0){
            this.setCheckGroupAndCheckItem(checkGroupId,checkitemIds);
        }
    }

    /**
     * 建立表关系
     */
    public void setCheckGroupAndCheckItem(Integer checkGroupId,Integer[] checkitemIds){
        if (checkitemIds != null && checkitemIds.length > 0 ){
            for (Integer checkitemId : checkitemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkgroupId",checkGroupId);
                map.put("checkitemId",checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }

    /**
     * 分页查询
     * @param currentPage 当前页
     * @param pageSize 每页显示的条数
     * @param queryString 查询条件
     * @return
     */
    @Override
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
        //获取总条数
        PageHelper.startPage(currentPage,pageSize);
        //调用dao层的查询方法
        Page<CheckGroup> checkGroups = checkGroupDao.selectByCondition(queryString);
        long total = checkGroups.getTotal();
        List<CheckGroup> result = checkGroups.getResult();
        return new PageResult(total,result);
    }

    /**
     * 根据id查询
     * @param id 检查组id
     * @return
     */
    @Override
    public CheckGroup findById(Integer id) {
      return  checkGroupDao.findById(id);
    }

    /**
     * 根据检查组id查询检查项id
     * @param id 检查组id
     * @return
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    /**
     * 修改
     * @param checkGroup 检查组
     * @param checkitemIds  检查项
     */
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        //先删除原来的关联
        checkGroupDao.deleteAssociation(checkGroup.getId());
        //然后进行修改
        checkGroupDao.edit(checkGroup);
        Integer id = checkGroup.getId();
        //再添加关联
        this.setCheckGroupAndCheckItem(id,checkitemIds);
    }

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }
}
