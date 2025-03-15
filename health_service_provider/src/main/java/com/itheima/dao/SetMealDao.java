package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @author pc
 * 检查套餐dao层接口
 */
public interface SetMealDao {
    /**
     * 添加
     * @param setmeal
     */
    public void add(Setmeal setmeal);

    /**
     * 检查套餐和检查组之间的关联表
     * @param map
     */
    public void setMealIdAndCheckGroupIds(Map<String, Integer> map);

    /**
     * 根据条件查询
     * @param queryString
     */
    Page<Setmeal> selectByCondition(String queryString);

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
     * 查询所有套餐会员
     * @return
     */
    public List<Map<String, Object>> findSetmealCount();
}
