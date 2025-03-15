package com.itheima.entity;

import java.io.Serializable;

public class MedicalReport implements Serializable {
    private Double bloodSugar;
    private Double bloodPressure;


    public MedicalReport(Double bloodSugar, Double bloodPressure) {
        this.bloodSugar = bloodSugar;
        this.bloodPressure = bloodPressure;
    }

    public MedicalReport() {
    }

    public Double getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(Double bloodSugar) {
        this.bloodSugar = bloodSugar;
    }

    public Double getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(Double bloodPressure) {
        this.bloodPressure = bloodPressure;
    }
}
