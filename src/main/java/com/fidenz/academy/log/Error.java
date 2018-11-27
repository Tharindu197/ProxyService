package com.fidenz.academy.log;

public class Error {
    private int status;
    private String message;
    private long timestamp;

    public Error(int status, String message, long timestamp) {
        this.status = status;
        this.setMessage(message);
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
