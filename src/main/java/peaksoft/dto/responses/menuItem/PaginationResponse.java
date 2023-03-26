package peaksoft.dto.responses.menuItem;

import lombok.*;
import peaksoft.models.MenuItem;

import java.util.List;

@Getter
@Setter
public class PaginationResponse{
       private List<MenuItem> MenuItems;
       private int currentPage;
       private int pageSize;
}
