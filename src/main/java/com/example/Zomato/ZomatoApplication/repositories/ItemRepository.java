package com.example.Zomato.ZomatoApplication.repositories;

import com.example.Zomato.ZomatoApplication.entites.ItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<ItemsEntity, Long> {
}
