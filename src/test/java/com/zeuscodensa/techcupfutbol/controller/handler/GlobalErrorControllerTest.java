package com.zeuscodensa.techcupfutbol.controller.handler;

import com.zeuscodensa.techcupfutbol.controller.dto.ApiErrorDTO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalErrorControllerTest {

    private final GlobalErrorController controller = new GlobalErrorController();

    @Test
    public void testHandleErrorDefaultsTo500WhenStatusMissing() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(null);

        ResponseEntity<ApiErrorDTO> res = controller.handleError(request);

        assertEquals(500, res.getStatusCode().value());
        assertEquals(500, res.getBody().getCode());
        assertEquals("Internal Server Error", res.getBody().getMessage());
    }

    @Test
    public void testHandleErrorReturns400Message() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(400);

        ResponseEntity<ApiErrorDTO> res = controller.handleError(request);

        assertEquals(400, res.getStatusCode().value());
        assertEquals("Bad request", res.getBody().getMessage());
    }

    @Test
    public void testHandleErrorReturns401Message() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(401);

        ResponseEntity<ApiErrorDTO> res = controller.handleError(request);

        assertEquals(401, res.getStatusCode().value());
        assertEquals("Unauthorized", res.getBody().getMessage());
    }

    @Test
    public void testHandleErrorReturns404Message() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(404);

        ResponseEntity<ApiErrorDTO> res = controller.handleError(request);

        assertEquals(404, res.getStatusCode().value());
        assertEquals("Resource not found", res.getBody().getMessage());
    }

    @Test
    public void testHandleErrorFallbackWhenStatusIsInvalidString() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn("abc");

        ResponseEntity<ApiErrorDTO> res = controller.handleError(request);

        assertEquals(500, res.getStatusCode().value());
        assertEquals("Internal Server Error", res.getBody().getMessage());
    }

    @Test
    public void testHandleErrorFallbackWhenStatusCodeUnknown() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(799);

        ResponseEntity<ApiErrorDTO> res = controller.handleError(request);

        assertEquals(500, res.getStatusCode().value());
        assertEquals("Internal Server Error", res.getBody().getMessage());
    }
}
