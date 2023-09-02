package com.example.finalprojectbackend.lab2you.db.model.wrappers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseWrapper<T> {
    private boolean successful;
    private String message;
    private T data;

    public ResponseWrapper(boolean successful, String message, T data) {
        this.successful = successful;
        this.message = message;
        this.data = data;
    }
}
