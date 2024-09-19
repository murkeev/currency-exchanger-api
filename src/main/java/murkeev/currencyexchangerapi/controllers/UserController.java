package murkeev.currencyexchangerapi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import murkeev.currencyexchangerapi.dto.UserDto;
import murkeev.currencyexchangerapi.dto.UserUpdateDto;
import murkeev.currencyexchangerapi.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get all users", description = "Returns a paginated list of all users.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Page<UserDto> getAll(@RequestParam(value = "page_number") int pageNumber,
                                @RequestParam(value = "page_size") int pageSize) {
        return userService.findAll(PageRequest.of(pageNumber, pageSize));
    }

    @Operation(summary = "Get users by registration date")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/find-register")
    public Page<UserDto> getAllByRegistrationDate(@RequestParam LocalDate date,
                                                  @RequestParam(value = "page_number") int pageNumber,
                                                  @RequestParam(value = "page_size") int pageSize) {
        return userService.findByRegistrationDate(date, PageRequest.of(pageNumber, pageSize));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{id}")
    public UserDto findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @Operation(summary = "Get current user's profile (only for ROLE_USER)")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/profile")
    public UserDto profile() {
        return userService.profile();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-firstname")
    public Page<UserDto> getAllByFirstname(@RequestParam String firstname,
                                           @RequestParam(value = "page_number") int pageNumber,
                                           @RequestParam(value = "page_size") int pageSize) {
        return userService.getAllByFirstname(firstname, PageRequest.of(pageNumber, pageSize));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping
    public ResponseEntity<UserDto> update(@RequestBody UserUpdateDto updateDto) {
        return new ResponseEntity<>(userService.update(updateDto), HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-lastname")
    public Page<UserDto> getAllByLastname(@RequestParam String lastname,
                                          @RequestParam(value = "page_number") int pageNumber,
                                          @RequestParam(value = "page_size") int pageSize) {
        return userService.getAllByLastname(lastname, PageRequest.of(pageNumber, pageSize));
    }

    @Operation(summary = "Order users by conversation count")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/order-conversation")
    public Page<UserDto> orderByConversation(@RequestParam(value = "page_number") int pageNumber,
                                             @RequestParam(value = "page_size") int pageSize) {
        return userService.orderByConversation(PageRequest.of(pageNumber, pageSize));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        userService.removeUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Delete current user's account",
            description = "Deletes the account of the currently authenticated user.")
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete-account")
    public ResponseEntity<Void> deleteAccount() {
        userService.deleteAccount();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @Operation(summary = "Order users by specified value",
            description = "Orders users by a specified value (e.g., username, email or date), with pagination.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/order-by/{value}")
    public Page<UserDto> orderBy(@PathVariable String value,
                                 @RequestParam(value = "page_number") int pageNumber,
                                 @RequestParam(value = "page_size") int pageSize) {
        return userService.orderBy(value, PageRequest.of(pageNumber, pageSize));
    }
}
