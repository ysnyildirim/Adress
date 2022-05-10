package com.yil.adress.auth;

import com.yil.adress.base.ApiResponce;
import com.yil.adress.base.ApiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@CrossOrigin
@RequestMapping("/v1/auth")
public class JwtAuthenticationController {

    private Logger logger = Logger.getLogger(JwtAuthenticationController.class.getName());
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Value("${jwt.header}")
    private String tokenHeader;

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public ResponseEntity<ApiResponce<JwtResponse>> auth(@RequestBody JwtRequest authenticationRequest) {
        try {
            Objects.requireNonNull(authenticationRequest);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            final String token = jwtTokenUtil.generateToken(userDetails);
            return ResponseEntity.ok(ApiResponce.ok(new JwtResponse(token)));
        } catch (DisabledException disabledException) {
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.DisabledUser));
        } catch (BadCredentialsException badCredentialsException) {
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.BadCredentials));
        } catch (AuthenticationException authenticationException) {

            logger.log(Level.SEVERE, null, authenticationException);
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        } catch (Exception exception) {
            logger.log(Level.SEVERE, null, exception.toString());
            return ResponseEntity.ok(ApiResponce.status(ApiStatus.Error));
        }
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refresh(HttpServletRequest request) {
        String authToken = request.getHeader(tokenHeader);
        final String token = authToken.substring(7);
        String username = jwtTokenUtil.getUserNameFromToken(token);
        UserDetailsImpl user = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
        String refreshedToken = jwtTokenUtil.refreshToken(token);
        return ResponseEntity.ok(ApiResponce.ok(new JwtResponse(refreshedToken)));
    }

}
