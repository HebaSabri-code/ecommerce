package se.lexicon.ecommerceworkshop.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import se.lexicon.ecommerceworkshop.entity.Category;
import se.lexicon.ecommerceworkshop.entity.Product;
import se.lexicon.ecommerceworkshop.entity.Promotion;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PromotionRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Test
    void shouldFindActivePromotionForProduct() {
        Category category = new Category();
        category.setName("Books");
        categoryRepository.save(category);

        Promotion promotion = new Promotion();
        promotion.setCode("AUTUMN20");
        promotion.setStartDate(LocalDate.of(2026, 9, 1));
        promotion.setEndDate(LocalDate.of(2026, 10, 31));
        promotionRepository.save(promotion);

        Product product = new Product();
        product.setName("Java Basics");
        product.setPrice(new BigDecimal("299.00"));
        product.setCategory(category);
        product.getPromotions().add(promotion);
        promotion.getProducts().add(product);
        productRepository.saveAndFlush(product);

        assertThat(promotionRepository.findActiveOn(LocalDate.of(2026, 10, 1)))
                .containsExactly(promotion);
        assertThat(promotionRepository.findActiveOn(LocalDate.of(2026, 12, 1)))
                .isEmpty();
    }
}
