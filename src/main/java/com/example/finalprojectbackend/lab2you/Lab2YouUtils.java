package com.example.finalprojectbackend.lab2you;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Lab2YouUtils {

    public static String encodePassword(String password) {
        return  new BCryptPasswordEncoder().encode(password);
    }
    public static boolean verifyEmailFormat(String email) {
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
    public static boolean validatePhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^\\d{8}$");
    }

    public static boolean validateCui(String cui) {
        return cui.matches("^\\d{13}$");
    }

    public static boolean validateNit(String nit) {
        return nit.matches("^\\d{9}$");
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
    public static boolean isObjectNullOrEmpty(Object obj) {
        return obj == null;
    }

    public static String generateRandomPassword() {
        String password = "";
        for (int i = 0; i < 8; i++) {
            password += (char) (Math.random() * 26 + 'a');
        }
        return password;
    }

    public static String generateExpedientNumber() {
        Random random = new Random();
        int firstGroup = random.nextInt(10000); // Generate a random number between 0 and 9999
        int secondGroup = random.nextInt(100);
        int thirdGroup = random.nextInt(100);
        int fourthGroup = random.nextInt(100);
        int fifthGroup = random.nextInt(10000000); // Generate a random number between 0 and 9999999

        return String.format("%04d-%02d-%02d-%02d-%07d", firstGroup, secondGroup, thirdGroup, fourthGroup, fifthGroup);
    }

    public static String generateRequestCode(Date dateOfReception) {
        Random random = new Random();
        String letters = random.nextBoolean() ? "IN" : "EX";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String date = dateFormat.format(dateOfReception);
        int numbers = random.nextInt(100000);
        return String.format("%s-%s-%05d", letters, date, numbers);
    }
    public static int calculateExpirationDays(Date dateOfReception) {
        Date currentDate = new Date();
        long diff = currentDate.getTime() - dateOfReception.getTime();
        return (int) (diff / (24 * 60 * 60 * 1000));
    }

    public static int calculateQuantityFromList(List<?> list) {
        return list.size();
    }
}

