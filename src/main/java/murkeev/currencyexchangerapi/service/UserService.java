package murkeev.currencyexchangerapi.service;

import lombok.AllArgsConstructor;
import murkeev.currencyexchangerapi.dto.RegistrationUserDto;
import murkeev.currencyexchangerapi.entity.User;
import murkeev.currencyexchangerapi.exceptions.UserNotFoundException;
import murkeev.currencyexchangerapi.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public User checkEmailOrUsername(String login) {
        User user;
        if (login.contains("@")) {
            user = userRepository.findByEmail(login).orElseThrow(
                    () -> new UserNotFoundException(String.format("User with email %s not found!", login)));
        } else {
            user = userRepository.findByUsername(login).orElseThrow(
                    () -> new UserNotFoundException(String.format("User with login %s not found!", login)));
        }

        if (user == null) {
            throw new UserNotFoundException("User is null.");
        }
        return user;
    }

    public void addUser(RegistrationUserDto registrationUserDto) {
        User user = modelMapper.map(registrationUserDto, User.class);
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        userRepository.save(user);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new RuntimeException("No authenticated user found");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> new UserNotFoundException("User not found"));
    }
}