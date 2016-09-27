package molsen.dw;

import com.porch.dropwizard.configuration.modular.ModularConfigurationSourceProvider;
import com.porch.dropwizard.configuration.modular.lua.LuaConfigurationGenerator;
import com.porch.dropwizard.core.bundle.DefaultExceptionMappingBundle;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;


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

        //Client jerseyClient = new JerseyClientBuilder(env).using(config.jerseyClient).build(getName());

        env.jersey().register(new MyResource());
    }
}
