package com.fidenz.academy.util;

public class URLFormatFactory {
    public static URLFormatterImpl buildFormatter(String URL){
        return new URLFormatterImpl(URL);
    }

    public static URLFormatterImpl getFormatter(){
        return new URLFormatterImpl();
    }
}
