package com.itheima.config;

import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;
import org.kie.spring.KModuleBeanFactoryPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@ComponentScan(basePackages = "com.itheima.config")
public class DroolsConfig {



    @Bean
    public KieContainer kieContainer() {
        KieServices kieServices = KieServices.get();

        // 从 classpath 加载 rules 目录下的所有 .drl 文件
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(ResourceFactory.newClassPathResource("rules/diet_rules.drl"));

        // 构建规则
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();

        // 验证规则
        Results results = kieBuilder.getResults();
        if (results.hasMessages(Message.Level.ERROR)) {
            throw new IllegalStateException("规则编译错误: " + results.getMessages());
        }

        return kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
    }

    /**
     * 必需: Spring 集成处理器
     */
    @Bean
    public KModuleBeanFactoryPostProcessor kiePostProcessor() {
        return new KModuleBeanFactoryPostProcessor();
    }

    /**
     * 必需: 创建 KieSession 原型 Bean
     */
    @Bean
    @Scope("prototype")
    public KieSession kieSession(KieContainer kieContainer) {
        return kieContainer.newKieSession();
    }
}