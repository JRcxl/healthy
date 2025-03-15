package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Flowdata;
import com.itheima.service.FlowdataService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flowdata")
public class FlowdataController {

    //    远程注入
    @Reference
    private FlowdataService service;

    /**
     * 新增
     *
     * @param flowdata
     * @return
     */
    @RequestMapping("/save")
    public Result save(@RequestBody Flowdata flowdata) {


        try {
            //保存
            service.save(flowdata);

        } catch (Exception e) {
            e.printStackTrace();

            //异常
            return new Result(false, MessageConstant.Error);
        }

        //返回结果
        //return save>0?new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS):new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        return new Result(true, "保存成功");
    }


    /**
     * 带条件分页查询
     *
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {

        PageResult page = null;
        try {
            //查询
            page = service.findPage(queryPageBean);

        } catch (Exception e) {
            e.printStackTrace();

            //异常
            return new Result(false, MessageConstant.Error);
        }

        //返回结果
        return page != null ? new Result(true, "查询成功", page) : new Result(false, "查询失败");
    }


    /**
     * 删除
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")
    public Result delete(Integer id) {

        try {
            //删除
            service.deleteById(id);

        } catch (Exception e) {
            e.printStackTrace();

            //异常
            return new Result(false, "删除失败");
        }
        //成功
        return new Result(true, "删除成功");
    }


    /**
     * 根据id查询 回显数据
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {

        try {
            //查询
            Flowdata flowdata = service.findById(id);
            //成功
            return new Result(true,"查询成功",flowdata);

        } catch (Exception e) {
            e.printStackTrace();

            //异常
            return new Result(false, "查询失败");
        }

    }


    /**
     * 修改
     * @param flowdata
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody Flowdata flowdata){

        try {

            //修改
            service.update(flowdata);

            //成功
            return new Result(true,"修改成功");
        }catch (Exception e){
            e.printStackTrace();
            //失败
            return new Result(false,"修改失败");
        }
    }
}
