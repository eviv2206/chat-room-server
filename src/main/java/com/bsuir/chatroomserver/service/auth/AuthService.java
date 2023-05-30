package com.bsuir.chatroomserver.service.auth;

import com.bsuir.chatroomserver.exception.DuplicateEntityException;
import com.bsuir.chatroomserver.exception.EntityNotFoundException;
import com.bsuir.chatroomserver.provider.JwtTokenProvider;
import com.bsuir.chatroomserver.service.auth.exception.BadCredentialsException;
import com.bsuir.chatroomserver.service.auth.model.CreateUserDto;
import com.bsuir.chatroomserver.service.auth.model.JwtToken;
import com.bsuir.chatroomserver.service.auth.model.LoginCredentials;
import com.bsuir.chatroomserver.service.email.EmailService;
import com.bsuir.chatroomserver.service.user.UserService;
import com.bsuir.chatroomserver.repository.user.User;
import com.bsuir.chatroomserver.util.PasswordEncoder;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@Service
public class AuthService {

    private final UserService userService;

    private final JwtTokenProvider tokenProvider;

    private final EmailService emailService;

    @Autowired
    public AuthService(UserService userService, JwtTokenProvider jwtTokenProvider, EmailService emailService){
        this.userService = userService;
        this.tokenProvider = jwtTokenProvider;
        this.emailService = emailService;
    }

    @ExceptionHandler({DuplicateEntityException.class})
    public void register(CreateUserDto createUserDto){
        if (Objects.nonNull(userService.findByEmail(createUserDto.getEmail()))){
            throw new DuplicateEntityException("Пользователь с такой почтой уже существует");
        }
        if (Objects.nonNull(userService.findByUsername(createUserDto.getLogin()))){
            throw new DuplicateEntityException("Пользователь с таким логином уже существует");
        }
        final User partlyUser = new User();
        partlyUser.setUsername(createUserDto.getLogin());
        partlyUser.setPassword(PasswordEncoder.encryptPassword(createUserDto.getPassword()));
        partlyUser.setEmail(createUserDto.getEmail());
        partlyUser.setEmailConfirmed(false);
        final User savedUser = this.userService.register(partlyUser);
        String jwt = tokenProvider.generateRegistrationToken(savedUser);
        try {
            emailService.sendConfirmationEmail(savedUser.getEmail(), jwt,  savedUser.getUsername());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void confirmEmail(String token){
        if (tokenProvider.validateToken(token)) {
            final User userToSave = userService.findByUsername(tokenProvider.getUsernameFromToken(token));
            userService.confirmEmail(userToSave);
        }
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public JwtToken login(LoginCredentials credentials){
        final User user = userService.findByUsername(credentials.getLogin());
        if (Objects.isNull(user)){
            throw new EntityNotFoundException("Неверный логин или пароль");
        }
        if (PasswordEncoder.checkPassword(credentials.getPassword(), user.getPassword())){
            return new JwtToken(tokenProvider.generateSignInToken(user));
        } else {
            throw new BadCredentialsException("Неверный логин или пароль");
        }
    }


}
