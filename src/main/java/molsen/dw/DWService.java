package molsen.dw;

import com.porch.dropwizard.client.ClientSupplierBundle;
import com.porch.dropwizard.configuration.modular.ModularConfigurationSourceProvider;
import com.porch.dropwizard.configuration.modular.lua.LuaConfigurationGenerator;
import com.porch.dropwizard.core.bundle.DefaultExceptionMappingBundle;
import com.porch.partner.auth.HeaderAuthenticatorBundle;
import com.porch.partner.auth.HeaderAuthenticatorBundleConfiguration;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;


public class DWService extends Application<MyConfig> {

    private ClientSupplierBundle<MyConfig> clientSupplierBundle = new ClientSupplierBundle<MyConfig>(getName()) {
        @Override
        protected JerseyClientConfiguration getClientConfiguration(MyConfig configuration) {
            return configuration.jerseyClient;
        }
    };

    private HeaderAuthenticatorBundle<MyConfig> headerAuthenticatorBundle = new HeaderAuthenticatorBundle<MyConfig>(clientSupplierBundle) {
        @Override
        protected HeaderAuthenticatorBundleConfiguration getConfigInternal(MyConfig configuration) {
            return new HeaderAuthenticatorBundleConfiguration(configuration.partnerDataUrl);
        }
    };


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
        bootstrap.addBundle(clientSupplierBundle);
        bootstrap.addBundle(headerAuthenticatorBundle);
    }

    public void run(MyConfig config, Environment env) throws Exception {
        env.jersey().register(new MyResource());
    }
}
