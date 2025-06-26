package com.example.Zomato.ZomatoApplication.repositories;

import com.example.Zomato.ZomatoApplication.entites.SessionEntity;
import com.example.Zomato.ZomatoApplication.entites.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<SessionEntity, Long> {
    List<SessionEntity> findByUser(UserEntity user);

    Optional<SessionEntity> findByRefreshToken(String refreshToken);

}
