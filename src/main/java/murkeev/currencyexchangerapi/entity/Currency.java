package murkeev.currencyexchangerapi.entity;

import jakarta.persistence.*;
import lombok.*;
import murkeev.currencyexchangerapi.enums.Status;

@Entity
@Table(name = "currencies")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Currency {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String code;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @NonNull
    private String fullName;
}
