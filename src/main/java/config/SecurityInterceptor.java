package config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Role;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.util.Base64;

@Component
public class SecurityInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        
        // Rutas publicas
        if (uri.startsWith("/api/auth/login") || uri.startsWith("/api/users/register") || uri.startsWith("/api/torneos/consulta")) {
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
            if (uri.startsWith("/api/teams/create") && roleEnum != Role.CAPITAN) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Prohibido: Se requiere rol CAPITAN");
                return false;
            }
            if (uri.startsWith("/api/torneos") && !uri.startsWith("/api/torneos/consulta") && roleEnum != Role.ORGANIZADOR_TORNEO) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Prohibido: Se requiere rol ORGANIZADOR_TORNEO");
                return false;
            }
            if (uri.startsWith("/api/partidos") && roleEnum != Role.ARBITRO && roleEnum != Role.ORGANIZADOR_TORNEO) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Prohibido: Se requiere rol ARBITRO u ORGANIZADOR_TORNEO");
                return false;
            }
            if (uri.startsWith("/api/inscripciones") && roleEnum != Role.CAPITAN && roleEnum != Role.ORGANIZADOR_TORNEO) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Prohibido: Rol irrelevante para inscripciones");
                return false;
            }

            return true;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("No autorizado: Token invalido");
            return false;
        }
    }
}
