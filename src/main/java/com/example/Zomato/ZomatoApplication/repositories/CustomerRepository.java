package com.example.Zomato.ZomatoApplication.repositories;

import com.example.Zomato.ZomatoApplication.entites.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
}
