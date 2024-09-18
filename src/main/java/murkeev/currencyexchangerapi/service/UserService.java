package murkeev.currencyexchangerapi.service;

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

import java.time.LocalDate;
import java.util.List;

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

    public void addUser(RegistrationUserDto registrationUserDto) {
        User user = modelMapper.map(registrationUserDto, User.class);
        user.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new EntityManipulationException("Failed in saving user");
        }
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new RuntimeException("No authenticated user found");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername()).orElseThrow(
                () -> new EntityNotFoundException("User not found"));
    }

    @Cacheable(cacheNames = "users", key = "'findByRegistrationDate:' + #date + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<UserDto> findByRegistrationDate(LocalDate date, Pageable pageable) {
        Page<User> userPage = userRepository.findByDate(date, pageable);
        if (userPage.isEmpty()) {
            throw new EntityNotFoundException("No data!");
        }
        return userPage.map(user -> modelMapper.map(user, UserDto.class));
    }

    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityManipulationException("User with id " + id + " not found!"));
        return modelMapper.map(user, UserDto.class);
    }

    @CachePut(cacheNames = "users", key = "#updateDto.id")
    public UserDto update(UserUpdateDto updateDto) {
        User existingUser = userRepository.findById(updateDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        modelMapper.map(updateDto, existingUser);
        try {
            User updateUser = userRepository.save(existingUser);
            return modelMapper.map(updateUser, UserDto.class);
        } catch (Exception e) {
            throw new EntityManipulationException("Failed in update history");
        }
    }

    public Page<UserDto> getAllByFirstname(String firstname, Pageable pageable) {
        Page<User> userPage = userRepository.findByFirstname(firstname, pageable);
        if (userPage.isEmpty()) {
            throw new EntityNotFoundException("Users not found!");
        }
        return userPage.map(user -> modelMapper.map(user, UserDto.class));
    }

    public Page<UserDto> getAllByLastname(String lastname, Pageable pageable) {
        Page<User> userPage = userRepository.findByLastname(lastname, pageable);
        if (userPage.isEmpty()) {
            throw new EntityNotFoundException("Users not found!");
        }
        return userPage.map(user -> modelMapper.map(user, UserDto.class));
    }

    public Page<UserDto> findAll(Pageable pageable) {
        Page<User> userPage = userRepository.getAllUsers(pageable);
        if (userPage.isEmpty()) {
            throw new EntityNotFoundException("Users not found!");
        }
        return userPage.map(user -> modelMapper.map(user, UserDto.class));
    }

    public Page<UserDto> orderByConversation(Pageable pageable) {
        Page<User> userPage = userRepository.orderByConversation(pageable);
        if (userPage.isEmpty()) {
            throw new EntityNotFoundException("Users not found!");
        }
        return userPage.map(user -> modelMapper.map(user, UserDto.class));
    }

    @Cacheable(cacheNames = "users", key = "'orderBy:' + #value  + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<UserDto> orderBy(String value, Pageable pageable) {
        if (!List.of("email", "date", "username").contains(value)) {
            throw new IllegalArgumentException("Invalid sort value: " + value);
        }

        return switch (value) {
            case "email" -> userRepository.orderByEmail(pageable).map(user -> modelMapper.map(user, UserDto.class));
            case "date" -> userRepository.orderByRegistration(pageable).map(user -> modelMapper.map(user, UserDto.class));
            case "username" -> userRepository.orderByUsername(pageable).map(user -> modelMapper.map(user, UserDto.class));
            default -> throw new IllegalArgumentException("Unexpected sort value: " + value);
        };
    }

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

    @CacheEvict(cacheNames = "users", key = "'delete:account'")
    public void deleteAccount() {
        User user = getCurrentUser();
        try {
            userRepository.deleteById(user.getId());
        } catch (Exception e) {
            throw new EntityManipulationException("Failed in deleting account.");
        }
    }

    public UserDto profile() {
        User user = getCurrentUser();
        return modelMapper.map(userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found")), UserDto.class);
    }
}