package com.dto;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Response_DTO implements Serializable {
    @Expose
    boolean success;
    @Expose
    Object message;

    public Response_DTO(boolean status, Object message) {
        this.success = status;
        this.message = message;
    }

    public Response_DTO() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
