package molsen.dw.scheduler;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DisallowConcurrentExecution
public class ExampleJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(ExampleJob.class);

    private final boolean enabled;
    private final ExampleWorker worker1;
    private final ExampleWorker worker2;

    public ExampleJob(boolean enabled, ExampleWorker worker1, ExampleWorker worker2) {
        this.enabled = enabled;
        this.worker1 = worker1;
        this.worker2 = worker2;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        if (enabled) {

            LOG.info("worker1.doWork");
            worker1.doWork();

            LOG.info("worker2.doWork");
            worker2.doWork();

        } else {
            LOG.info("DISABLED");
        }
    }

}

