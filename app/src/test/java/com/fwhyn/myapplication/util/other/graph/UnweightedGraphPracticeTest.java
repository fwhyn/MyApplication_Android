package com.fwhyn.myapplication.util.other.graph;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UnweightedGraphPracticeTest {
    UnweightedGraphPractice graph = null;

    @Before
    public void init() {
        graph = new UnweightedGraphPractice();
    }

    @Test
    public void mainTest() {
        Map<Integer, Set<Integer>> adj = new HashMap<>();

        graph.addEdge(adj, 0, 1, true);
        graph.addEdge(adj, 1, 2, true);
        graph.addEdge(adj, 0, 3, true);
        graph.addEdge(adj, 3, 4, true);
        graph.addEdge(adj, 3, 7, true);
        graph.addEdge(adj, 4, 5, true);
        graph.addEdge(adj, 4, 6, true);
        graph.addEdge(adj, 4, 7, true);
        graph.addEdge(adj, 5, 6, true);
        graph.addEdge(adj, 6, 7, true);

        int source = 0, dest = 2;

        Assert.assertEquals(2, graph.getShortestPath(adj, source, dest));
    }
}