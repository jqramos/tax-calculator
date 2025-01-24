package com.tax.models;

import com.tax.models.vehicle.Vehicle;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TollResponse {
    private Vehicle vehicle;
    private BigDecimal totalFees;
}
