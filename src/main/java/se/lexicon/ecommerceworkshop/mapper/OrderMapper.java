package se.lexicon.ecommerceworkshop.mapper;

import org.springframework.stereotype.Component;
import se.lexicon.ecommerceworkshop.dto.OrderItemRequest;
import se.lexicon.ecommerceworkshop.dto.OrderItemResponse;
import se.lexicon.ecommerceworkshop.dto.OrderRequest;
import se.lexicon.ecommerceworkshop.dto.OrderResponse;
import se.lexicon.ecommerceworkshop.entity.Customer;
import se.lexicon.ecommerceworkshop.entity.Order;
import se.lexicon.ecommerceworkshop.entity.OrderItem;
import se.lexicon.ecommerceworkshop.entity.Product;

import java.util.List;
import java.util.Map;

@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getPriceAtPurchase()
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getOrderDate(),
                order.getStatus(),
                order.getCustomer().getId(),
                itemResponses
        );
    }

    public Order toEntity(OrderRequest request, Customer customer, Map<Long, Product> products) {
        Order order = new Order();
        order.setCustomer(customer);

        for (OrderItemRequest itemRequest : request.items()) {
            Product product = products.get(itemRequest.productId());
            if (product == null) {
                throw new IllegalArgumentException("Product is missing from mapper data");
            }

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(itemRequest.quantity());
            item.setPriceAtPurchase(product.getPrice());
            order.addItem(item);
        }
        return order;
    }
}
