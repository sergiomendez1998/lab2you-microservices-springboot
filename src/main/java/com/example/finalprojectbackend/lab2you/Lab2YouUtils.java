package com.example.finalprojectbackend.lab2you;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Lab2YouUtils {

    public static String encodePassword(String password) {
        return  new BCryptPasswordEncoder().encode(password);
    }

    public static boolean passwordEncrypted(String password, String encodedPassword) {
        return new BCryptPasswordEncoder().matches(password, encodedPassword);
    }

    public static boolean verifyEmailFormat(String email) {
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    public static boolean validatePhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^\\d{8}$");
    }

    public static boolean validateCUI(String cui) {
        return cui.matches("^\\d{13}$");
    }

    public static boolean validateNit(String nit) {
        return nit.matches("^\\d{9}$");
    }

    public String removeSpaces(String str) {
        return str.replaceAll("\\s+","");
    }

    public String removeUnderScores(String str) {
        return str.replaceAll("_","");
    }

}
