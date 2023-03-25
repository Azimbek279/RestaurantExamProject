package peaksoft.dto.responses.user;


import lombok.Builder;
import peaksoft.models.enums.Role;

import java.time.LocalDate;

@Builder
public record AbstractApplicationClass(
        Long id,
        String fullName,
        LocalDate dateOfBirth,
        String email,
        String phoneNumber,
        Role role,
        Integer experience
) {


}
