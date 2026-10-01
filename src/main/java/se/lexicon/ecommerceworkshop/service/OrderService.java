package se.lexicon.ecommerceworkshop.service;

import se.lexicon.ecommerceworkshop.dto.OrderRequest;
import se.lexicon.ecommerceworkshop.dto.OrderResponse;

public interface OrderService {

    OrderResponse placeOrder(OrderRequest request);
}
