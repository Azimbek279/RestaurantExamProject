package peaksoft.service;

import peaksoft.dto.requests.StopListRequest;
import peaksoft.dto.responses.SimpleResponse;
import peaksoft.dto.responses.stopList.StopListResponse;

import java.util.List;

public interface StopListService {
    SimpleResponse save(StopListRequest request);

    List<StopListResponse> findAll();

   StopListResponse findById(Long id);
    SimpleResponse update(Long id,StopListRequest request);
    SimpleResponse delete(Long id);

}
