package com.re.cinemabookingapp.configuration.security;

import com.re.cinemabookingapp.entity.PersistentLogin;
import com.re.cinemabookingapp.repository.PersistentLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JpaPersistentTokenRepository implements PersistentTokenRepository {

    private final PersistentLoginRepository repository;

    @Override
    @Transactional
    public void createNewToken(PersistentRememberMeToken token) {
        PersistentLogin login = new PersistentLogin();
        login.setSeries(token.getSeries());
        login.setUsername(token.getUsername());
        login.setToken(token.getTokenValue());
        login.setLastUsed(token.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        repository.save(login);
    }

    @Override
    @Transactional
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        repository.findById(series).ifPresent(login -> {
            login.setToken(tokenValue);
            login.setLastUsed(lastUsed.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            repository.save(login);
        });
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        return repository.findById(seriesId)
                .map(login -> new PersistentRememberMeToken(
                        login.getUsername(),
                        login.getSeries(),
                        login.getToken(),
                        Date.from(login.getLastUsed().atZone(ZoneId.systemDefault()).toInstant())
                ))
                .orElse(null);
    }

    @Override
    @Transactional
    public void removeUserTokens(String username) {
        repository.deleteByUsername(username);
    }
}
