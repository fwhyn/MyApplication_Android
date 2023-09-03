package com.fwhyn.myapplication.util.other.graph;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class UnweightedGraphPractice {

    // Add edge and return vertices number
    public void addEdge(Map<Integer, Set<Integer>> adj, int src, int dst, boolean biDirection) {
        addEdge(adj, src, dst);

        if (biDirection) {
            addEdge(adj, dst, src);
        } else {
            // make sure all vortex registered to map
            addEdge(adj, dst, Integer.MAX_VALUE);
        }
    }

    // Add edge and return vertices number
    private void addEdge(Map<Integer, Set<Integer>> adj, int src, int dst) {
        Set<Integer> destination = adj.get(src);
        if (destination == null) {
            destination = new HashSet<>();
            adj.put(src, destination);
        }
        destination.add(dst);
    }

    public int getShortestPath(Map<Integer, Set<Integer>> adj, int src, int dst) {
        int vertices = adj.size();
        int shortestPath = Integer.MAX_VALUE;

        boolean[] visited = new boolean[vertices + 1];
        int[] path = new int[vertices + 1];
        for (int i = 1; i <= vertices; i++) {
            visited[i] = false;
            path[i] = Integer.MAX_VALUE;
        }

        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(src);
        visited[src] = true;
        path[src] = 0;

        while (!queue.isEmpty()) {
            int prevVertex = queue.remove();
            Set<Integer> vertexDestinations = adj.get(prevVertex);
            if (vertexDestinations == null) {
                System.out.println("No vertex found");
                continue;
            }

            for (int nextVertex : vertexDestinations) {
                if (nextVertex == Integer.MAX_VALUE || visited[nextVertex]) {
                    continue;
                }

                visited[nextVertex] = true;
                path[nextVertex] = path[prevVertex] + 1;

                if (nextVertex == dst) {
                    shortestPath = path[nextVertex];
                    break;
                } else {
                    queue.add(nextVertex);
                }
            }
        }

        return shortestPath;
    }
}
