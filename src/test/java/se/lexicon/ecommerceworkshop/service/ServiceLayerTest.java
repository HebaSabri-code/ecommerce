package se.lexicon.ecommerceworkshop.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.ecommerceworkshop.dto.CustomerRequest;
import se.lexicon.ecommerceworkshop.dto.CustomerResponse;
import se.lexicon.ecommerceworkshop.dto.OrderItemRequest;
import se.lexicon.ecommerceworkshop.dto.OrderRequest;
import se.lexicon.ecommerceworkshop.dto.OrderResponse;
import se.lexicon.ecommerceworkshop.dto.ProductRequest;
import se.lexicon.ecommerceworkshop.dto.ProductResponse;
import se.lexicon.ecommerceworkshop.entity.Category;
import se.lexicon.ecommerceworkshop.exception.CategoryAlreadyExistsException;
import se.lexicon.ecommerceworkshop.exception.EmailAlreadyExistsException;
import se.lexicon.ecommerceworkshop.exception.ResourceNotFoundException;
import se.lexicon.ecommerceworkshop.repository.CategoryRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ServiceLayerTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void shouldRegisterFindAndUpdateCustomer() {
        CustomerRequest request = customerRequest("service-customer@example.com", "Cairo");

        CustomerResponse registered = customerService.register(request);
        assertThat(customerService.findById(registered.id()).email())
                .isEqualTo("service-customer@example.com");

        CustomerRequest updateRequest = customerRequest("service-customer@example.com", "Alexandria");
        CustomerResponse updated = customerService.update(registered.id(), updateRequest);
        assertThat(updated.address().city()).isEqualTo("Alexandria");

        assertThatThrownBy(() -> customerService.register(request))
                .isInstanceOf(EmailAlreadyExistsException.class);
    }

    @Test
    void shouldCreateAndSearchForProduct() {
        Category books = categoryRepository.findByNameIgnoreCase("Books").orElseThrow();
        ProductRequest request = new ProductRequest(
                "Spring Guide",
                new BigDecimal("349.00"),
                books.getId(),
                List.of("spring-guide.jpg")
        );

        ProductResponse created = productService.create(request);

        assertThat(created.categoryName()).isEqualTo("Books");
        assertThat(productService.searchByName("spring")).hasSize(1);
        assertThat(productService.findAll()).isNotEmpty();
    }

    @Test
    void shouldPlaceOrderUsingCurrentProductPrice() {
        CustomerResponse customer = customerService.register(
                customerRequest("order-customer@example.com", "Cairo"));
        ProductResponse product = productService.searchByName("Java Basics").getFirst();

        OrderRequest request = new OrderRequest(
                customer.id(),
                List.of(new OrderItemRequest(product.id(), 2))
        );
        OrderResponse order = orderService.placeOrder(request);

        assertThat(order.id()).isNotNull();
        assertThat(order.items()).hasSize(1);
        assertThat(order.items().getFirst().priceAtPurchase())
                .isEqualByComparingTo(product.price());
    }

    @Test
    void shouldHandleCategoryAndMissingResources() {
        assertThat(categoryService.create("Games").name()).isEqualTo("Games");
        assertThat(categoryService.findAll()).isNotEmpty();

        assertThatThrownBy(() -> categoryService.create("games"))
                .isInstanceOf(CategoryAlreadyExistsException.class);
        assertThatThrownBy(() -> customerService.findById(999999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private CustomerRequest customerRequest(String email, String city) {
        return new CustomerRequest(
                "Heba",
                "Sabri",
                email,
                "secret12",
                "Main Street 10",
                city,
                "12345"
        );
    }
}
