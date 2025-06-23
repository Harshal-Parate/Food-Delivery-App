package com.example.Zomato.ZomatoApplication.repositories;

import com.example.Zomato.ZomatoApplication.entites.DriverEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<DriverEntity, Long> {
}
