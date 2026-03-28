package Zeus_Codensa.Techcup_Futbol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"Zeus_Codensa.Techcup_Futbol", "controller", "core", "dependencies"})
@EnableJpaRepositories(basePackages = "dependencies.persistence.repository")
@EntityScan(basePackages = "dependencies.persistence.entity")
public class TechcupFutbolApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechcupFutbolApplication.class, args);
	}

}
