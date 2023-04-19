package com.project.uda.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider provider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = provider.resolveRequest(request);
            if (StringUtils.hasText(token) && provider.validationToken(token)) {
                Authentication authentication = provider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }else if(token == null && request.getRequestURI().contains("/member/login")){
                log.info("Token Generate Process begin");
            }
        } catch (Exception ex) {
            log.error("Authentication Error IP : {} Reason : {}", request.getRemoteAddr(), ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
