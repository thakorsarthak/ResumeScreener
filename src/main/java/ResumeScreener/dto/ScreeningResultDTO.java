package ResumeScreener.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ScreeningResultDTO {
    private double matchScore;
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private String verdict;
    private String jobDescription;
}

