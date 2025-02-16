package com.example.spendiq.dto.email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmailVerificationResponseDTO {
    private String message;
    private String otpValidationToken;
}
