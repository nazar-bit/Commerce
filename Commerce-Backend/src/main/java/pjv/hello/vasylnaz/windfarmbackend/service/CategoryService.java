package pjv.hello.vasylnaz.windfarmbackend.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pjv.hello.vasylnaz.windfarmbackend.dto.CategoryResponse;
import pjv.hello.vasylnaz.windfarmbackend.dto.CreateCategoryRequest;
import pjv.hello.vasylnaz.windfarmbackend.entity.Category;
import pjv.hello.vasylnaz.windfarmbackend.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Category addCategory(CreateCategoryRequest request) {
        if(categoryRepository.existsByName(request.name())){
            throw new RuntimeException("Category name already exists");
        }

        Category category = new Category();
        category.setName(request.name());

        if(request.superCategoryName() != null){
            Category superCategory = categoryRepository.findByName(request.superCategoryName())
                .orElseThrow(()->new RuntimeException("Super category not found"));

            category.setSuperCategory(superCategory);
        }

        return categoryRepository.save(category);
    }


    public CategoryResponse findById(Long id) {
        Category category = categoryRepository.findById(id)
            .orElseThrow(()->new RuntimeException("Category not found"));

        return mapToResponse(category);
    }


    public List<CategoryResponse> getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(this::mapToResponse).collect(Collectors.toList());
    }


    public CategoryResponse mapToResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getSuperCategory() != null ? category.getSuperCategory().getId() : null
        );
    }
}
