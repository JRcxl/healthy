package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.entity.DietPlan;
import com.itheima.entity.MedicalReport;
import com.itheima.entity.MemberProfile;
import com.itheima.entity.Recipe;
import com.itheima.pojo.DietRecommendationRequest;
import com.itheima.service.DietService;
import com.itheima.service.RecommendService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DietController {

    @Reference
    private DietService dietService;

    @Reference
    private HealthProfileService healthProfileService;

    @PostMapping("/recommend")
    public DietPlan getRecommendation(
            @RequestBody DietRecommendationRequest request) {
        DietPlan plan = dietService.generateDietPlan(request.getMemberProfile(),
                request.getMedicalReport());

        return plan;
    }

    @PostMapping("/hospital_recmd")
    public ResponseEntity<?> getRecommendations(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "5000") int radius
    ) {
        List<HealthProfile> profiles =
                healthProfileService.getNearbyProfiles(lat, lng, radius);
        return ResponseEntity.ok(profiles);
    }


}
