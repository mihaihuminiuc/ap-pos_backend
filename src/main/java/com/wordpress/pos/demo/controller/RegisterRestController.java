package com.wordpress.pos.demo.controller;

import com.wordpress.pos.demo.dto.UserDTO;
import com.wordpress.pos.demo.service.impelemtation.UserServiceImpl;
import com.wordpress.pos.demo.util.Messages;
import com.wordpress.pos.demo.util.StatusObject;
import com.wordpress.pos.demo.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.stream.Collectors;

@RestController
public class RegisterRestController {

    @Autowired
    Messages messages;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @RequestMapping(value="${route.register}", method= RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ResponseEntity<StatusObject> registerUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult){

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
            if(userServiceImpl.getByUsername(userDTO.getUsername())!=null){
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

}
