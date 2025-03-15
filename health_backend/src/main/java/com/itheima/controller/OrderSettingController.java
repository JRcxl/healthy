package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author pc
 * 文件上传
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    //注入资源
    @Reference
    private OrderSettingService orderSettingService;
    /**
     * 文件上传
     * @param excelFile 页面传过来的对象
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile){

        //1.调用工具类的上传方法
        try {
            List<String[]> list = POIUtils.readExcel(excelFile);
            //2.做非空判断
            if (list != null && list.size() > 0 ){
                //3.将list集合的泛型改为ordersetting类型
                ArrayList<OrderSetting> orderSettings = new ArrayList<>();
                    //4.遍历list集合
                for (String[] strings : list) {
                    //5.创建一个ordersetting对象,将arraylist中的数据转入orderseting中
                    OrderSetting orderSetting = new OrderSetting(new Date(strings[0]),Integer.parseInt(strings[1]));
                        //6.将其添加到array list集合中
                    orderSettings.add(orderSetting);
                }
                //7.调用service的方法
                orderSettingService.add(orderSettings);
                return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return null ;
    }

    /**
     * 根据日期查询所有的预约设置信息
     * @param date
     * @return
     */
    @RequestMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String date){

        try {
            List<Map> list = orderSettingService.getOrderSettingByMonth(date);
            //返回结果
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
        } catch (Error e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }

    /**
     * 修改预约人数
     * @param orderSetting
     * @return
     */
    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
           orderSettingService.editNumberByDate(orderSetting);
            //返回结果
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
