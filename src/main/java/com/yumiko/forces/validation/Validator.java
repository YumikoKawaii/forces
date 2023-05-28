package com.yumiko.forces.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private static final String email_regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern email_pattern = Pattern.compile(email_regex);
    private static final long token_validity = 60 * 60 * 24 * 7;
    public static boolean validateEmail(String email) {
        Matcher matcher = email_pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean validatePassword(String password) {

        boolean containsDigit = false;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isDigit(password.charAt(i))) {
                containsDigit = true;
                break;
            }
        }

        boolean containsLetter = false;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isLetter(password.charAt(i))) {
                containsLetter = true;
                break;
            }
        }

        boolean containsSpecial = false;
        for (int i = 0; i < password.length(); i++) {
            if (!Character.isLetterOrDigit(password.charAt(i))) {
                containsSpecial = true;
                break;
            }
        }

        return password.length() >= 8 && containsDigit && containsLetter && containsSpecial;
    }

}
