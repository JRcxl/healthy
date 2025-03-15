package com.itheima.pojo;

import java.io.Serializable;
import java.util.Date;

public class ExamData implements Serializable {
    private  String username;
    private  String sex;
    private  String mealname;

    private  String result_data;

    private Date orderDate;

    private int mealid;

    public int getMealid() {
        return mealid;
    }

    public void setMealid(int mealid) {
        this.mealid = mealid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMealname() {
        return mealname;
    }

    public void setMealname(String mealname) {
        this.mealname = mealname;
    }

    public String getResult_data() {
        return result_data;
    }

    public void setResult_data(String result_data) {
        this.result_data = result_data;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
}
