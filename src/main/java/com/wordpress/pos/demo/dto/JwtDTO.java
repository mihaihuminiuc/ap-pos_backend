package com.wordpress.pos.demo.dto;

import java.io.Serializable;

public class JwtDTO implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    private final String token;

    public JwtDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}
