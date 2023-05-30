package com.bsuir.chatroomserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicateEntityException extends RuntimeException {
    public DuplicateEntityException(String message){
        super(message);
    }
}
