package com.yil.adress.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Lazy
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.isEmpty())
            throw new UsernameNotFoundException("Username is empty");

        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setUserId(0L);
        userDetails.setUserName(username);
        userDetails.setPassword(passwordEncoder.encode("1"));
        userDetails.setEnabled(true);
        userDetails.setAccountExpired(false);
        userDetails.setAccountLocked(false);
        userDetails.setCredentialsExpired(false);
        userDetails.setRoles(new String[]{"ADMIN", "USER", "EMPLOYEE"});
        if (Objects.isNull(userDetails))
            throw new UsernameNotFoundException("User not found with username: " + username);
        return userDetails;
    }
}
