package com.tax.service;

import com.tax.models.TollRequest;
import com.tax.models.TollEntry;
import com.tax.models.TollResponse;
import com.tax.models.enums.TollRangeCharges;
import com.tax.models.vehicle.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaxCalculatorServiceImpl implements TaxCalculatorService {

    private static final Map<String, Integer> tollFreeVehicles = new HashMap<>();
    static {
        tollFreeVehicles.put("Motorbike", 0);
        tollFreeVehicles.put("Bus", 1);
        tollFreeVehicles.put("Emergency", 2);
        tollFreeVehicles.put("Diplomat", 3);
        tollFreeVehicles.put("Foreign", 4);
        tollFreeVehicles.put("Military", 5);
    }

    private final BigDecimal MAX_DAILY_FEE = BigDecimal.valueOf(60);

    private static final List<LocalDateTime> HOLIDAY_LIST = List.of(
            LocalDateTime.of(2013, 1, 1, 0, 0),
            LocalDateTime.of(2013, 3, 29, 0, 0),
            LocalDateTime.of(2013, 4, 1, 0, 0),
            LocalDateTime.of(2013, 5, 1, 0, 0),
            LocalDateTime.of(2013, 5, 9, 0, 0),
            LocalDateTime.of(2013, 6, 6, 0, 0),
            LocalDateTime.of(2013, 6, 21, 0, 0),
            LocalDateTime.of(2013, 11, 1, 0, 0),
            LocalDateTime.of(2013, 12, 25, 0, 0),
            LocalDateTime.of(2013, 12, 26, 0, 0),
            LocalDateTime.of(2013, 12, 31, 0, 0)
    );

    @Override
    public TollResponse calculateTax(TollRequest tollRequest) {
        Vehicle vehicle = getVehicleType(tollRequest.getVehicle());
        if (isTollFreeVehicle(vehicle))
            return TollResponse.builder()
                .totalFees(BigDecimal.ZERO).vehicle(vehicle.getVehicleType()).build();

        List<TollEntry> tollEntries = tollRequest.getDates().stream()
                        .filter(date -> date.getYear() == 2013)
                        .sorted()
                        .map(date -> TollEntry.builder()
                                .entry(date)
                                .fee(getTollFee(date))
                                .build())
                        .collect(Collectors.toList());
        if (tollEntries.isEmpty()) {
            log.error("No toll entries found for the given dates");
            return TollResponse.builder()
                    .totalFees(BigDecimal.ZERO)
                    .vehicle(vehicle.getVehicleType())
                    .build();
        }

        BigDecimal totalFee = BigDecimal.ZERO;
        TollEntry startEntry = tollEntries.get(0);
        totalFee = calculateTotalFee(tollEntries);

        return TollResponse.builder()
                .totalFees(totalFee)
                .vehicle(vehicle.getVehicleType())
                .build();

    }

    public BigDecimal calculateTotalFee(List<TollEntry> tollEntries) {
        Map<LocalDate, List<TollEntry>> entriesByDate = tollEntries.stream()
                .collect(Collectors.groupingBy(entry -> entry.getEntry().toLocalDate()));

        BigDecimal totalFee = BigDecimal.ZERO;

        for (Map.Entry<LocalDate, List<TollEntry>> entry : entriesByDate.entrySet()) {
            List<TollEntry> dailyEntries = entry.getValue();
            BigDecimal dailyFee = calculateDailyFee(dailyEntries);
            totalFee = totalFee.add(dailyFee);
        }

        return totalFee;
    }

    private BigDecimal calculateDailyFee(List<TollEntry> dailyEntries) {
        BigDecimal dailyFee = BigDecimal.ZERO;
        TollEntry startEntry = dailyEntries.get(0);

        for (TollEntry current : dailyEntries) {
            if (startEntry.getEntry() != current.getEntry() && isWithinAnHour(startEntry.getEntry(), current.getEntry())) {
                log.info("Within an hour: {} - {}", startEntry.getEntry(), current.getEntry());
                dailyFee = dailyFee.subtract(BigDecimal.valueOf(startEntry.getFee()));
                startEntry.setFee(Math.max(startEntry.getFee(), current.getFee()));
            } else {
                log.info("Not within an hour: {} - {}", startEntry.getEntry(), current.getEntry());
                startEntry = current;
            }
            dailyFee = dailyFee.add(BigDecimal.valueOf(startEntry.getFee()));

            log.info("Running daily fee: {}", dailyFee);
        }

        if (dailyFee.compareTo(MAX_DAILY_FEE) > 0) {
            dailyFee = MAX_DAILY_FEE;
        }

        return dailyFee;
    }

    private Vehicle getVehicleType(String vehicleType) {
        switch (vehicleType) {
            case "Car":
                return new Car();
            case "Motorbike":
                return new Motorbike();
            case "Bus":
                return new Bus();
            case "Emergency":
                return new Emergency();
            case "Diplomat":
                return new Diplomat();
            case "Foreign":
                return new Foreign();
            case "Military":
                return new Military();
            default:
                return new Car();
        }
    }

    private boolean isWithinAnHour(LocalDateTime start, LocalDateTime end) {
        log.info("Start: {} End: {} Diff: {}", start, end, end.minusHours(1).isBefore(start));
        return end.minusHours(1).isBefore(start);
    }

    private boolean isTollFreeVehicle(Vehicle vehicle) {
        if (vehicle == null) return false;
        String vehicleType = vehicle.getVehicleType();
        return tollFreeVehicles.containsKey(vehicleType);
    }

    public int getTollFee(LocalDateTime date) {
        if (isTollFreeDate(date)) return 0;

        LocalTime time = date.toLocalTime();

        for (TollRangeCharges range : TollRangeCharges.values()) {
            if (range.isWithinRange(time)) {
                return range.getAmount();
            }
        }
        log.info("No toll fee for date: {}", date);
        return 0;
    }

    private Boolean isTollFreeDate(LocalDateTime date) {
        date = date.withHour(0).withMinute(0).withSecond(0).withNano(0);
        return (date.getDayOfWeek().getValue() == 6 || date.getDayOfWeek().getValue() == 7 || date.getMonthValue() == 7) ||
                (HOLIDAY_LIST.contains(date) || HOLIDAY_LIST.contains(date.plusDays(1)));
    }
}
