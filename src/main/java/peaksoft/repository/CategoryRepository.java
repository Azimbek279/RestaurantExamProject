package peaksoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import peaksoft.dto.responses.category.CategoryResponse;
import peaksoft.models.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select new peaksoft.dto.responses.category.CategoryResponse(c.name) from Category c")
    List<CategoryResponse> findAllCategoryResponses();


}