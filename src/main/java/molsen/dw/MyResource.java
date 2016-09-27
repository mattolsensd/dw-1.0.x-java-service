package molsen.dw;

import com.google.common.collect.Lists;
import com.porch.commons.response.ApiError;
import com.porch.commons.response.ApiResponse;
import com.porch.commons.response.ValidationError;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("test")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MyResource {

    private List<ValidationError> validationErrors = Lists.newArrayList(
            new ValidationError("id", "id is required"),
            new ValidationError("name", "That's a crap name")
    );

    // SUCCEED

    @GET
    @Path("success/string")
    public ApiResponse<String> respondWithString() {
        return ApiResponse.ok("Hello Test");
    }

    @GET
    @Path("success/dto")
    public ApiResponse<MyDTO> respondWithDto() {
        return ApiResponse.ok(new MyDTO(1L, "Thing1"));
    }

    // FAIL

    @GET
    @Path("failure/bad-request")
    public ApiResponse<MyDTO> respondBadRequest() {
        return ApiResponse.failed(ApiError.BAD_REQUEST);
    }

    @GET
    @Path("failure/conflict")
    public ApiResponse<MyDTO> respondConflict() {
        return ApiResponse.failed(ApiError.CONFLICT);
    }

    @GET
    @Path("failure/forbidden")
    public ApiResponse<MyDTO> respondForbidden() {
        return ApiResponse.failed(ApiError.FORBIDDEN);
    }

    @GET
    @Path("failure/not-found")
    public ApiResponse<MyDTO> respondNotFound() {
        return ApiResponse.failed(ApiError.NOT_FOUND);
    }

    @GET
    @Path("failure/unauthorized")
    public ApiResponse<MyDTO> respondUnauthorized() {
        return ApiResponse.failed(ApiError.UNAUTHORIZED);
    }

    @GET
    @Path("failure/validation-error")
    public ApiResponse<MyDTO> respondBadRequestWithMessage() {
        return ApiResponse.failed(new ApiError(ApiError.BAD_REQUEST, "Validation failed", validationErrors));
    }

    @GET
    @Path("failure/ise")
    public ApiResponse<MyDTO> responseISE() {
        return ApiResponse.failed(ApiError.SERVER_ERROR);
    }

    // THROW

    @GET
    @Path("throw/bad-request")
    public ApiResponse<MyDTO> throwBadRequest() {
        throw new BadRequestException("That didn't make sense");
    }

    @GET
    @Path("throw/conflict")
    public ApiResponse<MyDTO> throwConflict() {
        throw new WebApplicationException("OMG CONFLICT!", Response.Status.CONFLICT);
    }

    @GET
    @Path("throw/forbidden")
    public ApiResponse<MyDTO> throwForbidden() {
        throw new ForbiddenException("You can't have that");
    }

    @GET
    @Path("throw/not-found")
    public ApiResponse<MyDTO> throwNotFound() {
        throw new NotFoundException("I didn't find it");
    }

    @GET
    @Path("throw/unauthroized")
    public ApiResponse<MyDTO> throwUnauthorized() {
        throw new WebApplicationException("Who are you?", Response.Status.UNAUTHORIZED);
    }

    @GET
    @Path("throw/validation-error")
    public ApiResponse<MyDTO> throwValidationError() {
        throw new BadRequestException("One of your things has the wrong thing:\n\n" + StringUtils.join(validationErrors.stream().map(ValidationError::getMessage), "\n"));
    }

    @GET
    @Path("throw/ise")
    public ApiResponse<MyDTO> throwISE() {
        throw new InternalServerErrorException("EXPLODE!");
    }

    @GET
    @Path("throw/wae")
    public ApiResponse<MyDTO> throwWAE() {
        throw new WebApplicationException("EXPLODE!");
    }

    // BREAK

    @GET
    @Path("npe")
    public ApiResponse<MyDTO> npe() {
        MyDTO myDto = null;
        return ApiResponse.ok(new MyDTO(myDto.getId(), myDto.getName()));
    }

}
