package com.fidenz.academy.url.formatter;

public class URLFormatFactory {
    public static URLFormatterImpl buildFormatter(String URL){
        return new URLFormatterImpl(URL);
    }

    public static URLFormatterImpl getFormatter(){
        return new URLFormatterImpl();
    }
}
