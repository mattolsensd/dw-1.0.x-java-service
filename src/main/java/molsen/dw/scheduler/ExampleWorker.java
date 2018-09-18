package molsen.dw.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a typical dependency; a configured class instance that will be called from within a job.
 * For example, this could simulate a DB or API call.
 */
public class ExampleWorker {

    private static final Logger LOG = LoggerFactory.getLogger(ExampleWorker.class);

    private final int seconds;

    public ExampleWorker(int seconds) {
        this.seconds = seconds;
    }

    public void doWork() {
        try {
            LOG.info("Working " + seconds);
            Thread.sleep(seconds * 1000);
        } catch (Exception e) {
            LOG.error("Work failed", e);
        }
    }

}
