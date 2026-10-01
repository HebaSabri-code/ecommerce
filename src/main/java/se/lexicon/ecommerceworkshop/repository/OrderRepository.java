package se.lexicon.ecommerceworkshop.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import se.lexicon.ecommerceworkshop.entity.Order;
import se.lexicon.ecommerceworkshop.entity.OrderStatus;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerId(Long customerId);

    @EntityGraph(attributePaths = "items")
    List<Order> findByStatus(OrderStatus status);
}
