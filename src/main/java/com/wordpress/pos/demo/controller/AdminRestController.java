package com.wordpress.pos.demo.controller;

import com.wordpress.pos.demo.dto.AuthorityDTO;
import com.wordpress.pos.demo.dto.article.ArticleDTO;
import com.wordpress.pos.demo.jwt.JwtTokenUtil;
import com.wordpress.pos.demo.model.Authority;
import com.wordpress.pos.demo.model.User;
import com.wordpress.pos.demo.service.AdminServices;
import com.wordpress.pos.demo.service.AuthorityService;
import com.wordpress.pos.demo.service.UserService;
import com.wordpress.pos.demo.util.AuthorityName;
import com.wordpress.pos.demo.util.Messages;
import com.wordpress.pos.demo.util.StatusObject;
import com.wordpress.pos.demo.validator.AuthorityValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class AdminRestController {
    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private AdminServices adminServices;

    @Autowired
    Messages messages;

    @CrossOrigin
    @RequestMapping(value = "${route.admin.getauthority}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity<StatusObject> getAuthority(HttpServletRequest request){
        StatusObject statusObject = new StatusObject();
        String token = request.getHeader(tokenHeader).substring(7);
        User user = userService.getByUsername(jwtTokenUtil.getUsernameFromToken(token));

        if(userService.isUserAdmin(user)){
            statusObject.setStatus(2);
            statusObject.setMessage(messages.get("text.info.generalinfo"));
            statusObject.setGenericListResponse(authorityService.listAuthority());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .cacheControl(CacheControl.noCache())
                    .body(statusObject);
        } else {
            statusObject.setStatus(1);
            statusObject.setMessage(messages.get("text.error.notadmin"));
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .cacheControl(CacheControl.noCache())
                    .body(statusObject);
        }
    }

    @CrossOrigin
    @RequestMapping(value = "${route.admin.listusers}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity<StatusObject> listUser(HttpServletRequest request) {
        StatusObject statusObject = new StatusObject();

        String token = request.getHeader(tokenHeader).substring(7);
        User user = userService.getByUsername(jwtTokenUtil.getUsernameFromToken(token));

        if(userService.isUserAdmin(user)){
            statusObject.setStatus(2);
            statusObject.setMessage(messages.get("text.info.generalinfo"));
            statusObject.setGenericListResponse(adminServices.listUsers());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .cacheControl(CacheControl.noCache())
                    .body(statusObject);
        }else {
            statusObject.setStatus(1);
            statusObject.setMessage(messages.get("text.error.notadmin"));
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .cacheControl(CacheControl.noCache())
                    .body(statusObject);
        }
    }

    //WIP
    @CrossOrigin
    @RequestMapping(value = "${route.admin.setauthority}", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity<StatusObject> setUserAuthority(HttpServletRequest request,
                                                  @Valid @RequestBody AuthorityDTO authorityDTO, BindingResult bindingResult) {
        StatusObject statusObject = new StatusObject();

        AuthorityValidation authorityValidation = new AuthorityValidation();
        authorityValidation.validate(authorityDTO,bindingResult);

        String token = request.getHeader(tokenHeader).substring(7);
        User user = userService.getByUsername(jwtTokenUtil.getUsernameFromToken(token));

        if(bindingResult.hasErrors()){
            statusObject.setStatus(1);
            statusObject.setMessage(messages.get(bindingResult.getAllErrors().get(0).getCode()));
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .cacheControl(CacheControl.noCache())
                    .body(statusObject);
        } else {
            if(userService.isUserAdmin(user)){
                try{
                    Authority authority = new Authority();
                    authority.setName(AuthorityName.valueOf(authorityDTO.getUserAuthority()));

                    adminServices.setAuthority(authorityDTO.getUserUUID(),authority);

                    statusObject.setStatus(1);
                    statusObject.setMessage(messages.get("text.info.username.authorityupdate"));
                    statusObject.setGenericListResponse(adminServices.listUsers());
                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .cacheControl(CacheControl.noCache())
                            .body(statusObject);
                } catch (Exception e){
                        statusObject.setStatus(1);
                        statusObject.setMessage(messages.get("text.error.generalerror"));
                        return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .cacheControl(CacheControl.noCache())
                                .body(statusObject);
                }
            }else {
                statusObject.setStatus(1);
                statusObject.setMessage(messages.get("text.error.notadmin"));
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .cacheControl(CacheControl.noCache())
                        .body(statusObject);
            }
        }
    }

    @CrossOrigin
    @RequestMapping(value = "${route.admin.deleteuser}/{uuid}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity<StatusObject> deleteUser(HttpServletRequest request,@PathVariable String uuid) {
        StatusObject statusObject = new StatusObject();


        String token = request.getHeader(tokenHeader).substring(7);
        User user = userService.getByUsername(jwtTokenUtil.getUsernameFromToken(token));

        if(userService.isUserAdmin(user)){
            if(userService.getByUserUUID(uuid)==null) {
                statusObject.setStatus(1);
                statusObject.setMessage(messages.get("text.error.username.notfound"));
                statusObject.setGenericListResponse(null);
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .cacheControl(CacheControl.noCache())
                        .body(statusObject);
            } else {
                try{
                    adminServices.deleteUser(uuid);

                    statusObject.setStatus(2);
                    statusObject.setMessage(messages.get("text.info.username.deleted"));
                    statusObject.setGenericListResponse(null);
                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .cacheControl(CacheControl.noCache())
                            .body(statusObject);
                } catch (Exception e){
                    statusObject.setStatus(1);
                    statusObject.setMessage(messages.get("text.error.generalerror"));
                    return ResponseEntity
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .cacheControl(CacheControl.noCache())
                            .body(statusObject);
                }
            }
        }else {
            statusObject.setStatus(1);
            statusObject.setMessage(messages.get("text.error.notadmin"));
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .cacheControl(CacheControl.noCache())
                    .body(statusObject);
        }
    }
}
