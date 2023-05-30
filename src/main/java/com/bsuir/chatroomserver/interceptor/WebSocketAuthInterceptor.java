package com.bsuir.chatroomserver.interceptor;

import com.bsuir.chatroomserver.provider.JwtTokenProvider;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public WebSocketAuthInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");

            if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
                String jwtToken = token.substring(7);
                if (!jwtTokenProvider.validateToken(jwtToken)) {
                    throw new IllegalArgumentException("Invalid token");
                }
                if (!jwtTokenProvider.isEmailConfirmedFromToken(jwtToken)){
                    throw new IllegalArgumentException("Email is not confirmed");
                }
            } else {
                throw new IllegalArgumentException("Invalid token format");
            }
        }

        return message;
    }

}
