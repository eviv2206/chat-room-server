package com.bsuir.chatroomserver.service.encryption.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyDto {
    String username;
    String publicKey;
}
