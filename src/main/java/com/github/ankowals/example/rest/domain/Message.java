package com.github.ankowals.example.rest.domain;

public class Message {

    private int code;
    private String message;

    public Message(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Message() {}

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
