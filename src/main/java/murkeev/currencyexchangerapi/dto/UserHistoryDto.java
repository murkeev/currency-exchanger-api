package murkeev.currencyexchangerapi.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserHistoryDto {
    private double baseValue;
    private double targetValue;
    private String baseCurrencyName;
    private String targetCurrencyName;
    private LocalDateTime dateTime;
}
