package molsen.dw;

import com.porch.commons.response.ApiResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.CompletableFuture;


// TEST VARIOUS METHODS OF HANDLING OF FUTURES IN A RESOURCE


@Path("test/futures")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FuturesResource {

    // Block with .get and return result

    @GET
    @Path("get/success")
    public ApiResponse<String> testGetSuccess() throws Exception {
        CompletableFuture<String> future = CompletableFuture.completedFuture("Hello World");
        return ApiResponse.ok(future.get());
    }

    @GET
    @Path("get/failure")
    public ApiResponse<String> testGetFailure() throws Exception {
        CompletableFuture<String> future = new CompletableFuture<>();
        future.completeExceptionally(new WebApplicationException("BOOM"));
        return ApiResponse.ok(future.get());
    }

    // Return CompletableFuture

    @GET
    @Path("return/success")
    public CompletableFuture<ApiResponse<String>> testReturnFutureSuccess() throws Exception {
        CompletableFuture<String> future = CompletableFuture.completedFuture("Hello World");
        return future.thenApply(ApiResponse::ok);
    }

    @GET
    @Path("return/failure")
    public CompletableFuture<ApiResponse<String>> testReturnFutureFailure() throws Exception {
        CompletableFuture<String> future = new CompletableFuture<>();
        future.completeExceptionally(new WebApplicationException("BOOM"));
        return future.thenApply(ApiResponse::ok);
    }

}
