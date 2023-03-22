package peaksoft.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.dto.requests.SubCategoryRequest;
import peaksoft.dto.responses.SimpleResponse;
import peaksoft.dto.responses.subCategory.SubCategoryResponse;
import peaksoft.dto.responses.subCategory.SubCategoryResponsesByCategory;
import peaksoft.service.SubCategoryService;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/subCategories")
@RequiredArgsConstructor
public class SubCategoryAPI {
    private final SubCategoryService subcategoryService;

    @ExceptionHandler(NoSuchElementException.class)
    ResponseEntity<String> handlerExceptions(NoSuchElementException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("An error occurred: "+e.getMessage());
    }
    @PreAuthorize("permitAll()")
    @GetMapping
    public List<SubCategoryResponse> findAll(@RequestParam(required = false) Long id) {
        return subcategoryService.findAll(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public SimpleResponse save(@RequestBody SubCategoryRequest request) {
        return subcategoryService.save(request);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    public SubCategoryResponse findById(@PathVariable Long id) {
        return subcategoryService.findById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public SimpleResponse update(@PathVariable Long id, @RequestBody SubCategoryRequest request) {
        return subcategoryService.update(id, request);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public SimpleResponse delete(@PathVariable Long id) {
        return subcategoryService.delete(id);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/grouping")
    public Map<String, List<SubCategoryResponsesByCategory>> grouping() {
        return subcategoryService.groupingByCategory();
    }


}