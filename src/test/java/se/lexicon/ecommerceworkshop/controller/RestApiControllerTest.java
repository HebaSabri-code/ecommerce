package se.lexicon.ecommerceworkshop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.ecommerceworkshop.entity.Category;
import se.lexicon.ecommerceworkshop.entity.Customer;
import se.lexicon.ecommerceworkshop.repository.CategoryRepository;
import se.lexicon.ecommerceworkshop.repository.CustomerRepository;
import se.lexicon.ecommerceworkshop.repository.ProductRepository;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
class RestApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldRegisterFindAndUpdateCustomer() throws Exception {
        String customerJson = """
                {
                  "firstName": "Sara",
                  "lastName": "Ali",
                  "email": "sara.api@example.com",
                  "password": "secret12",
                  "street": "Main Street 2",
                  "city": "Stockholm",
                  "zipCode": "11122"
                }
                """;

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fullName").value("Sara Ali"))
                .andExpect(jsonPath("$.email").value("sara.api@example.com"));

        Customer customer = customerRepository.findByEmail("sara.api@example.com")
                .orElseThrow();

        mockMvc.perform(get("/api/v1/customers/{id}", customer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customer.getId()));

        String updateJson = """
                {
                  "firstName": "Sara",
                  "lastName": "Ahmed",
                  "email": "sara.api@example.com",
                  "password": "secret12",
                  "street": "New Street 8",
                  "city": "Stockholm",
                  "zipCode": "11123"
                }
                """;

        mockMvc.perform(put("/api/v1/customers/{id}", customer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Sara Ahmed"))
                .andExpect(jsonPath("$.address.street").value("New Street 8"));
    }

    @Test
    void shouldCreateListAndSearchProducts() throws Exception {
        Category category = categoryRepository.findByNameIgnoreCase("Electronics")
                .orElseThrow();

        String productJson = """
                {
                  "name": "Student Laptop",
                  "price": 6500.00,
                  "categoryId": %d,
                  "imageUrls": ["laptop.jpg"]
                }
                """.formatted(category.getId());

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Student Laptop"))
                .andExpect(jsonPath("$.categoryName").value("Electronics"));

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists());

        mockMvc.perform(get("/api/v1/products/search")
                        .param("name", "Student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Student Laptop"));
    }

    @Test
    void shouldReturnValidationAndNotFoundErrors() throws Exception {
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.fieldErrors.email").exists());

        mockMvc.perform(get("/api/v1/customers/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void shouldCreateAndListCategories() throws Exception {
        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Games\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Games"));

        mockMvc.perform(get("/api/v1/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", hasItem("Games")));
    }

    @Test
    void shouldPlaceAnOrder() throws Exception {
        String customerJson = """
                {
                  "firstName": "Omar",
                  "lastName": "Saleh",
                  "email": "omar.order@example.com",
                  "password": "secret12",
                  "street": "Order Street 5",
                  "city": "Stockholm",
                  "zipCode": "11223"
                }
                """;

        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(customerJson))
                .andExpect(status().isCreated());

        Customer customer = customerRepository.findByEmail("omar.order@example.com")
                .orElseThrow();
        Long productId = productRepository.findByNameContainingIgnoreCase("Wireless")
                .getFirst()
                .getId();

        String orderJson = """
                {
                  "customerId": %d,
                  "items": [
                    {
                      "productId": %d,
                      "quantity": 2
                    }
                  ]
                }
                """.formatted(customer.getId(), productId);

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.customerId").value(customer.getId()))
                .andExpect(jsonPath("$.items[0].quantity").value(2));
    }

    @Test
    void shouldExposeSwaggerDocumentation() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.info.title").value("E-commerce API"));

        mockMvc.perform(get("/swagger-ui.html"))
                .andExpect(status().is3xxRedirection());
    }
}
