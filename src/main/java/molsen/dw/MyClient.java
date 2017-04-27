package molsen.dw;

import com.porch.commons.response.ApiResponse;
import com.porch.dropwizard.client.PorchJerseyClient;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.net.URI;
import java.util.concurrent.Future;

public class MyClient extends PorchJerseyClient {

    private static final GenericType<ApiResponse<String>> API_RESPONSE_OF_STRING = new GenericType<ApiResponse<String>>() {
    };
    private static final GenericType<ApiResponse<MyDTO>> API_RESPONSE_OF_MY_DTO = new GenericType<ApiResponse<MyDTO>>() {
    };

    MyClient(URI baseUri, Client client) {
        super(baseUri, client);
    }

    @Override
    protected WebTarget getWebTarget() {
        return super.getWebTarget().path("test");
    }

    public Future<ApiResponse<String>> respondWithString() {
        WebTarget webTarget = getWebTarget().path("success/string");
        return getAsync(webTarget, API_RESPONSE_OF_STRING);
    }

    public Future<ApiResponse<MyDTO>> respondWithDTO() {
        WebTarget webTarget = getWebTarget().path("success/dto");
        return getAsync(webTarget, API_RESPONSE_OF_MY_DTO);
    }

    public Future<ApiResponse<MyDTO>> respondBadRequest() {
        WebTarget webTarget = getWebTarget().path("failure/bad-request");
        return getAsync(webTarget, API_RESPONSE_OF_MY_DTO);
    }

    public Future<ApiResponse<MyDTO>> respondConflict() {
        WebTarget webTarget = getWebTarget().path("failure/conflict");
        return getAsync(webTarget, API_RESPONSE_OF_MY_DTO);
    }

    public Future<ApiResponse<MyDTO>> respondForbidden() {
        WebTarget webTarget = getWebTarget().path("failure/forbidden");
        return getAsync(webTarget, API_RESPONSE_OF_MY_DTO);
    }

    public Future<ApiResponse<MyDTO>> respondNotFound() {
        WebTarget webTarget = getWebTarget().path("failure/not-found");
        return getAsync(webTarget, API_RESPONSE_OF_MY_DTO);
    }

    public Future<ApiResponse<MyDTO>> respondUnauthorized() {
        WebTarget webTarget = getWebTarget().path("failure/unauthorized");
        return getAsync(webTarget, API_RESPONSE_OF_MY_DTO);
    }

    public Future<ApiResponse<MyDTO>> respondValidationError() {
        WebTarget webTarget = getWebTarget().path("failure/validation-error");
        return getAsync(webTarget, API_RESPONSE_OF_MY_DTO);
    }

    public Future<ApiResponse<MyDTO>> respondValidationErrorWithCustomMessage() {
        WebTarget webTarget = getWebTarget().path("failure/validation-error-with-custom-message");
        return getAsync(webTarget, API_RESPONSE_OF_MY_DTO);
    }

    public Future<ApiResponse<MyDTO>> respondISE() {
        WebTarget webTarget = getWebTarget().path("failure/ise");
        return getAsync(webTarget, API_RESPONSE_OF_MY_DTO);
    }

    // THROW

    public Future<ApiResponse<MyDTO>> throwBadRequest() {
        WebTarget webTarget = getWebTarget().path("throw/bad-request");
        return getAsync(webTarget, API_RESPONSE_OF_MY_DTO);
    }

    public Future<ApiResponse<MyDTO>> throwConflict() {
        WebTarget webTarget = getWebTarget().path("throw/conflict");
        return getAsync(webTarget, API_RESPONSE_OF_MY_DTO);
    }

    public Future<ApiResponse<MyDTO>> throwForbidden() {
        WebTarget webTarget = getWebTarget().path("throw/forbidden");
        return getAsync(webTarget, API_RESPONSE_OF_MY_DTO);
    }

    public Future<ApiResponse<MyDTO>> throwNotFound() {
        WebTarget webTarget = getWebTarget().path("throw/not-found");
        return getAsync(webTarget, API_RESPONSE_OF_MY_DTO);
    }

    public Future<ApiResponse<MyDTO>> throwUnauthorized() {
        WebTarget webTarget = getWebTarget().path("throw/unauthroized");
        return getAsync(webTarget, API_RESPONSE_OF_MY_DTO);
    }

    public Future<ApiResponse<MyDTO>> throwValidationError() {
        WebTarget webTarget = getWebTarget().path("throw/validation-error");
        return getAsync(webTarget, API_RESPONSE_OF_MY_DTO);
    }

    public Future<ApiResponse<MyDTO>> throwISE() {
        WebTarget webTarget = getWebTarget().path("throw/ise");
        return getAsync(webTarget, API_RESPONSE_OF_MY_DTO);
    }

    public Future<ApiResponse<MyDTO>> throwWAE() {
        WebTarget webTarget = getWebTarget().path("throw/wae");
        return getAsync(webTarget, API_RESPONSE_OF_MY_DTO);
    }

    // BREAK

    public Future<ApiResponse<MyDTO>> npe() {
        WebTarget webTarget = getWebTarget().path("npe");
        return getAsync(webTarget, API_RESPONSE_OF_MY_DTO);
    }
}
