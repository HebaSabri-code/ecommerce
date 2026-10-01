package se.lexicon.ecommerceworkshop.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import se.lexicon.ecommerceworkshop.entity.Address;
import se.lexicon.ecommerceworkshop.entity.Customer;
import se.lexicon.ecommerceworkshop.entity.UserProfile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Test
    void shouldSaveCustomerAndFindRequiredData() {
        Address address = new Address();
        address.setStreet("Main Street 10");
        address.setCity("Stockholm");
        address.setZipCode("11122");

        UserProfile profile = new UserProfile();
        profile.setNickname("heba");
        profile.setPhoneNumber("0701234567");
        profile.setBio("Customer profile");

        Customer customer = new Customer();
        customer.setFirstName("Heba");
        customer.setLastName("Sabri");
        customer.setEmail("heba@example.com");
        customer.setAddress(address);
        customer.setProfile(profile);

        customerRepository.save(customer);

        assertThat(customer.getId()).isNotNull();
        assertThat(customer.getCreatedAt()).isNotNull();
        assertThat(customerRepository.findByEmail("heba@example.com")).isPresent();
        assertThat(customerRepository.findByLastNameIgnoreCase("SABRI")).hasSize(1);
        assertThat(customerRepository.findByAddressCityIgnoreCase("STOCKHOLM")).hasSize(1);

        List<Address> addresses = addressRepository.findByZipCode("11122");
        assertThat(addresses).hasSize(1);
        assertThat(userProfileRepository.findByNickname("heba")).isPresent();
        assertThat(userProfileRepository.findByPhoneNumberContaining("1234")).hasSize(1);
    }
}
