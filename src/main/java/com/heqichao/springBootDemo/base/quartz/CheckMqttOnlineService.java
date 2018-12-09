package com.heqichao.springBootDemo.base.quartz;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by heqichao on 2018-12-9.
 */
@Component
public class CheckMqttOnlineService {
    @Scheduled(cron = "0 0/10 * * * ?")
    public void timerToNow(){
        System.out.println("now time:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        //查找所有在线的MQTT设备
    }
}
