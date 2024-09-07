package murkeev.currencyexchangerapi.controllers;

import lombok.AllArgsConstructor;
import murkeev.currencyexchangerapi.dto.AuthRequestDto;
import murkeev.currencyexchangerapi.entity.User;
import murkeev.currencyexchangerapi.jwt.JwtTokenUtil;
import murkeev.currencyexchangerapi.service.AuthService;
import murkeev.currencyexchangerapi.service.UserDetailsServiceImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody User user) {
        return authService.addUser(user);
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequestDto authRequestDto) {
        return authService.authenticateAndGenerateToken(authRequestDto.username(), authRequestDto.password());
    }

    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile() {
        return "Welcome to User Profile";
    }

    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }
}
