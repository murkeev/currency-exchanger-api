package murkeev.currencyexchangerapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import murkeev.currencyexchangerapi.dto.CurrencyApiRecord;
import murkeev.currencyexchangerapi.dto.HistoryConversationDto;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NbuApiService {
    private final ObjectMapper objectMapper;
    private final HistoryConversationService historyConversationService;
    private final RestClient restClient;

    private static final String NBU_API_URL = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json";

    public double convertToTargetCurrency(double uah, String targetCurrencyCode) {
        ResponseEntity<String> response =
                restClient.get()
                        .uri(NBU_API_URL)
                        .retrieve()
                        .toEntity(String.class);
        String responseBody = response.getBody();

        if (responseBody == null) {
            throw new RuntimeException("Empty response from currency API");
        }

        try {
            double rate = getRate(targetCurrencyCode, responseBody);
            double targetValue = uah / rate;
            HistoryConversationDto historyDto = HistoryConversationDto.builder()
                    .baseValue(uah)
                    .targetValue(targetValue)
                    .baseCurrencyName("UAH")
                    .targetCurrencyName(targetCurrencyCode)
                    .build();
            historyConversationService.saveConversionHistory(historyDto);
            return targetValue;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to parse currency data: " + e.getMessage(), e);
        }
    }

    public List<CurrencyApiRecord> getAllCurrencies() throws Exception {
        ResponseEntity<String> response =
                restClient.get()
                        .uri(NBU_API_URL)
                        .retrieve()
                        .toEntity(String.class);
        try {
            String responseBody = response.getBody();
            CurrencyApiRecord[] currencyApiRecords = objectMapper.readValue(responseBody, CurrencyApiRecord[].class);
            return Arrays.asList(currencyApiRecords);
        } catch (Exception e) {
            throw new RuntimeException("Error during JSON response processing: " + e.getMessage(), e);
        }
    }

    private double getRate(String targetCurrencyCode, String responseBody) {
        JSONArray jsonArray = new JSONArray(responseBody);
        double rate = 0;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject currency = jsonArray.getJSONObject(i);
            if (currency.getString("cc").equalsIgnoreCase(targetCurrencyCode)) {
                rate = currency.optDouble("rate", -1);
                if (rate <= 0) {
                    throw new IllegalArgumentException("Invalid rate value: " + rate);
                }
            }
        }
        return rate;
    }
}