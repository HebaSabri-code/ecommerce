package se.lexicon.ecommerceworkshop.service;

import se.lexicon.ecommerceworkshop.dto.ProductRequest;
import se.lexicon.ecommerceworkshop.dto.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse create(ProductRequest request);

    List<ProductResponse> findAll();

    List<ProductResponse> searchByName(String name);
}
