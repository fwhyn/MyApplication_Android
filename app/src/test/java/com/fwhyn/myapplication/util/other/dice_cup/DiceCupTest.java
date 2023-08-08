package com.fwhyn.myapplication.util.other.dice_cup;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiceCupTest {

    @Test
    public void testGetCupList() {
        int total = 4;
        ArrayList<Cup> cups = DiceCup.getCupList(total);

        // test total
        Assert.assertEquals(total, cups.size());

        // first cup value
        for (int i = 1; i <= cups.size(); i++) {
            Cup cup = cups.get(i - 1);

            Assert.assertFalse(cup.isDiceExist());
            Assert.assertEquals(i, cup.getCupId());
        }
    }

    @Test
    public void testSetCupPosition() {
        int total = 4;
        ArrayList<Cup> cups = DiceCup.getCupList(total);

        // size remains the same
        ArrayList<Cup> cupsResult = DiceCup.setCupPosition(cups, 1, 3);
        Assert.assertEquals(total, cupsResult.size());

        // moved to right position
        // b 1234
        // a 2341

        cupsResult = DiceCup.setCupPosition(cups, 1, 4);
        Assert.assertEquals(2, cupsResult.get(0).getCupId());
        Assert.assertEquals(3, cupsResult.get(1).getCupId());
        Assert.assertEquals(4, cupsResult.get(2).getCupId());
        Assert.assertEquals(1, cupsResult.get(3).getCupId());


        // moved to right position
        // b 1234
        // a 2314

        cupsResult = DiceCup.setCupPosition(cups, 1, 3);
        Assert.assertEquals(2, cupsResult.get(0).getCupId());
        Assert.assertEquals(3, cupsResult.get(1).getCupId());
        Assert.assertEquals(1, cupsResult.get(2).getCupId());
        Assert.assertEquals(4, cupsResult.get(3).getCupId());
    }

    @Test
    public void testSetCupPositions() {
        int total = 4;
        ArrayList<Cup> cups = DiceCup.getCupList(total);

        ArrayList<List<Integer>> positions = new ArrayList<>();

        positions.add(Arrays.asList(1, 3));
        // b 1234
        // a 2314

        positions.add(Arrays.asList(4, 2));
        // b 2314
        // a 2431

        ArrayList<Cup> cupsResult = DiceCup.setCupPositions(cups, positions);
        Assert.assertEquals(2, cupsResult.get(0).getCupId());
        Assert.assertEquals(4, cupsResult.get(1).getCupId());
        Assert.assertEquals(3, cupsResult.get(2).getCupId());
        Assert.assertEquals(1, cupsResult.get(3).getCupId());
    }

    @Test
    public void testTebakCangkir() {
        int total = 4;
        int totalTransition = 3;
        int fistDicePosition = 3;

        ArrayList<List<Integer>> positions = new ArrayList<>();

        positions.add(Arrays.asList(1, 3));
        // b 1234
        // a 2314

        positions.add(Arrays.asList(4, 2));
        // b 2314
        // a 2431

        positions.add(Arrays.asList(3, 1));
        // b 2431
        // a 3241

        int position = DiceCup.tebakCangkir(total, totalTransition, fistDicePosition, positions);
        Assert.assertEquals(1, position);

        // ----------------------------------------------------------------
        int total1 = 4;
        int totalTransition1 = 2;
        int fistDicePosition1 = 3;

        ArrayList<List<Integer>> positions1 = new ArrayList<>();

        positions1.add(Arrays.asList(1, 3));
        // b 1234
        // a 2314

        positions1.add(Arrays.asList(4, 2));
        // b 2314
        // a 2431

        int position1 = DiceCup.tebakCangkir(total1, totalTransition1, fistDicePosition1, positions1);
        Assert.assertEquals(3, position1);

        // ----------------------------------------------------------------
//        4 4 3
//        1 2
//        1 3
//        3 2
//        2 3

        int total2 = 4;
        int totalTransition2 = 2;
        int fistDicePosition2 = 3;

        ArrayList<List<Integer>> positions2 = new ArrayList<>();

        positions2.add(Arrays.asList(1, 2));
        positions2.add(Arrays.asList(1, 3));
        positions2.add(Arrays.asList(3, 2));
        positions2.add(Arrays.asList(2, 3));

        int position2 = DiceCup.tebakCangkir(total2, totalTransition2, fistDicePosition2, positions2);
        Assert.assertEquals(2, position2);
    }
}