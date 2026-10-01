package se.lexicon.ecommerceworkshop.mapper;

import org.junit.jupiter.api.Test;
import se.lexicon.ecommerceworkshop.dto.CustomerRequest;
import se.lexicon.ecommerceworkshop.dto.OrderItemRequest;
import se.lexicon.ecommerceworkshop.dto.OrderRequest;
import se.lexicon.ecommerceworkshop.dto.ProductRequest;
import se.lexicon.ecommerceworkshop.entity.Category;
import se.lexicon.ecommerceworkshop.entity.Customer;
import se.lexicon.ecommerceworkshop.entity.Order;
import se.lexicon.ecommerceworkshop.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MapperTest {

    @Test
    void shouldMapCustomerRequestAndResponse() {
        CustomerMapper mapper = new CustomerMapper();
        CustomerRequest request = new CustomerRequest(
                "Heba", "Sabri", "heba@example.com", "secret12",
                "Main Street 10", "Stockholm", "11122"
        );

        Customer customer = mapper.toEntity(request);
        customer.setId(1L);

        assertThat(customer.getAddress().getCity()).isEqualTo("Stockholm");
        assertThat(mapper.toResponse(customer).fullName()).isEqualTo("Heba Sabri");
    }

    @Test
    void shouldMapProductRequestAndResponse() {
        ProductMapper mapper = new ProductMapper();
        Category category = new Category();
        category.setId(2L);
        category.setName("Books");

        ProductRequest request = new ProductRequest(
                "Java Basics", new BigDecimal("299.00"), 2L, List.of("book.jpg")
        );
        Product product = mapper.toEntity(request, category);
        product.setId(10L);

        assertThat(mapper.toResponse(product).categoryName()).isEqualTo("Books");
        assertThat(mapper.toResponse(product).imageUrls()).containsExactly("book.jpg");
    }

    @Test
    void shouldMapOrderRequestAndResponse() {
        OrderMapper mapper = new OrderMapper();

        Customer customer = new Customer();
        customer.setId(1L);

        Product product = new Product();
        product.setId(10L);
        product.setName("Java Basics");
        product.setPrice(new BigDecimal("299.00"));

        OrderRequest request = new OrderRequest(
                1L, List.of(new OrderItemRequest(10L, 2))
        );
        Order order = mapper.toEntity(request, customer, Map.of(10L, product));
        order.setId(5L);
        order.beforeSave();

        assertThat(order.getItems()).hasSize(1);
        assertThat(mapper.toResponse(order).items().getFirst().priceAtPurchase())
                .isEqualByComparingTo("299.00");
    }
}
