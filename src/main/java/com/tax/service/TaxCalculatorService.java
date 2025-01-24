package com.tax.service;

import com.tax.models.TollRequest;
import com.tax.models.TollResponse;

public interface TaxCalculatorService {

    TollResponse calculateTax(TollRequest tollRequest);
}
