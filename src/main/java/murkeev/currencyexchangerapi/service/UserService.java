package murkeev.currencyexchangerapi.service;

import lombok.AllArgsConstructor;
import murkeev.currencyexchangerapi.dto.HistoryConversationDto;
import murkeev.currencyexchangerapi.dto.RegistrationUserDto;
import murkeev.currencyexchangerapi.entity.HistoryConversation;
import murkeev.currencyexchangerapi.entity.User;
import murkeev.currencyexchangerapi.enums.Role;
import murkeev.currencyexchangerapi.exceptions.UserNotFoundException;
import murkeev.currencyexchangerapi.repository.HistoryConversationRepository;
import murkeev.currencyexchangerapi.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final HistoryConversationRepository historyConversationRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

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

    public void addUser(RegistrationUserDto registrationUserDto) {
        User user = modelMapper.map(registrationUserDto, User.class);
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        user.setDate(LocalDate.now());
        userRepository.save(user);
    }

    public void saveConversionHistory(Long userId, HistoryConversationDto historyConversationDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        HistoryConversation historyConversation = modelMapper.map(historyConversationDto, HistoryConversation.class);
        historyConversation.setUser(user);
        historyConversation.setDate(LocalDateTime.now());
        historyConversationRepository.save(historyConversation);
    }

    public HistoryConversationDto getConversionHistory(Long id) {
        HistoryConversation historyConversation = historyConversationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("History not found"));

        return modelMapper.map(historyConversation, HistoryConversationDto.class);
    }
}