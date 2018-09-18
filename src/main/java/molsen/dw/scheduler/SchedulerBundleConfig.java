package molsen.dw.scheduler;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SchedulerBundleConfig {

    private final boolean enabled;

    private final int seconds;

    public SchedulerBundleConfig(@JsonProperty("enabled") boolean enabled,
                                 @JsonProperty("seconds") int seconds) {
        this.enabled = enabled;
        this.seconds = seconds;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getSeconds() {
        return seconds;
    }
}
