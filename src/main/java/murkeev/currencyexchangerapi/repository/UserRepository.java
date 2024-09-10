package murkeev.currencyexchangerapi.repository;

import murkeev.currencyexchangerapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    @Query("SELECT u FROM User u WHERE u.email = :login")
    Optional<User> findByEmail(String login);

    @Query("SELECT u FROM User u WHERE u.username = :login")
    Optional<User> findByUsername(String login);
}