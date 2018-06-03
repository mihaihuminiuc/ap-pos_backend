package com.wordpress.pos.demo.controller;

import com.wordpress.pos.demo.dto.UserDTO;
import com.wordpress.pos.demo.dto.article.ArticleDTO;
import com.wordpress.pos.demo.dto.article.CompactArticleDTO;
import com.wordpress.pos.demo.jwt.JwtTokenUtil;
import com.wordpress.pos.demo.model.Authority;
import com.wordpress.pos.demo.model.User;
import com.wordpress.pos.demo.service.ArticleService;
import com.wordpress.pos.demo.service.UserService;
import com.wordpress.pos.demo.util.AuthorityName;
import com.wordpress.pos.demo.util.Messages;
import com.wordpress.pos.demo.util.StatusObject;
import com.wordpress.pos.demo.validator.ArticleValidation;
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
import java.util.Collections;
import java.util.List;

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
    ResponseEntity<StatusObject> getArticles(@PathVariable int page) {
        StatusObject statusObject = new StatusObject();
        List<CompactArticleDTO> compactArticleDTO = articleService.getAllArticles(page);

        if(compactArticleDTO != null && compactArticleDTO.size()>0){
            statusObject.setStatus(2);
            statusObject.setMessage(messages.get("text.info.generalinfo"));
            statusObject.setGenericListResponse(compactArticleDTO);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .cacheControl(CacheControl.noCache())
                    .body(statusObject);
        } else {
            statusObject.setStatus(2);
            statusObject.setMessage(messages.get("text.info.articles.notfound"));
            statusObject.setGenericListResponse(null);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .cacheControl(CacheControl.noCache())
                    .body(statusObject);
        }
    }

    @CrossOrigin
    @RequestMapping(value = "${route.article.getArticle}/{uuid}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity<StatusObject> gerArticle(@PathVariable String uuid) {


        StatusObject statusObject = new StatusObject();
        ArticleDTO articleDTO = articleService.getArticleDTOByUUID(uuid);

        if(articleDTO != null){
            statusObject.setStatus(2);
            statusObject.setMessage(messages.get("text.info.generalinfo"));
            statusObject.setGenericListResponse(Collections.singletonList(articleDTO));
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .cacheControl(CacheControl.noCache())
                    .body(statusObject);
        } else {
            statusObject.setStatus(2);
            statusObject.setMessage(messages.get("text.info.article.notfound"));
            statusObject.setGenericListResponse(null);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .cacheControl(CacheControl.noCache())
                    .body(statusObject);
        }
    }

    @CrossOrigin
    @RequestMapping(value = "${route.article.createarticle}", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity<StatusObject> createArticle(HttpServletRequest request,
                                               @Valid @RequestBody ArticleDTO articleDTO, BindingResult bindingResult) {
        StatusObject statusObject = new StatusObject();
        ArticleValidation articleValidation = new ArticleValidation();
        String token = request.getHeader(tokenHeader).substring(7);

        articleValidation.validate(articleDTO,bindingResult);

        if(bindingResult.hasErrors()){
            statusObject.setStatus(1);
            statusObject.setMessage(messages.get(bindingResult.getAllErrors().get(0).getCode()));
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .cacheControl(CacheControl.noCache())
                    .body(statusObject);
        } else {
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
    @RequestMapping(value = "${route.article.updatearticle}", method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity<StatusObject> updateArticle(HttpServletRequest request,
                                               @Valid @RequestBody ArticleDTO articleDTO, BindingResult bindingResult) {
        StatusObject statusObject = new StatusObject();
        ArticleValidation articleValidation = new ArticleValidation();

        articleValidation.validate(articleDTO,bindingResult);

        if(bindingResult.hasErrors()){
            statusObject.setStatus(1);
            statusObject.setMessage(messages.get(bindingResult.getAllErrors().get(0).getCode()));
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .cacheControl(CacheControl.noCache())
                    .body(statusObject);
        } else {
            try{
                long userId = userService.getByUsername(jwtTokenUtil.getUsernameFromToken(request.getHeader(tokenHeader).substring(7))).getId();
                if(articleService.getArticleByUUID(articleDTO.getArticleUUID()).getUser().getId().equals(userId)){
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

    @CrossOrigin
    @RequestMapping(value = "${route.article.deletearticle}/{uuid}", method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    ResponseEntity<StatusObject> deleteArticle(HttpServletRequest request, @PathVariable String uuid) {
        StatusObject statusObject = new StatusObject();

        String token = request.getHeader(tokenHeader).substring(7);
        User user = userService.getByUsername(jwtTokenUtil.getUsernameFromToken(token));

        if(userService.isUserAdmin(user)){

            if(articleService.getArticleByUUID(uuid)!=null){
                try{
                    articleService.deleteArticle(uuid);
                    statusObject.setStatus(2);
                    statusObject.setMessage(messages.get("text.info.article.delete"));
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
                statusObject.setMessage(messages.get("text.info.article.notfound"));
                return ResponseEntity
                        .status(HttpStatus.OK)
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
