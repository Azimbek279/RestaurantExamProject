package peaksoft.dto.responses.user;

import lombok.Builder;
import peaksoft.models.enums.Role;

@Builder
public record UserAllResponse(
        Long id,
        String fullName,
        String email,
        Role role
){
}
