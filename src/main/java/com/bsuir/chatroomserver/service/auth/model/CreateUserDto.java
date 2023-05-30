package com.bsuir.chatroomserver.service.auth.model;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserDto {
    @Size(min = 3, max = 20, message = "Логин должен содержать от 3 до 20 символов")
    String login;

    @Email(message = "Проверьте правильность введенной почты")
    String email;

    @Size(min = 4, max = 20, message = "Пароль должен содержать от 4 до 20 символов")
    String password;
}
