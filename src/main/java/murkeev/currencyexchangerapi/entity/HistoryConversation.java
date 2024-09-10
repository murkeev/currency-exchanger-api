package murkeev.currencyexchangerapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "history_of_conversations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HistoryConversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "base_value")
    private double baseValue;

    @Column(name = "target_value")
    private double targetValue;

    @Column(name = "base_currency_name")
    private String baseCurrencyName;

    @Column(name = "target_currency_name")
    private String targetCurrencyName;

    @CreationTimestamp
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
