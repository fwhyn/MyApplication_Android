package com.fwhyn.myapplication.util.javatest;

import java.util.Date;

public class CurrentDateRunnable implements Runnable {
    @Override
    public void run() {
        while (true) {
            System.out.println("Current date: " + new Date());

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
