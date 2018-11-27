package com.fidenz.academy.url.formatter;

public interface URLFormatter {
    public void addRequestParam(String key, Object value);
    public void addPath(String path);
    public String getURL();
    public void setURL(String URL);
}
