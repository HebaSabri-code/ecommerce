package se.lexicon.ecommerceworkshop.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DtoValidationTest {

    @Test
    void shouldValidateCustomerAndOrderRequests() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();

            CustomerRequest customerRequest = new CustomerRequest(
                    "", "Sabri", "wrong-email", "123", "", "Cairo", "12345"
            );
            assertThat(validator.validate(customerRequest)).hasSize(4);

            OrderRequest orderRequest = new OrderRequest(
                    1L, List.of(new OrderItemRequest(10L, 0))
            );
            assertThat(validator.validate(orderRequest)).hasSize(1);
        }
    }
}
