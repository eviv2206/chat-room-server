package com.bsuir.chatroomserver.service.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtToken {
    String accessToken;
}
