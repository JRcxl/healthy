package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckGroup;

import java.util.List;

/**
 * @author pc
 * 检查组业务层接口
 */
public interface CheckGroupService {
    /**
     * 添加
     * @param checkGroup
     */
    public void add(CheckGroup checkGroup,Integer[] checkitemIds);

    /**
     * 分页查询
     * @param currentPage 当前页
     * @param pageSize 每页显示的条数
     * @param queryString 查询条件
     * @return 返回结果
     */
    PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    /**
     *根据id查询
     * @param id
     */
    public CheckGroup findById(Integer id);

    /**
     * 根据检查组id查询检查项id
     * @param id 检查组id
     * @return
     */
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    /**
     * 修改
     * @param checkGroup 检查组
     * @param checkitemIds  检查项
     */
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds);

    /**
     * 查询所有
     * @return
     */
    public List<CheckGroup> findAll();

}
