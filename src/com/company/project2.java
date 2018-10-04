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
        double bias = 300;

//        hardActivation(0.75, alpha, allData, bias);
        softActivationMod(0.75, alpha, allData);
//        softActivation(0.75, alpha, allData, bias);
//        hardActivation(0.25, alpha, allData);
//        softActivation(0.25, alpha, allData);
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

    private static void hardActivation(double trainingData, double learning, List<Person> list, double bias) {

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


    public static void softActivationMod(double trainingData, double learning, List<Person> list){
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

//    public static void softActivation(double trainingData, double learning, List<Person> list, double bias) {
//
//        double net;
//        double weightMultiplier;
//        double[] weights = {1,2,1};
//        double[] weightChange = new double[3];
//        double error = 1;
//        double count = 0;
//        double k = 0.005;
//        double sum = 0.0;
//
//        while (error > 0.00005) {
//            if(count == 2){
//                System.out.println("not good enough");
//                break;
//            }
//            for (int i = 0; i < (list.size() * trainingData); i++) {
//                double out;
//                net = ((list.get(i).getHeight() * weights[0]) + (list.get(i).getWeight() * weights[1])
//                        + (bias * weights[2]));
//                System.out.println("net" + net);
//                out = 1 / ( 1 + Math.exp(net * -1 * k));
//                System.out.println("out: " + out);
//                weightMultiplier = learning * (list.get(i).getGender() - out);
//                weights[0] = list.get(i).getHeight() * weightMultiplier;
//                weights[1] = list.get(i).getWeight() * weightMultiplier;
//                weights[2] = bias * weightMultiplier;
//
//                error = errorCalc2(list, trainingData, weights, bias);
//                sum += Math.pow(error,2);
//                System.out.println(error);
//                sum += Math.pow(error,2);
//                for (int j = 0; j < weights.length; j++) {
//                    weights[j] = weights[j] - weightChange[j];
//                }
//
//            }
//            System.out.println("SUM " + sum);
//            count++;
//        }
//        System.out.println(weights[0]+weights[1]+weights[2]);
//    }
//
//    private static double errorCalculation(List<Person> list, double dataSize, double[] weights, double bias){
//        double error;
//        int out;
//        double correctCount = 0;
//        double incorrectCount = 0;
//        for (int i = (int) (list.size()*dataSize); i<list.size(); i++){
//
//            double net = ((list.get(i).getHeight() * weights[0]) + (list.get(i).getWeight() * weights[1])
//                    + bias * weights[2]);
//
//            if (net > 0) {
//                out = 1;
//            } else {
//                out = 0;
//            }
//
//            if(list.get(i).getGender() == out){
//                correctCount++;
//            }
//            else { incorrectCount++; }
//        }
//
//        error = incorrectCount/list.size();
//        //System.out.println("Error for weight: "+error);
//
//        return error;
//    }
//
//    private static double errorCalc2(List<Person> list, double dataSize, double[] weights, double bias){
//        double error = 0;
//        double correctCount = 0;
//        double incorrectCount = 0;
//
//
//        for (int i = (int) (list.size()*dataSize); i<list.size(); i++){
//
//            double net = ((list.get(i).getHeight() * weights[0]) + (list.get(i).getWeight() * weights[1])
//                    + (list.get(i).getGender() * weights[2]));
//
//            double out = (1 / ( 1 + Math.exp(net * -1* .005)));
//            double error1 = list.get(i).getGender() - out;
//            System.out.println("OUT " + out + "Desired " + list.get(i).getGender() + " =  "+ error1);
//            error+= error1;
//        }
//        return error;
//    }
}

class Person {

    public double height;
    public double weight;
    public int gender;

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
