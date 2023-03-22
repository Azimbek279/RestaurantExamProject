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
    @Query("select new peaksoft.dto.responses.restaurant.RestaurantResponse(r.name,r.location,r.resType,r.numberOfEmployees,r.service) from Restaurant r  where r.id=:id")
   Optional<RestaurantResponse> findRestaurantResponseById(Long id);
    @Query("select new peaksoft.dto.responses.restaurant.RestaurantAllResponse(r.name,r.location,c.priceAverage) from Restaurant r join User u join Cheque c ")
    List<RestaurantAllResponse> findAllRestaurantResponses();
}