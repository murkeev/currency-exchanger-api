package murkeev.currencyexchangerapi.repository;

import murkeev.currencyexchangerapi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    @Query("SELECT u FROM User u WHERE u.email = :login")
    Optional<User> findByEmail(String login);

    @Query("SELECT u FROM User u WHERE u.username = :login")
    Optional<User> findByUsername(String login);

    @Query("SELECT u FROM User u")
    Page<User> getAllUsers(Pageable pageable);

    @Query("SELECT u FROM User u ORDER BY u.date")
    Page<User> orderByRegistration(Pageable pageable);

    @Query("SELECT u FROM User u ORDER BY u.username")
    Page<User> orderByUsername(Pageable pageable);

    @Query("SELECT u FROM User u LEFT JOIN u.historyConversations hc GROUP BY u ORDER BY COUNT(hc) DESC")
    Page<User> orderByConversation(Pageable pageable);

    @Query("SELECT u FROM User u ORDER BY u.email")
    Page<User> orderByEmail(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.date = :date ")
    Page<User> findByDate(LocalDate date, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.firstname = :firstname")
    Page<User> findByFirstname(String firstname, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.lastname = :lastname")
    Page<User> findByLastname(String lastname, Pageable pageable);
}