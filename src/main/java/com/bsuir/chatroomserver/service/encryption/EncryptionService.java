package com.bsuir.chatroomserver.service.encryption;

import com.bsuir.chatroomserver.repository.user.User;
import com.bsuir.chatroomserver.service.encryption.model.KeyDto;
import com.bsuir.chatroomserver.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

    private final UserService userService;

    @Autowired
    public EncryptionService(UserService userService){
        this.userService = userService;
    }

    public void setPublicKey(KeyDto keyDto){
        User user =  this.userService.findByUsername(keyDto.getUsername());
        user.setPublicKey(keyDto.getPublicKey());
        this.userService.update(user);
    }

    public KeyDto getPublicKey(String username){
        KeyDto keyDto = new KeyDto();
        User user = this.userService.findByUsername(username);
        keyDto.setUsername(user.getUsername());
        keyDto.setPublicKey(user.getPublicKey());
        return keyDto;
    }

}
