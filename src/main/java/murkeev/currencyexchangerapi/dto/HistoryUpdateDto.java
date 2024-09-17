package murkeev.currencyexchangerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HistoryUpdateDto implements Serializable {
    private Long id;
    private double baseValue;
    private double targetValue;
    private String baseCurrencyName;
    private String targetCurrencyName;
    private LocalDateTime date;
}
