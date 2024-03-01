package com.javaFX.utils;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.TextFieldSkin;

public class MySkin extends TextFieldSkin {
    private char mask_char='*';

    public MySkin(TextField arg0){
        super(arg0);
    }
    protected String maskText(String text){
        if (getSkinnable() instanceof PasswordField){
            int size =text.length();
            return String.valueOf(mask_char).repeat(size);
        }
        else
            return text;
    }

}
