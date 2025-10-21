package com.example.finalprojectbackend.lab2you.db.model.wrappers;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PDFSummary {
    Map<String, Map<String,String>> pdfSummary = new HashMap<>();
    public void add(String key, Map<String,String> value) {
        pdfSummary.put(key, value);
    }
}
