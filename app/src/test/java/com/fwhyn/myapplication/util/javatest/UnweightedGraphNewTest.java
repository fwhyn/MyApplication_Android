package com.fwhyn.myapplication.util.javatest;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class UnweightedGraphNewTest {
    UnweightedGraphNew graph = null;

    @Before
    public void init() {
        graph = new UnweightedGraphNew();
    }

    @Test
    public void mainTest() {
        // No of vertices
        int v = 8;

        // Adjacency list for storing which vertices are connected
        ArrayList<ArrayList<Integer>> adj = new ArrayList<>(v);

        for (int i = 0; i < v; i++) {
            adj.add(new ArrayList<>());
        }

        graph.addEdge(adj, 0, 1);
        graph.addEdge(adj, 1, 2);
        graph.addEdge(adj, 0, 3);
        graph.addEdge(adj, 3, 4);
        graph.addEdge(adj, 3, 7);
        graph.addEdge(adj, 4, 5);
        graph.addEdge(adj, 4, 6);
        graph.addEdge(adj, 4, 7);
        graph.addEdge(adj, 5, 6);
        graph.addEdge(adj, 6, 7);

        int source = 2, dest = 6;

        graph.printShortestDistance(adj, source, dest, v);
    }
}