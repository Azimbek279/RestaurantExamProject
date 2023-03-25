package peaksoft.dto.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ChequeOneDayTotalAmountRequest(
        @NotNull
        Long waiterId,
        @NotNull
        LocalDate date
) {
}
