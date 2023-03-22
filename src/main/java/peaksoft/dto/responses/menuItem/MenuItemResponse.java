package peaksoft.dto.responses.menuItem;

public record MenuItemResponse(
        String name,
        String image,
        Integer price,
        String description,
        Boolean isVegetarian
) {
}
