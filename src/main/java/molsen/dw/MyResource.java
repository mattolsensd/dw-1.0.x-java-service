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

    public static final String MY_STRING = "Hello Test";

    public static final MyDTO MY_DTO = new MyDTO(1L, "Thing1");

    public static final String MY_BAD_REQUEST_MESSAGE = "The server cannot or will not process the request due to an apparent client error (e.g., malformed request syntax, too large size, invalid request message framing, or deceptive request routing).";
    public static final String MY_CONFLICT_MESSAGE = "Indicates that the request could not be processed because of conflict in the request, such as an edit conflict between multiple simultaneous updates.";
    public static final String MY_FORBIDDEN_MESSAGE = "The request was valid, but the server is refusing action. The user might not have the necessary permissions for a resource.";
    public static final String MY_NOT_FOUND_MESSAGE = "The requested resource could not be found but may be available in the future. Subsequent requests by the client are permissible.";
    public static final String MY_UNAUTHORIZED_MESSAGE = "Similar to 403 Forbidden, but specifically for use when authentication is required and has failed or has not yet been provided.";
    public static final String MY_ISE_MESSAGE = "A generic error message, given when an unexpected condition was encountered and no more specific message is suitable.";
    public static final String MY_WAE_MESSAGE = "Throwing a WebApplicationException will also result in a server error response";

    // Validation Errors are a special form of badRequest response
    public static final String MY_VALIDATION_ERROR_MESSAGE = "Request validation failed";

    public static final List<ValidationError> MY_VALIDATION_ERRORS = Lists.newArrayList(
            new ValidationError("id", "id is required"),
            new ValidationError("name", "That's a crap name")
    );

    // SUCCEED

    @GET
    @Path("success/string")
    public ApiResponse<String> respondWithString() {
        return ApiResponse.ok(MY_STRING);
    }

    @GET
    @Path("success/dto")
    public ApiResponse<MyDTO> respondWithDto() {
        return ApiResponse.ok(MY_DTO);
    }

    // FAIL

    @GET
    @Path("failure/bad-request")
    public ApiResponse<MyDTO> respondBadRequest() {
        return ApiResponse.failed(ApiError.BAD_REQUEST, MY_BAD_REQUEST_MESSAGE);
    }

    @GET
    @Path("failure/conflict")
    public ApiResponse<MyDTO> respondConflict() {
        return ApiResponse.failed(ApiError.CONFLICT, MY_CONFLICT_MESSAGE);
    }

    @GET
    @Path("failure/forbidden")
    public ApiResponse<MyDTO> respondForbidden() {
        return ApiResponse.failed(ApiError.FORBIDDEN, MY_FORBIDDEN_MESSAGE);
    }

    @GET
    @Path("failure/not-found")
    public ApiResponse<MyDTO> respondNotFound() {
        return ApiResponse.failed(ApiError.NOT_FOUND, MY_NOT_FOUND_MESSAGE);
    }

    @GET
    @Path("failure/unauthorized")
    public ApiResponse<MyDTO> respondUnauthorized() {
        return ApiResponse.failed(ApiError.UNAUTHORIZED, MY_UNAUTHORIZED_MESSAGE);
    }

    @GET
    @Path("failure/validation-error")
    public ApiResponse<MyDTO> respondValidationError() {
        return ApiResponse.failed(MY_VALIDATION_ERRORS);
    }

    @GET
    @Path("failure/validation-error-with-custom-message")
    public ApiResponse<MyDTO> respondValidationErrorWithCustomMessage() {
        return ApiResponse.failed(new ApiError(ApiError.VALIDATION_ERROR, MY_VALIDATION_ERROR_MESSAGE, MY_VALIDATION_ERRORS));
    }

    @GET
    @Path("failure/ise")
    public ApiResponse<MyDTO> respondISE() {
        return ApiResponse.failed(ApiError.SERVER_ERROR, MY_ISE_MESSAGE);
    }

    // THROW

    @GET
    @Path("throw/bad-request")
    public ApiResponse<MyDTO> throwBadRequest() {
        throw new BadRequestException(MY_BAD_REQUEST_MESSAGE);
    }

    @GET
    @Path("throw/conflict")
    public ApiResponse<MyDTO> throwConflict() {
        throw new WebApplicationException(MY_CONFLICT_MESSAGE, Response.Status.CONFLICT);
    }

    @GET
    @Path("throw/forbidden")
    public ApiResponse<MyDTO> throwForbidden() {
        throw new ForbiddenException(MY_FORBIDDEN_MESSAGE);
    }

    @GET
    @Path("throw/not-found")
    public ApiResponse<MyDTO> throwNotFound() {
        throw new NotFoundException(MY_NOT_FOUND_MESSAGE);
    }

    @GET
    @Path("throw/unauthroized")
    public ApiResponse<MyDTO> throwUnauthorized() {
        throw new WebApplicationException(MY_UNAUTHORIZED_MESSAGE, Response.Status.UNAUTHORIZED);
    }

    @GET
    @Path("throw/validation-error")
    public ApiResponse<MyDTO> throwValidationError() {
        throw new MyValidationException(MY_VALIDATION_ERRORS);
    }

    @GET
    @Path("throw/ise")
    public ApiResponse<MyDTO> throwISE() {
        throw new InternalServerErrorException(MY_ISE_MESSAGE);
    }

    @GET
    @Path("throw/wae")
    public ApiResponse<MyDTO> throwWAE() {
        throw new WebApplicationException(MY_WAE_MESSAGE);
    }

    // BREAK

    @GET
    @Path("npe")
    public ApiResponse<MyDTO> npe() {
        MyDTO myDto = null;
        return ApiResponse.ok(new MyDTO(myDto.getId(), myDto.getName()));
    }

}
