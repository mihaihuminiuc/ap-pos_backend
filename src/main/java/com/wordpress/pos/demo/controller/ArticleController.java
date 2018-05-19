package com.wordpress.pos.demo.controller;

import com.wordpress.pos.demo.dto.article.ArticleDTO;
import com.wordpress.pos.demo.dto.article.CompactArticleDTO;
import com.wordpress.pos.demo.jwt.JwtTokenUtil;
import com.wordpress.pos.demo.service.ArticleService;
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
public class ArticleController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    Messages messages;

    @CrossOrigin
    @RequestMapping(value = "${route.article.getarticles}/{page}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity<List<CompactArticleDTO>> getArticles(@PathVariable int page) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .cacheControl(CacheControl.noCache())
                .body(articleService.getAllArticles(page));
    }

    @CrossOrigin
    @RequestMapping(value = "${route.article.getArticle}/{uuid}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity<ArticleDTO> gerArticle(@PathVariable String uuid) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .cacheControl(CacheControl.noCache())
                .body(articleService.getArticleDTOByUUID(uuid));
    }

    @CrossOrigin
    @RequestMapping(value = "${route.article.createarticle}", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity<StatusObject> createArticle(HttpServletRequest request) throws IOException {
        StatusObject statusObject = new StatusObject();

        String token = request.getHeader(tokenHeader).substring(7);

        String collect = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        ArticleDTO articleDTO = new ArticleDTO(collect);

        try{
            articleService.createArticle(userService.getByUsername(jwtTokenUtil.getUsernameFromToken(token)), articleDTO);
            statusObject.setStatus(2);
            statusObject.setMessage(messages.get("text.info.article.created"));
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


    @CrossOrigin
    @RequestMapping(value = "${route.article.verifyarticle}/{uuid}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity<StatusObject> verifyArticleCreator(HttpServletRequest request, @PathVariable String uuid) {
        StatusObject statusObject = new StatusObject();

        String token = request.getHeader(tokenHeader).substring(7);

        long userId = userService.getByUsername(jwtTokenUtil.getUsernameFromToken(token)).getId();

        try{
            if(articleService.getArticleByUUID(uuid).getUser().getId().equals(userId)){
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
                        .status(HttpStatus.BAD_REQUEST)
                        .cacheControl(CacheControl.noCache())
                        .body(statusObject);
            }
        }catch (IllegalArgumentException e){
            statusObject.setStatus(1);
            statusObject.setMessage(messages.get("text.info.username.notowner"));

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .cacheControl(CacheControl.noCache())
                    .body(statusObject);
        }
    }

    @CrossOrigin
    @RequestMapping(value = "${route.article.updatearticle}", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity<StatusObject> updateArticle(HttpServletRequest request) throws IOException {
        StatusObject statusObject = new StatusObject();

        String token = request.getHeader(tokenHeader).substring(7);

        long userId = userService.getByUsername(jwtTokenUtil.getUsernameFromToken(token)).getId();

        try{

            String collect = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            ArticleDTO articleDTO = new ArticleDTO(collect);

            if(articleService.getArticleByUUID(articleDTO.getArticleDTO().getArticleUUID()).getUser().getId().equals(userId)){
                try{
                    articleService.updateArticle(articleDTO);
                    statusObject.setStatus(2);
                    statusObject.setMessage(messages.get("text.info.article.updated"));
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
                        .status(HttpStatus.BAD_REQUEST)
                        .cacheControl(CacheControl.noCache())
                        .body(statusObject);
            }
        }catch (IllegalArgumentException e){
            statusObject.setStatus(1);
            statusObject.setMessage(messages.get("text.info.username.notowner"));

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .cacheControl(CacheControl.noCache())
                    .body(statusObject);
        }
    }
}
