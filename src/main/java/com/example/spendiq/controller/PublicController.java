package com.example.spendiq.controller;

import com.example.spendiq.api.response.StatusOk;
import com.example.spendiq.dto.UserLoginResponseDTO;
import com.example.spendiq.dto.UserRequestDTO;
import com.example.spendiq.dto.UserResponseDTO;
import com.example.spendiq.entity.User;
import com.example.spendiq.exception.InvalidCredentialsException;
import com.example.spendiq.mapper.UserMapper;
import com.example.spendiq.services.UserService;
import com.example.spendiq.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;

    @Autowired
    public PublicController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserMapper userMapper) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
    }


    @GetMapping("/health-check")
    public String checkHealth() {
        return "Ok";
    }

    @PostMapping("/signup")
    public ResponseEntity<StatusOk<UserResponseDTO>> addUser(@RequestBody UserRequestDTO requestData) {
        User user = userMapper.toUser(requestData);
        User addedUser = userService.addUser(user);
        return new ResponseEntity<>(new StatusOk<>(userMapper.toUserResponseDTO(addedUser)), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<StatusOk<UserLoginResponseDTO>> login(@RequestBody UserRequestDTO requestData) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestData.getUserName(), requestData.getPassword()));
            String token = jwtUtil.generateToken(requestData.getUserName());
            return new ResponseEntity<>(new StatusOk<>(new UserLoginResponseDTO(token)), HttpStatus.OK);
        }
        catch (BadCredentialsException e) {
            log.error("Invalid Username or Password");
            throw new InvalidCredentialsException("Invalid Username or Password");
        }
    }
}
