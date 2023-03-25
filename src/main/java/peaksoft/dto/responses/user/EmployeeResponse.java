package peaksoft.dto.responses.user;

import peaksoft.models.enums.Role;

import java.time.LocalDate;

public record EmployeeResponse(
        Long id,
        String firstName,
        String lastName,
        LocalDate dateOfBrith,
        String email,
        Role role,
        String phoneNumber,
        Integer experience
) {
}
