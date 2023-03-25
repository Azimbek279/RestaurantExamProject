package peaksoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import peaksoft.dto.responses.restaurant.RestaurantAllResponse;
import peaksoft.dto.responses.restaurant.RestaurantResponse;
import peaksoft.models.Restaurant;

import java.util.List;
import java.util.Optional;
@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Query("select new peaksoft.dto.responses.restaurant.RestaurantResponse(r.name,r.location,r.resType,r.service) from Restaurant r  where r.id=:id")
   Optional<RestaurantResponse> findRestaurantResponseById(Long id);
    @Query("select new peaksoft.dto.responses.restaurant.RestaurantResponse(r.name,r.location,r.resType,r.service) from Restaurant r  ")
    RestaurantResponse findAllRestaurantResponses();

    @Query("select case when count(r) > 0 then true else false end from Restaurant r where r.name is not null")
    Boolean existsRestaurant();
    @Query("select r from Restaurant r" )
    Optional<Restaurant> findRestaurant();
}