package com.company;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class project2 {

    public static void main(String[] args) throws IOException {
        List<Person> allData = readData();
        double alpha = 0.3;

        hardActivation(0.75, alpha, allData);
        softActivation(0.75, alpha, allData);
        hardActivation(0.25, alpha, allData);
        softActivation(0.25, alpha, allData);
    }

    private static List<Person> readData() throws IOException {
        ArrayList<Person> allDataPoints = new ArrayList<>();
        File dataFile = new File("./src/Data/data.csv");
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
        double globalError;
        double out;
        int count = 0;

        do {
            count++;
            globalError = 0;

            for (int i = 0; i < (list.size() * trainingData); i++) {

                net = list.get(i).getHeight()*weights[0] + list.get(i).getWeight()*weights[1] + weights[2];

                if (net >= 0){
                    out = 1;
                } else { out = 0; }

                weightMultiplier = learning * (list.get(i).getGender() - out);
                weights[0] += list.get(i).getHeight() * weightMultiplier * list.get(i).height;
                weights[1] += list.get(i).getWeight() * weightMultiplier * list.get(i).weight;
                weights[2] += weightMultiplier;

                globalError += Math.pow( (list.get(i).getGender() - out), 2);
            }

            System.out.println("Iterations: "+count+" : Global Error = "+globalError+" : RootMean = "+Math.sqrt(globalError/list.size()));
        } while ( globalError>0.00005 && count<=100);

        System.out.println("\n=====\n Decision equations");
        System.out.println(weights[0]+"*height + "+weights[1]+"*weight "+weights[2]+" = 0");
    }


    private static void softActivation(double trainingData, double learning, List<Person> list){
        double net;
        double weightMultiplier;
        double[] weights = {1,2,-1};
        double globalError;
        double out;
        int count = 0;

        do {
            count++;
            globalError = 0;

            for (int i = 0; i < (list.size() * trainingData); i++) {

                net = list.get(i).getHeight()*weights[0] + list.get(i).getWeight()*weights[1] + weights[2];

                out = 1/(1 + Math.exp(net));

                weightMultiplier = learning * (list.get(i).getGender() - out);
                weights[0] += list.get(i).getHeight() * weightMultiplier * list.get(i).height;
                weights[1] += list.get(i).getWeight() * weightMultiplier * list.get(i).weight;
                weights[2] += weightMultiplier;

                globalError += Math.pow( (list.get(i).getGender() - out), 2);

            }
            System.out.println("Iterations: "+count+" : Global Error = "+globalError+" : RootMean = "+Math.sqrt(globalError/list.size()));
        }while ( globalError>0.00005 && count<=100);

        System.out.println("\n=====\nDecision equation:");
        System.out.println(weights[0]+"*height + "+weights[1]+"*weight "+weights[2]+" = 0");
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
