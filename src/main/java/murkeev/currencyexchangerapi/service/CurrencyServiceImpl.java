package murkeev.currencyexchangerapi.service;

import lombok.AllArgsConstructor;
import murkeev.currencyexchangerapi.enums.Status;
import murkeev.currencyexchangerapi.entity.Currency;
import murkeev.currencyexchangerapi.repository.CurrencyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class CurrencyServiceImpl {
    private final CurrencyRepository currencyRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public List<Currency> findAll() {
        return currencyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Currency findById(Long id) {
        return currencyRepository.findById(id).orElseThrow(() -> new RuntimeException(""));
    }

    @Transactional(readOnly = true)
    public Currency findByCode(String code) {
        return currencyRepository.findByCode(code).orElseThrow(() -> new RuntimeException("Currencies not found"));
    }

    @Transactional(readOnly = true)
    public int countAllCurrencies() {
        return currencyRepository.countAllCurrencies();
    }

    @Transactional(readOnly = true)
    public Page<Currency> findByStatus(Status status, int size) {
        return currencyRepository.findByStatus(status, PageRequest.of(0, size));
    }

    @Transactional(readOnly = true)
    public Page<Currency> orderByCodeAsc(int size) {
        return currencyRepository.orderByCode(PageRequest.of(0, size));
    }

    @Transactional(readOnly = true)
    public Page<Currency> orderByNameAsc(int size) {
        return currencyRepository.orderByName(PageRequest.of(0, size));
    }

    @Transactional
    @Modifying
    public void saveCurrency(Currency currency) {
        currencyRepository.save(currency);
    }

    @Transactional
    @Modifying
    public void deleteById(Long id) {
        currencyRepository.deleteById(id);
    }

    @Transactional
    @Modifying
    public void updateCurrency(Currency updatedCurrency) {
        Currency existingCurrency = currencyRepository.findById(updatedCurrency.getId())
                .orElseThrow(() -> new RuntimeException("Currency not found."));
        modelMapper.map(updatedCurrency, existingCurrency);
        currencyRepository.save(existingCurrency);
    }
}
