package peaksoft.dto.responses.user;

import lombok.*;
import org.springframework.http.HttpStatus;


@Builder
public record ApplicationResponse(
        HttpStatus status,
        String applicationStatus,
        UserResponse user) {
}
