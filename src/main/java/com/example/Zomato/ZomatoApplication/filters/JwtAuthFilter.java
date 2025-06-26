package com.example.Zomato.ZomatoApplication.filters;

import com.example.Zomato.ZomatoApplication.dtos.UserDto;
import com.example.Zomato.ZomatoApplication.entites.UserEntity;
import com.example.Zomato.ZomatoApplication.services.UserService;
import com.example.Zomato.ZomatoApplication.services.impl.JwtService;
import com.example.Zomato.ZomatoApplication.services.impl.UserServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserServiceImpl userService;
    private final ModelMapper mapper;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private final HandlerExceptionResolver handlerExceptionResolver; // to handle all JWT related exceptions on Global exception handler

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            final String requestTokenHeader = request.getHeader("Authorization");
            if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer")) {
                filterChain.doFilter(request, response);
                return;
            } else {
                String token = requestTokenHeader.split("Bearer ")[1];
                Claims claimsFromToken = jwtService.getClaimsFromToken(token);
                Long userId = jwtService.getUserId(claimsFromToken);

                if (Objects.nonNull(userId) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                    UserDto userDTO = userService.loadUserById(userId);
                    UserEntity userEntity = mapper.map(userDTO, UserEntity.class);

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userEntity, null, userEntity.getAuthorities());

                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                }
                filterChain.doFilter(request, response);
            }
        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }

    }
}
