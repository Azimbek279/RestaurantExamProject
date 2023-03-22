package peaksoft.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.dto.requests.ChequeRequest;
import peaksoft.dto.responses.SimpleResponse;
import peaksoft.dto.responses.cheque.ChequeResponse;
import peaksoft.service.ChequeService;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@RestController
@RequestMapping("/api/cheques")
@RequiredArgsConstructor
public class ChequeAPI {
   private final ChequeService chequeService;

    @ExceptionHandler(NoSuchElementException.class)
    ResponseEntity<String> handlerExceptions(NoSuchElementException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("An error occurred: "+e.getMessage());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','WAITER')")
    @PostMapping
    public SimpleResponse save(@RequestBody ChequeRequest request){
        return  chequeService.save(request);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','WAITER')")
    @GetMapping("/{id}")
    public ChequeResponse findById(@PathVariable Long id){
        return chequeService.findById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public SimpleResponse update(@PathVariable Long id,@RequestBody ChequeRequest request ){
        return chequeService.update(id,request);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public SimpleResponse delete(@PathVariable Long id){
        return chequeService.delete(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','WAITER')")
    @GetMapping
    public List<ChequeResponse> findAll(){
        return chequeService.findAll();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/total/{id}")
    public SimpleResponse totalSum(@PathVariable Long id,
                                   @RequestParam(required = false) LocalDate date){
        return chequeService.totalSum(id,Objects.requireNonNullElseGet(date,LocalDate::now));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/avg")
    public SimpleResponse avg(@RequestParam(required = false) LocalDate date){
        return chequeService.avg(Objects.requireNonNullElseGet(date, LocalDate::now));
    }


}