package murkeev.currencyexchangerapi.dto;

import lombok.Builder;

@Builder
public record RegistrationUserDto(
        String username,
        String password,
        String confirmPassword,
        String email
) {
}
