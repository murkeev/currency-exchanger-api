package murkeev.currencyexchangerapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .defaultHeader("Content-Type", "application/json")
                .baseUrl("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json")
                .build();
    }
}
