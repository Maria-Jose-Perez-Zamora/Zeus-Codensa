package com.zeuscodensa.techcupfutbol.controller.handler;

import com.zeuscodensa.techcupfutbol.controller.dto.ApiErrorDTO;
import com.zeuscodensa.techcupfutbol.core.exception.BusinessRuleException;
import com.zeuscodensa.techcupfutbol.core.exception.PersistenceAccessException;
import com.zeuscodensa.techcupfutbol.core.exception.ResourceNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.Set;

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
        assertEquals("Internal server error", res.getBody().getMessage());
    }

    @Test
    public void testHandleMethodArgumentNotValidException() {
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("loginRequestDTO", "email", "must be valid");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<ApiErrorDTO> res = handler.handleMethodArgumentNotValidException(ex, createMockRequest());
        assertEquals(400, res.getStatusCode().value());
        assertEquals("email: must be valid", res.getBody().getMessage());
    }

    @Test
    public void testHandleConstraintViolationException() {
        @SuppressWarnings("unchecked")
        ConstraintViolation<Object> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("request.email");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("must be valid");

        ConstraintViolationException ex = new ConstraintViolationException(Set.of(violation));
        ResponseEntity<ApiErrorDTO> res = handler.handleConstraintViolationException(ex, createMockRequest());

        assertEquals(400, res.getStatusCode().value());
        assertEquals("request.email: must be valid", res.getBody().getMessage());
    }

    @Test
    public void testHandleHttpMessageNotReadableException() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Invalid JSON");
        ResponseEntity<ApiErrorDTO> res = handler.handleHttpMessageNotReadableException(ex, createMockRequest());

        assertEquals(400, res.getStatusCode().value());
        assertEquals("Malformed request body", res.getBody().getMessage());
    }

    @Test
    public void testHandleNoHandlerFoundException() {
        NoHandlerFoundException ex = new NoHandlerFoundException("GET", "/not-found", null);
        ResponseEntity<ApiErrorDTO> res = handler.handleNoHandlerFoundException(ex, createMockRequest());

        assertEquals(404, res.getStatusCode().value());
        assertEquals("Endpoint not found", res.getBody().getMessage());
    }

    @Test
    public void testHandleAuthenticationException() {
        AuthenticationException ex = new AuthenticationException("Token invalid") {};
        ResponseEntity<ApiErrorDTO> res = handler.handleAuthenticationException(ex, createMockRequest());

        assertEquals(401, res.getStatusCode().value());
        assertEquals("Unauthorized", res.getBody().getMessage());
    }

    @Test
    public void testHandlePersistenceAccessException() {
        PersistenceAccessException ex = new PersistenceAccessException("DB unavailable");
        ResponseEntity<ApiErrorDTO> res = handler.handlePersistenceAccessException(ex, createMockRequest());

        assertEquals(503, res.getStatusCode().value());
        assertEquals("Persistence service unavailable", res.getBody().getMessage());
    }
}
