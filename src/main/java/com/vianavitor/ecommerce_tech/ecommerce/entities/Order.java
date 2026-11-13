package com.vianavitor.ecommerce_tech.ecommerce.entities;

import com.vianavitor.ecommerce_tech.auth.entities.User;

import com.vianavitor.ecommerce_tech.ecommerce.entities.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private OrderStatus status;

    private LocalDate orderedAt;
    private LocalDate statusUpdatedAt;
}
