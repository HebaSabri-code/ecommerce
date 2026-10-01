package se.lexicon.ecommerceworkshop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceUnitUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import se.lexicon.ecommerceworkshop.entity.Address;
import se.lexicon.ecommerceworkshop.entity.Category;
import se.lexicon.ecommerceworkshop.entity.Customer;
import se.lexicon.ecommerceworkshop.entity.Order;
import se.lexicon.ecommerceworkshop.entity.OrderItem;
import se.lexicon.ecommerceworkshop.entity.OrderStatus;
import se.lexicon.ecommerceworkshop.entity.Product;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldSaveOrderAndFindItWithItems() {
        Address address = new Address();
        address.setStreet("Park Street 5");
        address.setCity("Malmo");
        address.setZipCode("21122");

        Customer customer = new Customer();
        customer.setFirstName("Sara");
        customer.setLastName("Ali");
        customer.setEmail("sara@example.com");
        customer.setAddress(address);
        customerRepository.save(customer);

        Category category = new Category();
        category.setName("Home");
        categoryRepository.save(category);

        Product product = new Product();
        product.setName("Desk Lamp");
        product.setPrice(new BigDecimal("249.00"));
        product.setCategory(category);
        productRepository.save(product);

        Order order = new Order();
        order.setCustomer(customer);

        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(2);
        item.setPriceAtPurchase(product.getPrice());
        order.addItem(item);

        orderRepository.saveAndFlush(order);
        entityManager.clear();

        assertThat(orderRepository.findByCustomerId(customer.getId())).hasSize(1);

        List<Order> createdOrders = orderRepository.findByStatus(OrderStatus.CREATED);
        assertThat(createdOrders).hasSize(1);
        assertThat(createdOrders.getFirst().getItems()).hasSize(1);

        PersistenceUnitUtil persistenceUnitUtil =
                entityManager.getEntityManagerFactory().getPersistenceUnitUtil();
        assertThat(persistenceUnitUtil.isLoaded(createdOrders.getFirst(), "items")).isTrue();
    }

    @Test
    void shouldRejectOrderWithoutItems() {
        Order order = new Order();

        assertThatThrownBy(order::beforeSave)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Order must contain at least one item");
    }
}
