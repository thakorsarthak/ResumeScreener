package ResumeScreener.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfig {
    @Value("${huggingface.api.key}")
    private String apiKey;

    @Bean
    public WebClient huggingFaceWebClient(){
        return WebClient.builder()
                .baseUrl("https://api-inference.huggingface.co")
                .defaultHeader("Authorization" , "Bearer "+apiKey)
                .defaultHeader("Content-Type" , "application/json")
                .codecs(config -> config
                        .defaultCodecs()
                        .maxInMemorySize(10 * 1024 * 1024))
                .build();
    }

    @Bean
    public CommandLineRunner testBean(ApplicationContext context) {
        return args -> {
            System.out.println(context.getBean(WebClient.class));
        };
    }



}
