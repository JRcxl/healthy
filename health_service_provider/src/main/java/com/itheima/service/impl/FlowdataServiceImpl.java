package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.FlowdataDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Flowdata;
import com.itheima.service.FlowdataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 流动人群
 */
@Service(interfaceClass = FlowdataService.class)
@Transactional
public class FlowdataServiceImpl implements FlowdataService {

    @Autowired
    private FlowdataDao flowdataDao;

    /**
     * 获取体检中心流动人群来源地址
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> getAddress() {
        return flowdataDao.findAddressCount();
    }


    //新增
    @Override
    public void save(Flowdata flowdata) {
        flowdataDao.save(flowdata);
    }


    /**
     * 带条件分页查询
     * @param queryPageBean 封装的页码信息与带条件的查询类
     * @return  分页结果封装对象
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {

        //设置分页
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());

        //查询
        Page<Flowdata> page = flowdataDao.findPage(queryPageBean.getQueryString());

        //封装结果,并返回

        return new PageResult(page.getTotal(),page.getResult());
    }

    //删除根据id
    @Override
    public void deleteById(Integer id) {
        flowdataDao.deleteById(id);
    }

    //根据id查询
    @Override
    public Flowdata findById(Integer id) {
        return flowdataDao.findById(id);
    }

    //修改
    @Override
    public void update(Flowdata flowdata) {
        flowdataDao.update(flowdata);
    }
}
