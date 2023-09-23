package com.fwhyn.myapplication.util.other.graph.weighted;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;

public class WeightedGraphPractice {

    public int getShortestPath(Edge adj, String src, String dst) {
        int shortestPath = Integer.MAX_VALUE;

        Queue<String> queue = new ArrayDeque<>();
        queue.add(src);

        String prevKey = src;
        Vertex prevVertex = adj.get(prevKey);
        if (prevVertex == null) {
            onVertexInvalid();
            return shortestPath;
        } else {
            prevVertex.cost = 0;
        }

        while (!queue.isEmpty()) {
            prevKey = queue.remove();
            prevVertex = adj.get(prevKey);

            if (prevVertex == null) {
                onVertexInvalid();
                continue;
            }

            for (String nextKey : prevVertex.keySet()) {
                Integer weight = prevVertex.get(nextKey);
                Vertex nextVertex = adj.get(nextKey);
                if (nextVertex == null || nextVertex.visited) {
                    continue;
                }

                if (weight != null) {
                    int cost = weight + prevVertex.cost;
                    if (cost < nextVertex.cost) {
                        nextVertex.cost = cost;
                    }
                }

                if (dst.equals(nextKey) && nextVertex.cost < shortestPath) {
                    shortestPath = nextVertex.cost;
                } else {
                    queue.add(nextKey);
                }
            }

            prevVertex.visited = true;
        }

        return shortestPath;
    }

    private void onVertexInvalid() {
        System.out.println("Vertex source is invalid");
    }

    public static class Edge extends HashMap<String, Vertex> {
        public void addBi(String src, String dst, int weight) {
            add(src, dst, weight);
            add(dst, src, weight);
        }

        public void addUni(String src, String dst, int weight) {
            add(src, dst, weight);
            // make sure all vortex registered
            add(dst, String.valueOf(Integer.MAX_VALUE), Integer.MAX_VALUE);
        }

        private void add(String src, String dst, int weight) {
            Vertex vertex = this.get(src);
            if (vertex == null) {
                vertex = new Vertex();
                this.put(src, vertex);
            }

            vertex.put(dst, weight);
        }
    }

    public static class Vertex extends HashMap<String, Integer> {
        boolean visited = false;
        int cost = Integer.MAX_VALUE;
    }
}