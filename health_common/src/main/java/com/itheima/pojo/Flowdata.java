package com.itheima.pojo;

import java.io.Serializable;

/**
 * 体检中心流动人群来源地址
 */
public class Flowdata implements Serializable {
    private Integer id;
    private String address;
    private String name;
    private String idcard;
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public Flowdata() {
    }

    public Flowdata(Integer id, String address, String name, String idcard, String remark) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.idcard = idcard;
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Flowdata{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", idcard='" + idcard + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
