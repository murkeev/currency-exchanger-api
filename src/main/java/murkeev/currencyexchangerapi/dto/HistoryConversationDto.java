package murkeev.currencyexchangerapi.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public class HistoryConversationDto implements Serializable {
    private double baseValue;
    private double targetValue;
    private String baseCurrencyName;
    private String targetCurrencyName;
}
