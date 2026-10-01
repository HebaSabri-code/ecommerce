package se.lexicon.ecommerceworkshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.lexicon.ecommerceworkshop.entity.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryNameIgnoreCase(String categoryName);

    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<Product> findByNameContainingIgnoreCase(String name);
}
