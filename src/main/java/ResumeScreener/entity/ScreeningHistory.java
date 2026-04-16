package ResumeScreener.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "screening_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreeningHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sessionId;

    @Column(nullable = false)
    private String resumeFileName;

    @Column(columnDefinition = "TEXT")
    private String jobDescription;

    @Column(columnDefinition = "TEXT")
    private String resumeText;

    @Column(nullable = false)
    private Double matchScore;

    @Column(columnDefinition = "TEXT")
    private String matchedSkills;  // stored as comma-separated

    @Column(columnDefinition = "TEXT")
    private String missingSkills;  // stored as comma-separated

    @Column(nullable = false)
    private String verdict;

    @Column(nullable = false)
    private LocalDateTime screenedAt;

    @PrePersist
    public void prePersist() {
        screenedAt = LocalDateTime.now();
    }

}
