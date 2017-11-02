package com.mina.collegehelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mina on 21/10/17.
 */

class regex {
    static String name = "^[a-z A-Z]{4,}$";
    static String email = "^[\\w-_\\.]+@\\w+\\.[a-z]{2,}$";
    static String password = ".{6,}";
    static String code = "\\w{10}";
}

public class Validator {

    public static Boolean validateName(String name) {
        return validateRegex(name, regex.name);
    }

    public static Boolean validateEmail(String email) {
        return validateRegex(email, regex.email);
    }

    public static Boolean validatePassword(String password) {
        return validateRegex(password, regex.password);
    }

    private static Boolean validateRegex(String value, String regex) {
        return Pattern.compile(regex).matcher(value).matches();
    }

    public static boolean validateCode(String code) {
        return validateRegex(code, regex.code);
    }
}
