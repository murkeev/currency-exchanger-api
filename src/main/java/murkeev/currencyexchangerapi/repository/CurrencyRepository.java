package murkeev.currencyexchangerapi.repository;

import murkeev.currencyexchangerapi.enums.Status;
import murkeev.currencyexchangerapi.entity.Currency;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    List<Currency> findAll();

    @Query("SELECT c FROM Currency c WHERE c.code = :code")
    Optional<Currency> findByCode(String code);

    @Query("SELECT COUNT(c) FROM Currency c")
    int countAllCurrencies();

    @Query("SELECT c FROM Currency c WHERE c.status = :status")
    Page<Currency> findByStatus(Status status, Pageable pageable);

    @Query("SELECT c FROM Currency c ORDER BY c.code ASC")
    Page<Currency> orderByCode(Pageable pageable);

    @Query("SELECT c FROM Currency c ORDER BY c.fullName ASC")
    Page<Currency> orderByName(Pageable pageable);
}
