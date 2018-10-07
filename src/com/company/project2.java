package com.company;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class project2 {

    public static void main(String[] args) throws IOException {
        List<Person> allData = readData();
        double alpha = 0.3;
        Collections.shuffle(allData);
        System.out.println("------------ HARD ACTIVATION 0.75 -----------");
        hardActivation(0.75, alpha, allData);
        System.out.println("\n\n------------ SOFT ACTIVATION 0.75 -----------\n\n");
        softActivation(0.75, alpha, allData );
        System.out.println("\n\n------------ HARD ACTIVATION 0.25 -----------\n\n");
        hardActivation(0.25, alpha, allData);
        System.out.println("\n\n------------ SOFT ACTIVATION 0.25 -----------\n\n");
        softActivation(0.25, alpha, allData);
    }

    private static List<Person> readData() throws IOException {
        ArrayList<Person> allDataPoints = new ArrayList<>();
        File dataFile = new File("/Users/vishakha/Documents/Projects/AI-Project2-409/src/Data/data.csv");
        Scanner scanFile = new Scanner(dataFile);

        while (scanFile.hasNextLine()) {
            String dataPoint = scanFile.nextLine();
            String[] data = dataPoint.split(",");
            allDataPoints.add(new Person(Double.parseDouble(data[0]), Double.parseDouble(data[1]),
                    Integer.parseInt(data[2].trim())));
        }
        scanFile.close();
        return allDataPoints;
    }

    private static void hardActivation(double trainingData, double learning, List<Person> list) {

        double net;
        double weightMultiplier;
        double[] weights = {1,2,-1};
        double totalError;
        double out;
        int count = 0;
        double testTotalError = 0;
        double testOut;
        double incorrect = 0;

        do {
            count++;
            totalError = 0;

            for (int i = 0; i < (list.size() * trainingData); i++) {

                net = list.get(i).getHeight()*weights[0] + list.get(i).getWeight()*weights[1] + weights[2];

                if (net >= 0){
                    out = 1;
                } else { out = 0; }

                weightMultiplier = learning * (list.get(i).getGender() - out);
                weights[0] +=  weightMultiplier * list.get(i).height;
                weights[1] +=  weightMultiplier * list.get(i).weight;
                weights[2] += weightMultiplier;

                totalError += Math.pow( (list.get(i).getGender() - out), 2);
            }

//            System.out.println("Iterations: "+count+" : Total Error = "+totalError+" : RootMeanError = "+Math.sqrt(totalError/list.size()));
        } while ( totalError>0.00005 && count<1000);
//
        System.out.println("\n=====\nDecision equation");
        System.out.println(weights[0]+"*height + "+weights[1]+"*weight "+weights[2]+" = 0");

//        System.out.println("\n=====\n ");

        for (double j=(list.size()*(trainingData)); j<(list.size()); j++){
            net = list.get((int)j).getHeight()*weights[0] + list.get((int)j).getWeight()*weights[1] + weights[2];

            if (net >= 0){
                testOut = 1;
            } else { testOut = 0; }

            if (list.get((int)j).getGender() != testOut){
                incorrect++;
            }

            testTotalError += Math.pow( (list.get((int)j).getGender() - testOut), 2);
//            System.out.println("Test: "+j+1+" : Total Error = "+testTotalError+" : RootMeanError = "+Math.sqrt(testTotalError/(list.size() - (list.size()*(trainingData)))));
        }
//        System.out.println("\n=====\n ");
        System.out.println("Percent incorrect: "+(incorrect/(list.size()*(1 - trainingData)))*100);


    }


    private static void softActivation(double trainingData, double learning, List<Person> list){
        double net;
        double weightMultiplier;
        double[] weights = {798761,-30242,138151};
        double globalError;
        double out;
        double k = 0.005;
        int count = 0;
        double testTotalError = 0;
        double testOut;
        double incorrect = 0;

        do {
            count++;
            globalError = 0;

            for (int i = 0; i < (list.size() * trainingData); i++) {

                net = list.get(i).getHeight()*weights[0] + list.get(i).getWeight()*weights[1] + weights[2];

                out = 1/(1 + Math.exp(net * -1 * k));

                weightMultiplier = learning * (list.get(i).getGender() - out);
                weights[0] +=  weightMultiplier * list.get(i).height;
                weights[1] +=  weightMultiplier * list.get(i).weight;
                weights[2] += weightMultiplier;

                globalError += Math.pow( (list.get(i).getGender() - out), 2);

            }
//            System.out.println("Iterations: "+count+" : Total Error = "+globalError+" : RootMean = "+Math.sqrt(globalError/list.size()));
        }while ( globalError>0.00005 && count<1000);

        System.out.println("\n=====\nDecision equation:");
        System.out.println(weights[0]+"*height + "+weights[1]+"*weight "+weights[2]+" = 0");

        for (double j=(list.size()*(trainingData)); j<(list.size()); j++){
            net = list.get((int)j).getHeight()*weights[0] + list.get((int)j).getWeight()*weights[1] + weights[2];

            testOut = 1/(1 + Math.exp(net * -1 * k));

            if (list.get((int)j).getGender() != Math.rint(testOut)){
                incorrect++;
            }
            testTotalError += Math.pow( (list.get((int)j).getGender() - testOut), 2);
//            System.out.println("Test: "+j+1+" : Total Error = "+testTotalError+" : RootMeanError = "+Math.sqrt(testTotalError/(list.size() - (list.size()*(trainingData)))));
        }
//        System.out.println("\n=====\n ");
        System.out.println("Percent incorrect: "+(incorrect/(list.size()*(1 - trainingData)))*100);
    }
}

class Person {

    double height;
    double weight;
    private int gender;

    Person(double height, double weight, int gender) {
        this.height = height;
        this.weight = weight;
        this.gender = gender;
    }

    double getHeight() {
        return this.height;
    }

    double getWeight() {
        return this.weight;
    }

    int getGender() {
        return this.gender;
    }
}
