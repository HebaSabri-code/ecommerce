package se.lexicon.ecommerceworkshop.dto;

import se.lexicon.ecommerceworkshop.entity.OrderStatus;

import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        Instant orderDate,
        OrderStatus status,
        Long customerId,
        List<OrderItemResponse> items
) {
}
