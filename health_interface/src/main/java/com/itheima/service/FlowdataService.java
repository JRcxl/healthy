package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Flowdata;

import java.util.List;
import java.util.Map;

/**
 * 流动人群
 */
public interface FlowdataService {

    /**
     * 获取体检中心流动人群来源地址
     * @return
     */
    List<Map<String,Object>> getAddress();



    /**
     * 保存检查项
     * @param flowdata
     * @return
     */
    void save(Flowdata flowdata);


    /**
     * 带条件分页查询
     * @param queryPageBean 封装的页码信息与带条件的查询类
     * @return  分页结果封装对象
     */
    PageResult findPage(QueryPageBean queryPageBean);

    /**
     * 根据id删除
     * @param id
     */
    void deleteById(Integer id);


    /**
     * 根据id查询
     * @param id
     * @return
     */
    Flowdata findById(Integer id);


    /**
     * 修改数据
     * @param flowdata
     */
    void update(Flowdata flowdata);

}
