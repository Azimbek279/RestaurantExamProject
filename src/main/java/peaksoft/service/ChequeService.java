package peaksoft.service;

import peaksoft.dto.requests.ChequeOfRestaurantAmountDayRequest;
import peaksoft.dto.requests.ChequeOneDayTotalAmountRequest;
import peaksoft.dto.requests.ChequeRequest;
import peaksoft.dto.responses.SimpleResponse;
import peaksoft.dto.responses.cheque.ChequeFinalResponse;
import peaksoft.dto.responses.cheque.ChequeOfRestaurantAmountDayResponse;
import peaksoft.dto.responses.cheque.ChequeOneDayTotalAmountResponse;
import peaksoft.dto.responses.cheque.ChequeResponse;

import java.time.LocalDate;
import java.util.List;

public interface ChequeService {
    List<ChequeResponse> findAll();

    SimpleResponse save(ChequeRequest request);

    ChequeFinalResponse findById(Long id);

    SimpleResponse update(Long id,ChequeRequest request);
    SimpleResponse delete(Long id);

    SimpleResponse totalSum(Long id,LocalDate date);

    SimpleResponse avg(LocalDate date);

    ChequeOneDayTotalAmountResponse findAllChequesOneDayTotalAmount(ChequeOneDayTotalAmountRequest request);
    ChequeOfRestaurantAmountDayResponse countRestGrantTotalForDay(ChequeOfRestaurantAmountDayRequest chequeOfRestaurantAmountDayRequest);
}
