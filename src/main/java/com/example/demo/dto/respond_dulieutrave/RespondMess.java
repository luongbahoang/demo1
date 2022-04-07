package com.example.demo.dto.respond_dulieutrave;

public class RespondMess {
    private String message;

    public RespondMess(String message) {
        this.message = message;
    }

    public RespondMess() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
