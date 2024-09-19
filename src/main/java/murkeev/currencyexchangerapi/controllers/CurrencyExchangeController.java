package murkeev.currencyexchangerapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Convert UAH to a target currency")
    @GetMapping("/convert")
    public ResponseEntity<Double> convert(@RequestParam double uah, @RequestParam String targetCurrencyCode) {
        return ResponseEntity.ok(nbuApiService.convertToTargetCurrency(uah, targetCurrencyCode));
    }

    @Operation(summary = "Get all currencies and exchange rates")
    @GetMapping("/all")
    public List<CurrencyApiRecord> getAllCurrencies() {
        return nbuApiService.getAllCurrencies();
    }
}
