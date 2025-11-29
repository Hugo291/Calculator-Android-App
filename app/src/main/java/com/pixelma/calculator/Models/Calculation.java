package com.pixelma.calculator.Models;

import java.util.Random;

public class Calculation {

    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String MULTIPLICATION = "*";
    public static final String DIVIDE = "÷";

    private static final int MAX_GENERATION_ATTEMPTS = 100;
    private static final int[] LEVEL_THRESHOLDS = {10, 20, 35, 60, 80, 100};

    private final int gameOperator;
    private final String[] operations = {PLUS, MINUS, MULTIPLICATION, DIVIDE};
    private final Random random = new Random();

    private int val1;
    private int val2;
    private int operator;
    private int result;
    private int maxValue;
    private int maxOperator;
    private int minValue = 1;
    private int testLevel = 0;

    public Calculation(int level, int activityOperator) {
        this.gameOperator = activityOperator;
        configureLevelParameters(level);
        generateValidCalculation();
    }

    /**
     * Configure level parameters based on current level
     */
    private void configureLevelParameters(int level) {
        if (level < LEVEL_THRESHOLDS[0]) {
            maxValue = 15;
            setMaxOperator(3);
            testLevel = 0;
        } else if (level < LEVEL_THRESHOLDS[1]) {
            maxValue = 50;
            minValue = 26;
            setMaxOperator(2);
            testLevel = 1;
        } else if (level < LEVEL_THRESHOLDS[2]) {
            maxValue = 100;
            minValue = 40;
            setMaxOperator(3);
            testLevel = 2;
        } else if (level < LEVEL_THRESHOLDS[3]) {
            maxValue = 120;
            minValue = 20;
            setMaxOperator(3);
            testLevel = 3;
        } else if (level < LEVEL_THRESHOLDS[4]) {
            maxValue = 150;
            minValue = 20;
            setMaxOperator(3);
            testLevel = 4;
        } else if (level < LEVEL_THRESHOLDS[5]) {
            maxValue = 200;
            minValue = 20;
            setMaxOperator(3);
            testLevel = 5;
        } else {
            maxValue = 300;
            minValue = 20;
            setMaxOperator(3);
            testLevel = 7;
        }
    }

    /**
     * Generate a valid calculation without recursion
     * Tries MAX_GENERATION_ATTEMPTS times before using fallback
     */
    private void generateValidCalculation() {
        operator = (gameOperator >= 0) ? gameOperator : random.nextInt(maxOperator + 1);

        int attempts = 0;
        boolean isValid = false;

        while (!isValid && attempts < MAX_GENERATION_ATTEMPTS) {
            val1 = getRandomValue();
            val2 = getRandomValue();
            result = calculateResult();

            isValid = isValidCalculation();
            attempts++;
        }

        // Fallback to safe values if generation failed
        if (!isValid) {
            generateFallbackCalculation();
        }
    }

    /**
     * Calculate result based on operator
     */
    private int calculateResult() {
        switch (operations[operator]) {
            case PLUS:
                return val1 + val2;
            case MINUS:
                return val1 - val2;
            case MULTIPLICATION:
                return val1 * val2;
            case DIVIDE:
                return (val2 != 0) ? val1 / val2 : -1;
            default:
                return -1;
        }
    }

    /**
     * Check if the current calculation is valid
     */
    private boolean isValidCalculation() {
        // Result must be positive
        if (result < 0) return false;

        // Result must not exceed maximum
        if (result > getMaxResult()) return false;

        // For division, check that it's exact
        if (operations[operator].equals(DIVIDE)) {
            if (val2 == 0) return false;
            if (val1 % val2 != 0) return false;
            if (val1 == val2) return false;
            if (val1 == 1 || val2 == 1) return false;
        }

        return true;
    }

    /**
     * Generate a safe fallback calculation
     */
    private void generateFallbackCalculation() {
        switch (operations[operator]) {
            case PLUS:
                val1 = 5;
                val2 = 3;
                break;
            case MINUS:
                val1 = 10;
                val2 = 3;
                break;
            case MULTIPLICATION:
                val1 = 5;
                val2 = 2;
                break;
            case DIVIDE:
                val1 = 10;
                val2 = 2;
                break;
        }
        result = calculateResult();
    }

    /**
     * Get random value within configured range
     */
    private int getRandomValue() {
        return random.nextInt((maxValue - minValue) + 1) + minValue;
    }

    // Getters
    public String getCalculationString() {
        return val1 + " " + operations[operator] + " " + val2;
    }

    public String getOperator() {
        return operations[operator];
    }

    public int getResult() {
        return result;
    }

    public int getMaxResult() {
        return maxValue * 5;
    }

    public int getVal1() {
        return val1;
    }

    public int getVal2() {
        return val2;
    }

    public int getTestLevel() {
        return testLevel;
    }

    public int[] getLEVEL() {
        return LEVEL_THRESHOLDS;
    }

    private void setMaxOperator(int operator) {
        if (gameOperator == -1) {
            this.maxOperator = operator;
        }
    }
}