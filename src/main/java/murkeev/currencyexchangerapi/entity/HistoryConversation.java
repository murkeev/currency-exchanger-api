package murkeev.currencyexchangerapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
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

    @Column(name = "base_currency")
    private BigDecimal baseValue;

    @Column(name = "target_currency")
    private BigDecimal targetValue;

    @Column(name = "base_currency_name")
    private String baseCurrencyName;

    @Column(name = "target_currency_name")
    private String targetCurrencyName;

    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
