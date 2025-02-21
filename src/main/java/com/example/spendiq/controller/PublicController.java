package com.example.spendiq.controller;

import com.example.spendiq.api.response.StatusOk;
import com.example.spendiq.dto.email.*;
import com.example.spendiq.dto.user.*;
import com.example.spendiq.entity.User;
import com.example.spendiq.exception.InvalidCredentialsException;
import com.example.spendiq.exception.UserNotVerifiedException;
import com.example.spendiq.mapper.UserMapper;
import com.example.spendiq.services.EmailService;
import com.example.spendiq.services.UserService;
import com.example.spendiq.util.JwtOtpUtil;
import com.example.spendiq.util.JwtUtil;
import com.example.spendiq.util.Placeholder;
import com.example.spendiq.util.Utils;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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
    private final JwtOtpUtil jwtOtpUtil;
    private final EmailService emailService;

    @Autowired
    public PublicController(UserService userService,
                            AuthenticationManager authenticationManager,
                            JwtUtil jwtUtil,
                            UserMapper userMapper,
                            EmailService emailService,
                            JwtOtpUtil jwtOtpUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
        this.jwtOtpUtil = jwtOtpUtil;
        this.emailService = emailService;
    }


    @GetMapping("/health-check")
    public String checkHealth() {
        return "Ok";
    }

    @PostMapping("/signup")
    public ResponseEntity<StatusOk<UserResponseDTO>> addUser(@RequestBody @Valid UserRequestDTO requestData) {
        User user = userMapper.toUser(requestData);
        User addedUser = userService.addUser(user);
        return new ResponseEntity<>(new StatusOk<>(userMapper.toUserResponseDTO(addedUser)), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<StatusOk<UserLoginResponseDTO>> login(@RequestBody UserRequestDTO requestData) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestData.getEmail(), requestData.getPassword()));
            User user = userService.findUserByEmail(requestData.getEmail());
            if(user.getRoles().contains(Placeholder.ROLE_UNVERIFIED)) {
                throw new UserNotVerifiedException("User account is not verified. Please verify your email.");
            }
            String token = jwtUtil.generateToken(requestData.getEmail());
            return new ResponseEntity<>(new StatusOk<>(new UserLoginResponseDTO(token)), HttpStatus.OK);
        }
        catch (BadCredentialsException e) {
            log.error("Invalid Email or Password");
            throw new InvalidCredentialsException("Invalid Email or Password");
        }
    }

    @Transactional
    @PostMapping("/verify-email")
    public ResponseEntity<StatusOk<EmailVerificationResponseDTO>> verifyEmail(@RequestBody EmailVerificationRequestDTO requestData) throws MessagingException {
        if(userService.isEmailVerified(requestData.getEmail())) {
            return new ResponseEntity<>(new StatusOk<>(new EmailVerificationResponseDTO("Email Already Verified",null)),HttpStatus.OK);
        }
        String randomOtp = Utils.generateRandomOtp();
        String token = jwtOtpUtil.generateToken(randomOtp,requestData.getEmail());
        emailService.sendOtp(requestData.getEmail(), randomOtp);
        return new ResponseEntity<>(new StatusOk<>(new EmailVerificationResponseDTO("OTP Generated Successfully.",token)), HttpStatus.OK);
    }

    @PostMapping("/verify-email/validate-otp")
    public ResponseEntity<StatusOk<ValidateOtpResponseDTO>> validateOtp(@RequestBody ValidateOtpRequestDTO requestData) {
        boolean isValid = jwtOtpUtil.validateOtp(requestData.getToken(), requestData.getOtp());
        if (isValid) {
            String email = jwtOtpUtil.extractEmail(requestData.getToken());
            userService.updateRoleToUser(email);
            return new ResponseEntity<>(new StatusOk<>(new ValidateOtpResponseDTO("Email Verified Successfully")), HttpStatus.OK);
        }
        else throw new BadCredentialsException("Invalid Otp");
    }
}
