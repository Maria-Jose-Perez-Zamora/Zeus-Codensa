package dependencies.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import io.swagger.v3.oas.models.OpenAPI;

public class SwaggerConfigTest {
    @Test
    public void testCustomOpenAPI() {
        SwaggerConfig config = new SwaggerConfig();
        OpenAPI api = config.customOpenAPI();
        assertNotNull(api);
        assertNotNull(api.getInfo());
    }
}
