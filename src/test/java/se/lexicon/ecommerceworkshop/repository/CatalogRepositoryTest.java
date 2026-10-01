package se.lexicon.ecommerceworkshop.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import se.lexicon.ecommerceworkshop.entity.Category;
import se.lexicon.ecommerceworkshop.entity.Product;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CatalogRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldSaveAndSearchProducts() {
        Category category = new Category();
        category.setName("Electronics");
        categoryRepository.save(category);

        Product product = new Product();
        product.setName("Wireless Headphones");
        product.setPrice(new BigDecimal("799.00"));
        product.setCategory(category);
        product.setImageUrls(List.of("headphones-front.jpg", "headphones-side.jpg"));
        productRepository.save(product);

        assertThat(categoryRepository.findByNameIgnoreCase("ELECTRONICS")).isPresent();
        assertThat(categoryRepository.existsByNameIgnoreCase("electronics")).isTrue();
        assertThat(productRepository.findByCategoryNameIgnoreCase("electronics")).hasSize(1);
        assertThat(productRepository.findByPriceBetween(
                new BigDecimal("500.00"), new BigDecimal("1000.00"))).hasSize(1);
        assertThat(productRepository.findByNameContainingIgnoreCase("headphones")).hasSize(1);
    }
}
