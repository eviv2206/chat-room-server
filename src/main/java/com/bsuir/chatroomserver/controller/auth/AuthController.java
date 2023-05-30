package com.bsuir.chatroomserver.controller.auth;

import com.bsuir.chatroomserver.service.auth.AuthService;
import com.bsuir.chatroomserver.service.auth.model.CreateUserDto;
import com.bsuir.chatroomserver.service.auth.model.JwtToken;
import com.bsuir.chatroomserver.service.auth.model.LoginCredentials;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }


    @PostMapping("/register")
    public void registerUser(@RequestBody @Valid CreateUserDto credentials) {
        this.authService.register(credentials);
    }

    @GetMapping("/confirm")
    @ResponseBody
    public String confirmRegistration(@RequestParam("token") String token) {
       this.authService.confirmEmail(token);
       return "Email is confirmed";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<JwtToken> login(@RequestBody LoginCredentials credentials) {
        return ResponseEntity.ok(authService.login(credentials));
    }
}
