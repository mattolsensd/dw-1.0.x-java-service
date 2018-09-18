package molsen.dw;

import com.porch.dropwizard.configuration.modular.ModularConfigurationSourceProvider;
import com.porch.dropwizard.configuration.modular.lua.LuaConfigurationGenerator;
import com.porch.dropwizard.core.bundle.ConfiguredSupplierBundle;
import com.porch.dropwizard.core.bundle.DefaultExceptionMappingBundle;
import com.porch.dropwizard.quartz.QuartzBundle;
import com.porch.dropwizard.quartz.QuartzBundleConfiguration;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import molsen.dw.scheduler.ExampleWorker;
import molsen.dw.scheduler.SchedulerBundle;


public class DWService extends Application<MyConfig> {

    public static void main(String[] args) throws Exception {
        new DWService().run(args);
    }

    ConfiguredSupplierBundle<ExampleWorker, MyConfig> exampleWorkerBundle = new ConfiguredSupplierBundle<ExampleWorker, MyConfig>() {
        @Override
        protected ExampleWorker createSupplied(MyConfig myConfig, Environment environment) throws Exception {
            return new ExampleWorker(myConfig.externalExampleWorkerSeconds);
        }
    };

    QuartzBundle<MyConfig> quartzBundle = new QuartzBundle<MyConfig>() {
        @Override
        protected QuartzBundleConfiguration getBundleConfig(MyConfig config) {
            return config.quartzBundleConfiguration;
        }
    };

    SchedulerBundle<MyConfig> schedulerBundle = new SchedulerBundle<MyConfig>(
            (MyConfig config) -> config.schedulerBundleConfig, quartzBundle, exampleWorkerBundle);

    @Override
    public String getName() {
        return "dw-1.0.x-java-service";
    }

    @Override
    public void initialize(Bootstrap<MyConfig> bootstrap) {
        super.initialize(bootstrap);

        bootstrap.setConfigurationSourceProvider(new ModularConfigurationSourceProvider(this.getName(), new LuaConfigurationGenerator()));
        bootstrap.addBundle(new DefaultExceptionMappingBundle());
        bootstrap.addBundle(quartzBundle);
        bootstrap.addBundle(exampleWorkerBundle);
        bootstrap.addBundle(schedulerBundle);
    }

    public void run(MyConfig config, Environment env) throws Exception {

        //Client jerseyClient = new JerseyClientBuilder(env).using(config.jerseyClient).build(getName());

        env.jersey().register(new MyResource());
    }
}
