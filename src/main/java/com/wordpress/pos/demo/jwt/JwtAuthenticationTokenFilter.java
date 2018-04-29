package com.wordpress.pos.demo.jwt;

import com.wordpress.pos.demo.util.Messages;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    Messages messages;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String requestHeader = request.getHeader(this.tokenHeader);

        String username = null;
        String authToken = null;
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            authToken = requestHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(authToken);

                logger.info("checking authentication for user " + username);
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    // It is not compelling necessary to load the use details from the database. You could also store the information
                    // in the token and read it from it. It's up to you ;)
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                    // For simple validation it is completely sufficient to just check the token integrity. You don't have to call
                    // the database compellingly. Again it's up to you ;)
                    if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        logger.info("authenticated user " + username + ", setting security context");
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (IllegalArgumentException e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,messages.get("text.error.token.username"));
                logger.error("an error occured during getting username from token ", e);
            } catch (ExpiredJwtException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,messages.get("text.error.token.expired"));
                logger.warn("the token is expired and not valid anymore ", e);
            }catch (SignatureException e){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,messages.get("text.error.token.signature"));
                logger.warn("the token signature is not valid ", e);
            }catch(DisabledException e){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,messages.get("text.error.token.disabled"));
                logger.warn("the token signature is not valid ", e);
            }
        } else {
            logger.warn("couldn't find bearer string, will ignore the header");
        }

        chain.doFilter(request, response);
    }
}