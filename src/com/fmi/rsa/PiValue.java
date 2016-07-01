package com.fmi.rsa;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

public class PiValue implements Callable {

    private double pi;
    private double x;
    private double y;
    private long insidePoints;
    private long pointsToCalculate;
    private boolean isQuietMode;
    private int threadNumber;

    public PiValue(long pointsToCalculate, boolean isQuietMode, int threadNumber) {
        this.pointsToCalculate = pointsToCalculate;
        this.isQuietMode = isQuietMode;
        this.threadNumber = threadNumber;
    }

    public Long call() {
        if(!isQuietMode) {
            System.out.println(String.format("Thread %d started!", threadNumber));
        }
        long startTime = System.currentTimeMillis();
        for (double i = 0; i < pointsToCalculate; i++) {
            // get random x and y coordinates for a point
            x = ThreadLocalRandom.current().nextDouble();
            y = ThreadLocalRandom.current().nextDouble();
            // check if point is inside the circle
            if (x * x + y * y <= 1) {
                insidePoints++;
            }
        }
        // calculate the result for the current thread
        //pi = 4 * (double) insidePoints / pointsToCalculate;
        long stopTime = System.currentTimeMillis();
        if(!isQuietMode) {
            System.out.println(String.format("Thread %d finished!", threadNumber));
            System.out.println(String.format("Thread %d execution time was: %d ms.", threadNumber, stopTime - startTime));
        }
        return insidePoints;
    }
}