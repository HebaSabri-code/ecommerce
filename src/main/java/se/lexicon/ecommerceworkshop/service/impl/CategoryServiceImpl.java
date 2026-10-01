package se.lexicon.ecommerceworkshop.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.ecommerceworkshop.dto.CategoryResponse;
import se.lexicon.ecommerceworkshop.entity.Category;
import se.lexicon.ecommerceworkshop.exception.CategoryAlreadyExistsException;
import se.lexicon.ecommerceworkshop.repository.CategoryRepository;
import se.lexicon.ecommerceworkshop.service.CategoryService;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public CategoryResponse create(String name) {
        String categoryName = name.trim();
        if (categoryName.isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }
        if (categoryRepository.existsByNameIgnoreCase(categoryName)) {
            throw new CategoryAlreadyExistsException(categoryName);
        }

        Category category = new Category();
        category.setName(categoryName);
        Category savedCategory = categoryRepository.save(category);
        return new CategoryResponse(savedCategory.getId(), savedCategory.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryResponse(category.getId(), category.getName()))
                .toList();
    }
}
