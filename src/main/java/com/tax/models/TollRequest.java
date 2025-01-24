package com.tax.models;


import com.tax.models.vehicle.Vehicle;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class TollRequest {
    @NonNull
    private List<LocalDateTime> dates;

    @NonNull
    private String vehicle;

}
