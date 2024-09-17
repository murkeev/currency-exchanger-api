package murkeev.currencyexchangerapi.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto implements Serializable {
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private LocalDate date;
}
