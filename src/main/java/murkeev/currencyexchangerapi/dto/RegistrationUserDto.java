package murkeev.currencyexchangerapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegistrationUserDto {
    @Pattern(regexp = "^[a-zA-Z0-9]{4,}$",
            message = "Username must be at least 4 characters long and contain only letters and digits")
    private String username;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]{8,}$",
            message = "Password must be at least 8 characters long and contain at least one uppercase letter," +
                      "one lowercase letter, and one digit")
    private String password;

    @Email
    private String email;
}
