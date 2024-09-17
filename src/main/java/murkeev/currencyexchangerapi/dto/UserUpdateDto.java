package murkeev.currencyexchangerapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserUpdateDto implements Serializable {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
}
