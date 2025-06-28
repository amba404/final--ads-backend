package ru.skypro.homework.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.skypro.homework.config.UserConfig;
import ru.skypro.homework.dto.Role;

@Entity
@Data
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(name = "email", nullable = false, unique = true, length = UserConfig.EMAIL_MAX_LENGTH)
    @Email
    private String email;

    @Column(name = "first_name", nullable = false, length = UserConfig.FIRST_NAME_MAX_LENGTH)
    @Size(min = UserConfig.FIRST_NAME_MIN_LENGTH, max = UserConfig.FIRST_NAME_MAX_LENGTH)
    @NotBlank
    private String firstName;

    @Column(name = "last_name", nullable = false, length = UserConfig.LAST_NAME_MAX_LENGTH)
    @Size(min = UserConfig.LAST_NAME_MIN_LENGTH, max = UserConfig.LAST_NAME_MAX_LENGTH)
    @NotBlank
    private String lastName;

    @Column(name = "phone", nullable = false, length = UserConfig.PHONE_LENGTH)
    @Pattern(regexp = UserConfig.PHONE_PATTERN)
    private String phone;

    @Column(name = "role", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "image")
    private String image;

    @Column(name = "password_encoded", nullable = false)
    @NotBlank
    private String passwordEncoded;

}
