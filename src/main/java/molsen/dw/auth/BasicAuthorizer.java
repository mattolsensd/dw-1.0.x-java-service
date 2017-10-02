package molsen.dw.auth;

import io.dropwizard.auth.Authorizer;

public class BasicAuthorizer implements Authorizer<BasicUser> {
    @Override
    public boolean authorize(BasicUser user, String role) {
        return user.getRoles() != null && user.getRoles().contains(role);
    }
}