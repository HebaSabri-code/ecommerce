package se.lexicon.ecommerceworkshop.mapper;

import org.springframework.stereotype.Component;
import se.lexicon.ecommerceworkshop.dto.ProductRequest;
import se.lexicon.ecommerceworkshop.dto.ProductResponse;
import se.lexicon.ecommerceworkshop.entity.Category;
import se.lexicon.ecommerceworkshop.entity.Product;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getCategory().getName(),
                List.copyOf(product.getImageUrls())
        );
    }

    public Product toEntity(ProductRequest request, Category category) {
        Product product = new Product();
        product.setName(request.name());
        product.setPrice(request.price());
        product.setCategory(category);

        if (request.imageUrls() != null) {
            product.setImageUrls(new ArrayList<>(request.imageUrls()));
        }
        return product;
    }
}
