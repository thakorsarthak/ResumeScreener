package ResumeScreener.repository;

import ResumeScreener.entity.ScreeningHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScreeningHistoryRepository extends JpaRepository<ScreeningHistory , Long> {

    List<ScreeningHistory> findBySessionIdOrderByScreenedAtDesc(String sessionId);

    List<ScreeningHistory> findAllByOrderByScreenedAtDesc();

}
