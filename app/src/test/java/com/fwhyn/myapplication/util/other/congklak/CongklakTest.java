package com.fwhyn.myapplication.util.other.congklak;

import org.junit.Assert;
import org.junit.Test;

public class CongklakTest {
    private final Congklak congklak = new Congklak();

    @Test
    public void testInputLength() {
        int[] output = congklak.stringToIntsOrNull("9 2 4 6 7 9 8 5");
        Assert.assertTrue(congklak.isInputLengthCorrect(output, Congklak.INPUT_LENGTH));

        output = congklak.stringToIntsOrNull("9 2 4 6 7 9 8 5 8");
        Assert.assertFalse(congklak.isInputLengthCorrect(output, Congklak.INPUT_LENGTH));
    }

    @Test
    public void testStringToInts() {
        int[] output = congklak.stringToIntsOrNull("9 2 4 6 7 9 8 5");
        Assert.assertEquals(9, output[0]);
        Assert.assertEquals(6, output[3]);
        Assert.assertEquals(5, output[7]);
    }

    @Test
    public void testStringToIntsWrongInputFormat() {
        int[] output = congklak.stringToIntsOrNull("9,2 4 6 7 9 8 5");
        Assert.assertNull(output);
    }

    @Test
    public void testStringToIntsInputOutOfRange() {
        int[] output = congklak.stringToIntsOrNull("1000001");
        Assert.assertNull(output);

        output = congklak.stringToIntsOrNull("-20");
        Assert.assertNull(output);
    }

    @Test
    public void testStringToArray() {
        String[] output = congklak.stringToArray("1 2 4");
        Assert.assertEquals("1", output[0]);
        Assert.assertEquals("2", output[1]);
        Assert.assertEquals("4", output[2]);
    }

    @Test
    public void testIsOutOfRange() {
        Assert.assertTrue(congklak.isOutOfRange(Congklak.INPUT_MAX + 1));
        Assert.assertTrue(congklak.isOutOfRange(Congklak.INPUT_MIN - 1));

        Assert.assertFalse(congklak.isOutOfRange(Congklak.INPUT_MAX));
        Assert.assertFalse(congklak.isOutOfRange(Congklak.INPUT_MAX - 1));
        Assert.assertFalse(congklak.isOutOfRange(Congklak.INPUT_MIN));
        Assert.assertFalse(congklak.isOutOfRange(Congklak.INPUT_MIN + 1));
    }
}