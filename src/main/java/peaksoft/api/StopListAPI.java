package peaksoft.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.dto.requests.StopListRequest;
import peaksoft.dto.responses.SimpleResponse;
import peaksoft.dto.responses.stopList.StopListResponse;
import peaksoft.service.StopListService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/stopLists")
@RequiredArgsConstructor
public class StopListAPI {
    private final StopListService stopListService;

    @ExceptionHandler(NoSuchElementException.class)
    ResponseEntity<String> handlerExceptions(NoSuchElementException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("An error occurred: "+e.getMessage());
    }
    @PreAuthorize("permitAll()")
    @GetMapping
    public List<StopListResponse> findAll() {
        return stopListService.findAll();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CHEF')")
    @PostMapping
    public SimpleResponse save(@RequestBody StopListRequest request) {
        return stopListService.save(request);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    public StopListResponse findById(@PathVariable Long id) {
        return stopListService.findById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CHEF')")
    @PutMapping("/{id}")
    public SimpleResponse update(@PathVariable Long id, @RequestBody StopListRequest request) {
        return stopListService.update(id, request);
    }


    @PreAuthorize("hasAnyAuthority('ADMIN','CHEF')")
    @DeleteMapping("/{id}")
    public SimpleResponse delete(@PathVariable Long id) {
        return stopListService.delete(id);
    }
}
