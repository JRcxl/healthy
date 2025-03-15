package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Flowdata;

import java.util.List;
import java.util.Map;

/**
 * 流动人群
 */
public interface FlowdataDao {

    /**
     * 获取体检中心流动人群来源地址
     * @return
     */
    List<Map<String,Object>> findAddressCount();




    /**
     * 保存检查项
     * @param flowdata
     * @return
     */
    void save(Flowdata flowdata);


    /**
     * 带条件查询
     * @param queryString
     * @return
     */
    Page<Flowdata> findPage(String queryString);

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
