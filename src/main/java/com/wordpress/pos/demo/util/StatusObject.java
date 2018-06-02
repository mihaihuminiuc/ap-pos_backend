package com.wordpress.pos.demo.util;

import java.util.List;

public class StatusObject {

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public <T> List<T> getGenericListResponse() {
        return (List<T>) genericListResponse;
    }

    public void setGenericListResponse(List<?> genericListResponse) {
        this.genericListResponse = genericListResponse;
    }

    private int status;
    private String message;
    private List<?> genericListResponse;

    public StatusObject() {}
}
