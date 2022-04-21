package ru.iu3.backend.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;


/**
 * Класс, в котором указываются настройки spring boot security
 * @author kostya
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final RequestMatcher PROTECTED_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/api/**")
    );

    // Провайдер для аутентификации
    AuthenticationProvider provider;

    /**
     * Конструктор
     * @param authenticationProvider - провайдер аутентификации - поиск в БД
     */
    public SecurityConfiguration(final AuthenticationProvider authenticationProvider) {
        super();
        this.provider = authenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(this.provider);
    }

    /**
     * Ещё одна дополнительная конфигурация. Указываем адресный путь, откуда будет вызываться авторизация
     * @param web - параметр web
     * @throws Exception - ошибка, требуется обязательно
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/auth/login");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // Устнавливаем определённые политики доступа
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER).and().exceptionHandling().and().authenticationProvider(provider)
                .addFilterBefore(authenticationFilter(), AnonymousAuthenticationFilter.class)
                .authorizeRequests().requestMatchers(PROTECTED_URLS).authenticated().and().csrf().disable()
                .formLogin().disable().httpBasic().disable().logout().disable().cors();
    }

    /**
     * Метод - дополнительная фильтрация. Извлекать можно только из определённого диапазона адресов
     * @return - фильтр
     * @throws Exception - ошибка, которая извлекается
     */
    @Bean
    AuthenticationFilter authenticationFilter() throws Exception {
        final AuthenticationFilter filter = new AuthenticationFilter(PROTECTED_URLS);

        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    /**
     * Метод, возвращающий статус ошибки - 403
     * @return - HttpStatus
     */
    @Bean
    AuthenticationEntryPoint forbiddenEntryPoint() {
        return new HttpStatusEntryPoint(HttpStatus.FORBIDDEN);
    }
}
