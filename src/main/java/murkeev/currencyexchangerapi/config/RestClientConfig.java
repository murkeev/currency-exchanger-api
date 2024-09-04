package murkeev.currencyexchangerapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

public class RestClientConfig {
    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
