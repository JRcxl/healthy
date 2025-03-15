package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.*;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 报表
 *
 * @author pc
 */
@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private MemberService memberService;
    @Reference
    private SetMealService setMealService;
    @Reference
    private OrderService orderService;
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private FlowdataService flowdataService;

    /**
     * 获取报表数据
     *
     * @return
     */
    @RequestMapping("/getMemberReport")
    public Result getMemberReport() {

        //.要返回data 里面的months 和 membercount

        //1.创建一个map来装
        HashMap<String, Object> hashMap = new HashMap<>();
        //2.可以通过计算来获取当前日期12个月
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -12);
        //3.months里面的数据可以通过list来存储
        ArrayList<String> list = new ArrayList<>();
        //4.遍历拿到每一个month
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            list.add(new SimpleDateFormat("yyyy.MM").format(calendar.getTime()));
        }
        //5.将list集合添加到map集合中
        hashMap.put("months", list);

        //6.获取到member count，需要通过查询数据库来获取会员数量
        List<Integer> memberCount = memberService.findMemberByMonths(list);
        hashMap.put("memberCount", memberCount);
        //7.返回结果
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, hashMap);
    }

    /**
     * 套餐占比统计
     *
     * @return
     */
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport() {
        //思路
        //1.对传过来的data 用map来接  ，其中的setmealName 用一个list集合来装  setmealCount 用list 泛型为map

        try {
            //2.创建一个map
            HashMap<String, Object> map = new HashMap<>();
            //4.调用service层的方法来，获取setmealname
            List<Map<String, Object>> list = setMealService.findSetmealCount();
            map.put("setmealCount", list);
            //3.创建一个list集合，用类装setmealname
            ArrayList<String> setmealNames = new ArrayList<>();
            //4.遍历list集合
            for (Map<String, Object> setmealCount : list) {
                String name = (String) setmealCount.get("name");
                setmealNames.add(name);
            }
            map.put("setmealNames", setmealNames);
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    @Reference
    private ReportService reportService;

    /**
     * 获取报表内容
     *
     * @return
     */
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        try {
            //1.以map返回，才可以对应上
            Map<String, Object> map = reportService.exportBusinessReport();
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }


    }

    /**
     * 导出excel报表
     *
     * @return
     */
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            //1.获取到报表中的内容
            Map<String, Object> map = reportService.exportBusinessReport();
            String reportDate = (String) map.get("reportDate");
            Integer todayNewMember = (Integer) map.get("todayNewMember");
            Integer totalMember = (Integer) map.get("totalMember");
            Integer thisWeekNewMember = (Integer) map.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) map.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) map.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) map.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) map.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) map.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) map.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) map.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) map.get("hotSetmeal");

            //2.创建一个工作簿，并动态获取模板文件的绝对路径
            String excelFilePath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(excelFilePath)));
            //3.向模板中写入内容
            XSSFSheet sheetAt = workbook.getSheetAt(0);

            XSSFRow row = sheetAt.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期

            row = sheetAt.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//今日新增会员数
            row.getCell(7).setCellValue(totalMember);//总的会员数

            row = sheetAt.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheetAt.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日新增预约数
            row.getCell(7).setCellValue(todayVisitsNumber); //今日到诊数

            row = sheetAt.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheetAt.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月就诊数

            //热门套餐
            int rowNum = 12;
            //遍历热门套餐
            for (Map map1 : hotSetmeal) {
                String name = (String) map1.get("name");
                Long setmeal_count = (Long) map1.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map1.get("proportion");
                row = sheetAt.getRow(rowNum ++);
               row.getCell(4).setCellValue(name);
               row.getCell(5).setCellValue(setmeal_count);
               row.getCell(6).setCellValue(proportion.doubleValue());
            }
            //4.设置下载格式
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            //5.输出文件，通过浏览器
            ServletOutputStream  outputStream =  response.getOutputStream();
            workbook.write(outputStream);
            //6.关闭资源
            outputStream.flush();
            outputStream.close();
            workbook.close();

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /**
     * 导出PDF报表
     * @return
     */
    @RequestMapping("/exportBusinessReport4PDF")
    public Result exportBusinessReport4PDF(HttpServletRequest request, HttpServletResponse response){
        try {
            //1.获取模板文件中的数据
            Map<String, Object> map = reportService.exportBusinessReport();
            //2.把map获取出来
            List<Map> hotSetmeal = (List<Map>) map.get("hotSetmeal");
            //3.通过绝地路径获取的模板文件,并编译模板
            String jasperFilePath = request.getSession().getServletContext().getRealPath("template")+File.separator+"health_business3.jrxml";
            String jasperTemplate = request.getSession().getServletContext().getRealPath("template")+File.separator+"health_business3.jasper";
            JasperCompileManager.compileReportToFile(jasperFilePath,jasperTemplate);
            //4.填充数据
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperTemplate, map, new JRBeanCollectionDataSource(hotSetmeal));
            //5.获取输出流和文件下载方式
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("application/pdf");
            response.setHeader("content-Disposition","attachment;filename=report.pdf");
            //6.输出文件
            JasperExportManager.exportReportToPdfStream(jasperPrint,outputStream);
            //7.关闭资源
            outputStream.flush();
            outputStream.close();
            return  null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }


//=============================================


    /**
     * 查询本月到诊人数 未到诊人数
     * @return
     */
    @RequestMapping("/getNumberReport")
    public Result getNumberReport(){

        try{
            //查询本月到诊人数/未到诊人数
            List<String> PeopleNumber=orderService.findPeopleNumberByDate(new SimpleDateFormat("yyyy-MM").format(new Date()));
            //成功
            return new Result(true, "查询成功",PeopleNumber);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, "查询失败");
        }
    }


    /**
     * 统计年龄段占比 1-18  18-45  45-60  60以上
     * @return
     */
    @RequestMapping("/getAge")
    public Result getAge(){

        try{
            //统计年龄段占比 1-18  18-45  45-60  60以上
            Map<String, Integer> maps = memberService.findAge();
            List<String> list1=new ArrayList<>();
            List<Integer> list2=new ArrayList<>();
            Set<String> set = maps.keySet();
            for (String key : set) {
                list1.add(key);
                list2.add(maps.get(key));
            }

            Map<String,Object> map=new HashMap<>();
            map.put("name",list1);
            map.put("value",list2);

            //成功
            return new Result(true, "查询成功",map);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, "查询失败");
        }
    }



    /**
     * 查询性别占比
     * @return
     */
    @RequestMapping("/getSexReport")
    public Result getSexReport(){

        try{
            //判断redis是否有数据
            List<String> sexReport = jedisPool.getResource().lrange("getSexReport", 0, -1);
            if(sexReport!=null && sexReport.size()>0){

            }else {
                //查询性别占比
                List<Integer> list = memberService.findSex();
                for (Integer s : list) {
                    //存入redis中
                    jedisPool.getResource().rpush("getSexReport",s.toString());
                }

                sexReport=jedisPool.getResource().lrange("getSexReport", 0, -1);
            }

            //成功
            return new Result(true, "查询成功",sexReport);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, "查询失败");
        }
    }


    /**
     * 全年/每月 体检人数
     * @return
     */
    @RequestMapping("/findYear")
    public Result findYear(){

        try{

            //全年/每月 体检人数
            List<Integer> list=orderService.findYear();
            //成功
            return new Result(true, "查询成功",list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, "查询失败");
        }
    }


    /**
     * 获取体检中心流动人群来源地址
     * @return
     */
    @RequestMapping("/getAddress")
    public Result getAddress(){

        try{

            //获取体检中心流动人群来源地址
            List<Map<String, Object>> list = flowdataService.getAddress();
            Map<String,Object> map=new HashMap<>();
            List<Object> list1=new ArrayList<>();
            List<Object> list2=new ArrayList<>();
            for (Map<String, Object> ll : list) {
                list1.add(ll.get("address"));
                list2.add(ll.get("value"));
            }
            map.put("address",list1);
            map.put("value",list2);
            //成功
            return new Result(true, "查询成功",map);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, "查询失败");
        }
    }


    //============
    /**
     * 获取统计套餐预约占比
     *
     * @return
     */
    @RequestMapping("/getSetmealReports")
    public Result getSetmealReports() {
        try {

            List<Map<String, Object>> countSetmeal = setMealService.findSetmealCount();
            //获取数据成功
            return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, countSetmeal);

        } catch (Exception e) {

            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }

    }
}
