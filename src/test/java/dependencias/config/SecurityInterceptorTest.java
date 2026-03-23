package dependencias.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import core.model.Role;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SecurityInterceptorTest {

    private final SecurityInterceptor interceptor = new SecurityInterceptor();

    private HttpServletRequest mockRequest(String uri, String tokenContent) {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getRequestURI()).thenReturn(uri);
        if (tokenContent != null) {
            String encoded = Base64.getEncoder().encodeToString(tokenContent.getBytes());
            when(req.getHeader("Authorization")).thenReturn("Bearer " + encoded);
        }
        return req;
    }

    private HttpServletResponse mockResponse() throws Exception {
        HttpServletResponse res = mock(HttpServletResponse.class);
        PrintWriter writer = new PrintWriter(new StringWriter());
        when(res.getWriter()).thenReturn(writer);
        return res;
    }

    @Test
    public void testPublicRoutes() throws Exception {
        assertTrue(interceptor.preHandle(mockRequest("/api/auth/login", null), mockResponse(), null));
        assertTrue(interceptor.preHandle(mockRequest("/api/users/register", null), mockResponse(), null));
        assertTrue(interceptor.preHandle(mockRequest("/api/torneos/consulta", null), mockResponse(), null));
    }

    @Test
    public void testMissingToken() throws Exception {
        HttpServletResponse res = mockResponse();
        assertFalse(interceptor.preHandle(mockRequest("/api/teams/create", null), res, null));
        verify(res).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    public void testInvalidToken() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getRequestURI()).thenReturn("/api/teams/create");
        when(req.getHeader("Authorization")).thenReturn("Bearer invalid_base64");
        HttpServletResponse res = mockResponse();
        assertFalse(interceptor.preHandle(req, res, null));
        verify(res).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    public void testTokenMissingParts() throws Exception {
        HttpServletResponse res = mockResponse();
        assertFalse(interceptor.preHandle(mockRequest("/api/teams/create", "user_only_no_colon"), res, null));
        verify(res).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    public void testRoleCapitanAllowed() throws Exception {
        assertTrue(interceptor.preHandle(mockRequest("/api/teams/create", "user:" + Role.CAPITAN.name()), mockResponse(), null));
    }

    @Test
    public void testRoleCapitanForbidden() throws Exception {
        HttpServletResponse res = mockResponse();
        assertFalse(interceptor.preHandle(mockRequest("/api/teams/create", "user:" + Role.JUGADOR.name()), res, null));
        verify(res).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    public void testRoleOrganizadorTorneoAllowed() throws Exception {
        assertTrue(interceptor.preHandle(mockRequest("/api/torneos/create", "user:" + Role.ORGANIZADOR_TORNEO.name()), mockResponse(), null));
    }

    @Test
    public void testRoleOrganizadorTorneoForbidden() throws Exception {
        HttpServletResponse res = mockResponse();
        assertFalse(interceptor.preHandle(mockRequest("/api/torneos/create", "user:" + Role.CAPITAN.name()), res, null));
        verify(res).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    public void testRoleArbitroAllowedPartidos() throws Exception {
        assertTrue(interceptor.preHandle(mockRequest("/api/partidos/update", "user:" + Role.ARBITRO.name()), mockResponse(), null));
        assertTrue(interceptor.preHandle(mockRequest("/api/partidos/update", "user:" + Role.ORGANIZADOR_TORNEO.name()), mockResponse(), null));
    }

    @Test
    public void testRoleArbitroForbiddenPartidos() throws Exception {
        HttpServletResponse res = mockResponse();
        assertFalse(interceptor.preHandle(mockRequest("/api/partidos/update", "user:" + Role.JUGADOR.name()), res, null));
        verify(res).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    public void testRoleInscripcionesAllowed() throws Exception {
        assertTrue(interceptor.preHandle(mockRequest("/api/inscripciones/add", "user:" + Role.CAPITAN.name()), mockResponse(), null));
        assertTrue(interceptor.preHandle(mockRequest("/api/inscripciones/add", "user:" + Role.ORGANIZADOR_TORNEO.name()), mockResponse(), null));
    }

    @Test
    public void testRoleInscripcionesForbidden() throws Exception {
        HttpServletResponse res = mockResponse();
        assertFalse(interceptor.preHandle(mockRequest("/api/inscripciones/add", "user:" + Role.JUGADOR.name()), res, null));
        verify(res).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}
