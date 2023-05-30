package com.bsuir.chatroomserver.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtExceptions {
    public static class JwtAuthenticationException extends AuthenticationException {
        public JwtAuthenticationException(String message) {
            super(message);
        }
    }

    public static class InvalidJwtTokenException extends JwtAuthenticationException {
        public InvalidJwtTokenException(String message) {
            super(message);
        }
    }

    public static class ExpiredJwtTokenException extends JwtAuthenticationException {
        public ExpiredJwtTokenException(String message) {
            super(message);
        }
    }

    public static class UnsupportedJwtTokenException extends JwtAuthenticationException {
        public UnsupportedJwtTokenException(String message) {
            super(message);
        }
    }

    public static class JwtTokenValidationException extends JwtAuthenticationException {
        public JwtTokenValidationException(String message) {
            super(message);
        }
    }

}
