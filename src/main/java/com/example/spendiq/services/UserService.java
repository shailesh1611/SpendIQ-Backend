package com.example.spendiq.services;

import com.example.spendiq.entity.Role;
import com.example.spendiq.entity.User;
import com.example.spendiq.exception.UserAlreadyExistsException;
import com.example.spendiq.exception.UserNotFoundException;
import com.example.spendiq.repository.UserRepository;
import com.example.spendiq.util.Placeholder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
            Role role = new Role(Placeholder.ROLE_USER);
            user.addRole(role);
            user.setEmailVerified(false);
            user.setCreatedAt(LocalDateTime.now());
            return userRepository.save(user);
        }
        catch (DataIntegrityViolationException e) {
            log.error("User with email {} already exists", user.getEmail());
            throw new UserAlreadyExistsException("User with username " + user.getEmail() + " already exists");
        }
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    public boolean isEmailVerified(String email) {
        User user = findUserByEmail(email);
        return user.isEmailVerified();
    }

    public void updateEmailVerified(String email, boolean b) {
        User user = findUserByEmail(email);
        if(user.isEmailVerified() == b) return;
        user.setEmailVerified(b);
        userRepository.save(user);
    }
}
