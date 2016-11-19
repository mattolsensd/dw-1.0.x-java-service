package molsen.dw;

import com.porch.commons.response.ApiError;
import com.porch.commons.response.ApiResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.CompletableFuture;

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

    // Error Handling

    @GET
    @Path("try-and-return")
    public ApiResponse<String> testTryAndReturnFailure() throws Exception {
        try {
            CompletableFuture<ApiResponse<String>> future = new CompletableFuture<>();
            future.completeExceptionally(new WebApplicationException("BOOM"));
            return future.get();
        } catch (Exception e) {
            return ApiResponse.failed(new ApiError(ApiError.UNKNOWN, e.getMessage()));
        }
    }

    @GET
    @Path("try-and-throw")
    public ApiResponse<String> testTryAndThrowFailure() throws Exception {
        try {
            CompletableFuture<ApiResponse<String>> future = new CompletableFuture<>();
            future.completeExceptionally(new WebApplicationException("BOOM"));
            return future.get();
        } catch (Exception e) {
            throw new InternalServerErrorException("My future failed!", e);
        }
    }

}
