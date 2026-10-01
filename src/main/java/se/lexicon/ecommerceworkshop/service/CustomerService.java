package se.lexicon.ecommerceworkshop.service;

import se.lexicon.ecommerceworkshop.dto.CustomerRequest;
import se.lexicon.ecommerceworkshop.dto.CustomerResponse;

public interface CustomerService {

    CustomerResponse register(CustomerRequest request);

    CustomerResponse findById(Long id);

    CustomerResponse update(Long id, CustomerRequest request);
}
