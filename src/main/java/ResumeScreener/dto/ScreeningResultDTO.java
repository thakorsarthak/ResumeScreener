package ResumeScreener.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ScreeningResultDTO {
    private Long id;
    private String sessionId;
    private double matchScore;
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private String verdict;
    private String jobDescription;
    private String resumeFileName;
    private LocalDateTime screenAt;
}

