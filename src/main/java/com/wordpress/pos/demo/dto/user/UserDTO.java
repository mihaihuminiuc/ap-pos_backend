package com.wordpress.pos.demo.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;

public class UserDTO {
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserDTO(){}

    public UserDTO(String response){
        Gson gson = new Gson();
        this.userDTO = gson.fromJson(response,UserDTO.class);
    }

    @JsonIgnore
    private UserDTO userDTO;

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String email;
}
