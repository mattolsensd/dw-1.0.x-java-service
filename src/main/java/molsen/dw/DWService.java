package molsen.dw;

import com.fasterxml.jackson.module.scala.DefaultScalaModule;
import com.porch.dropwizard.configuration.modular.ModularConfigurationSourceProvider;
import com.porch.dropwizard.configuration.modular.lua.LuaConfigurationGenerator;
import com.porch.dropwizard.core.bundle.DefaultExceptionMappingBundle;
import com.porch.partner.client.PartnerDataClient;
import com.porch.partner.client.PartnerDataClientImpl;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.ws.rs.client.Client;
import java.net.URI;


public class DWService extends Application<MyConfig> {

    public static void main(String[] args) throws Exception {
        new DWService().run(args);
    }

    @Override
    public String getName() {
        return "dw-1.0.x-java-service";
    }

    @Override
    public void initialize(Bootstrap<MyConfig> bootstrap) {
        super.initialize(bootstrap);

        bootstrap.setConfigurationSourceProvider(new ModularConfigurationSourceProvider(this.getName(), new LuaConfigurationGenerator()));
        bootstrap.addBundle(new DefaultExceptionMappingBundle());
    }

    public void run(MyConfig config, Environment env) throws Exception {

        env.getObjectMapper().registerModule(new DefaultScalaModule());

        Client jerseyClient = new JerseyClientBuilder(env).using(config.jerseyClient).build(getName());
        PartnerDataClient partnerDataClient = new PartnerDataClientImpl(URI.create("http://localhost:9456"), jerseyClient);

        env.jersey().register(new MyResource(partnerDataClient));
    }
}
