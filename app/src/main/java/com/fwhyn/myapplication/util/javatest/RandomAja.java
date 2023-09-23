package com.fwhyn.myapplication.util.javatest;

import java.util.List;

public class RandomAja {

    DataClass data = new DataClass();

    void callRunnable() {
        Thread thread = new Thread(new CurrentDateRunnable());
        thread.start();
    }

//    int testAja() {
//        Square square = x -> x*x;
//        return square.calculate(5);
//    }

    void createData() {
        data.value = 4;

        callData(data.clone());
    }

    void callData(DataClass data) {
        data.value = 5;
    }

    public static long arrayManipulation(int n, List<List<Integer>> queries) {
        // Write your code here
        // long[] elements = new long[n];

        int a = 0;
        int b = 0;
        int k = 0;
        long max = 0;

        // int querySize = queries.size();
        // List<Integer> query = null;

        // for (int i = 0; i < querySize; i++){
        // // for (List<Integer> query : queries) {
        //     query = queries.get(i);
        //     a = query.get(0);
        //     b = query.get(1);
        //     k = query.get(2);

        //     for (int j = a - 1 ; j < b; j++) {
        //         elements[j] += k;
        //     }
        // }

        // for (long element : elements) {
        //     max = Math.max(element, max);
        // }

        long[] elements = new long[n];
        for (List<Integer> query : queries) {
            a = query.get(0);
            b = query.get(1);
            k = query.get(2);
            elements[a - 1] += k;
            elements[b] -= k;
        }

        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum += elements[i];
            max = Math.max(sum, max);
        }

        return max;

    }
}
