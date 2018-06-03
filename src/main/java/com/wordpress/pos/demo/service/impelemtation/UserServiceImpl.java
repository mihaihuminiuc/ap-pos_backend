package com.wordpress.pos.demo.service.impelemtation;

import com.wordpress.pos.demo.dto.user.UserDTO;
import com.wordpress.pos.demo.model.Authority;
import com.wordpress.pos.demo.model.User;
import com.wordpress.pos.demo.repository.UserRepository;
import com.wordpress.pos.demo.service.UserService;
import com.wordpress.pos.demo.util.AuthorityName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Override
    public User getByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public User getByUserUUID(String uuid) {
        return this.userRepository.findByUserUUID(uuid);
    }

    @Override
    public User saveUsername(UserDTO userDTO) throws PersistenceException{

        bCryptPasswordEncoder = new BCryptPasswordEncoder();

        User user = new User();

        user.setUsername(userDTO.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        user.setUserUUID(UUID.randomUUID().toString());
        user.setEnabled(true);

        this.userRepository.save(user);

        return user;
    }

    @Override
    public void updateUser(UserDTO userDTO) throws PersistenceException {

        userDTO = userDTO.getUserDTO();

        long userId;

        userId = this.userRepository.findByUsername(userDTO.getUsername()).getId();

        this.userRepository.saveUserData(userId,userDTO.getFirstName(),userDTO.getLastName(),userDTO.getEmail());
    }

    @Override
    public boolean isUserAdmin(User user) {
        boolean ret = false;

        for(Authority a : user.getAuthorities()){
            ret |= a.getName().equals(AuthorityName.ROLE_ADMIN);
        }

        return ret;
    }

}
