package se.lexicon.ecommerceworkshop.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.ecommerceworkshop.dto.OrderItemRequest;
import se.lexicon.ecommerceworkshop.dto.OrderRequest;
import se.lexicon.ecommerceworkshop.dto.OrderResponse;
import se.lexicon.ecommerceworkshop.entity.Customer;
import se.lexicon.ecommerceworkshop.entity.Order;
import se.lexicon.ecommerceworkshop.entity.Product;
import se.lexicon.ecommerceworkshop.exception.ResourceNotFoundException;
import se.lexicon.ecommerceworkshop.mapper.OrderMapper;
import se.lexicon.ecommerceworkshop.repository.CustomerRepository;
import se.lexicon.ecommerceworkshop.repository.OrderRepository;
import se.lexicon.ecommerceworkshop.repository.ProductRepository;
import se.lexicon.ecommerceworkshop.service.OrderService;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository,
                            CustomerRepository customerRepository,
                            ProductRepository productRepository,
                            OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional
    public OrderResponse placeOrder(OrderRequest request) {
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with id: " + request.customerId()));

        Map<Long, Product> products = new LinkedHashMap<>();
        for (OrderItemRequest item : request.items()) {
            Product product = productRepository.findById(item.productId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product not found with id: " + item.productId()));
            products.put(item.productId(), product);
        }

        Order order = orderMapper.toEntity(request, customer, products);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponse(savedOrder);
    }
}
