package murkeev.currencyexchangerapi.service;

import lombok.AllArgsConstructor;
import murkeev.currencyexchangerapi.enums.Status;
import murkeev.currencyexchangerapi.entity.Currency;
import murkeev.currencyexchangerapi.exceptions.CurrencyNotFoundException;
import murkeev.currencyexchangerapi.exceptions.SaveException;
import murkeev.currencyexchangerapi.exceptions.UpdateException;
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
        List<Currency> currencies = currencyRepository.findAll();
        if (currencies.isEmpty()) {
            throw new CurrencyNotFoundException("Currencies not found.");
        }
        return currencies;
    }

    @Transactional(readOnly = true)
    public Currency findById(Long id) {
        return currencyRepository.findById(id).orElseThrow(() -> new CurrencyNotFoundException("Currencies not found."));
    }

    @Transactional(readOnly = true)
    public Currency findByCode(String code) {
        return currencyRepository.findByCode(code).orElseThrow(() -> new CurrencyNotFoundException("Currencies not found."));
    }

    @Transactional(readOnly = true)
    public int countAllCurrencies() {
        int count = currencyRepository.countAllCurrencies();
        if(count < 0) {
            throw new IllegalArgumentException("count less then 0.");
        }
        return count;
    }

    @Transactional(readOnly = true)
    public Page<Currency> findByStatus(Status status, int size) {
        Page<Currency> currencyPage = currencyRepository.findByStatus(status, PageRequest.of(0, size));
        if(currencyPage.isEmpty()) {
            throw new CurrencyNotFoundException("Currencies not found.");
        }
        return currencyPage;
    }

    @Transactional(readOnly = true)
    public Page<Currency> orderByCodeAsc(int size) {
        Page<Currency> currencyPage = currencyRepository.orderByCode(PageRequest.of(0, size));
        if(currencyPage.isEmpty()) {
            throw new CurrencyNotFoundException("Currencies not found.");
        }
        return currencyPage;
    }

    @Transactional(readOnly = true)
    public Page<Currency> orderByNameAsc(int size) {
        Page<Currency> currencyPage = currencyRepository.orderByName(PageRequest.of(0, size));
        if(currencyPage.isEmpty()) {
            throw new CurrencyNotFoundException("Currencies not found.");
        }
        return currencyPage;
    }

    @Transactional
    @Modifying
    public void saveCurrency(Currency currency) {
        try {
            currencyRepository.save(currency);
        } catch (Exception e) {
            throw new SaveException("Failed in saving currency.");
        }
    }

    @Transactional
    @Modifying
    public void deleteById(Long id) {
        Currency currency = currencyRepository.findById(id).orElseThrow(() -> new CurrencyNotFoundException("Currency not found."));
        currencyRepository.deleteById(id);
    }

    @Transactional
    @Modifying
    public void updateCurrency(Currency updatedCurrency) {
        Currency existingCurrency = currencyRepository.findById(updatedCurrency.getId())
                .orElseThrow(() -> new CurrencyNotFoundException("Currency not found."));
        modelMapper.map(updatedCurrency, existingCurrency);
        try {
            currencyRepository.save(existingCurrency);
        } catch (Exception e) {
            throw new UpdateException("Failed in updating currency.");
        }
    }
}
