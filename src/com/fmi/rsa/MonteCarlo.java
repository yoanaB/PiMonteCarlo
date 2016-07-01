package com.fmi.rsa;

import java.util.ArrayList;
import java.util.concurrent.*;

public class MonteCarlo {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();
        long totalPointsToCalculate = 0;
        int threads = 0;
        boolean isQuietMode = false;

        // Parse command line arguments.
        if (args.length < 4) {
            displayWrongArgumentsError();
            return;
        } else {
            if (args[0].equals("-s") && args[2].equals("-t")) {
                try {
                    totalPointsToCalculate = Long.parseLong(args[1]);
                    threads = Integer.parseInt(args[3]);
                } catch (Exception e) {
                    displayWrongArgumentsError();
                    return;
                }
            }
            if (args.length == 5 && args[4].equals("-q")) {
                isQuietMode = true;
            }
        }

        // Calculate pi using the given arguments.
        long pointsToCalculate = totalPointsToCalculate / threads;
        double pi = calculatePi(pointsToCalculate, threads, isQuietMode);
        System.out.println("Calculated Pi value: " + pi);
        long stopTime = System.currentTimeMillis();
        System.out.println(String.format("Total execution time for current run: %d ms", stopTime - startTime));
        System.exit(0);
    }

    private static void displayWrongArgumentsError() {
        System.out.println("Arguments are invalid!");
        System.out.println("To run use: java -jar PiMonteCarlo.jar -s {points} -t {threads} [-q]");
    }

    private static double calculatePi(long pointsToCalculate, int threads, boolean isQuietMode)
            throws InterruptedException, ExecutionException {

        ArrayList<Future<Double>> values = new ArrayList<Future<Double>>();
        ExecutorService exec = Executors.newFixedThreadPool(threads);

        // Start the given number of threads.
        for (int i = 0; i < threads; i++) {
            Callable<Double> callable = new PiValue(pointsToCalculate, isQuietMode, i + 1);
            Future<Double> future = exec.submit(callable);
            values.add(future);
        }

        // Sum the results for Pi from all the threads.
        Double sum = 0.0;
        for (Future<Double> f : values) {
            sum += f.get();
        }

        // The final result is the arithmetic mean of the results from all the threads.
        double pi = 4 * sum / pointsToCalculate;
        return pi;
    }
}
