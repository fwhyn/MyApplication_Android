package com.fwhyn.myapplication.util.other.congklak;

import android.util.Log;

public class Congklak {

    public final static int INPUT_LENGTH = 8;
    public final static int INPUT_MIN = 1;
    public final static int INPUT_MAX = 1000000;

    final static String TAG = "aaa";
    final static String REGEX = " ";

    public int congklak(String B, String A) {
        int[] arrayA = stringToIntsOrNull(A);
        int[] arrayB = stringToIntsOrNull(B);


        int[] arrayARef = arrayA.clone();
        int rock = 0;
//        for (int i = 0; i < length; i++) {
//            rock = arrayARef[i];
//            int execArray[];
//
//                for (int j = i; j >= 0 ; j--) {
//
//                    for (int j = i; j < length; j++) {
//
//                    }
//
//                    for (int l = length ; k < 0 ; k--) {
//
//                    }
//                }
//
//
//        }

        return 2;
    }

    boolean isNotError(int[] inputA, int[] inputB) {
        if (isCongklakInputLengthCorrect(inputA, inputB)) {
            Log.e(TAG, "input tiap baris harus " + INPUT_LENGTH + " angka");

            return false;
        }

        return false;
    }

    boolean isCongklakInputLengthCorrect(int[] inputA, int[] inputB) {
        return (isInputLengthCorrect(inputA, INPUT_LENGTH) &&
                isInputLengthCorrect(inputB, INPUT_LENGTH)
        );
    }

    boolean isInputLengthCorrect(int[] input, int targetLength) {
        return input.length == targetLength;
    }

    int[] stringToIntsOrNull(String input) {
        String[] strings = stringToArray(input);
        int length = strings.length;

        int[] intArray = new int[length];
        for (int i = 0; i < length; i++) {
            try {
                intArray[i] = Integer.parseInt(strings[i]);
                if (isOutOfRange(intArray[i])) {
                    Log.e(TAG, "input di luar range");

                    return null;
                }

            } catch (NumberFormatException e) {
                Log.e(TAG, "input tidak sesuai format");

                return null;
            }
        }

        return intArray;
    }

    String[] stringToArray(String input) {
        return input.split(REGEX);
    }

    boolean isOutOfRange(int input) {
        return !(INPUT_MIN <= input && input <= INPUT_MAX);
    }
}
