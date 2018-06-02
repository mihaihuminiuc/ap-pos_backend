package com.wordpress.pos.demo.controller;

import com.wordpress.pos.demo.dto.UserDTO;
import com.wordpress.pos.demo.jwt.JwtAuthenticationRequest;
import com.wordpress.pos.demo.dto.JwtDTO;
import com.wordpress.pos.demo.jwt.JwtTokenUtil;
import com.wordpress.pos.demo.jwt.JwtUser;
import com.wordpress.pos.demo.util.Messages;
import com.wordpress.pos.demo.util.StatusObject;
import com.wordpress.pos.demo.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class AuthenticationRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    Messages messages;

    @Autowired
    private UserDetailsService userDetailsService;

    @RequestMapping(value = "${route.authentication.path}", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult, Device device) throws AuthenticationException {

        StatusObject statusObject = new StatusObject();
        UserValidator userValidator = new UserValidator();

        userValidator.validate(userDTO,bindingResult);
        if(bindingResult.hasErrors()){
            statusObject.setStatus(1);
            statusObject.setMessage(messages.get(bindingResult.getAllErrors().get(0).getCode()));
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .cacheControl(CacheControl.noCache())
                    .body(statusObject);
        }else {
            try {
                final Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                userDTO.getUsername(),
                                userDTO.getPassword()
                        )
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Reload password post-security so we can generate token
                final UserDetails userDetails = userDetailsService.loadUserByUsername(userDTO.getUsername());
                final String token = jwtTokenUtil.generateToken(userDetails, device);

                // Return the token
                return ResponseEntity.ok(new JwtDTO(token));
            }catch (AuthenticationException e){
                statusObject.setStatus(1);
                statusObject.setMessage(e.getMessage());
                return ResponseEntity.badRequest().body(statusObject);
            }
        }
    }

    @RequestMapping(value = "${route.authentication.logout}", method = RequestMethod.GET)
    public ResponseEntity<?> createAuthenticationToken(){
        StatusObject statusObject = new StatusObject();

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(null);

        statusObject.setStatus(2);
        statusObject.setMessage("successfully logged out");
        return ResponseEntity.badRequest().body(statusObject);
    }
}
