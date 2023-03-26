package peaksoft.dto.requests;

import java.util.List;

public record ChequeRequest(
        Long userId,
        Long menuItemId
) {
}
