package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * 套餐接口
 * @author pc
 */
public interface SetMealService {
    /**
     * 添加
     * @param setmeal 检查套餐对象
     * @param checkgroupIds 检查组ids
     */
    public void add(Setmeal setmeal, Integer[] checkgroupIds);

    /**
     * 分页查询
     * @param queryPageBean 分页查询对象
     * @return
     */
    public PageResult pageQuery(QueryPageBean queryPageBean);

    /**
     * 查询所有
     * @return
     */
    public List<Setmeal> findAll();

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public Setmeal findById(Integer id);


    /**
     * 查询所有数量
     * @return
     */
    public List<Map<String, Object>> findSetmealCount();
}
