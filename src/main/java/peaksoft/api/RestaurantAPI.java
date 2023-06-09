package peaksoft.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.dto.requests.RestaurantRequest;
import peaksoft.dto.responses.SimpleResponse;
import peaksoft.dto.responses.restaurant.RestaurantResponse;
import peaksoft.service.RestaurantService;

import java.util.NoSuchElementException;


@RestController
@RequestMapping("/api/restaurant")
@RequiredArgsConstructor
public class RestaurantAPI {
    private final RestaurantService restaurantService;

    @ExceptionHandler(NoSuchElementException.class)
    ResponseEntity<String> handlerExceptions(NoSuchElementException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("An error occurred: "+e.getMessage());
    }
    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    public RestaurantResponse getById(@PathVariable Long id){
            return restaurantService.getById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public SimpleResponse save(@RequestBody RestaurantRequest request){
        return restaurantService.save(request);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public RestaurantResponse findAll(){
        return restaurantService.findAll();
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping ("/{id}")
    public SimpleResponse update(@PathVariable Long id,@RequestBody RestaurantRequest request){
        return restaurantService.update(id, request);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public SimpleResponse delete(@PathVariable Long id){
        return restaurantService.delete(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getWorkers")
    public String getAllWorkers(){
        return restaurantService.count();
    }



}
