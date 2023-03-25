package peaksoft.dto.responses.cheque;

import lombok.Builder;

@Builder
public record ChequeOfRestaurantAmountDayResponse(
        int numberOfWaiters,
                                                  int numberOfCheque,
                                                  int totalAmount
) {

}
