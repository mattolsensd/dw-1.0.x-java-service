package molsen.dw.scheduler;

import com.porch.commons.response.ApiResponse;
import com.porch.dropwizard.core.ApiResponseFactory;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Path("scheduler")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SchedulerResource {

    private static final Logger LOG = LoggerFactory.getLogger(SchedulerResource.class);

    private static final JobKey EXAMPLE_JOB_KEY = JobKey.jobKey(ExampleJob.class.getSimpleName(), SchedulerBundle.GROUP_NAME);

    private final Scheduler scheduler;

    public SchedulerResource(Supplier<Scheduler> schedulerSupplier) {
        this.scheduler = Objects.requireNonNull(schedulerSupplier, "scheduleSupplier cannot be null!").get();
    }


    @GET
    @Path("example-job/executing")
    public ApiResponse<Boolean> isExampleJobExecuting() {
        LOG.info("CHECK IS EXAMPLE JOB EXECUTING");
        return ApiResponseFactory.create(() -> isRunning(EXAMPLE_JOB_KEY));
    }

    @PUT
    @Path("example-job/trigger")
    public ApiResponse<Boolean> triggerExampleJob() {
        LOG.info("MANUALLY TRIGGERING EXAMPLE JOB");
        return triggerJob(EXAMPLE_JOB_KEY);
    }

    @PUT
    @Path("example-job/interrupt")
    public ApiResponse<Boolean> interruptExampleJob() {
        LOG.info("INTERRUPTING EXAMPLE JOB");
        return interruptJob(EXAMPLE_JOB_KEY);
    }

    @PUT
    @Path("example-job/pause")
    public ApiResponse<Boolean> pauseExampleJob() {
        LOG.info("PAUSING EXAMPLE JOB");
        return ApiResponseFactory.create(() -> {
            scheduler.pauseJob(EXAMPLE_JOB_KEY);
            return true;
        });
    }

    @PUT
    @Path("example-job/resume")
    public ApiResponse<Boolean> unpauseExampleJob() {
        LOG.info("RESUME EXAMPLE JOB");
        return ApiResponseFactory.create(() -> {
            scheduler.resumeJob(EXAMPLE_JOB_KEY);
            return true;
        });
    }


    private ApiResponse<Boolean> interruptJob(JobKey jobKey) {
        return ApiResponseFactory.create(() -> {
            if (isRunning(jobKey)) {
                scheduler.interrupt(jobKey);
                return true;
            } else return false;
        });
    }


    private ApiResponse<Boolean> triggerJob(JobKey jobKey) {
        return ApiResponseFactory.create(() -> {
            if (!isRunning(jobKey)) {
                scheduler.triggerJob(jobKey);
                return true;
            } else {
                return false;
            }
        });
    }


    private boolean isRunning(JobKey jobKey) throws SchedulerException {
        return scheduler.getCurrentlyExecutingJobs().stream()
                .filter(j -> j.getJobDetail().getKey().getName().equals(jobKey.getName()))
                .collect(Collectors.toSet()).size() > 0;
    }

}
