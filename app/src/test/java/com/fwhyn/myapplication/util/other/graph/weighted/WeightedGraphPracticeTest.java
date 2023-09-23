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

    @Test
    public void mainTest4() {
        WeightedGraphPractice.Edge adj = new WeightedGraphPractice.Edge();

        adj.addUni("A", "B", 2);
        adj.addUni("B", "C", 1);
        adj.addUni("B", "E", 6);
        adj.addUni("C", "D", 3);
        adj.addUni("D", "B", 10);
        adj.addUni("E", "F", 4);
        adj.addUni("E", "G", 3);
        adj.addUni("F", "B", 5);
        adj.addUni("F", "A", 3);
        adj.addUni("G", "H", 1);


        String source = "C";
        String destination = "E";

        Assert.assertEquals(19, graph.getShortestPath(adj, source, destination));
    }

    @Test
    public void mainTest5() {
        WeightedGraphPractice.Edge adj = new WeightedGraphPractice.Edge();

        adj.addUni("A", "B", 2);
        adj.addUni("B", "C", 1);
        adj.addUni("B", "E", 6);
        adj.addUni("C", "D", 3);
        adj.addUni("D", "B", 10);
        adj.addUni("E", "F", 4);
        adj.addUni("E", "G", 3);
        adj.addUni("F", "B", 5);
        adj.addUni("F", "A", 3);
        adj.addUni("G", "H", 1);


        String source = "H";
        String destination = "G";

        Assert.assertEquals(2147483647, graph.getShortestPath(adj, source, destination));
    }

    @Test
    public void mainTest6() {
        WeightedGraphPractice.Edge adj = new WeightedGraphPractice.Edge();

        adj.addUni("A", "B", 2);
        adj.addUni("B", "C", 1);
        adj.addUni("B", "E", 6);
        adj.addUni("C", "D", 3);
        adj.addUni("D", "B", 10);
        adj.addUni("E", "F", 4);
        adj.addUni("E", "G", 3);
        adj.addUni("F", "B", 5);
        adj.addUni("F", "A", 3);
        adj.addUni("G", "H", 1);


        String source = "E";
        String destination = "B";

        Assert.assertEquals(9, graph.getShortestPath(adj, source, destination));
    }

    @Test
    public void mainTest7() {
        WeightedGraphPractice.Edge adj = new WeightedGraphPractice.Edge();

        adj.addUni("A", "B", 2);
        adj.addUni("B", "C", 1);
        adj.addUni("B", "E", 6);
        adj.addUni("C", "D", 3);
        adj.addUni("D", "B", 10);
        adj.addUni("E", "F", 4);
        adj.addUni("E", "G", 3);
        adj.addUni("F", "B", 5);
        adj.addUni("F", "A", 3);
        adj.addUni("G", "H", 1);


        String source = "E";
        String destination = "D";

        Assert.assertEquals(13, graph.getShortestPath(adj, source, destination));
    }
}