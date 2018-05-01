package com.wordpress.pos.demo.controller;

import com.wordpress.pos.demo.dto.CommentDTO;
import com.wordpress.pos.demo.jwt.JwtTokenUtil;
import com.wordpress.pos.demo.model.Comments;
import com.wordpress.pos.demo.service.ArticleService;
import com.wordpress.pos.demo.service.CommentService;
import com.wordpress.pos.demo.service.UserService;
import com.wordpress.pos.demo.util.Messages;
import com.wordpress.pos.demo.util.StatusObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
    ResponseEntity<List<Comments>> getRestaurants(@PathVariable String uuid) {
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
    ResponseEntity<StatusObject> createComment(HttpServletRequest request) throws IOException {
        StatusObject statusObject = new StatusObject();

        String token = request.getHeader(tokenHeader).substring(7);

        String collect = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        CommentDTO commentDTO = new CommentDTO(collect);

        try{
            commentService.createComment(commentDTO,userService.getByUsername(jwtTokenUtil.getUsernameFromToken(token)), articleService.getArticleByUUID(commentDTO.getCommentDTO().getArticleUUID()));
            statusObject.setStatus(2);
            statusObject.setMessage(messages.get("text.info.comment.created"));
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

