package peaksoft.dto.responses.category;

import lombok.Builder;

@Builder
public record CategoryResponse(
        String name
) {
}
