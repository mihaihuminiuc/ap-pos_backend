package com.wordpress.pos.demo.dto;

import com.wordpress.pos.demo.dto.user.UserDTO;

public class AuthorityDTO {
    String userAuthority;
    String userUUID;

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public String getUserAuthority() {
        return userAuthority;
    }

    public void setUserAuthority(String userAuthority) {
        this.userAuthority = userAuthority;
    }

    public AuthorityDTO(){}
}
