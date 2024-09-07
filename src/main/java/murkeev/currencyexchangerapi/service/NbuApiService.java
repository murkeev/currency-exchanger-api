package murkeev.currencyexchangerapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import murkeev.currencyexchangerapi.dto.CurrencyApiRecord;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NbuApiService {
    private final ObjectMapper objectMapper;
    private final RestClient restClient;

    private static final String NBU_API_URL = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json";

    public double convertToTargetCurrency(double uah, String targetCurrencyCode) {
        ResponseEntity<String> response =
                restClient.get()
                        .uri(NBU_API_URL)
                        .retrieve()
                        .toEntity(String.class);
        String responseBody = response.getBody();

        try {
            double rate = getRate(targetCurrencyCode, responseBody);
            return uah / rate;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to parse currency data");
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
        JSONObject jsonObject = new JSONObject(responseBody);
        if (!jsonObject.has("cc") || !jsonObject.has("rate")) {
            throw new IllegalArgumentException("Response does not contain necessary fields.");
        }
        String code = jsonObject.getString("cc");
        if (!code.equals(targetCurrencyCode)) {
            throw new IllegalArgumentException("Currency code not found: " + targetCurrencyCode);
        }
        double rate = jsonObject.optDouble("rate", -1);
        if (rate <= 0) {
            throw new IllegalArgumentException("Invalid rate value: " + rate);
        }
        return rate;
    }
}
