package com.example.mohamed.chattapp.utils;

import android.text.TextUtils;
import android.util.Patterns;

public class InputValidation {

    public static boolean isValidEmail(String input){
        return (!TextUtils.isEmpty(input) && Patterns.EMAIL_ADDRESS.matcher(input).matches());
    }

    public static boolean isNotEmpty(String input){
        return (!TextUtils.isEmpty(input));
    }

    public static boolean passwordMatch(String pass, String passConfirm){
        return (pass.equals(passConfirm) && !pass.equals("") && !passConfirm.equals(""));
    }

    public static int isUsernameValid(String input){
        if (input.trim().contains(" ")){
            return 1;
        }
        else if (input.trim().isEmpty()){
            return 2;
        }
        else {
            return 0;
        }
    }
}
