package ResumeScreener.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResumeUploadResponsesDTO {
    private boolean success;
    private String extractedText;
    private int workCount;
    private  String message;
}
