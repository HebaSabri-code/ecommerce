package se.lexicon.ecommerceworkshop.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import se.lexicon.ecommerceworkshop.entity.Category;
import se.lexicon.ecommerceworkshop.entity.Product;
import se.lexicon.ecommerceworkshop.repository.CategoryRepository;
import se.lexicon.ecommerceworkshop.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public DataSeeder(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        Category electronics = createCategory("Electronics");
        Category books = createCategory("Books");
        Category clothes = createCategory("Clothes");

        createProduct("Wireless Headphones", new BigDecimal("799.00"), electronics,
                "headphones.jpg");
        createProduct("Java Basics", new BigDecimal("299.00"), books,
                "java-book.jpg");
        createProduct("Cotton T-Shirt", new BigDecimal("199.00"), clothes,
                "t-shirt.jpg");
    }

    private Category createCategory(String name) {
        return categoryRepository.findByNameIgnoreCase(name)
                .orElseGet(() -> {
                    Category category = new Category();
                    category.setName(name);
                    return categoryRepository.save(category);
                });
    }

    private void createProduct(String name, BigDecimal price, Category category, String imageUrl) {
        if (!productRepository.existsByNameIgnoreCase(name)) {
            Product product = new Product();
            product.setName(name);
            product.setPrice(price);
            product.setCategory(category);
            product.setImageUrls(List.of(imageUrl));
            productRepository.save(product);
        }
    }
}
