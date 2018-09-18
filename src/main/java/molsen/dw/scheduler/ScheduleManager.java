package molsen.dw.scheduler;

import com.porch.dropwizard.quartz.LoggingJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.spi.TriggerFiredBundle;

import java.util.HashMap;
import java.util.Map;

public class ScheduleManager {

    private class JobFactory implements org.quartz.spi.JobFactory {

        private final Map<JobDetail, Job> jobsByKey = new HashMap<>();

        @Override
        public Job newJob(TriggerFiredBundle triggerFiredBundle, Scheduler scheduler) throws SchedulerException {
            Job foundJob = jobsByKey.get(triggerFiredBundle.getJobDetail());
            return foundJob != null ? foundJob : new LoggingJob();
        }

        public void register(JobDetail jobDetail, Job job) {
            jobsByKey.put(jobDetail, job);
        }
    }


    private final JobFactory jobFactory = new JobFactory();
    private final Scheduler scheduler;

    public ScheduleManager(Scheduler scheduler) throws SchedulerException {
        this.scheduler = scheduler;
        scheduler.setJobFactory(jobFactory);
    }

    public void registerCronJob(String groupName, String cronExpression, Job job) throws SchedulerException {
        JobDetail jobDetail = getJobDetail(job, groupName);
        String jobName = getJobName(job);

        deleteJobDetailIfExists(jobDetail);

        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobName, groupName)
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();

        jobFactory.register(jobDetail, job);
        scheduler.scheduleJob(jobDetail, trigger);
    }

    public void deleteCronJob(String groupName, Job job) throws SchedulerException {
        JobDetail jobDetail = getJobDetail(job, groupName);
        deleteJobDetailIfExists(jobDetail);
    }

    private JobDetail getJobDetail(Job job, String groupName) {
        return JobBuilder.newJob(job.getClass())
                .withIdentity(getJobName(job), groupName)
                .build();
    }

    private String getJobName(Job job) {
        return job.getClass().getSimpleName();
    }

    private void deleteJobDetailIfExists(JobDetail jobDetail) throws SchedulerException {
        if (scheduler.checkExists(jobDetail.getKey())) {
            scheduler.deleteJob(jobDetail.getKey());
        }
    }

}
