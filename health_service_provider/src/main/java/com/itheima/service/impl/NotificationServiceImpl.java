package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.service.NotificationService;
import com.itheima.service.OrderService;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = NotificationService.class)
@Transactional
public class NotificationServiceImpl implements NotificationService{
}
