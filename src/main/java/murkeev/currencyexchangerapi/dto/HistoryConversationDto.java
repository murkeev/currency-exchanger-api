package murkeev.currencyexchangerapi.dto;

import lombok.Builder;

@Builder
public record HistoryConversationDto(
        double baseValue,
        double targetValue,
        String baseCurrencyName,
        String targetCurrencyName
) {
}
