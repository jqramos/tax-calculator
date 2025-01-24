package com.tax.controller;

import com.tax.models.TollRequest;
import com.tax.models.TollResponse;
import com.tax.service.TaxCalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tax")
public class TaxCalculatorController {

    private final TaxCalculatorService taxCalculatorService;

    @PostMapping("/calculate")
    public ResponseEntity<TollResponse> calculateTax(@RequestBody TollRequest tollRequest) {
        return ResponseEntity.ok(taxCalculatorService.calculateTax(tollRequest));
    }


}
