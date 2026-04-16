package ResumeScreener.components;

import ResumeScreener.repository.ScreeningHistoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@EnableScheduling
public class SessionCleanupJob {

    @Autowired
    private ScreeningHistoryRepository historyRepository;

    @Transactional
    @Scheduled(cron = "0 */5 * * * *")
    public void cleanOldSession(){

        LocalDateTime cutoff= LocalDateTime.now().minusMinutes(10);
        historyRepository.deleteByScreenedAtBefore(cutoff);
    }
}
