package molsen.dw;

import com.porch.commons.response.ApiError;
import com.porch.commons.response.ApiResponse;
import com.porch.commons.response.ValidationError;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.List;

class MyValidationException extends BadRequestException {

    public static final String MESSAGE = "Request validation failed";

    private List<ValidationError> validationErrors;
    private String message;

    MyValidationException(List<ValidationError> validationErrors) {
        this.validationErrors = validationErrors;
        this.message = MESSAGE;
    }

    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public Response getResponse() {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(ApiResponse.failed(new ApiError(ApiError.VALIDATION_ERROR, message, validationErrors))).build();
    }

}
