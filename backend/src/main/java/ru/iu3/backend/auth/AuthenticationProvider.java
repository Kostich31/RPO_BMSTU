package ru.iu3.backend.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import ru.iu3.backend.repositories.UserRepository;
//import ru.iu3.backend.models.User;
import java.time.LocalDateTime;
import java.util.Optional;


@Component
public class AuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    // Поле, взятое из конфига - тайм-аут пользователя
    @Value("${private.session-timeout}")
    private int sessionTimeOut;

    // Репозиторий пользователя, который обеспечивает доступ к таблице пользователей в БД
    @Autowired
    UserRepository userRepository;

    // Всё нормально: здесь должен быть пустой метод
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
    }


    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {


        Object token = authentication.getCredentials();

        Optional<ru.iu3.backend.models.User> uu = userRepository.findByToken(String.valueOf(token));
        if (!uu.isPresent()) {
            throw new UsernameNotFoundException("User is not found");
        }

        ru.iu3.backend.models.User u = uu.get();

        boolean timeout = true;
        LocalDateTime dt = LocalDateTime.now();

        if (u.activity != null) {
            LocalDateTime nt = u.activity.plusSeconds(sessionTimeOut);

            if (dt.isBefore(nt)) {
                timeout = false;
            }
        }
        if (timeout) {
            u.token = null;
            userRepository.save(u);
        } else {
            u.activity = dt;
            userRepository.save(u);
        }

        UserDetails user = new User(u.login, u.password, true, true, true, true,
                AuthorityUtils.createAuthorityList("USER"));

        return user;
    }
}