package exception;

import dto.ApiErrorDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    private WebRequest createMockRequest() {
        WebRequest mockRequest = mock(WebRequest.class);
        when(mockRequest.getDescription(false)).thenReturn("uri=/test");
        return mockRequest;
    }

    @Test
    public void testHandleBusinessRuleException() {
        BusinessRuleException ex = new BusinessRuleException("Error");
        ResponseEntity<ApiErrorDTO> res = handler.handleBusinessRuleException(ex, createMockRequest());
        assertEquals(400, res.getStatusCode().value());
        assertEquals("Error", res.getBody().getMessage());
    }

    @Test
    public void testHandleResourceNotFoundException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Not found");
        ResponseEntity<ApiErrorDTO> res = handler.handleResourceNotFoundException(ex, createMockRequest());
        assertEquals(404, res.getStatusCode().value());
        assertEquals("Not found", res.getBody().getMessage());
    }

    @Test
    public void testHandleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid");
        ResponseEntity<ApiErrorDTO> res = handler.handleIllegalArgumentException(ex, createMockRequest());
        assertEquals(400, res.getStatusCode().value());
        assertEquals("Invalid", res.getBody().getMessage());
    }

    @Test
    public void testHandleGlobalException() {
        Exception ex = new Exception("Global");
        ResponseEntity<ApiErrorDTO> res = handler.handleAllExceptions(ex, createMockRequest());
        assertEquals(500, res.getStatusCode().value());
        assertEquals("Global", res.getBody().getMessage());
    }
}
