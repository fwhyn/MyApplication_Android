package com.fwhyn.myapplication.javatest;

import java.util.ArrayList;
import java.util.List;

public class CustomSet {
    Object[] object = new Object[4];

    public void add (Object value) {
        object[0] = value;
    }

    boolean isSame(int a, int b) {
        return (a == b);
    }

    boolean isSame(String a, String b) {
        return (a == b);
    }

    boolean isSame() {
        String a = "Test";
        String b = new String(a);

        return b.equals(a);
    }

    void test() {
        List<String> a = new ArrayList<>();
        a.add("a");

    }
}
