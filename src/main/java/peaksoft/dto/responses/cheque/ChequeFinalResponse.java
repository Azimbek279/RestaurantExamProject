package peaksoft.dto.responses.cheque;

import lombok.Builder;
import peaksoft.dto.responses.menuItem.MenuItemResponse;

import java.util.List;
import java.util.Set;

@Builder
public record ChequeFinalResponse(
        String fullName,
        List<MenuItemResponse> items,
        int price,
        int service,
        int  globalTotal) {


}
