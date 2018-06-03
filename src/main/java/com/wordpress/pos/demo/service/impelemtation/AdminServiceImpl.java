package com.wordpress.pos.demo.service.impelemtation;

import com.wordpress.pos.demo.dto.user.CompactUserDTO;
import com.wordpress.pos.demo.dto.user.UserDTO;
import com.wordpress.pos.demo.model.Authority;
import com.wordpress.pos.demo.model.User;
import com.wordpress.pos.demo.repository.UserRepository;
import com.wordpress.pos.demo.service.AdminServices;
import com.wordpress.pos.demo.util.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminServices {

    private final UserRepository userRepository;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<CompactUserDTO> listUsers() {
        List<CompactUserDTO> userDTOList = new ArrayList<>();
        for(User u: userRepository.findAll()){
            userDTOList.add(ObjectMapperUtils.map(u, CompactUserDTO.class));
        }

        return userDTOList;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteUser(String uuid) {
        User user = userRepository.findByUserUUID(uuid);
        userRepository.delete(user);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void setAuthority(String uuid, Authority authority) {
        User user = userRepository.findByUserUUID(uuid);
        authority.setUsers(Collections.singletonList(user));
        user.setAuthorities(Collections.singletonList(authority));
        userRepository.save(user);
    }
}
