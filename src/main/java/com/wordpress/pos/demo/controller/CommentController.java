package com.wordpress.pos.demo.controller;

import com.wordpress.pos.demo.dto.CommentDTO;
import com.wordpress.pos.demo.jwt.JwtTokenUtil;
import com.wordpress.pos.demo.model.Comments;
import com.wordpress.pos.demo.service.ArticleService;
import com.wordpress.pos.demo.service.CommentService;
import com.wordpress.pos.demo.service.UserService;
import com.wordpress.pos.demo.util.Messages;
import com.wordpress.pos.demo.util.StatusObject;
import com.wordpress.pos.demo.validator.Commentvalidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
public class CommentController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    Messages messages;

    @RequestMapping(value = "${route.comment.getcomments}/{uuid}"  , method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity<List<Comments>> getComments(@PathVariable String uuid) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .cacheControl(CacheControl.noCache())
                .body(commentService.getAllCommentsForArticleId(uuid));
    }

    @RequestMapping(value = "${route.comment.createcomment}", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity<StatusObject> createComment(HttpServletRequest request,
                                               @Valid @RequestBody CommentDTO commentDTO, BindingResult bindingResult) {
        StatusObject statusObject = new StatusObject();
        String token = request.getHeader(tokenHeader).substring(7);
        Commentvalidation commentvalidation = new Commentvalidation();

        commentvalidation.validate(commentDTO,bindingResult);

        if(bindingResult.hasErrors()){
            statusObject.setStatus(1);
            statusObject.setMessage(messages.get(bindingResult.getAllErrors().get(0).getCode()));
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .cacheControl(CacheControl.noCache())
                    .body(statusObject);
        } else {
            try{
                commentService.createComment(commentDTO,userService.getByUsername(jwtTokenUtil.getUsernameFromToken(token)), articleService.getArticleByUUID(commentDTO.getArticleUUID()));
                statusObject.setStatus(2);
                statusObject.setMessage(messages.get("text.info.comment.created"));
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .cacheControl(CacheControl.noCache())
                        .body(statusObject);
            }catch (IllegalArgumentException | PersistenceException e){
                statusObject.setStatus(1);
                statusObject.setMessage(messages.get("text.error.generalerror"));
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .cacheControl(CacheControl.noCache())
                        .body(statusObject);
            }
        }
    }

    @RequestMapping(value = "${route.comment.updatecomment}", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity<StatusObject> updateComment(HttpServletRequest request,
                                               @Valid @RequestBody CommentDTO commentDTO, BindingResult bindingResult){
        StatusObject statusObject = new StatusObject();
        String token = request.getHeader(tokenHeader).substring(7);
        long userId = userService.getByUsername(jwtTokenUtil.getUsernameFromToken(token)).getId();

        Commentvalidation commentvalidation = new Commentvalidation();

        commentvalidation.validate(commentDTO,bindingResult);

        if(bindingResult.hasErrors()){
            statusObject.setStatus(1);
            statusObject.setMessage(messages.get(bindingResult.getAllErrors().get(0).getCode()));
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .cacheControl(CacheControl.noCache())
                    .body(statusObject);
        } else {
            try{
                if(commentService.getCommentByUUID(commentDTO.getCommentDTO().getCommentUUID())
                        .getUser().getId().equals(userId)){
                    try{
                        commentService.updateComment(commentDTO);

                        statusObject.setStatus(2);
                        statusObject.setMessage(messages.get("text.info.comment.updated"));
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
                }else{
                    statusObject.setStatus(1);
                    statusObject.setMessage(messages.get("text.info.username.notowner"));
                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .cacheControl(CacheControl.noCache())
                            .body(statusObject);
                }
            }catch (IllegalArgumentException e){
                statusObject.setStatus(1);
                statusObject.setMessage(messages.get("text.info.username.notowner"));
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .cacheControl(CacheControl.noCache())
                        .body(statusObject);
            }
        }
    }

    @RequestMapping(value = "${route.comment.verifycomment}/{uuid}", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity<StatusObject> verifyComment(HttpServletRequest request, @PathVariable String uuid) {
        StatusObject statusObject = new StatusObject();

        String token = request.getHeader(tokenHeader).substring(7);

        long userId = userService.getByUsername(jwtTokenUtil.getUsernameFromToken(token)).getId();

        try{
            if(commentService.getCommentByUUID(uuid).getUser().getId().equals(userId)){
                statusObject.setStatus(2);
                statusObject.setMessage(messages.get("text.info.username.isowner"));
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .cacheControl(CacheControl.noCache())
                        .body(statusObject);
            }else{
                statusObject.setStatus(1);
                statusObject.setMessage(messages.get("text.info.username.notowner"));
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .cacheControl(CacheControl.noCache())
                        .body(statusObject);
            }
        }catch (IllegalArgumentException e){
            statusObject.setStatus(1);
            statusObject.setMessage(messages.get("text.info.username.notowner"));
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .cacheControl(CacheControl.noCache())
                    .body(statusObject);
        }
    }

    @CrossOrigin
    @RequestMapping(value = "${route.comment.deletecomment}/{uuid}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity<StatusObject> deleteComment(HttpServletRequest request, @PathVariable String uuid) {
        StatusObject statusObject = new StatusObject();

        if(request.isUserInRole("ROLE_ADMIN")){

            try{
                commentService.deleteComment(uuid);
                statusObject.setStatus(2);
                statusObject.setMessage(messages.get("text.info.comment.delete"));
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
        } else {
            statusObject.setStatus(1);
            statusObject.setMessage(messages.get("text.error.notadmin"));
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .cacheControl(CacheControl.noCache())
                    .body(statusObject);
        }
    }
}

