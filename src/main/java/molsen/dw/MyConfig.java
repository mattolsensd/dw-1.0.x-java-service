package molsen.dw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.porch.dropwizard.configuration.modular.yaml.ConfigPath;
import com.porch.dropwizard.quartz.QuartzBundleConfiguration;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import molsen.dw.scheduler.SchedulerBundleConfig;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@ConfigPath("dw-1.0.x-java-service")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyConfig extends Configuration {

    @Valid
    @NotNull
    @JsonProperty("jerseyClient")
    JerseyClientConfiguration jerseyClient;

    @Valid
    @NotNull
    @JsonProperty("quartz")
    QuartzBundleConfiguration quartzBundleConfiguration;

    @Valid
    @NotNull
    @JsonProperty("scheduler")
    SchedulerBundleConfig schedulerBundleConfig;

    @JsonProperty("externalExampleWorkerSeconds")
    int externalExampleWorkerSeconds;

}
