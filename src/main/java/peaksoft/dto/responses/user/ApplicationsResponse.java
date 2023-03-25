package peaksoft.dto.responses.user;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;


@Builder
public record ApplicationsResponse  (
     HttpStatus status,
      String applicationStatus,
     List<UserAllResponse> users
){}
