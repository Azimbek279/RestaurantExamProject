package peaksoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import peaksoft.dto.responses.user.EmployeeResponse;
import peaksoft.dto.responses.user.UserAllResponse;
import peaksoft.dto.responses.user.UserResponse;
import peaksoft.models.User;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query("SELECT NEW peaksoft.dto.responses.user.UserAllResponse(u.id,concat(u.lastName,' ',u.firstName),u.email,u.role) FROM User u")
    List<UserAllResponse> findAllUserResponses();

    @Query("SELECT NEW peaksoft.dto.responses.user.UserAllResponse(u.id,concat(u.lastName,' ',u.firstName),u.email,u.role) FROM User u where  u.role=:role")
    List<UserAllResponse> findAllUserResponsesByRole(String role);

    @Query("SELECT NEW peaksoft.dto.responses.user.UserAllResponse(u.id,concat(u.lastName,' ',u.firstName),u.email,u.role) " +
            "FROM User u WHERE  u.lastName=:lastName")
    List<UserAllResponse> findAllUserResponsesByLastName(String lastName);
    @Query("SELECT NEW peaksoft.dto.responses.user.UserResponse(u.id,concat(u.lastName,' ',u.firstName),u.dateOfBirth,u.email,u.phoneNumber,u.role,u.experience) " +
            "FROM User u WHERE u.id=:id")
    Optional<UserResponse> findUserResponseById(Long id);
//    List<User> findAllByAcceptedFalse();
//    @Query("SELECT NEW peaksoft.dto.responses.user.UserAllResponse(u.id,concat(u.lastName,' ',u.firstName),u.email,u.role)" +
//            " FROM User u WHERE u.accepted=false")
//    List<UserAllResponse> findAllUserResponsesByAcceptedFalse();

    @Query("select new peaksoft.dto.responses.user.EmployeeResponse(u.id,u.firstName,u.lastName,u.dateOfBirth,u.email,u.role,u.phoneNumber,u.experience)from User u where u.restaurant.id=:id")
    List<EmployeeResponse> getAllWorkers(Long id);

    @Query("select u from User u where u.id=:id")
    User findByIdQuery(Long id);
}