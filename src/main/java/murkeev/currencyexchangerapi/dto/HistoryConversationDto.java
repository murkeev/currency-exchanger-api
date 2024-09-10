package murkeev.currencyexchangerapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record HistoryConversationDto(
        BigDecimal baseValue,
        BigDecimal targetValue,
        String baseCurrencyName,
        String targetCurrencyName,
        LocalDateTime date
) {
}
