package ResumeScreener.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Apply to all endpoints
                        .allowedOrigins(
                                "http://localhost:4200",
                                "${FRONTEND_URL:https://resume-screener-lilac.vercel.app}"  // Angular dev server
                        )
                        .allowedMethods("GET", "POST")
                        .allowedHeaders()
                        .allowCredentials(true);
            }
        };
    }
}
