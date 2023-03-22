package peaksoft.dto.responses.user;

import lombok.Builder;

@Builder
public record UserTokenResponse (
        String email,
        String token
) {
}
