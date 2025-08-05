package com.syos.dto;

import java.io.Serializable;

public class MessageDTO implements Serializable {
    private String message;

    public MessageDTO() {}

    public MessageDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
