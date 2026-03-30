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
    void applicationStarts() {
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
            TechcupFutbolApplication.main(new String[]{"--server.port=0", "--spring.profiles.active=test"});
        });
    }
}
