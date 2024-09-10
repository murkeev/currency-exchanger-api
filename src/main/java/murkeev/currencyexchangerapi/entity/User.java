package murkeev.currencyexchangerapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import murkeev.currencyexchangerapi.enums.Role;
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
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Email
    @Column(unique = true)
    private String email;

    @CreationTimestamp
    @Column(name = "register_time")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ROLE_USER'")
    private Role role = Role.ROLE_USER;

    @OneToMany(mappedBy = "user")
    private List<HistoryConversation> historyConversations;

    public int getCountOfConversations() {
        if (historyConversations != null) {
            return historyConversations.size();
        } else {
            return 0;
        }
    }
}