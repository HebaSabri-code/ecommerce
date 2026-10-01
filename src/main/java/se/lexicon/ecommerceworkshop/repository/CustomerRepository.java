package se.lexicon.ecommerceworkshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.lexicon.ecommerceworkshop.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);

    List<Customer> findByLastNameIgnoreCase(String lastName);

    List<Customer> findByAddressCityIgnoreCase(String city);
}
