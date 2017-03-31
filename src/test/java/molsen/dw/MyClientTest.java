package molsen.dw;

import com.porch.commons.response.ApiError;
import com.porch.commons.response.ApiResponse;
import com.porch.dropwizard.core.jersey.errors.EarlyEofExceptionMapper;
import com.porch.dropwizard.core.jersey.errors.LoggingExceptionMapper;
import com.porch.dropwizard.core.jersey.jackson.JsonProcessingExceptionMapper;
import com.porch.dropwizard.core.jersey.validation.ConstraintViolationExceptionMapper;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.junit.DropwizardClientRule;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;

import static org.junit.Assert.*;

public class MyClientTest {

    // Here we are running the actual resource class to test against
    // (In this case, it makes no external calls and returns static data)
    // Alternatively, we could build a mock resource class to test against
    // This would prevent downstream calls going to other services
    // and also allow us to specify (and predict) return values
    // Of course, when using a resource double, the test will not
    // catch path errors or problems caused by changes to the API
    // (You will have to update the mocks to reflect API changes)
    // Mocking is necessary to test a client for an external resource

    // Build
    //@Path("/test")
    //public static class MyResourceDouble {
    //    @GET
    //    @Path("success/string")
    //    public ApiResponse<String> respondWithString() {
    //        return ApiResponse.ok("My String");
    //    }
    //}

    @ClassRule
    public static final DropwizardClientRule dropwizard = new DropwizardClientRule(
            new MyResource(),
            // Set up exception mapping to match the real service
            ConstraintViolationExceptionMapper.class,
            EarlyEofExceptionMapper.class,
            JsonProcessingExceptionMapper.class,
            LoggingExceptionMapper.class
    );

    private static class MyClientProvider {

        private final static Client client = new JerseyClientBuilder(dropwizard.getEnvironment()).build("test-client");
        private final static MyClient myClient = new MyClient(dropwizard.baseUri(), client);

        static MyClient get() {
            return myClient;
        }
    }

    @Test
    public void shouldReturnString() throws Exception {
        final ApiResponse<String> response = MyClientProvider.get().respondWithString().get();

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertTrue(response.getBody().isPresent());
        final String result = response.getBody().get();
        assertEquals(MyResource.MY_STRING, result);
    }

    @Test
    public void shouldReturnDTO() throws Exception {
        final ApiResponse<MyDTO> response = MyClientProvider.get().respondWithDTO().get();

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertTrue(response.getBody().isPresent());
        final MyDTO result = response.getBody().get();
        assertEquals(MyResource.MY_DTO, result);
    }

    @Test
    public void shouldReturnBadRequest() throws Exception {
        final ApiResponse<MyDTO> response = MyClientProvider.get().respondBadRequest().get();

        assertNotNull(response);
        assertTrue(!response.isSuccess());
        assertTrue(response.getError().isPresent());
        final ApiError e = response.getError().get();
        assertEquals(ApiError.BAD_REQUEST, e.getCode());
        assertEquals(MyResource.MY_BAD_REQUEST_MESSAGE, e.getMessage());
    }

    @Test
    public void shouldReturnConflict() throws Exception {
        final ApiResponse<MyDTO> response = MyClientProvider.get().respondConflict().get();

        assertNotNull(response);
        assertTrue(!response.isSuccess());
        assertTrue(response.getError().isPresent());
        final ApiError e = response.getError().get();
        assertEquals(ApiError.CONFLICT, e.getCode());
        assertEquals(MyResource.MY_CONFLICT_MESSAGE, e.getMessage());
    }

    @Test
    public void shouldReturnForbidden() throws Exception {
        final ApiResponse<MyDTO> response = MyClientProvider.get().respondForbidden().get();

        assertNotNull(response);
        assertTrue(!response.isSuccess());
        assertTrue(response.getError().isPresent());
        final ApiError e = response.getError().get();
        assertEquals(ApiError.FORBIDDEN, e.getCode());
        assertEquals(MyResource.MY_FORBIDDEN_MESSAGE, e.getMessage());
    }

    @Test
    public void shouldReturnNotFound() throws Exception {
        final ApiResponse<MyDTO> response = MyClientProvider.get().respondNotFound().get();

        assertNotNull(response);
        assertTrue(!response.isSuccess());
        assertTrue(response.getError().isPresent());
        final ApiError e = response.getError().get();
        assertEquals(ApiError.NOT_FOUND, e.getCode());
        assertEquals(MyResource.MY_NOT_FOUND_MESSAGE, e.getMessage());
    }

    @Test
    public void shouldReturnUnauthorized() throws Exception {
        final ApiResponse<MyDTO> response = MyClientProvider.get().respondUnauthorized().get();

        assertNotNull(response);
        assertTrue(!response.isSuccess());
        assertTrue(response.getError().isPresent());
        final ApiError e = response.getError().get();
        assertEquals(ApiError.UNAUTHORIZED, e.getCode());
        assertEquals(MyResource.MY_UNAUTHORIZED_MESSAGE, e.getMessage());
    }

    @Test
    public void shouldReturnValidationError() throws Exception {
        final ApiResponse<MyDTO> response = MyClientProvider.get().respondValidationError().get();

        assertNotNull(response);
        assertTrue(!response.isSuccess());
        assertTrue(response.getError().isPresent());
        final ApiError e = response.getError().get();
        assertEquals(ApiError.VALIDATION_ERROR, e.getCode());
        assertEquals("", e.getMessage());
    }

    @Test
    public void shouldReturnValidationErrorWithCustomMessage() throws Exception {
        final ApiResponse<MyDTO> response = MyClientProvider.get().respondValidationErrorWithCustomMessage().get();

        assertNotNull(response);
        assertTrue(!response.isSuccess());
        assertTrue(response.getError().isPresent());
        final ApiError e = response.getError().get();
        assertEquals(ApiError.VALIDATION_ERROR, e.getCode());
        assertEquals(MyResource.MY_VALIDATION_ERROR_MESSAGE, e.getMessage());
    }

    @Test
    public void shouldReturnServerError() throws Exception {
        final ApiResponse<MyDTO> response = MyClientProvider.get().responseISE().get();

        assertNotNull(response);
        assertTrue(!response.isSuccess());
        assertTrue(response.getError().isPresent());
        final ApiError e = response.getError().get();
        assertEquals(ApiError.SERVER_ERROR, e.getCode());
        assertEquals(MyResource.MY_ISE_MESSAGE, e.getMessage());
    }

    @Test
    public void shouldAlsoReturnBadRequest() throws Exception {
        final ApiResponse<MyDTO> response = MyClientProvider.get().throwBadRequest().get();

        assertNotNull(response);
        assertTrue(!response.isSuccess());
        assertTrue(response.getError().isPresent());
        final ApiError e = response.getError().get();
        assertEquals(ApiError.BAD_REQUEST, e.getCode());
        assertEquals(MyResource.MY_BAD_REQUEST_MESSAGE, e.getMessage());
    }

    @Test
    public void shouldAlsoReturnConflict() throws Exception {
        final ApiResponse<MyDTO> response = MyClientProvider.get().throwConflict().get();

        assertNotNull(response);
        System.out.print(response);
        assertTrue(!response.isSuccess());
        assertTrue(response.getError().isPresent());
        final ApiError e = response.getError().get();
        assertEquals(ApiError.CONFLICT, e.getCode());
        assertEquals(MyResource.MY_CONFLICT_MESSAGE, e.getMessage());
    }

    // ...

    @Test
    public void shouldAlsoReturnServerError() throws Exception {
        final ApiResponse<MyDTO> response = MyClientProvider.get().throwISE().get();

        assertNotNull(response);
        assertTrue(!response.isSuccess());
        assertTrue(response.getError().isPresent());
        final ApiError e = response.getError().get();
        assertEquals(ApiError.SERVER_ERROR, e.getCode());
        assertEquals(MyResource.MY_ISE_MESSAGE, e.getMessage());
    }

    @Test
    public void shouldReturnServerErrorWithLoggingExceptionMapperMessage() throws Exception {
        final ApiResponse<MyDTO> response = MyClientProvider.get().npe().get();

        assertNotNull(response);
        System.out.print(response.isSuccess());
        assertTrue(!response.isSuccess());
        assertTrue(response.getError().isPresent());
        final ApiError e = response.getError().get();
        assertEquals(ApiError.SERVER_ERROR, e.getCode());
        assertTrue(e.getMessage().contains("There was an error processing your request. It has been logged"));
    }

}