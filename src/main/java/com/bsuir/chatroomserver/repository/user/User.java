package com.bsuir.chatroomserver.repository.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 3, max = 20, message = "Логин должен содержать от 3 до 20 символов")
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Email(message = "Проверьте правильность введенной почты")
    private String email;

    @Column
    private boolean emailConfirmed;

    @Column(columnDefinition = "LONGTEXT")
    private String publicKey;
}
