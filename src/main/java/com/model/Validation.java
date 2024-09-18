package com.model;

public class Validation {
    public static boolean validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    public static boolean validatePassword(String password) {
        String passwordRegex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";
        return password.matches(passwordRegex);
    }

    public static boolean validateDoubleValue(String value) {
        String doubleRegex = "^[+-]?([0-9]*[.])?[0-9]+$";
        return value.matches(doubleRegex);
    }

    public static boolean validateIntegerValue(String value) {
        String integerRegex = "^[+-]?\\d+$";
        return value.matches(integerRegex);
    }

}
