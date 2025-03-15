package com.itheima.jobs;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.service.RecommendService;
import org.springframework.scheduling.annotation.Scheduled;

public class SimilarityUpdateTask {
    @Reference
    private RecommendService recommendService;

    // 每天凌晨2点更新
    @Scheduled(cron = "0 0 2 * * ?")
    public void updateSimilarities() {
        recommendService.updateSimilarities();
    }
}
