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
import java.util.concurrent.Future;

@Path("test/futures")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FuturesResource {

    @GET
    @Path("get/success")
    public ApiResponse<String> testGetSuccess() throws Exception {
        Future<String> future = CompletableFuture.completedFuture("Hello World");
        return ApiResponse.ok(future.get());
    }

    @GET
    @Path("get/failure")
    public ApiResponse<String> testGetFailure() throws Exception {
        Future<String> future = new CompletableFuture<>();
        future.completeExceptionally(new WebApplicationException("BOOM"));
        return ApiResponse.ok(future.get());
    }

    @GET
    @Path("return/success")
    public Future<ApiResponse<String>> testSyncSuccess() throws Exception {
        return CompletableFuture.completedFuture(ApiResponse.ok("Hello World"));
    }

    @GET
    @Path("return/failure")
    public Future<ApiResponse<String>> testSyncFailure() throws Exception {
        Future<String> future = new CompletableFuture<>();
        future.completeExceptionally(new WebApplicationException("BOOM"));
        return future.map(v = > ApiResponse.ok(v));
    }

    @GET
    @Path("try-and-return/success")
    public ApiResponse<String> testTryAndReturnSuccess() throws Exception {
        try {
            return CompletableFuture.completedFuture(ApiResponse.ok("Hello World")).get();
        } catch (Exception e) {
            return ApiResponse.failed(new ApiError(ApiError.UNKNOWN, e.getMessage()));
        }
    }

    @GET
    @Path("try-and-return/failure")
    public ApiResponse<String> testTryAndReturnFailure() throws Exception {
        try {
            Future<String> future = new CompletableFuture<>();
            future.completeExceptionally(new WebApplicationException("BOOM"));
            future.get();
        } catch (Exception e) {
            return ApiResponse.failed(new ApiError(ApiError.UNKNOWN, e.getMessage()));
        }
    }

    @GET
    @Path("try-and-throw/success")
    public ApiResponse<String> testTryAndThrowSuccess() throws Exception {
        try {
            return CompletableFuture.completedFuture(ApiResponse.ok("Hello World")).get();
        } catch (Exception e) {
            throw new InternalServerErrorException("My future failed!", e);
        }
    }

    @GET
    @Path("try-and-throw/failure")
    public ApiResponse<String> testTryAndThrowFailure() throws Exception {
        try {
            Future<String> future = new CompletableFuture<>();
            future.completeExceptionally(new WebApplicationException("BOOM"));
            future.get();
        } catch (Exception e) {
            throw new InternalServerErrorException("My future failed!", e);
        }
    }

}
