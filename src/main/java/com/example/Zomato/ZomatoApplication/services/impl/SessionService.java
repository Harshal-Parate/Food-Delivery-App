package com.example.Zomato.ZomatoApplication.services.impl;

import com.example.Zomato.ZomatoApplication.entites.SessionEntity;
import com.example.Zomato.ZomatoApplication.entites.UserEntity;
import com.example.Zomato.ZomatoApplication.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final Integer SESSION_LIMIT = 1;

    public void generateNewSession(UserEntity user, String refreshToken) {
        List<SessionEntity> userSession = sessionRepository.findByUser(user);

        if (userSession.size() == SESSION_LIMIT) {
            userSession.sort(Comparator.comparing(SessionEntity::getLastUsedAt));
            SessionEntity lastUsedSession = userSession.getFirst();
            sessionRepository.delete(lastUsedSession);
        }

        SessionEntity newSession = SessionEntity.builder()
                .setUser(user)
                .setRefreshToken(refreshToken)
                .build();

        sessionRepository.save(newSession);
    }

    public void validateSession(String refreshToken) {
        SessionEntity session = sessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new SessionAuthenticationException("Sesssion not found for refresh token "+refreshToken));
        session.setLastUsedAt(LocalDateTime.now());
        sessionRepository.save(session);
    }
}
