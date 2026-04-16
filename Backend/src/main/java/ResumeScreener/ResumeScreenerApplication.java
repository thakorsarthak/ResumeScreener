package ResumeScreener;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class ResumeScreenerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResumeScreenerApplication.class, args);
	}


}
