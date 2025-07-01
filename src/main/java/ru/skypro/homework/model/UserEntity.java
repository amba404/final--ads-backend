package ru.skypro.homework.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.config.UserConfig;
import ru.skypro.homework.dto.Role;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(name = "username", nullable = false, unique = true, length = UserConfig.EMAIL_MAX_LENGTH)
    @Email
    private String username;

    @Column(name = "firstname", nullable = false, length = UserConfig.FIRST_NAME_MAX_LENGTH)
    @Size(min = UserConfig.FIRST_NAME_MIN_LENGTH, max = UserConfig.FIRST_NAME_MAX_LENGTH)
    @NotBlank
    private String firstName;

    @Column(name = "lastname", nullable = false, length = UserConfig.LAST_NAME_MAX_LENGTH)
    @Size(min = UserConfig.LAST_NAME_MIN_LENGTH, max = UserConfig.LAST_NAME_MAX_LENGTH)
    @NotBlank
    private String lastName;

    @Column(name = "phone", nullable = false, length = UserConfig.PHONE_LENGTH)
    @Pattern(regexp = UserConfig.PHONE_PATTERN)
    private String phone;

    @Column(name = "role", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "password", nullable = false)
    @NotBlank
    private String password;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private ImageEntity image;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AdEntity> ads;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CommentEntity> comments;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
}
