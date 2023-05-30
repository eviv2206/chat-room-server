package com.bsuir.chatroomserver.service.user;

import com.bsuir.chatroomserver.repository.user.User;
import com.bsuir.chatroomserver.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(User user) {
        return userRepository.save(user);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public void confirmEmail(User user) {
        user.setEmailConfirmed(true);
        userRepository.save(user);
    }

}
