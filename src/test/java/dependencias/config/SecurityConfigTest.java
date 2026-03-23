package dependencias.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import static org.mockito.Mockito.*;

public class SecurityConfigTest {
    @Test
    public void testAddInterceptors() {
        SecurityInterceptor mockInterceptor = mock(SecurityInterceptor.class);
        SecurityConfig config = new SecurityConfig(mockInterceptor);
        InterceptorRegistry registry = mock(InterceptorRegistry.class);
        InterceptorRegistration registration = mock(InterceptorRegistration.class);
        
        when(registry.addInterceptor(any())).thenReturn(registration);
        when(registration.addPathPatterns(anyString())).thenReturn(registration);
        when(registration.excludePathPatterns(anyString())).thenReturn(registration);
        
        config.addInterceptors(registry);
        verify(registry).addInterceptor(mockInterceptor);
    }
}
