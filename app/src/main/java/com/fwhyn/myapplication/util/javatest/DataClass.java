package com.fwhyn.myapplication.util.javatest;

import androidx.annotation.NonNull;

public class DataClass implements Cloneable {
    int value;

    @NonNull
    @Override
    public DataClass clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (DataClass) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
