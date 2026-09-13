package com.paymentgateway.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data 
public class ErrorResponse {
    
    private String message;
    private LocalDateTime timeStamp;

    public ErrorResponse(String message){
        this.message = message;
        this.timeStamp = LocalDateTime.now();
    }

    // public String getMessage() {
    //     return message;
    // }

    // public LocalDateTime getTimeStamp() {
    //     return timeStamp;
    // }
}
