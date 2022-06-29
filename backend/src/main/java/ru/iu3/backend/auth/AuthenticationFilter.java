package ru.iu3.backend.auth;

import org.apache.commons.lang3.StringUtils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class AuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    // Конструктор
    AuthenticationFilter(final RequestMatcher requiresAuth) {
        super(requiresAuth);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        // Извлекаем заголовки
        Enumeration headerNames = request.getHeaderNames();

        // Извлекаем токены из заголовка
        String token = request.getHeader(AUTHORIZATION);
        if (token != null) {
            // Если токен не пуст, то фильтруем всякий мусор и убираем пробел после слова
            token = StringUtils.removeStart(token,"Bearer").trim();
        }

        // Моё предположение, что здесь нужно именно это, потому что идёт запрос с кренделями
        Authentication requestAuthentication = new UsernamePasswordAuthenticationToken(request, token);
        return getAuthenticationManager().authenticate(requestAuthentication);
    }


    @Override
    protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }
}