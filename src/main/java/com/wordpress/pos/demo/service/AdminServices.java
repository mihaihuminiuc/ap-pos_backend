package com.wordpress.pos.demo.service;

import com.wordpress.pos.demo.dto.user.CompactUserDTO;
import com.wordpress.pos.demo.dto.user.UserDTO;
import com.wordpress.pos.demo.model.Authority;

import java.util.List;

public interface AdminServices {
    public List<CompactUserDTO> listUsers();
    public void deleteUser(String uuid);
    public void setAuthority(String uuid, Authority authority);
}
