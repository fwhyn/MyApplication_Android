package com.fwhyn.myapplication.util.javatest;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class RandomAjaTest {

    RandomAja randomAja = new RandomAja();

    @Test
    public void testAja() {
        randomAja.createData();

        Assert.assertEquals(5, randomAja.data.value);
    }

    @Test
    public void maxSum() {
        List<List<Integer>> list = new LinkedList<>();
        list.add(Arrays.asList(4, 8, 3));
        list.add(Arrays.asList(5, 8, 7));
        list.add(Arrays.asList(4, 9, 1));
        list.add(Arrays.asList(4, 5, 8));
        list.add(Arrays.asList(8, 9, 10));

        Assert.assertEquals(21, RandomAja.arrayManipulation(10, list));
    }
}