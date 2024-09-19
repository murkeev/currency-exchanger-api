package murkeev.currencyexchangerapi.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import murkeev.currencyexchangerapi.enums.Role;
import murkeev.currencyexchangerapi.util.DateSerializer;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(name = "first_name")
    private String firstname;

    @Column(name = "last_name")
    private String lastname;

    @Email
    @Column(unique = true)
    private String email;

    @CreationTimestamp
    @Column(name = "register_time")
    @JsonSerialize(using = DateSerializer.class)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ROLE_USER'")
    private Role role = Role.ROLE_USER;

    @OneToMany(mappedBy = "user")
    private List<HistoryConversation> historyConversations;
}