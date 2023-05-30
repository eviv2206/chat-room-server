package com.bsuir.chatroomserver.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Метод для шифрования пароля
    public static String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    // Метод для проверки соответствия пароля и его зашифрованной версии
    public static boolean checkPassword(String password, String encryptedPassword) {
        return passwordEncoder.matches(password, encryptedPassword);
    }
}
