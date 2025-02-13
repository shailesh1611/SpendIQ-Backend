package com.example.spendiq.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusOk<T> {
    private T body;
    private Boolean success;

    public StatusOk(T response) {
        success = true;
        body = response;
    }
}
