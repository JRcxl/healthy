package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.SetMealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetMealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pc
 * 检查套餐实现类
 */
@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private SetMealDao setMealDao;

    //注入jedis资源
    @Autowired
    private JedisPool jedisPool;
    //注入生成静态页面对象
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${out_put_path}")//读取配置文件中的文件目录
    private String outPutPath;
    /**
     * 添加
     * @param setmeal 套餐对象
     * @param checkgroupIds 检查组ids
     */
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        //调用dao层方法
        setMealDao.add(setmeal);
        Integer setmealId = setmeal.getId();
        //设置检查套餐和检查组之间的关联表
        if (checkgroupIds != null && checkgroupIds.length > 0 ) {
            this.setMealIdAndCheckGroupIds(setmealId,checkgroupIds);
        }
        savePic2Redis(setmeal.getImg());
        generateMobileStaticHtml();
    }

    /**
     * 页面静态化
     */
    private void generateMobileStaticHtml() {

        //1.调用findAll方法，获取所有数据
        List<Setmeal> setmealList = this.findAll();
        //2.生成套餐静态页面数据
        generateMobileSetmealListHtml(setmealList);
        //3.生成套餐详情静态页面数据
        generateMobileSetmealDetailHtml(setmealList);
    }

    /**
     * 生成多个套餐详情静态页面数据
     * @param setmealList
     */
    private void generateMobileSetmealDetailHtml(List<Setmeal> setmealList) {
        //1.做非空判断
        if (setmealList != null && setmealList.size() > 0){
            for (Setmeal setmeal : setmealList) {
                //2.创建一个hashmap
                HashMap<String, Object> hashMap = new HashMap<>();
                //3.添加数据
                hashMap.put("setmeal",this.findById(setmeal.getId()));
                //4.生成页面
                this.generateHtml("mobile_setmeal_detail.ftl","setmeal_detail_"+setmeal.getId()+".html",hashMap);
            }
        }
    }

    /**
     * 生成套餐静态页面数据方法
     * @param setmealList
     */
    private void generateMobileSetmealListHtml(List<Setmeal> setmealList) {
        //1.创建一个hashmap集合
        HashMap<String, Object> map = new HashMap<>();
        //2.将传过来的数据添加到集合中
        map.put("setmealList",setmealList);
        //3.调用生成模板的方法,将原始模块、生成的页面、和map集合添加进去
        this.generateHtml("mobile_setmeal.ftl","m_setmeal.html",map);
    }

    /**
     * 实现静态页面的公共方法
     * @param templateName
     * @param htmlPageName
     * @param map
     */
    private void generateHtml(String templateName, String htmlPageName, HashMap<String, Object> map) {
        //1.调用getConfiguration
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        //4.声明
        Writer out = null ;
        //2.生成数据
        try {
            //3.生成模板
            Template template = configuration.getTemplate(templateName);
            File file = new File(outPutPath + "\\" + htmlPageName);
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            //5.输出文件
            try {
                template.process(map,out);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    if (out != null){
                        out.flush();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  jedis,完善图片上传
     */
    private void savePic2Redis(String img) {
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,img);
    }

    /**
     * 检查套餐和检查组之间的关联表
     * @param setmealId 检查套餐id
     * @param checkgroupIds 检查组id
     */
    public void setMealIdAndCheckGroupIds(Integer setmealId, Integer[] checkgroupIds) {
        //1.先判断
        if (checkgroupIds != null && checkgroupIds.length > 0 ){
            for (Integer checkgroupId : checkgroupIds) {
                //2.存入map集合中
                Map<String, Integer> map = new HashMap<>();
                map.put("setmealId",setmealId);
                map.put("checkgroupId",checkgroupId);
                setMealDao.setMealIdAndCheckGroupIds(map);
            }
        }
    }

    /**
     * 分页查询
     * @param queryPageBean 分页查询对象
     * @return
     */
    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        //1.获取当前页、每页显示的条数、查询条件
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //2获取总条数
        PageHelper.startPage(currentPage,pageSize);
        //3调用dao层的方法
        Page<Setmeal> setmeals = setMealDao.selectByCondition(queryString);
        //4封装到result对象中
        long total = setmeals.getTotal();
        List<Setmeal> result = setmeals.getResult();
        PageResult pageResult = new PageResult(total, result);
        //5返回结果
        return pageResult;
    }

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<Setmeal> findAll() {
        return setMealDao.findAll();
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(Integer id) {
        return setMealDao.findById(id);
    }

    /**
     * 查询所有会员数量
     * @return
     */
    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setMealDao.findSetmealCount();
    }
}
