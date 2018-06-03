package com.wordpress.pos.demo.service;


import com.wordpress.pos.demo.dto.user.UserDTO;
import com.wordpress.pos.demo.model.User;

import javax.persistence.PersistenceException;

public interface UserService {
    public User getByUsername(String username);
    public User getByUserUUID(String uuid);
    public User saveUsername(UserDTO userDTO);
    public void updateUser(UserDTO userDTO) throws PersistenceException;
    public boolean isUserAdmin(User User);
}
