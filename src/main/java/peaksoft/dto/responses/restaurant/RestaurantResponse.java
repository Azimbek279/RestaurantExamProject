package peaksoft.dto.responses.restaurant;

public record RestaurantResponse(
        String name,
        String location,
        String resType,
        Integer service
) {
}
