package dependencies.config;

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

    private HttpServletRequest mockRequest(String uri, String method, String tokenContent) {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getRequestURI()).thenReturn(uri);
        when(req.getMethod()).thenReturn(method != null ? method : "GET");
        if (tokenContent != null) {
            String encoded = Base64.getEncoder().encodeToString(tokenContent.getBytes());
            when(req.getHeader("Authorization")).thenReturn("Bearer " + encoded);
        }
        return req;
    }

    private HttpServletRequest mockRequest(String uri, String tokenContent) {
        return mockRequest(uri, "GET", tokenContent);
    }

    private HttpServletResponse mockResponse() throws Exception {
        HttpServletResponse res = mock(HttpServletResponse.class);
        PrintWriter writer = new PrintWriter(new StringWriter());
        when(res.getWriter()).thenReturn(writer);
        return res;
    }

    @Test
    public void testPublicRoutes() throws Exception {
        assertTrue(interceptor.preHandle(mockRequest("/api/auth", "POST", null), mockResponse(), null));
        assertTrue(interceptor.preHandle(mockRequest("/api/users", "POST", null), mockResponse(), null));
        assertTrue(interceptor.preHandle(mockRequest("/api/tournaments/consulta", null), mockResponse(), null));
    }

    @Test
    public void testMissingToken() throws Exception {
        HttpServletResponse res = mockResponse();
        assertFalse(interceptor.preHandle(mockRequest("/api/teams", "POST", null), res, null));
        verify(res).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    public void testInvalidToken() throws Exception {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getRequestURI()).thenReturn("/api/teams");
        when(req.getMethod()).thenReturn("POST");
        when(req.getHeader("Authorization")).thenReturn("Bearer invalid_base64");
        HttpServletResponse res = mockResponse();
        assertFalse(interceptor.preHandle(req, res, null));
        verify(res).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    public void testTokenMissingParts() throws Exception {
        HttpServletResponse res = mockResponse();
        assertFalse(interceptor.preHandle(mockRequest("/api/teams", "POST", "user_only_no_colon"), res, null));
        verify(res).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    public void testRoleCapitanAllowed() throws Exception {
        assertTrue(interceptor.preHandle(mockRequest("/api/teams", "POST", "user:" + Role.CAPTAIN.name()), mockResponse(), null));
    }

    @Test
    public void testRoleCapitanForbidden() throws Exception {
        HttpServletResponse res = mockResponse();
        assertFalse(interceptor.preHandle(mockRequest("/api/teams", "POST", "user:" + Role.PLAYER.name()), res, null));
        verify(res).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    public void testRoleTournamentOrganizerAllowed() throws Exception {
        assertTrue(interceptor.preHandle(mockRequest("/api/tournaments", "POST", "user:" + Role.TOURNAMENT_ORGANIZER.name()), mockResponse(), null));
    }

    @Test
    public void testRoleTournamentOrganizerForbidden() throws Exception {
        HttpServletResponse res = mockResponse();
        assertFalse(interceptor.preHandle(mockRequest("/api/tournaments", "POST", "user:" + Role.CAPTAIN.name()), res, null));
        verify(res).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    public void testRoleArbitroAllowedPartidos() throws Exception {
        assertTrue(interceptor.preHandle(mockRequest("/api/matches/123/score", "PUT", "user:" + Role.REFEREE.name()), mockResponse(), null));
        assertTrue(interceptor.preHandle(mockRequest("/api/matches/123/score", "PUT", "user:" + Role.TOURNAMENT_ORGANIZER.name()), mockResponse(), null));
    }

    @Test
    public void testRoleArbitroForbiddenPartidos() throws Exception {
        HttpServletResponse res = mockResponse();
        assertFalse(interceptor.preHandle(mockRequest("/api/matches/123/score", "PUT", "user:" + Role.PLAYER.name()), res, null));
        verify(res).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    public void testRoleInscripcionesAllowed() throws Exception {
        assertTrue(interceptor.preHandle(mockRequest("/api/registrations", "POST", "user:" + Role.CAPTAIN.name()), mockResponse(), null));
        assertTrue(interceptor.preHandle(mockRequest("/api/registrations", "POST", "user:" + Role.TOURNAMENT_ORGANIZER.name()), mockResponse(), null));
    }

    @Test
    public void testRoleInscripcionesForbidden() throws Exception {
        HttpServletResponse res = mockResponse();
        assertFalse(interceptor.preHandle(mockRequest("/api/registrations", "POST", "user:" + Role.PLAYER.name()), res, null));
        verify(res).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}
