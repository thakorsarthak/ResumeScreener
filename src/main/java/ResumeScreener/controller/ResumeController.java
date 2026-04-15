package ResumeScreener.controller;

import ResumeScreener.dto.ResumeUploadResponsesDTO;
import ResumeScreener.dto.ScreeningResultDTO;
import ResumeScreener.service.HuggingFaceService;
import ResumeScreener.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/resume")
@Tag(name = "Resume Screener", description = "Resume parshing and job matching APIS")
public class ResumeController {


    private static final Logger log = LoggerFactory.getLogger(ResumeController.class);
    private  final ResumeService resumeService;
    private final HuggingFaceService huggingFaceService;

    public ResumeController(ResumeService resumeService
            , HuggingFaceService huggingFaceService){
        this.resumeService = resumeService;
        this.huggingFaceService = huggingFaceService;
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
            @RequestParam("jobDescription") String jobDescription){


        log.info("Screening Resume: {}", file.getOriginalFilename());

        // 1 --> Extracting text
        ResumeUploadResponsesDTO extractedText = resumeService.extractTextFromPdf(file);

        // 2 --> Screening against job description
        ScreeningResultDTO result = huggingFaceService
                .screenResume(extractedText.getExtractedText(), jobDescription);
        return ResponseEntity.ok(result);

    }
}
