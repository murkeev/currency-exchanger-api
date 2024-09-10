package murkeev.currencyexchangerapi.controllers;

import lombok.AllArgsConstructor;
import murkeev.currencyexchangerapi.dto.CurrencyApiRecord;
import murkeev.currencyexchangerapi.service.NbuApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exchange")
@AllArgsConstructor
public class CurrencyExchangeController {
    private final NbuApiService nbuApiService;

    @GetMapping("/convert")
    public ResponseEntity<Double> convert(@RequestParam double uah, @RequestParam String targetCurrencyCode) {
        return ResponseEntity.ok(nbuApiService.convertToTargetCurrency(uah, targetCurrencyCode));
    }

    @GetMapping("/all")
    public List<CurrencyApiRecord> getAllCurrencies() {
        return nbuApiService.getAllCurrencies();
    }
}
