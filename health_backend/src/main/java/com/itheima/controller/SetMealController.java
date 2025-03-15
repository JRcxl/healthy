package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetMealService;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

/**
 * 检查套餐
 *
 * @author pc
 */
@RestController

@RequestMapping("/setmeal")

public class SetMealController {
    //注入redis资源
    @Autowired
    private JedisPool jedisPool;

    //图片上传
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {
        System.out.println("imgFile:"+imgFile);
        //1.调用工具类
        try {
            //2.获取原始文件名
            String originalFilename = imgFile.getOriginalFilename();
            int index = originalFilename.lastIndexOf(".");
            String extention = originalFilename.substring(index - 1);
            //3.获取随机名
            String fileName = UUID.randomUUID().toString() + extention;
            System.out.println("fileName:"+fileName);
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);

            Result result = new Result(true, MessageConstant.UPLOAD_SUCCESS, fileName);
            //完善
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, fileName);

            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    //暴露service层资源
    @Reference
    private SetMealService setMealService;

    /**
     * 添加套餐
     *
     * @param setmeal       套餐对象
     * @param checkgroupIds 检查组ids
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {

        try {
            setMealService.add(setmeal, checkgroupIds);
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    /**
     * 分页查询
     *
     * @param queryPageBean 封装分页的对象
     * @return 返回total rows
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
            return setMealService.pageQuery(queryPageBean);
    }


}
