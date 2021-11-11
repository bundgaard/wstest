package org.tretton63.wstest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tretton63.wstest.entity.OrderItem;

import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
}
