package com.mit.community.module.device.controller;


import com.mit.community.service.com.mit.community.service.hik.SnapFaceDataHikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author qishengjun
 * @Date Created in 10:11 2020/1/10
 * @Company: mitesofor </p>
 * @Description:~
 */
@Component
public class ScheduledTask {

    @Autowired
    private SnapFaceDataHikService snapFaceDataHikService;
    @Scheduled(cron="0 0 1 * * *")
    public void scheduledTask1(){

    }
}
