package ResumeScreener.service;

import ResumeScreener.dto.ResumeUploadResponsesDTO;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@Slf4j
public class ResumeService {

    public ResumeUploadResponsesDTO extractTextFromPdf(MultipartFile file) {

        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || !originalFileName.endsWith(".pdf")) {
            throw new RuntimeException("only pdf file are accepted");
        }

        try {
            //PDFBox extraction
            PDDocument document = Loader.loadPDF(file.getBytes());
            PDFTextStripper stripper = new PDFTextStripper();
            String extractedText = stripper.getText(document);
            document.close();

            // clean the text
            String cleanedtext = extractedText
                    .replace("\\s+", "")
                    .trim();

            log.info("Successfully extracted {} words from pdf"
                    , cleanedtext.split("\\s+").length);

            return ResumeUploadResponsesDTO.builder()
                    .success(true)
                    .extractedText(cleanedtext)
                    .workCount(cleanedtext.split("\\s+").length)
                    .message("Text extracted successfully")
                    .build();

        } catch (IOException e) {
            log.error("Failed to extract text from PDF: {}", e.getMessage());
            throw new RuntimeException("Failed to process PDF: " + e.getMessage());
        }

    }
}


