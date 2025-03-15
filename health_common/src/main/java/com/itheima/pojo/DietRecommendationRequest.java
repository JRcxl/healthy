package com.itheima.pojo;

import com.itheima.entity.MedicalReport;
import com.itheima.entity.MemberProfile;

import java.io.Serializable;

public class DietRecommendationRequest implements Serializable {
    private MemberProfile memberProfile;
    private MedicalReport medicalReport;

    public MemberProfile getMemberProfile() {
        return memberProfile;
    }

    public void setMemberProfile(MemberProfile memberProfile) {
        this.memberProfile = memberProfile;
    }

    public MedicalReport getMedicalReport() {
        return medicalReport;
    }

    public void setMedicalReport(MedicalReport medicalReport) {
        this.medicalReport = medicalReport;
    }
}
