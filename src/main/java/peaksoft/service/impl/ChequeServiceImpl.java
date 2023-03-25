package peaksoft.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import peaksoft.dto.requests.ChequeOfRestaurantAmountDayRequest;
import peaksoft.dto.requests.ChequeOneDayTotalAmountRequest;
import peaksoft.dto.requests.ChequeRequest;
import peaksoft.dto.responses.SimpleResponse;
import peaksoft.dto.responses.cheque.*;
import peaksoft.dto.responses.menuItem.MenuItemResponse;
import peaksoft.excetions.NotFoundException;
import peaksoft.models.Cheque;
import peaksoft.models.MenuItem;
import peaksoft.models.Restaurant;
import peaksoft.models.User;
import peaksoft.models.enums.Role;
import peaksoft.repository.ChequeRepository;
import peaksoft.repository.MenuItemRepository;
import peaksoft.repository.RestaurantRepository;
import peaksoft.repository.UserRepository;
import peaksoft.service.ChequeService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ChequeServiceImpl implements ChequeService {
    private final ChequeRepository chequeRepository;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;

    private final RestaurantRepository restaurantRepository;


    @Override
    public List<ChequeResponse> findAll() {
        return null;
    }

    @Override
    public SimpleResponse save(ChequeRequest request) {

        MenuItem menuItem = menuItemRepository.findById(request.menuItemId())
                .orElseThrow(() -> new NoSuchElementException(String.format("MenuItem with id: %s not found", request.menuItemId())));

        Cheque cheque = Cheque.builder()
                .user(userRepository.findById(request.userId())
                        .orElseThrow(() -> new NoSuchElementException(String.format("User with id: %s not found", request.userId()))))
                .createdAt(LocalDate.now())
                .build();
        menuItem.addCheque(cheque);
        chequeRepository.save(cheque);

        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message(String.format("Cheque with MenuItem name: %s successfully saved", menuItem.getName()))
                .build();
    }

    @Override
    public ChequeFinalResponse findById(Long id) {
        Cheque cheque;
        cheque = chequeRepository.findById(id).orElseThrow();
        User employee = cheque.getUser();

        List<MenuItemResponse> menuItems = convert(cheque.getMenuItems());
        int totalPrice = 0;
        for (MenuItemResponse menu : menuItems) {
            totalPrice = totalPrice + menu.price().intValue();
        }
        int service = totalPrice + (totalPrice * employee.getRestaurant().getService() / 100);


        return ChequeFinalResponse.builder().
                fullName(cheque.getUser()
                        .getFirstName()
                        .concat(" ")
                        .concat(cheque.getUser()
                                .getLastName()))
                .service(totalPrice * employee.getRestaurant().getService() / 100)
                .price(totalPrice)
                .globalTotal(service)
                .items(convert(cheque.getMenuItems())).build();
    }


    @Override
    public SimpleResponse update(Long id, ChequeRequest request) {

        Cheque cheque = chequeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("User with id: %s not found", id)));

        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message(String.format("Cheque with id: %s successfully updated", id))
                .build();
    }

    @Override
    public SimpleResponse delete(Long id) {
        if (!chequeRepository.existsById(id)) {
            return SimpleResponse.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message(String.format("Cheque with id: %s not found", id))
                    .build();
        }

        chequeRepository.deleteById(id);

        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message(String.format("Cheque with id: %s successfully deleted", id))
                .build();
    }

    @Override
    public SimpleResponse totalSum(Long id, LocalDate date) {

        Integer top = chequeRepository.getTopByCreatedAt(date, id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(
                        String.format("User with id: %s not found", id)));

        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message(String.format("Total number of %s %s checks as of the date of %s total: %s",
                        user.getLastName(), user.getFirstName(), date, top))
                .build();
    }


    @Override
    public SimpleResponse avg(LocalDate date) {
        Integer avg = chequeRepository.avg(date);
        return SimpleResponse.builder()
                .status(HttpStatus.OK)
                .message(String.format("Average check as of date %s total : %s", date, avg))
                .build();
    }

    private MenuItemResponse convert(MenuItem menuItem) {
        return MenuItemResponse.builder().description(menuItem.getDescription()).image(menuItem.getImage()).price(menuItem.getPrice()).isVegetarian(menuItem.getIsVegetarian()).name(menuItem.getName()).build();
    }

    private List<MenuItemResponse> convert(List<MenuItem> menuItems) {
        List<MenuItemResponse> menuItemResponses = new ArrayList<>();
        for (MenuItem menuItem : menuItems) {
            menuItemResponses.add(convert(menuItem));
        }
        return menuItemResponses;
    }
    @Override
    public ChequeOneDayTotalAmountResponse findAllChequesOneDayTotalAmount(ChequeOneDayTotalAmountRequest request) {
        System.out.println(userRepository.findByIdQuery(request.waiterId()));
        User user = userRepository.findById(request.waiterId()).orElseThrow(
                () -> new NotFoundException("User with id : " + request.waiterId() + "is not found!"));
        System.out.println(user);
        int chequeCount = 0;
        int totalAmount = 0;
        if (user.getRole().equals(Role.WAITER)) {
            for (Cheque che : user.getCheques()) {
                if (che.getCreatedAt().equals(request.date())) {
                    int service = che.getPriceAverage() * user.getRestaurant().getService() / 100;
                    totalAmount = service + che.getPriceAverage();
                    ++chequeCount;

                }
            }
        }
        return ChequeOneDayTotalAmountResponse.builder().numberOfCheques(chequeCount).totalAmount(BigDecimal.valueOf(totalAmount)).walterFullName(user.getFirstName() + " " + user.getLastName()).build();

    }

    @Override
    public ChequeOfRestaurantAmountDayResponse countRestGrantTotalForDay(ChequeOfRestaurantAmountDayRequest chequeOfRestaurantAmountDayRequest) {
        Restaurant restaurant = restaurantRepository.findRestaurant().orElseThrow(()->new NotFoundException("not restaurant"));
        int numberOfWaiters = 0;
        int numberOfCheque = 0;
        int totalAmount = 0;
        for (User userWaiter : restaurant.getUsers()) {
            if (userWaiter.getRole().equals(Role.WAITER)) {
                for (Cheque waiterCh : userWaiter.getCheques()) {
                    if (waiterCh.getCreatedAt() == chequeOfRestaurantAmountDayRequest.date()) {
                        var restaurantService = waiterCh.getPriceAverage() * restaurant.getService() / 100;
                        totalAmount = restaurantService + waiterCh.getPriceAverage();
                        numberOfCheque++;
                    }

                }
                numberOfWaiters++;
            }
        }
        return ChequeOfRestaurantAmountDayResponse.builder().numberOfCheque(numberOfCheque).numberOfWaiters(numberOfWaiters).totalAmount(totalAmount).build();
    }
}
