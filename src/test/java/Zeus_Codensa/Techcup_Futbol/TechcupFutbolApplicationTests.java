package Zeus_Codensa.Techcup_Futbol;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TechcupFutbolApplicationTests {

	@Test
	void contextLoads() {
	}

    @Test
    void applicationStarts() {
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
            TechcupFutbolApplication.main(new String[]{"--server.port=0"});
        });
    }
}
