package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * 新增项接口
 *
 * @author pc
 */
public interface CheckItemService {
    /**
     * 新增
     *
     * @param checkItem 对象
     */
    public void add(CheckItem checkItem);

    /**
     * 分页查询
     *
     * @param currentPage 当前页
     * @param pageSize    每页显示的条数
     * @param queryString 查询条件
     * @return 返回查询结果
     */
    public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString);

    /**
     * 删除
     * @param id 用户id
     */
    public void delete(Integer id);

    /**
     * 编辑
     * @param checkItem 对象
     */
    public void edit(CheckItem checkItem);

    /**
     * 根据id查询
     * @param id
     */
    public CheckItem findById(Integer id);

    /**
     * 查询所有
     * @return
     */
    List<CheckItem> findAll();
}
