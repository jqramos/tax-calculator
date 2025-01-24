package com.tax.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TollEntry {

    LocalDateTime entry;
    int fee;
}
