package pjv.hello.vasylnaz.windfarmbackend.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pjv.hello.vasylnaz.windfarmbackend.dto.CreateCategoryRequest;
import pjv.hello.vasylnaz.windfarmbackend.entity.Category;
import pjv.hello.vasylnaz.windfarmbackend.repository.CategoryRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void addNewCategoryWithoutSuperCategory() {
        CreateCategoryRequest request = new CreateCategoryRequest(
            "Toys",
            null
        );

        categoryService.addCategory(request);
        Category savedCategory = categoryRepository.findByName(request.name())
                .orElseThrow(()->new RuntimeException("Category not found"));

        assertEquals("Toys", savedCategory.getName());
    }

    @Test
    void addTwoCategoriesWithTheSameName() {
        CreateCategoryRequest request1 = new CreateCategoryRequest(
                "Toys",
                null
        );

        CreateCategoryRequest request2 = new CreateCategoryRequest(
                "Toys",
                null
        );

        categoryService.addCategory(request1);
        assertThrows(RuntimeException.class,
                ()->categoryService.addCategory(request2));
    }

    @Test
    void addNewCategoryWithInvalidSuperCategory() {
        CreateCategoryRequest request = new CreateCategoryRequest(
                "Toys",
                "ForKids"
        );

        assertThrows(RuntimeException.class, () -> categoryService.addCategory(request));
    }

    @Test
    void addNewCategoryWithValidSuperCategory() {
        CreateCategoryRequest request1 = new CreateCategoryRequest(
                "Toys",
                null
        );

        CreateCategoryRequest request2 = new CreateCategoryRequest(
                "ToyCars",
                "Toys"
        );

        categoryService.addCategory(request1);
        categoryService.addCategory(request2);

        Category savedCategory1 = categoryRepository.findByName(request1.name())
            .orElseThrow(()->new RuntimeException("Category not found"));
        Category savedCategory2 = categoryRepository.findByName(request2.name())
            .orElseThrow(()->new RuntimeException("Category not found"));

        assertEquals("Toys", savedCategory1.getName());
        assertNull(savedCategory1.getSuperCategory());

        assertEquals("ToyCars", savedCategory2.getName());
        assertEquals("Toys", savedCategory2.getSuperCategory().getName());
    }
}
