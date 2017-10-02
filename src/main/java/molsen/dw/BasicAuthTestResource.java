package molsen.dw;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("basic-auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BasicAuthTestResource {

    @GET
    @Path("test1")
    @PermitAll
    public String test1() {
        return "test1 success";
    }

    @GET
    @Path("test2")
    @RolesAllowed({"TestRole2"})
    public String test2() {
        return "test2 success";
    }

    @GET
    @Path("test3")
    @RolesAllowed({"TestRole1", "TestRole2"})
    public String test3() {
        return "test3 success";
    }

}
