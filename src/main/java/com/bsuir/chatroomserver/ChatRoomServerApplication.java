package com.bsuir.chatroomserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan("com.bsuir.chatroomserver.repository")
@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class}
)
public class ChatRoomServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatRoomServerApplication.class, args);
    }

}
