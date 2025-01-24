package com.tax.models.enums;

import java.time.LocalTime;

public enum TollRangeCharges {
    RANGE_1(LocalTime.of(6, 0), LocalTime.of(6, 29), 8),
    RANGE_2(LocalTime.of(6, 30), LocalTime.of(6, 59), 13),
    RANGE_3(LocalTime.of(7, 0), LocalTime.of(7, 59), 18),
    RANGE_4(LocalTime.of(8, 0), LocalTime.of(8, 29), 13),
    RANGE_5(LocalTime.of(8, 30), LocalTime.of(14, 59), 8),
    RANGE_6(LocalTime.of(15, 0), LocalTime.of(15, 29), 13),
    RANGE_7(LocalTime.of(15, 30), LocalTime.of(16, 59), 18),
    RANGE_8(LocalTime.of(17, 0), LocalTime.of(17, 59), 13),
    RANGE_9(LocalTime.of(18, 0), LocalTime.of(18, 29), 8),
    RANGE_10(LocalTime.of(18, 30), LocalTime.of(23, 59), 0),
    RANGE_11(LocalTime.of(0, 0), LocalTime.of(5, 59), 0);

    private final LocalTime start;
    private final LocalTime end;
    private final int amount;

    TollRangeCharges(LocalTime start, LocalTime end, int amount) {
        this.start = start;
        this.end = end;
        this.amount = amount;
    }

    public boolean isWithinRange(LocalTime time) {
        return !time.isBefore(start) && !time.isAfter(end);
    }

    public int getAmount() {
        return amount;
    }
}
