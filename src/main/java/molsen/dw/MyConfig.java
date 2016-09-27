package molsen.dw;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.porch.dropwizard.configuration.modular.yaml.ConfigPath;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@ConfigPath("dw-1.0.x-java-service")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MyConfig extends Configuration {

    @Valid
    @NotNull
    @JsonProperty("jerseyClient")
    JerseyClientConfiguration jerseyClient;

}
