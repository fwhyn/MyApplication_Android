package com.fwhyn.myapplication.util.other.graph;

import java.util.ArrayList;
import java.util.LinkedList;

// Java program to find shortest path in an undirected graph
public class UnweightedGraph {
    // function to form edge between two vertices
    // source and dest
    public void addEdge(ArrayList<ArrayList<Integer>> adj, int i, int j) {
        adj.get(i).add(j);
        adj.get(j).add(i);
    }

    // function to print the shortest distance and path
    // between source vertex and destination vertex
    public void printShortestDistance(ArrayList<ArrayList<Integer>> adj, int s, int dest, int v) {
        // predecessor[i] array stores predecessor of
        // i and distance array stores distance of i
        // from s
        int[] pred = new int[v];
        int[] dist = new int[v];

        if (!BFS(adj, s, dest, v, pred, dist)) {
            System.out.println("Given source and destination" +
                    "are not connected");
            return;
        }

        // LinkedList to store path
        LinkedList<Integer> path = new LinkedList<>();
        int crawl = dest;
        path.add(crawl);
        while (pred[crawl] != -1) {
            path.add(pred[crawl]);
            crawl = pred[crawl];
        }

        // Print distance
        System.out.println("Shortest path length is: " + dist[dest]);

        // Print path
        System.out.println("Path is ::");
        for (int i = path.size() - 1; i >= 0; i--) {
            System.out.print(path.get(i) + " ");
        }
    }

    // a modified version of BFS that stores predecessor
    // of each vertex in array pred
    // and its distance from source in array dist
    private boolean BFS(ArrayList<ArrayList<Integer>> adj, int src,
                        int dest, int v, int[] pred, int[] dist) {
        // a queue to maintain queue of vertices whose
        // adjacency list is to be scanned as per normal
        // BFS algorithm using LinkedList of Integer type
        LinkedList<Integer> queue = new LinkedList<>();

        // boolean array visited[] which stores the
        // information whether ith vertex is reached
        // at least once in the Breadth first search
        boolean[] visited = new boolean[v];

        // initially all vertices are unvisited
        // so v[i] for all i is false
        // and as no path is yet constructed
        // dist[i] for all i set to infinity
        for (int i = 0; i < v; i++) {
            visited[i] = false;
            dist[i] = Integer.MAX_VALUE;
            pred[i] = -1;
        }

        // now source is first to be visited and
        // distance from source to itself should be 0
        visited[src] = true;
        dist[src] = 0;
        queue.add(src);

        // bfs Algorithm
        while (!queue.isEmpty()) {
            int u = queue.remove();
            for (int i = 0; i < adj.get(u).size(); i++) {
                int index = adj.get(u).get(i);
                if (!visited[index]) {
                    visited[index] = true;
                    dist[index] = dist[u] + 1;
                    pred[index] = u;

                    queue.add(index);

                    // stopping condition (when we find
                    // our destination)
                    if (index == dest)
                        return true;
                }
            }
        }
        return false;
    }
}

// This code is contributed by Sahil Vaid