package peaksoft.dto.requests;

import jakarta.validation.constraints.Min;

public record MenuItemRequest(
        String name,
        String image,
        @Min(value = 1,message = "Price cannot be negative!")
        Integer price,
        String description,
        Boolean isVegetarian,
        Long restaurantId,
        Long subCategoryId
) {
}
