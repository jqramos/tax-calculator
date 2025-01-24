package com.tax.taxcalculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.tax")
public class TaxCalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaxCalculatorApplication.class, args);
    }

}
