package com.yil.adress.config;

import com.yil.adress.auth.UserDetailsImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
public class AuditorAwareConfig implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext == null)
            return Optional.empty();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication == null)
            return Optional.empty();
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        if (user == null)
            return Optional.empty();
        return Optional.ofNullable(user.getUserId());
    }
}
