package se.lexicon.ecommerceworkshop.dto;

public record AddressResponse(
        String street,
        String city,
        String zipCode
) {
}
