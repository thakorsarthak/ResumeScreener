package ResumeScreener.dto;

import jakarta.validation.constraints.NotBlank;

public class MatchRequestDTO {
    @NotBlank(message = "Job description is required")
    private String jobDescription;

    @NotBlank(message = "Resume text is required")
    private String resumeText;
}
