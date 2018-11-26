package com.fidenz.academy.util;

public class URLFormatter {

    private String URL;

    public URLFormatter(){}

    public URLFormatter(String URL){
        this.URL = URL;
    }

    public void addRequestParam(String key, Object value){
        if(this.URL.contains("?")){
            this.URL += "&" + key + "=" + String.valueOf(value);
        } else {
            this.URL += "?" + key + "=" + String.valueOf(value);
        }
    }

    public void addPath(String path){
        if(this.URL.charAt(this.URL.length() - 1)!= '/'){
            this.URL += "/";
        }
        this.URL += path + "/";
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
