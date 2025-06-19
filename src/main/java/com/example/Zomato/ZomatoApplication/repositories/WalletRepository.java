package com.example.Zomato.ZomatoApplication.repositories;

import com.example.Zomato.ZomatoApplication.entites.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<WalletEntity, Long> {
}
