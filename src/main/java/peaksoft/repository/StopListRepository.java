package peaksoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import peaksoft.dto.responses.stopList.StopListResponse;
import peaksoft.models.StopList;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Repository
public interface StopListRepository extends JpaRepository<StopList, Long> {
    boolean existsByMenuItem_NameAndDateAndIdNot(String name, LocalDate date, Long id);
    @Query("select new peaksoft.dto.responses.stopList.StopListResponse(s.menuItem.name,s.reason,s.date) from StopList s")
    List<StopListResponse> findAllStopList();
    boolean existsByDateAndMenuItem_Name(LocalDate date, String name);
    @Query("select new peaksoft.dto.responses.stopList.StopListResponse(s.menuItem.name,s.reason,s.date) from StopList s where s.id=:id")
    Optional<StopListResponse> findStopListById(Long id);
}