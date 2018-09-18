package molsen.dw.scheduler;

import com.porch.dropwizard.core.bundle.ConfiguredBundle;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;
import java.util.function.Supplier;

public class SchedulerBundle<T extends Configuration> extends ConfiguredBundle<T, SchedulerBundleConfig> {

    private static final Logger LOG = LoggerFactory.getLogger(SchedulerBundle.class);

    private final Function<T, SchedulerBundleConfig> extractConfig;
    private final Supplier<Scheduler> schedulerSupplier;
    private final Supplier<ExampleWorker> externalWorkerSupplier;

    public static final String GROUP_NAME = "molsen-dw1java-test";

    public static final String ONCE_A_DAY = "0 0 0 1/1 * ? *";
    public static final String FIVE_PM_PST_DAILY = "0 0 17 1/1 * ? *";
    public static final String EVERY_12_HOURS = "0 0 0/12 1/1 * ? *";
    public static final String EVERY_20_SECS = "*/20 * * * * ?";
    public static final String EVERY_30_MIN = "0 0/30 * 1/1 * ? *";
    public static final String EVERY_6_HOURS = "0 0 0/6 1/1 * ? *";
    public static final String EVERY_MONDAY = "0 0 18 ? * MON *";
    public static final String EVERY_LEAP_DAY = "0 0 12 29 2 ? *";

    public SchedulerBundle(Function<T, SchedulerBundleConfig> extractConfig, Supplier<Scheduler> schedulerSupplier, Supplier<ExampleWorker> externalWorkerSupplier) {
        this.extractConfig = extractConfig;
        this.schedulerSupplier = schedulerSupplier;
        this.externalWorkerSupplier = externalWorkerSupplier;
    }

    @Override
    protected SchedulerBundleConfig extractBundleConfiguration(T t) throws Exception {
        return extractConfig.apply(t);
    }

    @Override
    protected void runBundle(SchedulerBundleConfig config, Environment environment) throws Exception {
        final ScheduleManager scheduleManager = new ScheduleManager(schedulerSupplier.get());

        final ExampleWorker internalExampleWorker = new ExampleWorker(config.getSeconds());

        final ExampleJob exampleJob = new ExampleJob(config.isEnabled(), internalExampleWorker, externalWorkerSupplier.get());
        scheduleManager.registerCronJob(GROUP_NAME, EVERY_20_SECS, exampleJob);

        final SchedulerResource schedulerResource = new SchedulerResource(schedulerSupplier);
        environment.jersey().register(schedulerResource);
    }

}