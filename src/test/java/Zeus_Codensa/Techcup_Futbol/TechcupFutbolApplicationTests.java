package Zeus_Codensa.Techcup_Futbol;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class TechcupFutbolApplicationTests {

	@Test
	void contextLoads() {
	}

    @Test
    void mainClassInstantiatesWithoutError() {
        // Validates TechcupFutbolApplication can be instantiated
        TechcupFutbolApplication app = new TechcupFutbolApplication();
        org.junit.jupiter.api.Assertions.assertNotNull(app);
    }
}
