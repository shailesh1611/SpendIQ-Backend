package com.example.spendiq.services;

import com.example.spendiq.entity.User;
import com.example.spendiq.exception.UserAlreadyExistsException;
import com.example.spendiq.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = encoder;
    }

    public User addUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(List.of("USER"));
            user.setCreatedAt(LocalDateTime.now());
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            log.error("User with username {} already exists", user.getUserName());
            throw new UserAlreadyExistsException("User with username " + user.getUserName() + " already exists");
        }
    }

    public User findUserByUserName(String userName) {
        return userRepository.findUserByUserName(userName);
    }
}
