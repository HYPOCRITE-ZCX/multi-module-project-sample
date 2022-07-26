package com_zcx_chant_crud.cron;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SampleSchedule {


    /**
     * 定时器 示例：每天上午10:15执行任务
     */
    @Scheduled(cron = "0 15 10 ? * *")
    public void infoBaseSync() {

    }

}
