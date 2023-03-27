package peaksoft.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import peaksoft.config.jwt.JwtUtil;
import peaksoft.dto.requests.AuthUserRequest;
import peaksoft.dto.requests.UserApplicationRequest;
import peaksoft.dto.requests.UserRequest;
import peaksoft.dto.responses.SimpleResponse;
import peaksoft.dto.responses.user.*;
import peaksoft.excetions.NotFoundException;
import peaksoft.models.Restaurant;
import peaksoft.models.User;
import peaksoft.models.enums.Role;
import peaksoft.repository.RestaurantRepository;
import peaksoft.repository.UserRepository;
import peaksoft.service.UserService;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    private final RestaurantRepository restaurantRepository;



    @PostConstruct
    public void init() {
        User user = User.builder()
                .firstName("Admin")
                .lastName("Adminov")
                .email("admin@gmail.com")
                .password(encoder.encode("admin123"))
                .role(Role.ADMIN)
                .build();
        if (!userRepository.existsByEmail(user.getEmail())) {
            userRepository.save(user);
        }
    }

    public UserTokenResponse authenticate(AuthUserRequest userRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userRequest.email(),
                        userRequest.password()
                )
        );

        User user = userRepository.findByEmail(userRequest.email())
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("User with email: %s not found!", userRequest.email())));

        String token = jwtUtil.generateToken(user);

        return UserTokenResponse.builder()
                .email(user.getEmail())
                .token(token)
                .build();
    }

    @Override
    public List<UserAllResponse> findAll(String role, String lastName) {
        if (role == null && lastName == null) {
            return userRepository.findAllUserResponses();
        } else if (role != null) {
            return userRepository.findAllUserResponsesByRole(role.toUpperCase() + "%");
        } else {
            return userRepository.findAllUserResponsesByLastName(lastName + "%");
        }
    }

    @Override
    public SimpleResponse save(UserRequest request) {

        Restaurant restaurant = restaurantRepository.findRestaurant()
                .orElseThrow(() -> new NoSuchElementException("Restaurant with not found!"));

        if (restaurant.getUsers().size() > 15) {
            return SimpleResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("The number of employees cannot be more than 15")
                    .build();
        }

        if (userRepository.existsByEmail(request.email())) {
            return SimpleResponse.builder()
                    .status(HttpStatus.CONFLICT)
                    .message(String.format("User with email: %s already exists!", request.email()))
                    .build();
        }


        if (request.role().equals(Role.CHEF)) {
            Period period = Period.between(request.dateOfBirth(), LocalDate.now());
            if (period.getYears() < 25 || period.getYears() > 45) {
                return SimpleResponse.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("For the vacancy of a cook, the age range is from 25 to 45 years!")
                        .build();
            }
            if (request.experience() <= 1) {
                return SimpleResponse.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("Cooking experience must be at least 2 years!")
                        .build();
            }
        } else if (request.role().equals(Role.WAITER)) {
            Period period = Period.between(request.dateOfBirth(), LocalDate.now());
            if (period.getYears() < 18 || 30 < period.getYears()) {
                return SimpleResponse.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("For the vacancy of a waiter, the age range is from 18 to 30 years!")
                        .build();
            }
            if (request.experience() <= 1) {
                return SimpleResponse.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("Experience as a waiter must be at least 1 year!")
                        .build();
            }
        }


        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .dateOfBirth(request.dateOfBirth())
                .email(request.email())
                .password(encoder.encode(request.password()))
                .role(request.role())
                .phoneNumber(request.phoneNumber())
                .experience(request.experience())
                .restaurant(restaurant)
                .build();

        userRepository.save(user);

        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message(String.format("User with fullName: %s %s successfully saved!", user.getLastName(), user.getFirstName()))
                .build();
    }

    @Override
    public UserResponse findById(Long id) {
        return userRepository.findUserResponseById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("User with id: %s not found!", id)));
    }

    @Override
    public SimpleResponse update(Long id, UserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("User with id: %s not found!", id)));



        if (userRepository.existsByEmail(request.email())) {
            return SimpleResponse.builder()
                    .status(HttpStatus.CONFLICT)
                    .message(String.format("User with email: %s already exists!", request.email()))
                    .build();
        }


        if (request.role().equals(Role.CHEF)) {
            Period period = Period.between(request.dateOfBirth(), LocalDate.now());
            if (period.getYears() < 25 || period.getYears() > 45) {
                return SimpleResponse.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("For the vacancy of a cook, the age range is from 25 to 45 years!")
                        .build();
            }
            if (request.experience() <= 1) {
                return SimpleResponse.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("Cooking experience must be at least 2 years!")
                        .build();
            }
        } else if (request.role().equals(Role.WAITER)) {
            Period period = Period.between(request.dateOfBirth(), LocalDate.now());
            if (period.getYears() < 18 || 30 < period.getYears()) {
                return SimpleResponse.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("For the vacancy of a waiter, the age range is from 18 to 30 years!")
                        .build();
            }
            if (request.experience() <= 1) {
                return SimpleResponse.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("Experience as a waiter must be at least 1 year!")
                        .build();
            }
        }

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setDateOfBirth(request.dateOfBirth());
        user.setEmail(request.email());
        user.setPassword(encoder.encode(request.password()));
        user.setPhoneNumber(request.phoneNumber());
        user.setRole(request.role());
        user.setExperience(request.experience());

        userRepository.save(user);

        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message(String.format("User with id: %s successfully updated", id))
                .build();
    }

    @Override
    public SimpleResponse deleteById(Long id) {

        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException(
                    String.format("User with id: %s not found!", id));
        }
        userRepository.deleteById(id);

        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message(String.format("User with id: %s successfully deleted", id))
                .build();
    }

    @Override
    public SimpleResponse application(UserRequest request) {

        Restaurant restaurant = restaurantRepository.findById(1L).get();
        if (restaurant.getUsers().size() > 15) {
            return SimpleResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("No vacancy!")
                    .build();
        }

        if (request.role().equals(Role.ADMIN)) {
            return SimpleResponse.builder()
                    .status(HttpStatus.FORBIDDEN)
                    .message("There is no vacancy for the administrator!")
                    .build();
        }

        if (userRepository.existsByEmail(request.email())) {
            return SimpleResponse.builder()
                    .status(HttpStatus.CONFLICT)
                    .message(String.format("User with email: %s already exists!", request.email()))
                    .build();
        }

        if (request.role().equals(Role.CHEF)) {
            Period period = Period.between(request.dateOfBirth(), LocalDate.now());
            if (period.getYears() < 25 || period.getYears() > 45) {
                return SimpleResponse.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("For the vacancy of a cook, the age range is from 25 to 45 years!")
                        .build();
            }
            if (request.experience() <= 1) {
                return SimpleResponse.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("Cooking experience must be at least 2 years!")
                        .build();
            }
        } else if (request.role().equals(Role.WAITER)) {
            Period period = Period.between(request.dateOfBirth(), LocalDate.now());
            if (period.getYears() < 18 || 30 < period.getYears()) {
                return SimpleResponse.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("For the vacancy of a waiter, the age range is from 18 to 30 years!")
                        .build();
            }
            if (request.experience() <= 1) {
                return SimpleResponse.builder()
                        .status(HttpStatus.BAD_REQUEST)
                        .message("Experience as a waiter must be at least 1 year!")
                        .build();
            }
        }

        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .dateOfBirth(request.dateOfBirth())
                .email(request.email())
                .password(encoder.encode(request.password()))
                .role(request.role())
                .phoneNumber(request.phoneNumber())
                .experience(request.experience())
                .build();

        userRepository.save(user);

        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Your application has been successfully sent")
                .build();
    }

    @Override
    public SimpleResponse applications(UserApplicationRequest applicationRequest) {
        User user = userRepository.findById(applicationRequest.id())
                .orElseThrow(() -> new NotFoundException("This id:" + applicationRequest.id() + " does not exist"));

        if (applicationRequest.accepted()) {
            user.setRestaurant(restaurantRepository.findById(restaurantRepository.findRestaurant()
                    .orElseThrow(()->new NotFoundException("This Restaurant is null!!")).getId()).orElseThrow(() -> new NotFoundException("This Restaurant does not exist")));
            userRepository.save(user);
            return new SimpleResponse(HttpStatus.OK, "Congratulations you have successfully got a job!!");
        } else {
            userRepository.delete(user);
            return new SimpleResponse(HttpStatus.OK, "You couldn't get a job");
        }
    }




}
