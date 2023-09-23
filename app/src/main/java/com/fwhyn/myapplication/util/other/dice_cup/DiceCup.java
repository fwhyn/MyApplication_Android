package com.fwhyn.myapplication.util.other.dice_cup;

import java.util.ArrayList;
import java.util.List;

public class DiceCup {

    public static final int N_MIN = 1;
    public static final int N_MAX = 1000;
    public static final int T_MIN = 1;
    public static final int T_MAX = 100000;

    /*
     * Complete the 'tebakCangkir' function below.
     *
     * The function is expected to return an INTEGER.
     * The function accepts following parameters:
     *  1. INTEGER N total cups
     *  2. INTEGER T total transition
     *  3. INTEGER P first dice position
     *  4. 2D_INTEGER_ARRAY posisi
     */

    public static int tebakCangkir(int N, int T, int P, List<List<Integer>> posisi) {

        int totalCups = N;
        int totalTransition = T;
        int firstDicePosition = P;

        checkError(totalCups, totalTransition, firstDicePosition);

        ArrayList<Cup> cups = getCupList(totalCups);
        cups.get(firstDicePosition - 1).setDiceExist(true);

        // transition
        int i;
        cups = setCupPositions(cups, posisi);
        for (i = 0; i < cups.size(); i++) {
            if (cups.get(i).isDiceExist()) {
                break;
            }
        }

        return i + 1;
    }

    static ArrayList<Cup> setCupPositions(ArrayList<Cup> cups, List<List<Integer>> positions) {
        int oldPosition = 0;
        int newPosition = 0;
        ArrayList<Cup> tempCups = (ArrayList) cups.clone();

        for (List<Integer> position : positions) {
            oldPosition = position.get(0);
            newPosition = position.get(1);

            tempCups = setCupPosition(tempCups, oldPosition, newPosition);
        }

        return tempCups;
    }

    static ArrayList<Cup> setCupPosition(ArrayList<Cup> cups, int oldPosition, int newPosition) {
        int oldPositionIndex = oldPosition - 1;
        int newPositionIndex = newPosition - 1;

        ArrayList<Cup> tempCups = (ArrayList) cups.clone();
        Cup currentCup = tempCups.get(oldPositionIndex);

        tempCups.remove(oldPositionIndex);
        tempCups.add(newPositionIndex, currentCup);

        return tempCups;
    }

    static void checkError(int totalCups, int totalTransition, int firstDicePosition) {
        if (isTotalCupsOutOfRange(totalCups)) {
            throw new RuntimeException("total cups is out of range");
        }

        if (isTransitionOutOfRange(totalTransition)) {
            throw new RuntimeException("total transition is out of range");
        }

        if (isPositionOutOfRange(firstDicePosition)) {
            throw new RuntimeException("dice position is invalid");
        }
    }

    static ArrayList<Cup> getCupList(int totalCups) {
        ArrayList<Cup> cups = new ArrayList<>();
        for (int i = 1; i <= totalCups; i++) {
            cups.add(new Cup(i));
        }

        return cups;
    }

    static boolean isTotalCupsOutOfRange(int input) {
        return isOutOfRange(input, N_MIN, N_MAX);
    }

    static boolean isTransitionOutOfRange(int input) {
        return isOutOfRange(input, T_MIN, T_MAX);
    }

    static boolean isPositionOutOfRange(int input) {
        return isOutOfRange(input, N_MIN, N_MAX);
    }

    static boolean isOutOfRange(int input, int min, int max) {
        return !(min <= input && input <= max);
    }
}