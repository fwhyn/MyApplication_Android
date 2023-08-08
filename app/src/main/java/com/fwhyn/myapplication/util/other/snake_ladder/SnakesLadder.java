package com.fwhyn.myapplication.util.other.snake_ladder;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class SnakesLadder {

    /*
     * Complete the 'langkahTercepat' function below.
     *
     * The function is expected to return an INTEGER.
     * The function accepts following parameters:
     *  1. 2D_INTEGER_ARRAY ladders
     *  2. 2D_INTEGER_ARRAY snakes
     */

    public static int langkahTercepat(List<List<Integer>> ladders, List<List<Integer>> snakes) {
        int squares = 100;
        int start = 0;
        int dice = 6;
        int noSolution = -1;
        int win = squares - 1;

        // fill graph
        int[] graph = new int[squares];
        for (int i = 0; i < squares; i++) {
            graph[i] = noSolution;
        }

        // fill graph index (origin) with target ladder
        for (int i = 0; i < ladders.size(); i++) {
            int origin = ladders.get(i).get(0) - 1;
            int target = ladders.get(i).get(1) - 1;

            graph[origin] = target;
        }

        // fill graph index (origin) with target snakes
        for (int i = 0; i < snakes.size(); i++) {
            int origin = snakes.get(i).get(0) - 1;
            int target = snakes.get(i).get(1) - 1;

            graph[origin] = target;
        }

        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(start);

        boolean[] visited = new boolean[squares];
        visited[start] = true;

        int[] roll = new int[squares];

        while (!queue.isEmpty()) {

            int jump = queue.poll();

            for (int step = 1; step <= dice && jump + step < squares; ++step) {
                int currentStep = jump + step;
                while (!visited[currentStep]) {
                    visited[currentStep] = true;

                    if (currentStep == win) {
                        return roll[jump] + 1;
                    } else if (graph[currentStep] == -1) {
                        queue.add(currentStep);
                        roll[currentStep] = roll[jump] + 1;
                    } else {
                        currentStep = graph[currentStep];
                    }
                }
            }
        }

        return noSolution;
    }
}