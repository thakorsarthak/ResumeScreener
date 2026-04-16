package ResumeScreener.controller;

import ResumeScreener.dto.ResumeUploadResponsesDTO;
import ResumeScreener.dto.ScreeningResultDTO;
import ResumeScreener.entity.ScreeningHistory;
import ResumeScreener.repository.ScreeningHistoryRepository;
import ResumeScreener.service.HuggingFaceService;
import ResumeScreener.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/resume")
@Tag(name = "Resume Screener", description = "Resume parshing and job matching APIS")
public class ResumeController {


    private static final Logger log = LoggerFactory.getLogger(ResumeController.class);
    private  final ResumeService resumeService;
    private final HuggingFaceService huggingFaceService;
    private final ScreeningHistoryRepository screeningHistoryRepository;

    public ResumeController(ResumeService resumeService
            , HuggingFaceService huggingFaceService, ScreeningHistoryRepository screeningHistoryRepository){
        this.resumeService = resumeService;
        this.huggingFaceService = huggingFaceService;
        this.screeningHistoryRepository = screeningHistoryRepository;
    }

    @PostMapping(value = "/extract" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "upload a PDF resume and extract its text")
    public ResponseEntity<ResumeUploadResponsesDTO> extractResume(@RequestParam("file")MultipartFile file){

        log.info("Received resume upload: {}" , file.getOriginalFilename());
        ResumeUploadResponsesDTO response = resumeService.extractTextFromPdf(file);
        return ResponseEntity.ok(response);
    }


    @PostMapping(value = "/screen" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload resume PDF and match against a job description")
    public ResponseEntity<ScreeningResultDTO> screenResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam("jobDescription") String jobDescription,
            @RequestParam(value = "sessionId" , required = false) String sessionId) {


        // Generate sessionId if not provided (first time user)
        if (sessionId == null || sessionId.isBlank()) {
            sessionId = UUID.randomUUID().toString();
            log.info("Generated new sessionId: {}", sessionId);
        }

        log.info("Screening Resume: {}", file.getOriginalFilename());

        // 1 --> Extracting text
        ResumeUploadResponsesDTO extractedText = resumeService.extractTextFromPdf(file);

        // 2 --> Screening against job description
        ScreeningResultDTO result = huggingFaceService
                .screenResume(extractedText.getExtractedText(), jobDescription , file.getOriginalFilename(), sessionId);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/history")
    @Operation(summary = "Get all past screening results")
    public  ResponseEntity<List<ScreeningResultDTO>> getHistory(
            @RequestParam("sessionId") String sessionId){

        if (sessionId == null || sessionId.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

            List<ScreeningHistory> history = screeningHistoryRepository.findAllByOrderByScreenedAtDesc();

            List<ScreeningResultDTO> response = history.stream()
                    .map( h -> ScreeningResultDTO.builder()
                            .id(h.getId())
                            .sessionId(h.getSessionId())
                            .matchScore(h.getMatchScore())
                            .matchedSkills(h.getMatchedSkills() != null
                                    ? List.of(h.getMatchedSkills().split(",")): List.of())
                            .missingSkills(h.getMissingSkills() != null
                            ? List.of(h.getMissingSkills().split(",")): List.of())
                            .verdict(h.getVerdict())
                            .jobDescription(h.getJobDescription())
                            .resumeFileName(h.getResumeFileName())
                            .screenAt(h.getScreenedAt())
                            .build())
                    .collect(Collectors.toList());
            return ResponseEntity.ok(response);

    }
}
