package com.bsuir.chatroomserver.service.chat.model;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@ToString
public class Message {
    private String senderName;
    private String receiverName;
    private String message;
    private String date;
    private Type type;
    private Status status;
}