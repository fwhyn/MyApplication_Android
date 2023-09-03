package com.fwhyn.myapplication.util.other.graph.weighted;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WeightedGraphPracticeTest {
    WeightedGraphPractice graph = null;

    @Before
    public void init() {
        graph = new WeightedGraphPractice();
    }

    @Test
    public void mainTest() {
        WeightedGraphPractice.Edge adj = new WeightedGraphPractice.Edge();

        adj.addBi("A", "B", 3);
        adj.addBi("B", "C", 4);
        adj.addBi("C", "F", 5);
        adj.addBi("C", "D", 1);
        adj.addBi("C", "E", 5);
        adj.addBi("D", "E", 2);

        String source = "A";
        String destination = "E";

        Assert.assertEquals(10, graph.getShortestPath(adj, source, destination));
    }

    @Test
    public void mainTest1() {
        WeightedGraphPractice.Edge adj = new WeightedGraphPractice.Edge();

        adj.addBi("A", "B", 3);
        adj.addBi("B", "C", 4);
        adj.addBi("C", "F", 5);
        adj.addBi("C", "D", 1);
        adj.addBi("C", "E", 5);
        adj.addBi("D", "E", 2);

        String source = "A";
        String destination = "F";

        Assert.assertEquals(12, graph.getShortestPath(adj, source, destination));
    }

    @Test
    public void mainTest3() {
        WeightedGraphPractice.Edge adj = new WeightedGraphPractice.Edge();

        adj.addBi("A", "B", 3);
        adj.addBi("B", "C", 4);
        adj.addBi("C", "F", 5);
        adj.addBi("C", "D", 1);
        adj.addBi("C", "E", 5);
        adj.addBi("D", "E", 2);

        String source = "C";
        String destination = "E";

        Assert.assertEquals(3, graph.getShortestPath(adj, source, destination));
    }

//    @Test
//    public void mainTest4() {
//        WeightedGraphPractice.Edge adj = new WeightedGraphPractice.Edge();
//
//        adj.addUni("A", "B", 2);
//        adj.addUni("B", "C", 1);
//        adj.addUni("B", "E", 6);
//
//
//        String source = "C";
//        String destination = "E";
//
//        Assert.assertEquals(3, graph.getShortestPath(adj, source, destination));
//    }
}