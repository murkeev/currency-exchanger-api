package murkeev.currencyexchangerapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CurrencyApiRecord(
        int r030,
        @JsonProperty("txt")
        String currencyName,
        double rate,
        String cc,
        @JsonProperty("exchangedate")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
        LocalDate exchangeDate) {
}