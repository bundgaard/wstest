package org.tretton63.wstest.service;

import org.springframework.stereotype.Service;

@Service
public class CalculatorService {

    public double add(double a, double b) {
        return a + b;
    }

    public double sub(double a, double b) {
        return a - b;
    }
}
