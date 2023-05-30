package com.bsuir.chatroomserver.service.chat;


import com.bsuir.chatroomserver.service.chat.model.Message;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Objects;


@Service
public class ChatService {

    private static final String FTP_HOST = "192.168.182.1";
    private static final int FTP_PORT = 21;
    private static final String FTP_REMOTE_DIRECTORY = "/files";

    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public ChatService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void convertAndSendToUser(Message message) {
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(), "/private", message);
    }

    public void convertAndSendToAll(Message message) {
        simpMessagingTemplate.convertAndSend("/chatroom/public", message);
    }

    public void uploadFile(MultipartFile file) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(FTP_HOST, FTP_PORT);
            ftpClient.login("anonymous", "");
            if (ftpClient.isConnected()) {
                System.out.println("Соединение с FTP-сервером установлено");
            } else {
                System.out.println("Не удалось установить соединение с FTP-сервером");
            }
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            InputStream inputStream = file.getInputStream();
            ftpClient.changeWorkingDirectory(FTP_REMOTE_DIRECTORY);
            ftpClient.storeFile(fileName, inputStream);
            inputStream.close();
            ftpClient.logout();


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ResponseEntity<Resource> downloadFile(String filename) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(FTP_HOST, FTP_PORT);
            ftpClient.login("anonymous", "");
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            InputStream inputStream = ftpClient.retrieveFileStream(filename);

            Resource resource = new InputStreamResource(inputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        } finally {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
