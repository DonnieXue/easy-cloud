package com.easy.cloud.core.task.myexample.example1;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

import java.util.Date;

/**
 * @author daiqi
 * @create 2018-06-14 16:45
 */
public class MyCronJob implements Job{
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Trigger trigger = context.getTrigger();
        System.out.println(trigger.getKey() + "---------------当前时间为 : " + (new Date()));
        System.out.println(trigger.getKey() + "---------------trigger 开始时间为 : " + trigger.getStartTime());
    }
}
