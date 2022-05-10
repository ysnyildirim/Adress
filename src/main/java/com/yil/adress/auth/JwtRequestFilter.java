package com.yil.adress.auth;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.header}")
    private String tokenHeader;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        UserDetailsImpl d = (UserDetailsImpl) this.userDetailsServiceImpl.loadUserByUsername("ADMIN");

        final String requestHeader = "Bearer " + jwtTokenUtil.generateToken(d);

        logger.debug("Processing authentication for '{" + request.getRequestURL() + "}'");
        System.out.println(request.getHeader(this.tokenHeader));
//        final String requestHeader = request.getHeader(this.tokenHeader);

        if (requestHeader == null || !requestHeader.startsWith("Bearer ")) {
            logger.warn("Authorization failed. No JWT token found");
            return;
        }

        String username = null;
        String authToken = requestHeader.substring(7);

        try {
            username = jwtTokenUtil.getUserNameFromToken(authToken);
        } catch (IllegalArgumentException e) {
            logger.error("Error during getting username from token", e);
            return;
        } catch (ExpiredJwtException e) {
            logger.warn("The token has expired", e);
            return;
        }

        if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) return;

        logger.debug("Security context was null, so authorizing user '{" + username + "}'...");

        UserDetailsImpl userDetails = (UserDetailsImpl) this.userDetailsServiceImpl.loadUserByUsername(username);

        if (!jwtTokenUtil.validateToken(authToken, userDetails)) {
            logger.error("Not a valid token!!!");
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        logger.info("Authorized user '{" + username + "}', setting security context...");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }
}
