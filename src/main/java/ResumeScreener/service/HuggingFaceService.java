package ResumeScreener.service;

import ResumeScreener.config.WebClientConfig;
import ResumeScreener.dto.ScreeningResultDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
public class HuggingFaceService {

    private final WebClient huggingFaceWebClient;

    public HuggingFaceService(WebClient huggingFaceWebClient) {
        this.huggingFaceWebClient = huggingFaceWebClient;
    }

    private static final List<String> SKILL_LABELS = List.of(
            "Java", "Spring Boot", "Python", "JavaScript", "TypeScript",
            "React", "Angular", "Node.js", "MySQL", "MongoDB",
            "Docker", "Kubernetes", "AWS", "REST API", "Microservices",
            "Git", "Redis", "Kafka", "Machine Learning", "SQL"
    );


public ScreeningResultDTO screenResume(String resumeText , String jobDescription){

    // 1 --> Extracting skills
    List<String> resumeSkills = extractSkills(resumeText);

    // 2 --> Extracting skills from job description
    List<String> jobSkills = extractSkills(jobDescription);

    // 3 --> Calculating matched and missing
    List<String> matchedSkills = jobSkills.stream()
            .filter(skill -> jobSkills.stream()
                    .anyMatch(js -> js.equalsIgnoreCase(skill)))
            .collect(Collectors.toList());

    List<String> missingSkills = resumeSkills.stream()
            .filter(skill -> resumeSkills.stream()
                    .anyMatch(js -> js.equalsIgnoreCase(skill)))
            .collect(Collectors.toList());

    // 4 --> Score of basis of matched/ missing skills
    double score = jobSkills.isEmpty() ? 0 :
            (double) matchedSkills.size() / jobSkills.size() * 100;

    // 5 --> To Get semantic similarity score from HuggingFace
    double sematicScore = getSemanticSimilarity(resumeText,jobDescription);

    // 6 --> Final score = 60 % skill match + 40 % semantic score
    double finalScore = (score * 0.6) + (sematicScore * 0.4);

    String verdict = finalScore>= 70 ?"STRONG MATCH"
            : finalScore >= 50 ? "PARTIAL MATCH"
            : "WEAK MATCH";

    return ScreeningResultDTO.builder()
            .matchScore(Math.round(finalScore * 100.0)/100.0)
            .matchedSkills(matchedSkills)
            .missingSkills(missingSkills)
            .verdict(verdict)
            .jobDescription(jobDescription)
            .build();
}


private List<String> extractSkills(String text){
    //calling hugggingface zerp-shot api
    Map<String,Object> requestBody = Map.of(
            "inputs" , text.length() > 1000 ? text.substring(0,1000) : text,
            "parameters", Map.of(
                    "candidate_labels" , SKILL_LABELS,
                    "multi_labels", true));
    try{
        Map response = huggingFaceWebClient.post()
                .uri("https://router.huggingface.co/hf-inference/models/facebook/bart-large-mnli")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofSeconds(30))
                .block();


        // HuggingFace returns labels with scores
        // We take labels where score > 0.5 as "present"
        List<String> labels = (List<String>) response.get("labels");
        List<Double> scores = (List<Double>) response.get("scores");

        List<String> detectSkills = new ArrayList<>();
        for( int i = 0 ; i < labels.size(); i++){
            if (scores.get(i) >0.5){
                detectSkills.add(labels.get(i));
            }
        }

        return detectSkills;

    } catch (Exception e){

        log.error("HuggingFace API call failed: {}" , e.getMessage());
        // Fallback — simple keyword matching
        return SKILL_LABELS.stream()
                .filter(skill -> text.toLowerCase()
                        .contains(skill.toLowerCase()))
                .collect(Collectors.toList());
    }
}

    private double getSemanticSimilarity(String resumeText , String jobDescription){
        // Use sentence similarity model here
        Map<String, Object> requestBody = Map.of(
                "inputs" , Map.of(
                        "source_sentence" , jobDescription.length() > 500
                        ? jobDescription.substring(0,500) : jobDescription,
                        "sentences" ,List.of(
                                resumeText.length() > 500
                                ? resumeText.substring(0,500):resumeText
                        )
                )
        );
        try{

            List<Double> response = huggingFaceWebClient.post()
                    .uri("https://router.huggingface.co/hf-inference/models/sentence-transformers/all-MiniLM-L6-v2/pipeline/sentence-similarity")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Double>>() {})
                    .timeout(Duration.ofSeconds(30))
                    .block();
                    return response != null && !response.isEmpty()
                            ? response.get(0)* 100
                            : 50.0;
        } catch (Exception e){

            log.error("Semantic similarity failed: {}", e.getMessage());
            return 50.0;
        }

    }


}
