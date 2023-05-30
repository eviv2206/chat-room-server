package com.bsuir.chatroomserver.controller.encryption;

import com.bsuir.chatroomserver.service.encryption.EncryptionService;
import com.bsuir.chatroomserver.service.encryption.model.KeyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/encryption")
public class EncryptionController {

    private final EncryptionService encryptionService;

    @Autowired
    public EncryptionController(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @PostMapping("/set-key")
    public void setPublicKey(@RequestBody KeyDto keyDto) {
        this.encryptionService.setPublicKey(keyDto);
    }

    @GetMapping("/get-key")
    public KeyDto getPubicKey(@RequestParam String username) {
        return this.encryptionService.getPublicKey(username);
    }
}

