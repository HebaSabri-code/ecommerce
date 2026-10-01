package se.lexicon.ecommerceworkshop.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductResponse(
        Long id,
        String name,
        BigDecimal price,
        String categoryName,
        List<String> imageUrls
) {
}
