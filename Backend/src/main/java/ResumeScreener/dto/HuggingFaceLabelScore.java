package ResumeScreener.dto;

import lombok.Data;

@Data
public class HuggingFaceLabelScore {
    private String label;
    private Double score;
}
