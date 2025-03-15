package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * @author pc
 * 检查项数据层
 */
public interface CheckItemDao {
    /**
     * 添加
     * @param checkItem 对象
     */
    public void add(CheckItem checkItem);

    /**
     * 分页查询
     * @param queryString 查询条件
     * @return 返回结果页
     */
    public Page<CheckItem> selectByCondition(String queryString);

    /**
     * 根据检查项id查询所有
     * @param checkItemId 检查项id
     * @return 返回结果
     */
    public Long findCountByCheckItemId(Integer checkItemId);

    /**
     * @param id 检查项id
     */
    public void deleteById(Integer id);

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
    public List<CheckItem> findAll();
}
