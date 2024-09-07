package murkeev.currencyexchangerapi.dto;

import lombok.Builder;

@Builder
public record AuthRequestDto(
        String username,
        String password
) {
}
