package com.fidenz.academy.util;

public class URLFormatterImpl implements URLFormatter {

    private String URL;

    public URLFormatterImpl(){}

    public URLFormatterImpl(String URL){
        this.URL = URL;
    }

    @Override
    public void addRequestParam(String key, Object value){
        if(this.URL.contains("?")){
            this.URL += "&" + key + "=" + String.valueOf(value);
        } else {
            this.URL += "?" + key + "=" + String.valueOf(value);
        }
    }

    @Override
    public void addPath(String path){
        if(this.URL.charAt(this.URL.length() - 1)!= '/'){
            this.URL += "/";
        }
        this.URL += path + "/";
    }

    @Override
    public String getURL() {
        return URL;
    }

    @Override
    public void setURL(String URL) {
        this.URL = URL;
    }
}
