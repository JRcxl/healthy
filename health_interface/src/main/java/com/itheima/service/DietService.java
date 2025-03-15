package com.itheima.service;

import com.itheima.entity.DietPlan;
import com.itheima.entity.MedicalReport;
import com.itheima.entity.MemberProfile;


public interface DietService {
    DietPlan generateDietPlan(MemberProfile user, MedicalReport report);


}
