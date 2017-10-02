package molsen.dw.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class BasicAuthenticator implements Authenticator<BasicCredentials, BasicUser> {

    private static final String TEST_USER = "test";
    private static final String TEST_PASS = "test";
    private static final Set<String> TEST_ROLES = Collections.singleton("TestRole1");

    @Override
    public Optional<BasicUser> authenticate(BasicCredentials credentials) throws AuthenticationException {
        if (TEST_USER.equals(credentials.getUsername()) && TEST_PASS.equals(credentials.getPassword())) {
            return Optional.of(new BasicUser(credentials.getUsername(), TEST_ROLES));
        }
        return Optional.empty();
    }
}