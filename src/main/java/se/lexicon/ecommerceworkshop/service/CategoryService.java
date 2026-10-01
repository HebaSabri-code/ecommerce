package se.lexicon.ecommerceworkshop.service;

import se.lexicon.ecommerceworkshop.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse create(String name);

    List<CategoryResponse> findAll();
}
