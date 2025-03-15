package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @author pc
 * 定时任务清理器
 */
public class ClearImgJob {

    //1.注入jedispool常量类
    @Autowired
    private JedisPool jedisPool;

    //2.创建方法
    public void clearImg(){
        //3.使用常量类中的集合，来计算出垃圾图片
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        //4.遍历集合取出所有的垃圾文件
        if (set != null){
            for (String s : set) {
                //5.调用工具类的删除方法
                QiniuUtils.deleteFileFromQiniu(s);
                //6.清理redis数据库里面的垃圾图片名字
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,s);
            }
        }
    }
}
