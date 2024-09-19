package murkeev.currencyexchangerapi.service;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import murkeev.currencyexchangerapi.dto.RegistrationUserDto;
import murkeev.currencyexchangerapi.dto.UserDto;
import murkeev.currencyexchangerapi.dto.UserUpdateDto;
import murkeev.currencyexchangerapi.entity.User;
import murkeev.currencyexchangerapi.exceptions.EntityManipulationException;
import murkeev.currencyexchangerapi.exceptions.EntityNotFoundException;
import murkeev.currencyexchangerapi.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    private static final String USER_NOT_FOUND = "User not found";

    @Transactional(readOnly = true)
    public User checkEmailOrUsername(String login) {
        User user;
        if (login.contains("@")) {
            user = userRepository.findByEmail(login).orElseThrow(
                    () -> new EntityNotFoundException(String.format("User with email %s not found!", login)));
        } else {
            user = userRepository.findByUsername(login).orElseThrow(
                    () -> new EntityNotFoundException(String.format("User with login %s not found!", login)));
        }

        if (user == null) {
            throw new EntityNotFoundException("User is null.");
        }
        return user;
    }

    @Transactional
    public void addUser(RegistrationUserDto registrationUserDto) {
        User user = modelMapper.map(registrationUserDto, User.class);
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new EntityManipulationException("Failed in saving user");
        }
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalArgumentException("No authenticated user found");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> new EntityNotFoundException(USER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "users", key = "'findByRegistrationDate:' + #date + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<UserDto> findByRegistrationDate(LocalDate date, Pageable pageable) {
        Page<User> userPage = userRepository.findByDate(date, pageable);
        if (userPage.isEmpty()) {
            throw new EntityNotFoundException("No data!");
        }
        return userPage.map(user -> modelMapper.map(user, UserDto.class));
    }

    @Transactional(readOnly = true)
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityManipulationException("User with id " + id + " not found!"));
        return modelMapper.map(user, UserDto.class);
    }

    @Transactional
    @CachePut(cacheNames = "users", key = "#updateDto.id")
    public UserDto update(UserUpdateDto updateDto) {
        User existingUser = userRepository.findById(updateDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        modelMapper.map(updateDto, existingUser);
        try {
            User updateUser = userRepository.save(existingUser);
            return modelMapper.map(updateUser, UserDto.class);
        } catch (Exception e) {
            throw new EntityManipulationException("Failed in update history");
        }
    }

    @Transactional(readOnly = true)
    public Page<UserDto> getAllByFirstname(String firstname, Pageable pageable) {
        Page<User> userPage = userRepository.findByFirstname(firstname, pageable);
        if (userPage.isEmpty()) {
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }
        return userPage.map(user -> modelMapper.map(user, UserDto.class));
    }

    @Transactional(readOnly = true)
    public Page<UserDto> getAllByLastname(String lastname, Pageable pageable) {
        Page<User> userPage = userRepository.findByLastname(lastname, pageable);
        if (userPage.isEmpty()) {
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }
        return userPage.map(user -> modelMapper.map(user, UserDto.class));
    }

    @Transactional(readOnly = true)
    public Page<UserDto> findAll(Pageable pageable) {
        Page<User> userPage = userRepository.getAllUsers(pageable);
        if (userPage.isEmpty()) {
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }
        return userPage.map(user -> modelMapper.map(user, UserDto.class));
    }

    @Transactional(readOnly = true)
    public Page<UserDto> orderByConversation(Pageable pageable) {
        Page<User> userPage = userRepository.orderByConversation(pageable);
        if (userPage.isEmpty()) {
            throw new EntityNotFoundException(USER_NOT_FOUND);
        }
        return userPage.map(user -> modelMapper.map(user, UserDto.class));
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "users", key = "'orderBy:' + #value  + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<UserDto> orderBy(@NotNull String value, Pageable pageable) {

        return switch (value) {
            case "email" -> userRepository.orderByEmail(pageable).map(user -> modelMapper.map(user, UserDto.class));
            case "date" ->
                    userRepository.orderByRegistration(pageable).map(user -> modelMapper.map(user, UserDto.class));
            case "username" ->
                    userRepository.orderByUsername(pageable).map(user -> modelMapper.map(user, UserDto.class));
            default -> throw new IllegalArgumentException("Unexpected sort value: " + value);
        };
    }

    @Transactional
    @CacheEvict(cacheNames = "users", key = "'delete' + #id")
    public void removeUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found!"));
        try {
            userRepository.deleteById(user.getId());
        } catch (Exception e) {
            throw new EntityManipulationException("Error in remove user");
        }
    }

    @Transactional
    @CacheEvict(cacheNames = "users", key = "'delete:account'")
    public void deleteAccount() {
        User user = getCurrentUser();
        try {
            userRepository.deleteById(user.getId());
        } catch (Exception e) {
            throw new EntityManipulationException("Failed in deleting account.");
        }
    }

    @Transactional(readOnly = true)
    public UserDto profile() {
        User user = getCurrentUser();
        return modelMapper.map(user, UserDto.class);
    }
}