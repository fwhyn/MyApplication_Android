package com.fwhyn.myapplication.util.javatest;

import org.junit.Assert;
import org.junit.Test;

public class RandomAjaTest {

    RandomAja randomAja = new RandomAja();

    @Test
    public void testAja() {
        randomAja.createData();

        Assert.assertEquals(5, randomAja.data.value);
    }

}