package murkeev.currencyexchangerapi.controllers;

import lombok.AllArgsConstructor;
import murkeev.currencyexchangerapi.entity.Currency;
import murkeev.currencyexchangerapi.enums.Status;
import murkeev.currencyexchangerapi.service.CurrencyServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/currencies")
@AllArgsConstructor
public class CurrencyController {

    private final CurrencyServiceImpl currencyService;

    @GetMapping
    public List<Currency> getAll() {
        return currencyService.findAll();
    }

    @GetMapping("/{id}")
    public Currency getById(@PathVariable Long id) {
        return currencyService.findById(id);
    }

    @GetMapping("/code/{code}")
    public Currency getByCode(@PathVariable String code) {
        return currencyService.findByCode(code);
    }

    @GetMapping("/count-curriencies")
    public int getCountOfCurrencies() {
        return currencyService.countAllCurrencies();
    }

    @GetMapping("/status")
    public Page<Currency> getByStatus(@RequestParam Status status, @RequestParam("page_size") int size) {
        return currencyService.findByStatus(status, size);
    }

    @GetMapping("/order-code")
    public Page<Currency> orderByCode(@RequestParam("page_size") int size) {
        return currencyService.orderByCodeAsc(size);
    }

    @GetMapping("/order-name")
    public Page<Currency> orderByName(@RequestParam("page_size") int size) {
        return currencyService.orderByNameAsc(size);
    }

    @PostMapping
    public ResponseEntity<Void> createCurrency(@RequestBody Currency currency) {
        currencyService.saveCurrency(currency);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@RequestParam Long id) {
        currencyService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<Void> updateCurrency(@RequestBody Currency currency) {
        currencyService.updateCurrency(currency);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
