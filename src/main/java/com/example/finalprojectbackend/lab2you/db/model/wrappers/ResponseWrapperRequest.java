package com.example.finalprojectbackend.lab2you.db.model.wrappers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseWrapperRequest<T> {
    private T data;
    private String message;
    private boolean successful;

    public ResponseWrapperRequest(T data, String message, boolean successful) {
        this.data = data;
        this.message = message;
        this.successful = successful;
    }
}
