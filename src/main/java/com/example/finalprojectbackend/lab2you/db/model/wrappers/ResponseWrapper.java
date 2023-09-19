package com.example.finalprojectbackend.lab2you.db.model.wrappers;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ResponseWrapper {
    private boolean successful;
    private String message;
    private List<?> data;
    private Map<String, String> errors;

    public ResponseWrapper(){
        errors = new HashMap<>();
    }

    public ResponseWrapper(boolean successful, String message, List<?> data) {
        this.successful = successful;
        this.message = message;
        this.data = data;
    }

    public void addError(String key, String value){
        errors.put(key, value);
    }
}
