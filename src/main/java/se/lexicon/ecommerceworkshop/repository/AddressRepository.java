package se.lexicon.ecommerceworkshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.lexicon.ecommerceworkshop.entity.Address;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByZipCode(String zipCode);
}
