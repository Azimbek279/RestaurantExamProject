package peaksoft.dto.requests;

import lombok.Builder;

@Builder
public record UserApplicationRequest(
        Long id,
        Boolean accepted
) {
}
