package com.pixelma.calculator.Models;

import java.util.Random;

public class Calculation {

    private static final String TAG = "TAG_Calculation";

    public final static String PLUS = "+";
    public final static String MINUS = "-";
    public final static String MULTIPLICATION = "*";
    public final static String DIVIDE = "÷";

    private int gameOperator = -1;

    private int val1;
    private int val2;
    private int operator;
    private int result;

    private final String[] operations = new String[]{PLUS, MINUS, MULTIPLICATION, DIVIDE};

    public int maxValue;
    public int maxResult;
    public int minValue = 1;

    public int maxOperator;

    private int[] LEVEL = new int[]{10, 20, 35, 60, 80, 100};
    private int testLevel = 0;

    public Calculation(int level, int activityOperator) {

        this.gameOperator = activityOperator;

        if (level < LEVEL[0]) {
            maxValue = 15;
            setMaxOperator(3);
            this.testLevel = 0;

        } else if (level < LEVEL[1]) {
            maxValue = 50;
            minValue = 26;
            setMaxOperator(2);
            this.testLevel = 1;

        } else if (level < LEVEL[2]) {
            maxValue = 100;
            minValue = 40;
            setMaxOperator(3);
            this.testLevel = 2;

        } else if (level < LEVEL[3]) {
            maxValue = 120;
            minValue = 20;
            setMaxOperator(3);
            this.testLevel = 3;

        } else if (level < LEVEL[4]) {
            maxValue = 150;
            minValue = 20;
            setMaxOperator(3);
            this.testLevel = 4;

        } else if (level < LEVEL[5]) {
            maxValue = 200;
            minValue = 20;
            setMaxOperator(3);
            this.testLevel = 5;

        } else {
            maxValue = 300;
            minValue = 20;
            setMaxOperator(3);
            this.testLevel = 7;
        }

        if (this.gameOperator > 0) {
            this.create(gameOperator);
            return;
        }

        this.create();
    }

    public int[] getLEVEL() {
        return LEVEL;
    }

    public String getCalculationString() {
        return this.val1 + " " + operations[operator] + " " + this.val2;
    }

    private void create() {
        this.setVal1(getRandomValue());
        this.setVal2(getRandomValue());
        this.operator = getRandom(maxOperator);
        this.getResult();
    }

    private void create(int force) {
        this.setVal1(getRandomValue());
        this.setVal2(getRandomValue());
        this.operator = force;
        this.getResult();
    }

    private int getRandom(int max) {
        Random rand = new Random();
        return rand.nextInt((max) + 1);
    }

    private int getRandomValue() {
        Random rand = new Random();
        return rand.nextInt((maxValue - minValue) + 1) + minValue;
    }

    public void setMaxOperator(int operator) {
        if (gameOperator == -1) {
            this.maxOperator = operator;
        }
    }

    public String getOperator() {
        return operations[operator];
    }

    public int getResult() {
        int result = -1;

        switch (operations[operator]) {
            case PLUS:
                result = val1 + val2;
                break;
            case MINUS:
                result = val1 - val2;
                break;
            case MULTIPLICATION:
                result = val1 * val2;
                break;
            case DIVIDE:
                result = val1 / val2;
                break;
            default:
                break;
        }

        //result can't be negative and not be a int
        if (result < 0 ||
                (operations[operator].equals(DIVIDE) && val1 % val2 != 0))
            create(operator);


        //result must be less than result
        if (getMaxResult() < result)
            create(operator);


        //if is division
        if (operations[operator].equals(DIVIDE)) {

            if (val1 == val2)
                create(operator);

            if (val1 == 1 || val2 == 1)
                create(operator);

        }


        return result;
    }

    public int getMaxResult() {
        return this.maxValue * 5;
    }

    public int getVal1() {
        return val1;
    }

    public void setVal1(int val1) {
        this.val1 = val1;
    }

    public int getVal2() {
        return val2;
    }

    public void setVal2(int val2) {
        this.val2 = val2;
    }

    public int getTestLevel() {
        return testLevel;
    }

}
