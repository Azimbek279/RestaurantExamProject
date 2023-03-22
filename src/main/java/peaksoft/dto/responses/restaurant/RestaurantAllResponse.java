package peaksoft.dto.responses.restaurant;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record RestaurantAllResponse(
        String name,
        String location,
        BigDecimal averageCheck
) {
}
