package se.lexicon.ecommerceworkshop.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.lexicon.ecommerceworkshop.repository.CategoryRepository;
import se.lexicon.ecommerceworkshop.repository.ProductRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DataSeederTest {

    @Autowired
    private DataSeeder dataSeeder;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldNotInsertDuplicateData() throws Exception {
        assertThat(categoryRepository.count()).isEqualTo(3);
        assertThat(productRepository.count()).isEqualTo(3);

        dataSeeder.run();

        assertThat(categoryRepository.count()).isEqualTo(3);
        assertThat(productRepository.count()).isEqualTo(3);
    }
}
