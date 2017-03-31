package molsen.dw;

import com.porch.commons.response.ApiError;
import com.porch.commons.response.ApiResponse;
import com.porch.dropwizard.core.jersey.errors.LoggingExceptionMapper;
import io.dropwizard.testing.junit.ResourceTestRule;
import molsen.dw.util.ValidationErrorTestWrapper;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.net.URI;

import static org.junit.Assert.*;

public class MyResourceTest {

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new MyResource())
            // Set up exception mapping to match the real service
            .setRegisterDefaultExceptionMappers(false)
            .addProvider(new LoggingExceptionMapper())
            .build();

    private static final URI baseUri = URI.create("http://localhost:8080");

    private static final GenericType<ApiResponse<String>> API_RESPONSE_OF_STRING = new GenericType<ApiResponse<String>>() {
    };
    private static final GenericType<ApiResponse<MyDTO>> API_RESPONSE_OF_MY_DTO = new GenericType<ApiResponse<MyDTO>>() {
    };

    @Test
    public void shouldRespondWithString() throws Exception {
        final ApiResponse<String> response = resources.client()
                .target(baseUri)
                .path("test/success/string").request()
                .get(API_RESPONSE_OF_STRING);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertTrue(response.getBody().isPresent());
        final String result = response.getBody().get();
        assertEquals(MyResource.MY_STRING, result);
    }

    @Test
    public void shouldRespondWithDTO() throws Exception {
        final ApiResponse<MyDTO> response = resources.client()
                .target(baseUri)
                .path("test/success/dto").request()
                .get(API_RESPONSE_OF_MY_DTO);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertTrue(response.getBody().isPresent());
        final MyDTO result = response.getBody().get();
        assertEquals(MyResource.MY_DTO, result);
    }

    // ## Error responses with 2-xx Status Codes ##

    // This happens when you return a failed ApiResponse directly
    // as opposed to thrown WAEs, which map to responses with proper status codes

    // Of course, you can return a failed ApiResponse with proper status code with something like
    // return Response.status(400).entity(ApiResponse.failed(ApiError.BAD_REQUEST, "Bad request message")).build();
    // However, I can't think of any reason to go this route - just rely on the ExceptionMapper!

    // NOTE: The ApiError message property will be null if no message is supplied in the ApiError creation
    // As opposed to ApiErrors generated by a thrown exception; which supply default messages such as "HTTP 400 Bad Request" (if no message passed in to exception)


    @Test
    public void shouldRespond200BadRequest() throws Exception {
        // Returns failed ApiResponse with 200 status
        final ApiResponse<MyDTO> response = resources.client()
                .target(baseUri)
                .path("test/failure/bad-request").request()
                .get(API_RESPONSE_OF_MY_DTO);

        assertNotNull(response);
        assertTrue(!response.isSuccess());
        assertTrue(response.getError().isPresent());
        final ApiError e = response.getError().get();
        assertEquals(ApiError.BAD_REQUEST, e.getCode());
        assertEquals(MyResource.MY_BAD_REQUEST_MESSAGE, e.getMessage());
    }

    @Test
    public void shouldRespond200Conflict() throws Exception {
        // Returns failed ApiResponse with 200 status
        final ApiResponse<MyDTO> response = resources.client()
                .target(baseUri)
                .path("test/failure/conflict").request()
                .get(API_RESPONSE_OF_MY_DTO);

        assertNotNull(response);
        assertTrue(!response.isSuccess());
        assertTrue(response.getError().isPresent());
        final ApiError e = response.getError().get();
        assertEquals(ApiError.CONFLICT, e.getCode());
    }

    // shouldRespond200Forbidden
    // shouldRespond200NotFound
    // shouldRespond200Unauthorized
    // shouldRespond200ISE

    @Test
    public void shouldRespond200ValidationError() throws Exception {
        // Returns failed ApiResponse with 200 status
        final ApiResponse<MyDTO> response = resources.client()
                .target(baseUri)
                .path("test/failure/validation-error").request()
                .get(API_RESPONSE_OF_MY_DTO);

        assertNotNull(response);
        assertTrue(!response.isSuccess());
        assertTrue(response.getError().isPresent());
        final ApiError e = response.getError().get();
        assertEquals(ApiError.VALIDATION_ERROR, e.getCode());
        assertEquals("", e.getMessage());
        ValidationErrorTestWrapper.assertEqual(MyResource.MY_VALIDATION_ERRORS, e.getValidationErrors());
    }

    @Test
    public void shouldRespond200ValidationErrorWithCustomMessage() throws Exception {
        // Returns failed ApiResponse with 200 status
        final ApiResponse<MyDTO> response = resources.client()
                .target(baseUri)
                .path("test/failure/validation-error-with-custom-message").request()
                .get(API_RESPONSE_OF_MY_DTO);

        assertNotNull(response);
        assertTrue(!response.isSuccess());
        assertTrue(response.getError().isPresent());
        final ApiError e = response.getError().get();
        assertEquals(ApiError.VALIDATION_ERROR, e.getCode());
        assertEquals(MyResource.MY_VALIDATION_ERROR_MESSAGE, e.getMessage());
        ValidationErrorTestWrapper.assertEqual(MyResource.MY_VALIDATION_ERRORS, e.getValidationErrors());
    }

    // ## Error responses with non-2xx Status Codes ##

    // JerseyInvocation.translate converts the raw response to the expected type or to an exception
    // it checks if response.getStatusInfo().getFamily() == Family.SUCCESSFUL
    // If so, it tries to parse and return the response (If this fails, it throws ResponseProcessingException)
    // If not, it converts to an javax.ws.rs exception based on the response status code

    // The two BadRequest tests below show two different ways of handling error responses

    @Test
    public void shouldRespond400BadRequest() throws Exception {
        // Service returns failed ApiResponse with 400 status
        // Client throws BadRequestException
        try {
            resources.client()
                    .target(baseUri)
                    .path("test/throw/bad-request")
                    .request()
                    // Passing expected type here causes client to throw an exception
                    // if the response status code is non-2xx
                    .get(API_RESPONSE_OF_MY_DTO);
            throw new Exception("Should not execute");
        } catch (BadRequestException e) {
            // Caught exception as expected
            assertNotNull(e);
            assertNotNull(e.getResponse());
            final ApiResponse<MyDTO> entity = e.getResponse().readEntity(API_RESPONSE_OF_MY_DTO);
            assertNotNull(entity);
            assertFalse(entity.isSuccess());
            assertTrue(entity.getError().isPresent());
            final ApiError apiError = entity.getError().get();
            assertEquals(MyResource.MY_BAD_REQUEST_MESSAGE, apiError.getMessage());
        }
    }

    @Test
    public void shouldAlsoRespond400BadRequest() throws Exception {
        // Service returns failed ApiResponse with 400 status
        // Client deserializes the response body into an ApiResponse and returns it
        final Response response = resources.client()
                .target(baseUri)
                .path("test/throw/bad-request")
                .request()
                // not passing expected type here avoids the exception
                .get();

        assertNotNull(response);
        assertEquals(400, response.getStatus());

        final ApiResponse<MyDTO> apiResponse = response
                .readEntity(API_RESPONSE_OF_MY_DTO);

        assertNotNull(apiResponse);
        assertTrue(!apiResponse.isSuccess());
        assertTrue(apiResponse.getError().isPresent());
        final ApiError e = apiResponse.getError().get();
        assertEquals(ApiError.BAD_REQUEST, e.getCode());
        assertEquals(MyResource.MY_BAD_REQUEST_MESSAGE, e.getMessage());
    }

    @Test
    public void shouldRespond409Conflict() throws Exception {
        final Response response = resources.client()
                .target(baseUri)
                .path("test/throw/conflict")
                .request()
                .get();

        assertNotNull(response);
        assertEquals(409, response.getStatus());

        final ApiResponse<MyDTO> apiResponse = response
                .readEntity(API_RESPONSE_OF_MY_DTO);

        assertNotNull(apiResponse);
        assertTrue(!apiResponse.isSuccess());
        assertTrue(apiResponse.getError().isPresent());
        final ApiError e = apiResponse.getError().get();
        assertEquals(ApiError.CONFLICT, e.getCode());
        assertEquals(MyResource.MY_CONFLICT_MESSAGE, e.getMessage());
    }

    // shouldRespond403Forbidden
    // shouldRespond404NotFound
    // shouldRespond401Unauthorized

    @Test
    public void shouldRespond400ValidationError() throws Exception {
        final Response response = resources.client()
                .target(baseUri)
                .path("test/throw/validation-error")
                .request()
                .get();

        assertNotNull(response);
        assertEquals(400, response.getStatus());

        final ApiResponse<MyDTO> apiResponse = response
                .readEntity(API_RESPONSE_OF_MY_DTO);

        assertNotNull(apiResponse);
        assertTrue(!apiResponse.isSuccess());
        assertTrue(apiResponse.getError().isPresent());
        final ApiError e = apiResponse.getError().get();
        assertEquals(ApiError.VALIDATION_ERROR, e.getCode());
        assertEquals(MyResource.MY_VALIDATION_ERROR_MESSAGE, e.getMessage());
        ValidationErrorTestWrapper.assertEqual(MyResource.MY_VALIDATION_ERRORS, e.getValidationErrors());

    }

    @Test
    public void shouldRespond500ISE() throws Exception {
        final Response response = resources.client()
                .target(baseUri)
                .path("test/throw/ise")
                .request()
                .get();

        assertNotNull(response);
        assertEquals(500, response.getStatus());

        final ApiResponse<MyDTO> apiResponse = response
                .readEntity(API_RESPONSE_OF_MY_DTO);

        assertNotNull(apiResponse);
        assertTrue(!apiResponse.isSuccess());
        assertTrue(apiResponse.getError().isPresent());
        final ApiError e = apiResponse.getError().get();
        assertEquals(ApiError.SERVER_ERROR, e.getCode());
        assertEquals(MyResource.MY_ISE_MESSAGE, e.getMessage());
    }

    @Test
    public void shouldAlsoRespond500ISE() throws Exception {
        final Response response = resources.client()
                .target(baseUri)
                .path("test/throw/wae")
                .request()
                .get();

        assertNotNull(response);
        assertEquals(500, response.getStatus());

        final ApiResponse<MyDTO> apiResponse = response
                .readEntity(API_RESPONSE_OF_MY_DTO);

        assertNotNull(apiResponse);
        assertTrue(!apiResponse.isSuccess());
        assertTrue(apiResponse.getError().isPresent());
        final ApiError e = apiResponse.getError().get();
        assertEquals(ApiError.SERVER_ERROR, e.getCode());
        assertEquals(MyResource.MY_WAE_MESSAGE, e.getMessage());
    }

    @Test
    public void shouldAlsoRespond500ISEWithLoggingExceptionMapperMessage() throws Exception {
        final Response response = resources.client()
                .target(baseUri)
                .path("test/npe")
                .request()
                .get();

        assertNotNull(response);
        assertEquals(500, response.getStatus());

        final ApiResponse<MyDTO> apiResponse = response
                .readEntity(API_RESPONSE_OF_MY_DTO);

        assertNotNull(apiResponse);
        assertTrue(!apiResponse.isSuccess());
        assertTrue(apiResponse.getError().isPresent());
        final ApiError e = apiResponse.getError().get();
        assertEquals(ApiError.SERVER_ERROR, e.getCode());
        assertTrue(e.getMessage().contains("There was an error processing your request. It has been logged"));
    }

}