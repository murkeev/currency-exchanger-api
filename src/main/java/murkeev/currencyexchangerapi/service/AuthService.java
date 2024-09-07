package murkeev.currencyexchangerapi.service;

import lombok.AllArgsConstructor;
import murkeev.currencyexchangerapi.entity.User;
import murkeev.currencyexchangerapi.jwt.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public String authenticateAndGenerateToken(String login, String password) {
        User user = userService.checkEmailOrUsername(login);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
        UserDetails userDetails = userDetailsService.loadUserByUsername(login);
        return jwtTokenUtil.generateToken(userDetails);
    }
    public String addUser(User user) {
        return userService.addUser(user);
    }
}
