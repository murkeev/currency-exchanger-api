package murkeev.currencyexchangerapi.service;

import lombok.AllArgsConstructor;
import murkeev.currencyexchangerapi.dto.RegistrationUserDto;
import murkeev.currencyexchangerapi.entity.User;
import murkeev.currencyexchangerapi.exceptions.UserNotFoundException;
import murkeev.currencyexchangerapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User checkEmailOrUsername(String login) {
        User user;
        if (login.contains("@")) {
            user = userRepository.checkEmail(login).orElseThrow(
                    () -> new UserNotFoundException(String.format("User with email %s not found!", login)));
        } else {
            user = userRepository.checkUsername(login).orElseThrow(
                    () -> new UserNotFoundException(String.format("User with login %s not found!", login)));
        }

        if (user == null) {
            throw new UserNotFoundException("User is null.");
        }
        return user;
    }

    public String addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User added";
    }
}