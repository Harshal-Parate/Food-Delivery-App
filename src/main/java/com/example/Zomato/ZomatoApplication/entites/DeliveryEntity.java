package com.example.Zomato.ZomatoApplication.entites;

import com.example.Zomato.ZomatoApplication.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    private LocalDateTime deliveryTime;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private DriverEntity driver;

    @OneToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;
}

