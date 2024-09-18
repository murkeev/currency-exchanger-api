package murkeev.currencyexchangerapi.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import murkeev.currencyexchangerapi.util.DateTimeDeserializer;
import murkeev.currencyexchangerapi.util.DateTimeSerializer;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserHistoryDto implements Serializable {
    private double baseValue;
    private double targetValue;
    private String baseCurrencyName;
    private String targetCurrencyName;
    @JsonSerialize(using = DateTimeSerializer.class)
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private LocalDateTime date;
}
