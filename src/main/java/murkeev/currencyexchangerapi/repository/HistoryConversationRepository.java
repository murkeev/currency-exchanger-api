package murkeev.currencyexchangerapi.repository;

import murkeev.currencyexchangerapi.entity.HistoryConversation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface HistoryConversationRepository extends JpaRepository<HistoryConversation, Long> {
    @Query("SELECT hc FROM HistoryConversation hc WHERE hc.user.id = :id")
    Page<HistoryConversation> findAllConversionsByUserId(Long id, Pageable pageable);

    @Query("SELECT hc FROM HistoryConversation hc ORDER BY hc.date DESC")
    Page<HistoryConversation> orderByDate(Pageable pageable);

    @Query("SELECT hc FROM HistoryConversation hc ORDER BY hc.targetCurrencyName DESC")
    Page<HistoryConversation> orderByCurrencyValue(Pageable pageable);

    @Query("SELECT hc FROM HistoryConversation hc WHERE hc.user.id = :id")
    Page<HistoryConversation> getUserHistoryConversation(Long id, Pageable pageable);

    @Query("SELECT hc FROM HistoryConversation hc WHERE hc.date BETWEEN :startOfDay AND :endOfDay")
    Page<HistoryConversation> findAllByDateBetween(LocalDateTime startOfDay, LocalDateTime endOfDay, Pageable pageable);
}
