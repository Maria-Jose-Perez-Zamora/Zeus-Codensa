package Zeus_Codensa.Techcup_Futbol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
    "Zeus_Codensa.Techcup_Futbol",
    "controlador",
    "core",
    "dependencias"
})
public class TechcupFutbolApplication {

	public static void main(String[] args) {
		SpringApplication.run(TechcupFutbolApplication.class, args);
	}

}
