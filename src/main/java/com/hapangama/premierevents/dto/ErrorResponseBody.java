package com.hapangama.premierevents.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseBody {

    public ErrorResponseBody(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }

    private LocalDateTime timestamp = LocalDateTime.now();
    private int status;
    private String error;
    private String message;
}


