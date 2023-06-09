package peaksoft.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import peaksoft.dto.requests.AuthUserRequest;
import peaksoft.dto.requests.UserApplicationRequest;
import peaksoft.dto.requests.UserRequest;
import peaksoft.dto.responses.*;
import peaksoft.dto.responses.user.*;
import peaksoft.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApi {
    private final UserService userService;



    @PostMapping("/login")
    public UserTokenResponse login(@RequestBody @Valid AuthUserRequest userRequest) {
        return userService.authenticate(userRequest);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping()
    public List<UserAllResponse> finaAllUsers(@RequestParam(required = false) String role,
                                              @RequestParam(required = false) String lastName) {
        return userService.findAll(role, lastName);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public SimpleResponse saveUser(@RequestBody  UserRequest request) {

        return userService.save(request);

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable Long id) {
        return userService.findById(id);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public SimpleResponse update(@PathVariable Long id, @RequestBody @Valid UserRequest request) {
        return userService.update(id, request);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public SimpleResponse delete(@PathVariable Long id) {
        return userService.deleteById(id);
    }


    @PreAuthorize("hasAnyAuthority('WAITER','ADMIN')")
    @PostMapping("/application")
    public SimpleResponse application(@RequestBody @Valid UserRequest request) {
        return userService.application(request);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/applications")
    public SimpleResponse applications(@RequestBody UserApplicationRequest userApplicationRequest) {
        return userService.applications(userApplicationRequest);
    }





}
