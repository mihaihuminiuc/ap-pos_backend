package com.wordpress.pos.demo.controller;

import com.wordpress.pos.demo.jwt.JwtAuthenticationRequest;
import com.wordpress.pos.demo.dto.JwtDTO;
import com.wordpress.pos.demo.jwt.JwtTokenUtil;
import com.wordpress.pos.demo.jwt.JwtUser;
import com.wordpress.pos.demo.util.Messages;
import com.wordpress.pos.demo.util.StatusObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest, Device device) throws AuthenticationException {
        StatusObject statusObject = new StatusObject();
        // Perform the security
        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Reload password post-security so we can generate token
            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
            final String token = jwtTokenUtil.generateToken(userDetails, device);

            // Return the token
            return ResponseEntity.ok(new JwtDTO(token));
        }catch (AuthenticationException e){
            statusObject.setStatus(1);
            statusObject.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(statusObject);
        }
    }

    @RequestMapping(value = "${route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String authToken = request.getHeader(tokenHeader);
        final String token = authToken.substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtDTO(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
