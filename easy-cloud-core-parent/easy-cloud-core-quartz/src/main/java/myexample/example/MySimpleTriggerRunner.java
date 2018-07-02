package myexample.example;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author daiqi
 * @create 2018-06-14 14:51
 */
public class MySimpleTriggerRunner {
    public static void main(String[] args) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(MySimpleJob.class)
                .withIdentity("job1", "group")
                .build();

        JobDetail jobDetail2 = JobBuilder.newJob(MySimpleJob.class)
                .withIdentity("job2", "group2")
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("trigger1", "group")
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(2)
                                .withRepeatCount(100)
                )
                .build();

        Trigger trigger2 = TriggerBuilder.newTrigger()
                .withIdentity("trigger2", "group")
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(2)
                                .withRepeatCount(100)
                )
                .build();
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.scheduleJob(jobDetail, trigger);
//        scheduler.scheduleJob(jobDetail2, trigger);
        scheduler.scheduleJob(jobDetail, trigger2);
        scheduler.start();
    }
}
