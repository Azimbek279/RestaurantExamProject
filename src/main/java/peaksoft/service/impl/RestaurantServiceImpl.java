package peaksoft.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import peaksoft.dto.requests.RestaurantRequest;
import peaksoft.dto.responses.SimpleResponse;
import peaksoft.dto.responses.restaurant.RestaurantAllResponse;
import peaksoft.dto.responses.restaurant.RestaurantResponse;
import peaksoft.dto.responses.user.EmployeeResponse;
import peaksoft.dto.responses.user.UserResponse;
import peaksoft.excetions.AlreadyExistsException;
import peaksoft.excetions.NotFoundException;
import peaksoft.models.Restaurant;
import peaksoft.models.enums.Role;
import peaksoft.repository.RestaurantRepository;
import peaksoft.repository.UserRepository;
import peaksoft.service.RestaurantService;

import java.rmi.NotBoundException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;

    private final UserRepository userRepository;

    @Override
    public RestaurantResponse getById(Long id) {

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("Restaurant with id: %s not found!", id)));
        restaurant.setNumberOfEmployees(restaurant.getUsers().size());
        restaurantRepository.save(restaurant);

        return restaurantRepository.findRestaurantResponseById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("Restaurant with id: %s not found!", id)));
    }

    @Override
    public SimpleResponse save(RestaurantRequest request) {

        if(!restaurantRepository.existsRestaurant()){
            Restaurant restaurant = Restaurant.builder()
                    .name(request.name())
                    .location(request.location())
                    .resType(request.resType())
                    .service(request.service())
                    .build();

            restaurantRepository.save(restaurant);
        }else {
            throw  new AlreadyExistsException("Restaurant is already exists");
        }


        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message("Restaurant with name: "+request.name()+" successfully saved!" )
                .build();
    }

    @Override
    public RestaurantResponse findAll() {
        return restaurantRepository.findAllRestaurantResponses();
    }

    @Override
    public SimpleResponse delete(Long id) {
        if (!restaurantRepository.existsById(id)) {
            return SimpleResponse.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message(String.format("Restaurant with id: %s not found!",id))
                    .build();
        }
        restaurantRepository.deleteById(id);

        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message(String.format("Restaurant with id: %s successfully deleted",id))
                .build();
    }

    @Override
    public SimpleResponse update(Long id, RestaurantRequest request) {

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("Restaurant with id: %s successfully updated", id)));
        restaurant.setName(request.name());
        restaurant.setLocation(request.location());
        restaurant.setResType(request.resType());
        restaurant.setService(request.service());

        restaurantRepository.save(restaurant);

        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message(String.format("Restaurant with id: %s successfully updated",id))
                .build();
    }

    @Override
    public String count() {
        List<EmployeeResponse> allUsers = userRepository.getAllWorkers(restaurantRepository.findRestaurant().get().getId());
        int countChef = 0;
        int countWaiter = 0;
        for (EmployeeResponse allUser : allUsers) {
            if (allUser.role().equals(Role.WAITER)) {
                countWaiter++;
            }
            if (allUser.role().equals(Role.CHEF)) {
                countChef++;
            }
        }
        return "Currently the restaurant has " + allUsers.size() + " employees .\n" +
                "Chefs: " + countChef
                + "\nWaiters: " + countWaiter;
    }
}
