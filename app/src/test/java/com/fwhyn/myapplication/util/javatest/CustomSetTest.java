package com.fwhyn.myapplication.util.javatest;

import com.fwhyn.myapplication.util.javatest.CustomSet;

import org.junit.Assert;
import org.junit.Test;

public class CustomSetTest {

    @Test
    public void isSame() {
        Assert.assertTrue(new CustomSet().isSame(1, 1));
        Assert.assertTrue(new CustomSet().isSame("aaaa", "aaaa"));
        Assert.assertTrue(new CustomSet().isSame());
    }
}