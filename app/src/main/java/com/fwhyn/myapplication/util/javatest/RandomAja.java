package com.fwhyn.myapplication.util.javatest;

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
}
