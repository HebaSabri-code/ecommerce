package se.lexicon.ecommerceworkshop.mapper;

import org.springframework.stereotype.Component;
import se.lexicon.ecommerceworkshop.dto.AddressResponse;
import se.lexicon.ecommerceworkshop.dto.CustomerRequest;
import se.lexicon.ecommerceworkshop.dto.CustomerResponse;
import se.lexicon.ecommerceworkshop.entity.Address;
import se.lexicon.ecommerceworkshop.entity.Customer;

@Component
public class CustomerMapper {

    public CustomerResponse toResponse(Customer customer) {
        Address address = customer.getAddress();
        AddressResponse addressResponse = new AddressResponse(
                address.getStreet(),
                address.getCity(),
                address.getZipCode()
        );

        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName() + " " + customer.getLastName(),
                customer.getEmail(),
                addressResponse
        );
    }

    public Customer toEntity(CustomerRequest request) {
        Address address = new Address();
        address.setStreet(request.street());
        address.setCity(request.city());
        address.setZipCode(request.zipCode());

        Customer customer = new Customer();
        customer.setFirstName(request.firstName());
        customer.setLastName(request.lastName());
        customer.setEmail(request.email());
        customer.setAddress(address);
        return customer;
    }

    public void updateEntity(CustomerRequest request, Customer customer) {
        customer.setFirstName(request.firstName());
        customer.setLastName(request.lastName());
        customer.setEmail(request.email());
        customer.getAddress().setStreet(request.street());
        customer.getAddress().setCity(request.city());
        customer.getAddress().setZipCode(request.zipCode());
    }
}
