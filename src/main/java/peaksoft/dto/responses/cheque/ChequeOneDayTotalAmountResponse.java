package peaksoft.dto.responses.cheque;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ChequeOneDayTotalAmountResponse(
        String walterFullName,
        int numberOfCheques,
        BigDecimal totalAmount
) {
}
