package com.bsuir.chatroomserver.controller.chat;

import com.bsuir.chatroomserver.service.chat.ChatService;
import com.bsuir.chatroomserver.service.chat.model.Message;
import com.bsuir.chatroomserver.service.chat.model.Status;
import com.bsuir.chatroomserver.service.chat.model.Type;
import jakarta.servlet.annotation.MultipartConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Controller
@MultipartConfig
public class ChatController {
    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message receiveMessage(@Payload Message message) {
        return message;
    }

    @MessageMapping("/private-message")
    public Message recMessage(@Payload Message message) {
        this.chatService.convertAndSendToUser(message);
        return message;
    }


    @PostMapping("/upload")
    public ResponseEntity<Message> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam String receiverName, @RequestParam String senderName) {
        chatService.uploadFile(file);
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String messageText = "Загружен файл: " + fileName;

        Message message = new Message();
        message.setMessage(messageText);
        message.setType(Type.FILE);
        message.setSenderName(senderName);
        message.setStatus(Status.MESSAGE);
        if (!receiverName.equals("")) {
            message.setReceiverName(receiverName);
            chatService.convertAndSendToUser(message);
        } else {
            chatService.convertAndSendToAll(message);
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        return chatService.downloadFile(filename);
    }


}