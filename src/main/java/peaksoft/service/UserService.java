package peaksoft.service;

import peaksoft.dto.requests.AuthUserRequest;
import peaksoft.dto.requests.UserApplicationRequest;
import peaksoft.dto.requests.UserRequest;
import peaksoft.dto.responses.SimpleResponse;
import peaksoft.dto.responses.user.*;

import java.util.List;

public interface UserService {


    UserTokenResponse authenticate(AuthUserRequest userRequest);

    List<UserAllResponse> findAll(String role, String lastName);

    SimpleResponse save(UserRequest request);

    UserResponse findById(Long id);

    SimpleResponse update(Long id, UserRequest request);

    SimpleResponse deleteById(Long id);

    SimpleResponse application(UserRequest request);

//    ApplicationsResponse applications(UserApplicationRequest userApplicationRequest);

    SimpleResponse applications (UserApplicationRequest applicationRequest);

}
