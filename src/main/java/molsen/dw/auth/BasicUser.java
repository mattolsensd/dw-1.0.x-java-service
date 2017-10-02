package molsen.dw.auth;

import java.security.Principal;
import java.util.Set;

public class BasicUser implements Principal {

    private final String name;
    private final Set<String> roles;

    public BasicUser(String name, Set<String> roles) {
        this.name = name;
        this.roles = roles;
    }

    @Override
    public boolean equals(Object another) {
        return false;
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

    public Set<String> getRoles() {
        return roles;
    }
}
