package murkeev.currencyexchangerapi.repository;

import murkeev.currencyexchangerapi.entity.HistoryConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryConversationRepository extends JpaRepository<HistoryConversation, Long> {
}
