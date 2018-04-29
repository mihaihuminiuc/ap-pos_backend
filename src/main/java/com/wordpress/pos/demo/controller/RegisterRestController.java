package com.wordpress.pos.demo.controller;

import com.wordpress.pos.demo.dto.UserDTO;
import com.wordpress.pos.demo.service.impelemtation.UserServiceImpl;
import com.wordpress.pos.demo.util.Messages;
import com.wordpress.pos.demo.util.StatusObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

@RestController
public class RegisterRestController {

    @Autowired
    Messages messages;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @CrossOrigin
    @RequestMapping(value="${route.register}", method= RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<StatusObject> registerUser(HttpServletRequest request) throws IOException{

        String collect = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        System.out.println("REQUEST: " +collect);

        UserDTO userDTO = new UserDTO(collect);

        StatusObject statusObject = new StatusObject();

        if(userServiceImpl.getByUsername(userDTO.getUserDTO().getUsername())!=null){
            statusObject.setStatus(1);
            statusObject.setMessage(messages.get("text.error.username.alreadyTaken"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .cacheControl(CacheControl.noCache())
                    .body(statusObject);
        }
        else{
            try{
                userServiceImpl.saveUsername(userDTO);
                statusObject.setStatus(2);
                statusObject.setMessage(messages.get("text.info.username.created"));
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .cacheControl(CacheControl.noCache())
                        .body(statusObject);
            }catch (PersistenceException e){
                statusObject.setStatus(1);
                statusObject.setMessage(messages.get("text.error.generalerror"));
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .cacheControl(CacheControl.noCache())
                        .body(statusObject);
            }
        }
    }

}
