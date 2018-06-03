package com.wordpress.pos.demo.service.implemtation;

import com.wordpress.pos.demo.model.Authority;
import com.wordpress.pos.demo.repository.AuthorityRepository;
import com.wordpress.pos.demo.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityRepository authorityRepository;

    @Autowired
    public AuthorityServiceImpl(AuthorityRepository authorityRepository){
        this.authorityRepository = authorityRepository;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Authority> listAuthority() {
        return authorityRepository.findAll();
    }
}
