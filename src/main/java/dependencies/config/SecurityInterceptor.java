package dependencies.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import core.model.Role;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.util.Base64;

@Component
public class SecurityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        
        // Rutas publicas (sin token)
        if (uri.equalsIgnoreCase("/api/auth") && "POST".equalsIgnoreCase(request.getMethod())
            || uri.equalsIgnoreCase("/api/users") && "POST".equalsIgnoreCase(request.getMethod())
            || uri.startsWith("/api/tournaments/consulta")
            || uri.startsWith("/api/tournaments/query")
            || uri.startsWith("/swagger-ui")
            || uri.startsWith("/v3/api-docs")
            || uri.startsWith("/swagger-resources")
            || uri.startsWith("/webjars")) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("No autorizado: Falta token");
            return false;
        }

        String token = authHeader.substring(7);
        try {
            String decoded = new String(Base64.getDecoder().decode(token));
            String[] parts = decoded.split(":");
            if (parts.length != 2) throw new Exception();
            
            String userRole = parts[1];
            Role roleEnum = Role.valueOf(userRole);
            
            // Reglas de Autorizacion por Rol
            if (uri.startsWith("/api/teams") && "POST".equalsIgnoreCase(request.getMethod()) && roleEnum != Role.CAPTAIN) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Prohibido: Se requiere rol CAPTAIN");
                return false;
            }
            if (uri.startsWith("/api/tournaments") && !"GET".equalsIgnoreCase(request.getMethod()) && !uri.startsWith("/api/tournaments/consulta") && !uri.startsWith("/api/tournaments/query") && roleEnum != Role.TOURNAMENT_ORGANIZER) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Prohibido: Se requiere rol TOURNAMENT_ORGANIZER");
                return false;
            }
            if (uri.startsWith("/api/matches") && !"GET".equalsIgnoreCase(request.getMethod()) && roleEnum != Role.REFEREE && roleEnum != Role.TOURNAMENT_ORGANIZER) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Prohibido: Se requiere rol REFEREE u TOURNAMENT_ORGANIZER");
                return false;
            }
            if (uri.startsWith("/api/registrations")) {
                if ("POST".equalsIgnoreCase(request.getMethod()) && roleEnum != Role.CAPTAIN && roleEnum != Role.TOURNAMENT_ORGANIZER) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Prohibido: Solo CAPTAIN u ORGANIZER pueden crear inscripciones");
                    return false;
                }
                if ("PUT".equalsIgnoreCase(request.getMethod()) && roleEnum != Role.ADMINISTRADOR_SISTEMA && roleEnum != Role.TOURNAMENT_ORGANIZER) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Prohibido: Solo ADMIN u ORGANIZER pueden actualizar el estado de la inscripción");
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("No autorizado: Token invalido");
            return false;
        }
    }
}
