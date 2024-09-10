package murkeev.currencyexchangerapi.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import murkeev.currencyexchangerapi.dto.LoginRequestDto;
import murkeev.currencyexchangerapi.dto.RegistrationUserDto;
import murkeev.currencyexchangerapi.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegistrationUserDto registrationUserDto) {
        authService.addUser(registrationUserDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login (@RequestBody LoginRequestDto loginRequest) {
        String token = authService.authenticateAndGenerateToken(loginRequest.username(), loginRequest.password());
        return ResponseEntity.ok(token);
    }
}
