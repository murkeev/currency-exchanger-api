package murkeev.currencyexchangerapi.repository;

import murkeev.currencyexchangerapi.entity.HistoryConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryConversationRepository extends JpaRepository<HistoryConversation, Long> {
    @Query("SELECT hc FROM HistoryConversation hc WHERE hc.user.id = :id")
    List<HistoryConversation> findByUserId(Long id);
}
