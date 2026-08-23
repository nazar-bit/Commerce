package pjv.hello.vasylnaz.windfarmbackend.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pjv.hello.vasylnaz.windfarmbackend.dto.CreateCategoryRequest;
import pjv.hello.vasylnaz.windfarmbackend.entity.Category;
import pjv.hello.vasylnaz.windfarmbackend.repository.CategoryRepository;

import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public void addCategory(CreateCategoryRequest request) {
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

        categoryRepository.save(category);
    }
}
