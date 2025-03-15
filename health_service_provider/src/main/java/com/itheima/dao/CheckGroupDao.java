package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

/**
 * @author pc
 * 检查组数据层接口
 */
public interface CheckGroupDao {
    /**
     * 添加
     * @param checkGroup 检查组
     */
    public void add(CheckGroup checkGroup);

    /**
     * 检查组和检查项关系
     * @param map
     */
    public void setCheckGroupAndCheckItem(Map<String, Integer> map);

    /**
     * 分页查询
     * @param queryString 查询条件
     */
    public Page<CheckGroup> selectByCondition(String queryString);

    /**
     * 根据id查询
     * @param id 检查组id
     * @return
     */
    public CheckGroup findById(Integer id);

    /**
     * 根据检查组id查询检查项id
     * @param id
     * @return
     */
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    /**
     * 删除原有关联
     * @param id
     */
    public void deleteAssociation(Integer id);

    /**
     * 修改
     * @param checkGroup
     */
    public void edit(CheckGroup checkGroup);

    /**
     * 查询所有
     * @return
     */
    public List<CheckGroup> findAll();
}
