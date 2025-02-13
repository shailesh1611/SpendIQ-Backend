package com.example.spendiq.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private Boolean status;
    private ErrorBody body;
    public ErrorResponse(int errorCode, String message) {
        body = new ErrorBody();
        status = false;
        this.body.setCode(errorCode);
        this.body.setMessage(message);
    }

    @Getter
    @Setter
    private class ErrorBody {
        private int code;
        private String message;
    }
}
