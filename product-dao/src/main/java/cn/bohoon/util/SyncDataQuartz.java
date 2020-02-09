package cn.bohoon.util;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import cn.bohoon.interfaces.entity.SyncDataJob;

public class SyncDataQuartz {

	private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();
	public static String JOB_GROUP_NAME = "job";
	public static String TRIGGER_GROUP_NAME = "trigger";

	
	 /**
     * 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
     * @param jobModel  定时任务实现job
     */
    public static void addJob(SyncDataJob jobModel) throws Exception {
    	String jobName = jobModel.getId()+"" ;
    	String time = jobModel.getCronExpression() ;
        //设置任务
        JobDetail jobDetail = JobBuilder.newJob(SyncDataJobExecution.class).withIdentity(jobName,JOB_GROUP_NAME).build();
        //设置触发器
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName,TRIGGER_GROUP_NAME).withSchedule(CronScheduleBuilder.cronSchedule(time)).build();

        //获取Scheduler，并启动任务
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.scheduleJob(jobDetail,trigger);
        //启动
        if(!scheduler.isShutdown()){
            scheduler.start();
        }
    }
    
    
    
    /**
     * 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
     * @param jobModel   任务模型
     * @throws e
     */
    public static void modifyJobTime(SyncDataJob jobModel) throws  Exception{
    	String jobName = jobModel.getId()+"" ;
    	String time = jobModel.getCronExpression() ;
        Scheduler scheduler = schedulerFactory.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName,TRIGGER_GROUP_NAME);
        Trigger trigger = scheduler.getTrigger(triggerKey);
        if(null == trigger){
            return;
        }
        CronTrigger cronTrigger = (CronTrigger) trigger;
        String oldTime = cronTrigger.getCronExpression();
        if(!oldTime.equals(time)){
            // 触发器
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            // 触发器名,触发器组
            triggerBuilder.withIdentity(jobName, TRIGGER_GROUP_NAME);
            // 触发器时间设定
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(time));
            // 创建Trigger对象
            trigger = triggerBuilder.build();
            // 方式一 ：修改一个任务的触发时间
            scheduler.rescheduleJob(triggerKey, trigger);
        }
    }
    
    
    /**
     * 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
     * @param jobName 任务名称
     */
    public static void removeJob(SyncDataJob jobModel) throws Exception{
    	String jobName = jobModel.getId()+"" ;
        Scheduler scheduler = schedulerFactory.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName,TRIGGER_GROUP_NAME);
        JobKey jobKey = JobKey.jobKey(jobName,JOB_GROUP_NAME);
        //停止触发器
        scheduler.pauseTrigger(triggerKey);
        //移除触发器
        scheduler.unscheduleJob(triggerKey);
        //删除任务
        scheduler.deleteJob(jobKey);
    }
}
