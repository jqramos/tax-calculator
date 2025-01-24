package com.tax.taxcalculator.service;

import com.tax.models.TollRequest;
import com.tax.models.TollResponse;
import com.tax.models.vehicle.Car;
import com.tax.models.vehicle.Emergency;
import com.tax.service.TaxCalculatorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TaxCalculatorServiceImplTests {

    @InjectMocks
    private TaxCalculatorServiceImpl taxCalculatorService;

    @Test
    public void testCalculateTax() {
        List<LocalDateTime> dates = List.of(
                LocalDateTime.of(2013, 1, 14, 21, 0, 0),
                LocalDateTime.of(2013, 1, 15, 21, 0, 0),
                LocalDateTime.of(2013, 2, 7, 6, 23, 27),
                LocalDateTime.of(2013, 2, 7, 15, 27, 0),
                LocalDateTime.of(2013, 2, 9, 6, 27, 0),
                LocalDateTime.of(2013, 2, 9, 6, 20, 27),
                LocalDateTime.of(2013, 2, 9, 14, 35, 0),
                LocalDateTime.of(2013, 2, 9, 15, 29, 0),
                LocalDateTime.of(2013, 2, 9, 15, 47, 0),
                LocalDateTime.of(2013, 2, 9, 16, 1, 0),
                LocalDateTime.of(2013, 2, 9, 16, 48, 0),
                LocalDateTime.of(2013, 2, 9, 17, 49, 0),
                LocalDateTime.of(2013, 2, 9, 18, 29, 0),
                LocalDateTime.of(2013, 2, 9, 18, 35, 0),
                LocalDateTime.of(2013, 3, 26, 14, 25, 0),
                LocalDateTime.of(2013, 3, 28, 14, 7, 27)
        );

        TollResponse tollResponse = taxCalculatorService.calculateTax(
                TollRequest.builder().dates(dates).vehicle("Car").build()
        );

        assertEquals(new BigDecimal(29), tollResponse.getTotalFees());
    }

    @Test
    public void testCalculateTaxWith_60_min_rule() {
        List<LocalDateTime> dates = List.of(
                LocalDateTime.of(2013, 1, 6, 21, 0, 0),
                LocalDateTime.of(2013, 1, 15, 21, 0, 0),
                LocalDateTime.of(2013, 5, 7, 6, 23, 27),
                LocalDateTime.of(2013, 5, 7, 15, 27, 0),
                LocalDateTime.of(2013, 1, 8, 6, 27, 0),
                LocalDateTime.of(2013, 1, 8, 6, 20, 27),
                LocalDateTime.of(2013, 1, 8, 14, 35, 0),
                LocalDateTime.of(2013, 1, 8, 15, 29, 0),
                LocalDateTime.of(2013, 1, 8, 15, 47, 0),
                LocalDateTime.of(2013, 1, 8, 16, 1, 0),
                LocalDateTime.of(2013, 1, 8, 16, 48, 0),
                LocalDateTime.of(2013, 1, 8, 17, 49, 0),
                LocalDateTime.of(2013, 1, 8, 18, 29, 0),
                LocalDateTime.of(2013, 1, 8, 18, 35, 0),
                LocalDateTime.of(2013, 3, 26, 14, 25, 0),
                LocalDateTime.of(2013, 3, 28, 14, 7, 27)
        );

        TollResponse tollResponse = taxCalculatorService.calculateTax(
                TollRequest.builder().dates(dates).vehicle("Car").build()
        );

        assertEquals(new BigDecimal(89),  tollResponse.getTotalFees());
    }

    @Test
    void testComputeEmergencyVehicleFee() {
        TollResponse tollResponse = taxCalculatorService.calculateTax(
                TollRequest.builder().dates(List.of(LocalDateTime.of(2013, 1, 6, 21, 0, 0))).vehicle("Emergency").build()
        );

        assertEquals(new BigDecimal(0), tollResponse.getTotalFees());
    }

}
