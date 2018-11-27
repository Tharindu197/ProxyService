package com.fidenz.academy.util;

public class URLFormatFactory {
    public static URLFormatter buildFormatter(String URL){
        return new URLFormatter(URL);
    }

    public static URLFormatter getFormatter(){
        return new URLFormatter();
    }
}
